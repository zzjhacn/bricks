package com.bricks.facade.test;

import java.util.Arrays;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.bricks.facade.codegen.dao.tab.Tab;
import com.bricks.facade.codegen.dao.tab.TabDao;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class InterceptorTest {

	public static void main(String[] args) {
		ApplicationContext ctx  = new FileSystemXmlApplicationContext("classpath:/spring-test.xml");
		TabDao dao  = ctx.getBean(TabDao.class);
		Arrays.asList(ctx.getBeanDefinitionNames()).forEach(b->{
			LogAble.slog().info("Bean[{}] of type[{}]",b,ctx.getBean(b).getClass().getName());	
		});
//		] of type[org.springframework.aop.aspectj.AspectJExpressionPointcut
		AspectJExpressionPointcut ajp = ctx.getBean(AspectJExpressionPointcut.class);
//		LogAble.slog().info("cnt is {}",dao.count(new Tab()));
		LogAble.slog().info("tab id is[{}]",new Tab().get__id());
	}
}
