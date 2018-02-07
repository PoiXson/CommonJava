package com.poixson.tools;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.logger.xLog;
import com.poixson.logger.xLogRoot;


public class Keeper {
	private static final String LOG_NAME = "KEEPER";

	private static final AtomicReference<Keeper> instance =
			new AtomicReference<Keeper>(null);

	private static final CopyOnWriteArraySet<Object> holder =
			new CopyOnWriteArraySet<Object>();



	public static Keeper get() {
		if (instance.get() != null)
			return instance.get();
		// new instance
		final Keeper keeper = new Keeper();
		if (!instance.compareAndSet(null, keeper))
			return instance.get();
		return keeper;
	}



	public static void add(final Object obj) {
		if (obj == null) throw new RequiredArgumentException("obj");
		holder.add(obj);
		if (isDetailedLogging()) {
			log().detail(
				"Added to Keeper:",
				obj.getClass().getName()
			);
		}
	}



	public static void remove(final Object obj) {
		if (obj == null) throw new RequiredArgumentException("obj");
		holder.remove(obj);
		if (isDetailedLogging()) {
			log().detail(
				"Removed from Keeper:",
				obj.getClass().getName()
			);
		}
	}
	public static void removeAll() {
		holder.clear();
	}
	public static int removeAll(final Class<? extends Object> clss) {
		if (holder.isEmpty())
			return 0;
		int count = 0;
		final String expect = clss.getName();
		final Iterator<Object> it = holder.iterator();
		while (it.hasNext()) {
			final Object obj = it.next();
			final String actual = obj.getClass().getName();
			if (expect.equals(actual)) {
				count++;
				remove(obj);
			}
		}
		return count;
	}



	// logger
	private static xLog log() {
		return xLogRoot.get()
				.get(LOG_NAME);
	}



	// cached log level
	private static volatile SoftReference<Boolean> _detail = null;
	public static boolean isDetailedLogging() {
		if (_detail != null) {
			final Boolean detail = _detail.get();
			if (detail != null)
				return detail.booleanValue();
		}
		final boolean detail = log().isDetailLoggable();
		_detail = new SoftReference<Boolean>(Boolean.valueOf(detail));
		return detail;
	}



}
