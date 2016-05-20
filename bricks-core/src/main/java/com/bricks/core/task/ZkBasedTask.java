package com.bricks.core.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.bricks.lang.log.LogAble;
import com.bricks.utils.StringUtil;
import com.google.common.primitives.Ints;

/**
 * 基于zookeeper的任务处理器<br>
 * 工作模式：<br>
 * 1、生成基础数据<br>
 * 2、从数据节点拿到若干（具体数量可由派生子类指定，默认1）待处理数据，进行处理。<br>
 * 3、重复步骤2，若拿不到待处理数据，则任务结束。<br>
 * <br>
 * 派生子类需实现五个方法：<br>
 * 1、getName 指定任务名称<br>
 * 2、getRootNode 指定私有根节点<br>
 * 3、getBaseData 生成基础数据<br>
 * 4、handleData 处理基础数据<br>
 * 5、getBatchSize 指定批次大小（默认为1）<br>
 * 
 * @param <T>
 *            数据节点类型
 * @author bricks <long1795@gmail.com>
 */
public abstract class ZkBasedTask<T> implements Task, LogAble {

	protected static final int MAX_OFFSET = Integer.MAX_VALUE;
	protected static final int MIN_OFFSET = 0;
	protected static final int DEFAULT_BATCH_SIZE = 1;

	protected static final String NODE_NAME_PREFIX = "PCS_";
	protected static final String NODE_NAME_OFFSET = "offset";
	protected static final String NODE_NAME_DATA = "data";
	protected static final String NODE_NAME_LOCK = "locks";

	@Resource
	protected CuratorFramework curator;

	protected InterProcessLock lock;

	protected String offsetNode;
	protected String dataNode;
	protected String lockNode;

	/**
	 * 指定Task名字
	 * 
	 * @return
	 */
	protected String getName() {
		return this.getClass().getName();
	}

	/**
	 * 初始化,启动zk客户端,并初始节点
	 */
	@PostConstruct
	public void init() {
		log().info("Initializing task[{}]...", getName());
		Assert.notNull(curator, "ZooKeeperClient can't be null.");

		// 启动zk-client框架
		if (CuratorFrameworkState.STARTED != curator.getState()) {
			synchronized (curator) {
				if (CuratorFrameworkState.STARTED != curator.getState()) {
					curator.start();
				}
			}
		}

		// 初始化锁
		lockNode = getNode(NODE_NAME_LOCK);
		lock = new InterProcessMutex(curator, lockNode);

		// 初始化（检查）节点
		offsetNode = getAndCheckNode(NODE_NAME_OFFSET, Ints.toByteArray(MIN_OFFSET));
		dataNode = getAndCheckNode(NODE_NAME_DATA, "".getBytes());
	}

	/**
	 * 执行任务
	 */
	@Override
	public void execute() throws Exception {
		long startTime = System.currentTimeMillis();
		int all = prepareData();// 准备基础数据
		int cnt = executeImpl();// 执行任务操作
		log().info(taskReport(startTime, cnt, all).toString());// 输出报告
	}

	/**
	 * 准备基础数据，写入数据节点，并复位偏移量
	 * 
	 * @throws Exception
	 */
	protected int prepareData() throws Exception {
		final Integer key = 1;
		final Map<Integer, Integer> tmp = new HashMap<Integer, Integer>();
		tmp.put(key, 0);
		doWithLock(new innerHandler() {
			@Override
			public void handle() throws Exception {
				// 获取偏移量节点数据，若为MIN_OFFSET（初始为0） 或 MAX_OFFSET（每轮结束后重置为最大），则创建数据
				// 否则说明上轮任务尚未结束，不创建数据
				// 创建后，重置offset为1
				byte[] offsetData = curator.getData().forPath(offsetNode);
				int offset = Ints.fromByteArray(offsetData);
				if (offset == MAX_OFFSET || offset == MIN_OFFSET) {
					List<T> baseList = getBaseData();
					tmp.put(key, baseList.size());
					curator.setData().forPath(dataNode, JSON.toJSONString(baseList).getBytes());
					curator.setData().forPath(offsetNode, Ints.toByteArray(1));
					log().info("Base data prepared. [{}]", baseList.size());
					return;
				}
				log().info(
						"Last task hasn't finished yet, Or base-data was already prepared. Current offset is [{}].",
						offset);
			}
		});
		return tmp.get(key);
	}

	/**
	 * 获取基础数据
	 * 
	 * @return
	 */
	abstract protected List<T> getBaseData();

	/**
	 * 执行任务操作
	 * 
	 * @throws Exception
	 */
	protected int executeImpl() throws Exception {
		// 从数据节点获取待处理数据，进行处理
		// 若拿不到待处理数据，则任务结束
		List<T> dataList = getData();
		int i = 0;
		while (dataList != null && !dataList.isEmpty()) {
			i += dataList.size();
			dataList.parallelStream().forEach(t -> {
				try {
					handleData(t);
				} catch (Exception e) {}
			});
			dataList = getData();
		}
		return i;
	}

	/**
	 * 处理数据
	 * 
	 * @param data
	 *            待处理数据
	 */
	abstract protected void handleData(T data);

	/**
	 * 输出任务报告
	 */
	protected StringBuffer taskReport(long startTime, int cnt, int all) {
		StringBuffer log = new StringBuffer(256)//
				.append("Task[ ").append(getName()).append(" ] Report:")// 任务名
				.append(", Started at:").append(getDateStr(startTime))// 开始时间
				.append(", Finished at:").append(getDateStr(null))// 结束时间
				.append(", Spend time: ").append((System.currentTimeMillis() - startTime))// 耗时
				.append(" ms, Data handled: ").append(cnt)// 处理数据量
				.append(" of ").append(all).append(" .");// 总数据量
		return log;
	}

	protected String getDateStr(Long l) {
		Date d;
		if (l == null) {
			d = new Date();
		} else {
			d = new Date(l);
		}
		return StringUtil.format("HH:mm:ss", d);
	}

	/**
	 * 从数据节点获取待处理数据，获取后，递增偏移量
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected List<T> getData() throws Exception {
		final dataToWork dtw = new dataToWork();
		doWithLock(new innerHandler() {
			@Override
			public void handle() throws Exception {
				byte[] offsetData = curator.getData().forPath(offsetNode);
				int offset = Ints.fromByteArray(offsetData);
				if (offset == MAX_OFFSET || offset == MIN_OFFSET) {
					// 偏移量为初始或最大值时，说明当前无待处理数据
					return;
				}

				// 获取待处理数据
				byte[] data = curator.getData().forPath(dataNode);
				List<T> dataList = JSON.parseObject(new String(data), List.class);
				if (dataList == null || dataList.isEmpty() || dataList.size() < offset) {
					offset = MAX_OFFSET;
					curator.setData().forPath(offsetNode, Ints.toByteArray(offset));
					return;
				}

				dtw.data = getSubList(dataList, offset - 1, getBatchSize());

				// 递增偏移量
				offset += getBatchSize();
				if (offset > dataList.size()) {
					offset = MAX_OFFSET;
				}
				curator.setData().forPath(offsetNode, Ints.toByteArray(offset));
			}
		});
		return dtw.data;
	}

	/**
	 * 获取子list
	 * 
	 * @param list
	 *            list
	 * @param offset
	 *            偏移量
	 * @param batchSize
	 *            大小
	 * @return
	 */
	private List<T> getSubList(List<T> list, int offset, int batchSize) {
		List<T> r = new ArrayList<T>();
		for (int i = 0; i < batchSize; i++) {
			if (offset + i >= list.size()) {
				break;
			}
			r.add(list.get(offset + i));
		}
		return r;
	}

	/**
	 * 在锁内执行
	 * 
	 * @param handler
	 * @throws Exception
	 */
	protected void doWithLock(innerHandler handler) throws Exception {
		log().debug("Acquire the lock: {}", lockNode);
		if (!lock.acquire(30, TimeUnit.SECONDS)) {
			throw new IllegalStateException("Could not acquire the lock: " + lockNode);
		}

		try {
			handler.handle();
		} finally {
			log().debug("Releasing the lock: {}", lockNode);
			lock.release();
		}
	}

	@PreDestroy
	public void destroy() {
		log().info("Destroying task[{}]...", getName());
		curator.close();
	}

	/**
	 * 生成节点路径
	 * 
	 * @param node
	 *            节点
	 * @return 节点路径
	 */
	protected String getNode(final String node) {
		String rn = getRootNode();
		if (rn.endsWith("/")) {
			return rn + node;
		}
		return String.format("%1$s/%2$s", rn, node);
	}

	/**
	 * 检查节点,若不存在,则使用默认数据创建.
	 * 
	 * @param node
	 *            节点
	 * @param defaultData
	 *            默认数据
	 */
	protected void checkNode(final String node, final byte[] defaultData) {
		try {
			doWithLock(new innerHandler() {
				@Override
				public void handle() throws Exception {
					Stat stat = curator.checkExists().forPath(node);
					if (stat == null) {
						curator.create().creatingParentsIfNeeded().forPath(node, defaultData);
					}
				}
			});
		} catch (Throwable e) {
			throw new RuntimeException("Check zk-node [" + node + "] error. ", e);
		}
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	protected String getAndCheckNode(final String node, final byte[] defaultData) {
		final String fullNode = getNode(node);
		checkNode(fullNode, defaultData);
		return fullNode;
	}

	/**
	 * 指定批次大小
	 * 
	 * @return
	 */
	protected int getBatchSize() {
		return DEFAULT_BATCH_SIZE;
	}

	/**
	 * 获取当前根节点
	 * 
	 * @return
	 */
	protected abstract String getRootNode();

	/**
	 * 默认单一节点基础数据
	 * 
	 * @param t
	 * @return
	 */
	protected List<T> defaultSingleNodeBaseData(T t) {
		List<T> l = new ArrayList<T>();
		l.add(t);
		return l;
	}

	/**
	 * 由于List&lt;Long>序列化之后，再反序列化后，较小数值的节点会被认为是Integer，因此需要做特殊处理。
	 * 
	 * @param o
	 * @return
	 */
	protected Long getLongData(Object o) {
		if (o instanceof Long) {
			return (Long) o;
		}
		int i = (Integer) o;
		return (long) i;
	}

	/**
	 * 功能描述：内部处理器接口
	 * 
	 * @author zhaozhijie
	 *         time : 2013年9月3日 下午2:11:56
	 */
	protected interface innerHandler {
		void handle() throws Exception;
	}

	/**
	 * 功能描述：
	 * 
	 * @author zhaozhijie
	 *         time : 2013年9月4日 下午3:11:34
	 */
	protected class dataToWork {
		List<T> data;
	}
}
