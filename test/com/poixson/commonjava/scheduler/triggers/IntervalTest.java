package com.poixson.commonjava.scheduler.triggers;

import com.poixson.commonjava.Utils.xRunnable;
import com.poixson.commonjava.logger.xLogTest;
import com.poixson.commonjava.scheduler.xScheduledTask;
import com.poixson.commonjava.scheduler.xScheduler;
import com.poixson.commonjava.xLogger.xLog;


public class IntervalTest extends xRunnable {

	public final xScheduledTask task;



	public IntervalTest(final xScheduler sched) {
		this();
		if(sched != null)
			sched.schedule(this.task);
	}
	public IntervalTest() {
		super("IntervalTest");
		this.task = xScheduledTask.get();
		this.task.setRunnable(this);
		this.task.setRepeating(false);
		this.task.setTrigger(triggerInterval.get("1s"));
	}



	@Override
	public void run() {
		log().info("Interval Test Triggered!");
	}



	public boolean hasFinished() {
		return (this.task.getRunCount() > 0);
	}



	// logger
	public static xLog log() {
		return xLogTest.get();
	}



}