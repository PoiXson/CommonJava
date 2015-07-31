package com.poixson.commonjava.Utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import com.poixson.commonjava.xLogger.xLevel;
import com.poixson.commonjava.xLogger.xLog;


public final class utils {
	private utils() {}



	/**
	 * Is string empty.
	 * @param String
	 * @return True if string is null or empty.
	 */
	public static boolean isEmpty(final String value) {
		return (value == null || value.length() == 0);
	}
	/**
	 * Is string populated.
	 * @param String
	 * @return True if string is not null or contains data.
	 */
	public static boolean notEmpty(final String value) {
		return (value != null && value.length() > 0);
	}



	/**
	 * Is array empty.
	 * @param Object[]
	 * @return True if array is null or empty.
	 */
	public static boolean isEmpty(final Object[] array) {
		return (array == null || array.length == 0);
	}
	/**
	 * Is array populated.
	 * @param Object[]
	 * @return True if array is not null or contains data.
	 */
	public static boolean notEmpty(final Object[] array) {
		return (array != null && array.length > 0);
	}



	/**
	 * Is collection/set/list empty.
	 * @param Collection or Set or List
	 * @return True if collection is null or empty.
	 */
	public static boolean isEmpty(final Collection<?> collect) {
		return (collect == null || collect.isEmpty());
	}
	/**
	 * Is collection/set/list populated.
	 * @param Collection or Set or List
	 * @return True if collection is not null or contains data.
	 */
	public static boolean notEmpty(final Collection<?> collect) {
		return (collect != null && !collect.isEmpty());
	}



	/**
	 * Is map empty.
	 * @param Map
	 * @return True if map is null or empty.
	 */
	public static boolean isEmpty(final Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}
	/**
	 * Is map populated.
	 * @param Map
	 * @return True if map is not null or contains data.
	 */
	public static boolean notEmpty(final Map<?, ?> map) {
		return (map != null && !map.isEmpty());
	}



	/**
	 * Close safely, ignoring errors.
	 */
	public static void safeClose(Closeable obj) {
		if(obj == null) return;
		try {
			obj.close();
		} catch (IOException ignore) {}
	}



	/**
	 * Current system time ms.
	 * @return
	 */
	public static long getSystemMillis() {
		return System.currentTimeMillis();
	}



	public static void MemoryStats() {
		MemoryStats(xLog.getRoot());
	}
	public static void MemoryStats(final xLog log) {
		MemoryStats(null, log);
	}
	public static void MemoryStats(final xLevel level, final xLog log) {
		final int[] stats = getMemoryStats();
		final String[] str = new String[4];
		int longest = 0;
		for(int i=0; i<4; i++) {
			str[i] = Integer.toString(stats[i]);
			if(str[i].length() > longest)
				longest = str[i].length();
		}
		log.publish(level, "##### Heap utilization statistics [MB] #####");
		log.publish(level, "Used Memory:  "+utilsString.padFront(longest, str[0], ' ')+" MB");
		log.publish(level, "Free Memory:  "+utilsString.padFront(longest, str[1], ' ')+" MB");
		log.publish(level, "Total Memory: "+utilsString.padFront(longest, str[2], ' ')+" MB");
		log.publish(level, "Max Memory:   "+utilsString.padFront(longest, str[3], ' ')+" MB");
	}
	public static int[] getMemoryStats() {
		final int MB = 1024 * 1024;
		final Runtime runtime = Runtime.getRuntime();
		return new int[] {
				(int) ((runtime.totalMemory() - runtime.freeMemory()) / MB),
				(int) (runtime.freeMemory()  / MB),
				(int) (runtime.totalMemory() / MB),
				(int) (runtime.maxMemory()   / MB)
		};
	}



	public static void ListProperties() {
		final Properties props = System.getProperties();
		props.list(System.out);
	}



	public static boolean isLibAvailable(final String classpath) {
		try {
			Class.forName(classpath);
			return true;
		} catch (ClassNotFoundException ignore) {}
		return false;
	}
	public static boolean isJLineAvailable() {
		return isLibAvailable("jline.Terminal");
	}
	public static boolean isRxtxAvailable() {
		return isLibAvailable("gnu.io.CommPortIdentifier");
	}



	public static boolean validBaud(final int baud) {
		switch(baud) {
		case 300:
		case 1200:
		case 2400:
		case 4800:
		case 9600:
		case 14400:
		case 19200:
		case 28800:
		case 38400:
		case 57600:
		case 115200:
			return true;
		default:
		}
		return false;
	}



	// compare versions
	public static String compareVersions(final String versionA, final String versionB) {
		if(utils.isEmpty(versionA)) throw new NullPointerException("versionA argument is required!");
		if(utils.isEmpty(versionB)) throw new NullPointerException("versionB argument is required!");
		final String normA = normalisedVersion(versionA);
		final String normB = normalisedVersion(versionB);
		final int cmp = normA.compareTo(normB);
		if(cmp < 0)
			return "<";
		if(cmp > 0)
			return ">";
		return "=";
	}
	public static String normalisedVersion(final String version) {
		final int maxWidth = 5;
		final String[] split = Pattern.compile(".", Pattern.LITERAL).split(version);
		final StringBuilder output = new StringBuilder();
		for(final String s : split) {
			output.append(
					String.format("%"+maxWidth+'s', s)
			);
		}
		return output.toString();
	}
	public static boolean checkJavaVersion(final String requiredVersion) {
		final String javaVersion;
		{
			final String vers = System.getProperty("java.version");
			if(vers == null || vers.isEmpty()) throw new NullPointerException("Failed to get java version");
			javaVersion = vers.replace('_', '.');
		}
		return !(compareVersions(javaVersion, requiredVersion).equals("<"));
	}



	// logger
	public static xLog log() {
		return xLog.getRoot();
	}



}
