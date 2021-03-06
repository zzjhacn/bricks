package com.bricks.dal;

import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class Page extends BaseObject {
	private static final long serialVersionUID = 1L;

	public final static int DEFAULT_PAGE_SIZE = 20;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private int currentPage = 1;

	private int currentSize = 0;

	private int maxPage = 1;

	public Page(final int currentPage, final int cnt, final int currSize){
			this(currentPage, cnt, currSize, null);
	}

	public Page(final int currentPage, final int cnt, final int currSize, final Integer pageSize){
		this.currentPage = currentPage > 0 ? currentPage : 1;
		this.pageSize = (pageSize == null || pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
		int count = cnt > 0 ? cnt : 1;
		this.maxPage = count % this.pageSize == 0 ? cnt / this.pageSize : cnt / this.pageSize + 1;
		this.currentSize = currSize;
	}

	/**
	* Returns value of pageSize
	* @return
	*/
	public int getPageSize() {
		return pageSize;
	}

	/**
	* Sets new value of pageSize
	* @param
	*/
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

		/**
		* Returns value of currentPage
		* @return
		*/
		public int getCurrentPage() {
			return currentPage;
		}

		/**
		* Sets new value of currentPage
		* @param
		*/
		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}

		/**
		* Returns value of currentSize
		* @return
		*/
		public int getCurrentSize() {
			return currentSize;
		}

		/**
		* Sets new value of currentSize
		* @param
		*/
		public void setCurrentSize(int currentSize) {
			this.currentSize = currentSize;
		}

	/**
	* Returns value of maxPage
	* @return
	*/
	public int getMaxPage() {
		return maxPage;
	}

	/**
	* Sets new value of maxPage
	* @param
	*/
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
}
