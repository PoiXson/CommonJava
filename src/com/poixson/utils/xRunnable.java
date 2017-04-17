package com.poixson.utils;

import com.poixson.utils.exceptions.RequiredArgumentException;


public class xRunnable implements Runnable {

	protected volatile String taskName = null;
	protected final Runnable task;



	public xRunnable() {
		this.taskName = null;
		this.task     = null;
	}
	public xRunnable(final String taskName) {
		this(
			taskName,
			(Runnable) null
		);
	}
	public xRunnable(final xRunnable run) {
		this(
			run.getTaskName(),
			(Runnable) run
		);
	}
	public xRunnable(final Runnable run) {
		this(
			(String) null,
			run
		);
	}
	public xRunnable(final String taskName, final Runnable run) {
		if (Utils.notEmpty(taskName)) {
			this.taskName = taskName;
		} else
		if (run instanceof xRunnable) {
			this.taskName = ((xRunnable) run).getTaskName();
		}
		this.task = run;
	}



	public static xRunnable cast(final Runnable run) {
		return cast(null, run);
	}
	public static xRunnable cast(final String name, final Runnable run) {
		if (run == null) throw new RequiredArgumentException("run");
		if (run instanceof xRunnable) {
			final xRunnable xrun = (xRunnable) run;
			if (Utils.notEmpty(name)) {
				xrun.setTaskName(name);
			}
			return xrun;
		}
		return new xRunnable(
				Utils.isEmpty(name) ? "<Runnable>" : name,
				run
		);
	}



	@Override
	public void run() {
		if (this.task == null) throw new RequiredArgumentException("task");
		this.task.run();
	}



	public String getTaskName() {
		return this.taskName;
	}
	public void setTaskName(final String name) {
		this.taskName = Utils.isEmpty(name) ? null : name;
	}
	public boolean taskNameEquals(final String name) {
		if (Utils.isEmpty(name))
			return Utils.isEmpty(this.taskName);
		return name.equals(this.taskName);
	}



}
