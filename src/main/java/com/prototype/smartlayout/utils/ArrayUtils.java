package com.prototype.smartlayout.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ArrayUtils {

    public static int[] getLastTwoUniqueSmallestValuesInArray(int[] array) {
        int[] uniqueMins = new int[2];
        Integer[] copiedArray = Arrays.stream(array).boxed().toArray(Integer[]::new);
        copiedArray = removeAllSameValueFromArray(copiedArray, 0);
        Set<Integer> uniqueNumbers = convertArrayToSet(copiedArray);
        if (uniqueNumbers.size() < 2) {
            return uniqueNumbers.stream().mapToInt(Integer::intValue).toArray();
        } else {
            Iterator iter = uniqueNumbers.iterator();
            uniqueMins[0] = (Integer) iter.next();
            uniqueMins[1] = (Integer) iter.next();
            return uniqueMins;
        }
    }

    public static Integer[] removeAllSameValueFromArray(Integer[] array, int value) {
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

    public static <T> Set<T> convertArrayToSet(T[] array) {
        return new HashSet<>(Arrays.asList(array));
    }
}
