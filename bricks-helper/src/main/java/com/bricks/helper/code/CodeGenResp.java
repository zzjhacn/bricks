package com.bricks.helper.code;

import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class CodeGenResp extends BaseObject {
	private static final long serialVersionUID = 1L;

	private boolean succ;

	private String errmsg;

	private String filePath;

	public boolean isSucc(){
		return succ;
	}

	public void setSucc(boolean succ){
		this.succ = succ;
	}

	public String getErrmsg(){
		return errmsg;
	}

	public void setErrmsg(String errmsg){
		this.errmsg = errmsg;
	}

	public String getFilePath(){
		return filePath;
	}

	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
}
