package com.innee.czyhInterface.util;

import org.apache.commons.lang3.RandomUtils;

public class RandomColorUtil {

	private static String[] color = new String[] { "fabe00", "ea68a2", "8957a1", "e8372f", "004e8b", "00b8e7", "00a672",
			"aacf45", "486a00", "834e00", "7d0022", "00736d" };

	public static int getRandowColor() {
		return Integer.valueOf(color[RandomUtils.nextInt(0, 11)], 16);
	}
}
