package com.prototype.smartlayout.utils;

import java.util.Comparator;

public class ArrayIndexComparator implements Comparator<Integer> {
	private final int[] array;

	public ArrayIndexComparator (int[] array) {
		this.array = array;
	}

	public Integer[] createIndexArray () {
		Integer[] indexes = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			indexes[i] = i;
		}
		return indexes;
	}

	@Override
	public int compare (Integer index1, Integer index2) {
		return Integer.compare(array[index1], array[index2]);
	}
}
