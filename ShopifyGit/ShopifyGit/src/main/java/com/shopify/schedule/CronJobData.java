package com.shopify.schedule;

import com.shopify.common.util.UtilFunc;

//import lombok.Data;

/**
 * job 과 task 는 constructor 에서만 설정 가능
 * 사용할때는 status 와 content 만 바꾸어서 사용하기위한 목적
 */
//@Data
public class CronJobData {

	private String job;
	private String task;
	private String status;
	private String content;
	
	public CronJobData(String job) {
		super();
		this.job = job;
		
		this.task = job + "-" + UtilFunc.todayTime();
	}

	public String getJob() {
		return job;
	}

//	public void setJob(String job) {
//		this.job = job;
//	}

	public String getTask() {
		return task;
	}

//	public void setTask(String task) {
//		this.task = task;
//	}

	public String getStatus() {
		return status;
	}
	public String getContent() {
		return content;
	}

	public void setData(String status, String content) {
		this.status = status;
		this.content = content;
	}

	public void initData() {
		this.status = null;
		this.content = null;		
	}

}
