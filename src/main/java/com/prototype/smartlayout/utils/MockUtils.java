package com.prototype.smartlayout.utils;

import java.util.UUID;

public class MockUtils {

	public static String generateString (int charCount) {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, charCount);
	}
}
