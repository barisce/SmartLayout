package com.prototype.smartlayout.utils;

import com.prototype.smartlayout.model.AestheticData;
import java.util.ArrayList;
import java.util.List;

public class HistogramUtils {

	private static List<Double> balanceList = new ArrayList<>();
	private static List<Double> equilibriumList = new ArrayList<>();
	private static List<Double> symmetryList = new ArrayList<>();
	private static List<Double> sequenceList = new ArrayList<>();
	private static List<Double> cohesionList = new ArrayList<>();
	private static List<Double> unityList = new ArrayList<>();
	private static List<Double> proportionList = new ArrayList<>();
	private static List<Double> simplicityList = new ArrayList<>();
	private static List<Double> densityList = new ArrayList<>();
	private static List<Double> regularityList = new ArrayList<>();
	private static List<Double> economyList = new ArrayList<>();
	private static List<Double> homogeneityList = new ArrayList<>();
	private static List<Double> rhythmList = new ArrayList<>();
	private static List<Double> orderAndComplexityList = new ArrayList<>();
	private static List<Double> totalList = new ArrayList<>();

	public static void addData (AestheticData aestheticData) {
		balanceList.add(aestheticData.getBalance());
		equilibriumList.add(aestheticData.getEquilibrium());
		symmetryList.add(aestheticData.getSymmetry());
		sequenceList.add(aestheticData.getSequence());
		cohesionList.add(aestheticData.getCohesion());
		unityList.add(aestheticData.getUnity());
		proportionList.add(aestheticData.getProportion());
		simplicityList.add(aestheticData.getSimplicity());
		densityList.add(aestheticData.getDensity());
		regularityList.add(aestheticData.getRegularity());
		economyList.add(aestheticData.getEconomy());
		homogeneityList.add(aestheticData.getHomogeneity());
		rhythmList.add(aestheticData.getRhythm());
		orderAndComplexityList.add(aestheticData.getOrderAndComplexity());
		totalList.add(aestheticData.getTotal());
	}

	public static void clearLists () {
		balanceList.clear();
		equilibriumList.clear();
		symmetryList.clear();
		sequenceList.clear();
		cohesionList.clear();
		unityList.clear();
		proportionList.clear();
		simplicityList.clear();
		densityList.clear();
		regularityList.clear();
		economyList.clear();
		homogeneityList.clear();
		rhythmList.clear();
		orderAndComplexityList.clear();
		totalList.clear();
	}
}
