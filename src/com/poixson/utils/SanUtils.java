package com.poixson.utils;


public final class SanUtils {
	private SanUtils() {}



	public static void init() {
		Keeper.add(new SanUtils());
	}



	public static String AlphaNum(final String text) {
		if (text == null)   return null;
		if (text.isEmpty()) return "";
		return text.replaceAll("[^a-zA-Z0-9]+", "");
	}
	public static String AlphaNumUnderscore(final String text) {
		if (text == null)   return null;
		if (text.isEmpty()) return "";
		return text.replaceAll("[^a-zA-Z0-9\\-\\_\\.]+", "");
	}



//TODO: is this useful?
//	public static String AlphaNumPunc(final String text) {
//		if (text == null)   return null;
//		if (text.isEmpty()) return "";
//		return text.replaceAll("[^a-zA-Z0-9\\.\\?\\!]+", "");
//	}



	public static String FileName(final String text) {
		return text.replaceAll("[^a-zA-Z0-9\\._]+", "_");
	}



	public static String ValidateStringEnum(final String value, final String...valids) {
		if (Utils.isEmpty(value)) return null;
		if (valids.length == 0)   return null;
		for (final String v : valids) {
			if (v == null || v.isEmpty()) continue;
			if (v.equalsIgnoreCase(value))
				return v;
		}
		return null;
	}



}
