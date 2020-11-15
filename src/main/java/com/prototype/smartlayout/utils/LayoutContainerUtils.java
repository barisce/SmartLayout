package com.prototype.smartlayout.utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LayoutContainerUtils {

	public static int distribute (int[] capacityValues, int[] distribution, int remaining, int amount, int i) {
		distribution[i] += amount;
		remaining -= amount;
		// also subtract from values to notify
		capacityValues[i] -= amount;
		return remaining;
	}

	public static int checkDistributionComplete (int length, boolean[] removedIndex, int i, int capacityValue) {
		if (capacityValue <= 0) {
			// this node is distributed properly
			length--;
			removedIndex[i] = true;
		}
		return length;
	}

	public static void checkForDistributionCompletedSuccessfully (boolean[] removedIndex, String id) {
		for (boolean index : removedIndex) {
			if (!index) {
				log.trace("LayoutContainer with id: " + id + " is not a tight fit.");
				break; // not tight fit
			}
		}
	}
}
