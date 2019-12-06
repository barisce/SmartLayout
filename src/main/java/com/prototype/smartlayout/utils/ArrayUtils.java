package com.prototype.smartlayout.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ArrayUtils {

	public static int[] getLastTwoUniqueSmallestValuesInArray (int[] array) {
		int[] uniqueMins = new int[2];
		Integer[] copiedArray = Arrays.stream(array).boxed().toArray(Integer[]::new);
		copiedArray = removeAllSameValueFromArray(copiedArray, 0);
		Set<Integer> uniqueNumbers = convertArrayToSet(copiedArray);
		List<Integer> uniqueNumbersList = new ArrayList<>(uniqueNumbers);
		Collections.sort(uniqueNumbersList);
		if (uniqueNumbers.size() < 2) {
			return uniqueNumbers.stream().mapToInt(Integer::intValue).toArray();
		} else {
			uniqueMins[0] = uniqueNumbersList.get(0);
			uniqueMins[1] = uniqueNumbersList.get(1);
			return uniqueMins;
		}
	}

	public static Integer[] removeAllSameValueFromArray (Integer[] array, int value) {
		int targetIndex = 0;
		for (int sourceIndex = 0; sourceIndex < array.length; sourceIndex++) {
			if (array[sourceIndex] != value) {
				array[targetIndex++] = array[sourceIndex];
			}
		}
		Integer[] newArray = new Integer[targetIndex];
		System.arraycopy(array, 0, newArray, 0, targetIndex);
		return newArray;
	}

	public static <T> Set<T> convertArrayToSet (T[] array) {
		return new HashSet<>(Arrays.asList(array));
	}

	private int[] removeElement (int[] intArr, int i) {
		int[] newArr = new int[intArr.length - 1];
		for (int index = 0; index < i; index++) {
			newArr[index] = intArr[index];
		}
		for (int j = i; j < intArr.length - 1; j++) {
			newArr[j] = intArr[j + 1];
		}
		return newArr;
	}
}
