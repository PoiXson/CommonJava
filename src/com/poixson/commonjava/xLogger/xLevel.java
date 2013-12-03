package com.poixson.commonjava.xLogger;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.poixson.commonjava.Utils.utilsMath;


public class xLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final List<xLevel> knownLevels = new CopyOnWriteArrayList<xLevel>();
	private static volatile int minValue = 0;
	private static volatile int maxValue = 0;

	public static final transient xLevel OFF     = new xLevel("OFF",     Integer.MAX_VALUE);
	public static final transient xLevel FATAL   = new xLevel("FATAL",   2000);
	public static final transient xLevel SEVERE  = new xLevel("SEVERE",  1000);
	public static final transient xLevel WARNING = new xLevel("WARNING", 900);
	public static final transient xLevel INFO    = new xLevel("INFO",    800);
	public static final transient xLevel STATS   = new xLevel("STATS",   700);
	public static final transient xLevel DEBUG   = new xLevel("DEBUG",   600);
	public static final transient xLevel FINE    = new xLevel("FINE",    500);
	public static final transient xLevel FINER   = new xLevel("FINER",   400);
	public static final transient xLevel FINEST  = new xLevel("FINEST",  300);
	public static final transient xLevel ALL     = new xLevel("ALL",     Integer.MIN_VALUE);

	public final String name;
	public final int value;


	private xLevel(String name, int value) {
		if(name == null || name.isEmpty()) throw new NullPointerException("name cannot be null");
		this.name = name.toUpperCase();
		this.value = value;
		if(value != Integer.MIN_VALUE && value < minValue) minValue = value;
		if(value != Integer.MAX_VALUE && value > maxValue) maxValue = value;
		knownLevels.add(this);
	}
	@Override
	public Object clone() {
		return this;
	}


	public static xLevel FindLevel(String name) {
		if(name == null || name.isEmpty()) return null;
		if(utilsMath.isNumeric(name))
			return FindLevel(utilsMath.parseInt(name));
		name = name.toUpperCase();
		for(xLevel level : knownLevels)
			if(name.equals(level.name))
				return level;
		return null;
	}
	public static xLevel FindLevel(Integer value) {
		if(value == null) return null;
		if(value == xLevel.ALL.value) return xLevel.ALL;
		if(value == xLevel.OFF.value) return xLevel.OFF;
		xLevel level = xLevel.OFF;
		int offset = xLevel.OFF.value;
		for(xLevel lvl : knownLevels) {
			if(level.equals(xLevel.OFF) || level.equals(xLevel.ALL)) continue;
			if(value < lvl.value) continue;
			if(value - lvl.value < offset) {
				offset = value - lvl.value;
				level = lvl;
			}
		}
		if(level == null)
			return xLevel.OFF;
		return level;
	}


	public boolean isLoggable(xLevel level) {
		if(level == null) return false;
		// off (disabled)
		if(this.value == xLevel.OFF.value) return false;
		// all (forced)
		if(this.value == xLevel.ALL.value) return true;
		// check level
		if(level.value == xLevel.ALL.value) return true;
		return this.value <= level.value;
	}


	public boolean equals(xLevel level) {
		if(level == null) return false;
		return level.value == this.value;
	}


	@Override
	public String toString() {
		return name;
	}


}