package com.prototype.smartlayout.utils;

import com.prototype.smartlayout.model.Coordinate;
import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.Layoutable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * These measures does not include color shape etc.
 * formulas gained from <site>http://www.mi.sanu.ac.rs/vismath/ngo/index.html</site>
 */
// TODO : We don't know assignedX and assignedY values of leaf nodes
public class AestheticMeasureUtil {
	private static final double balanceFactor = 1;
	private static final double equilibriumFactor = 1;
	private static final double symmetryFactor = 1;
	private static final double sequenceFactor = 1;
	private static final double cohesionFactor = 1;
	private static final double unityFactor = 1;
	private static final double proportionFactor = 1;
	private static final double simplicityFactor = 1;
	private static final double densityFactor = 1;
	private static final double regularityFactor = 1;
	private static final double economyFactor = 1;
	private static final double homogeneityFactor = 1;
	private static final double rhythmFactor = 1;
	private static final double orderAndComplexityFactor = 1;
	private static final double divisionByZeroAvoider = 0.00001;
	// This is not multi thread safe
	private static List<Double> leftAreaList = new ArrayList<>();
	private static List<Double> rightAreaList = new ArrayList<>();
	private static List<Double> topAreaList = new ArrayList<>();
	private static List<Double> bottomAreaList = new ArrayList<>();
	private static List<Double> areaList = new ArrayList<>();
	private static List<Coordinate> centerCoordinateList = new ArrayList<>();
	private static List<LayoutComponent> nodeList = new ArrayList<>();
	private static List<LayoutComponent> ulList = new ArrayList<>();
	private static List<LayoutComponent> urList = new ArrayList<>();
	private static List<LayoutComponent> llList = new ArrayList<>();
	private static List<LayoutComponent> lrList = new ArrayList<>();
	private static double screenWidth = 0;
	private static double screenHeight = 0;

	private AestheticMeasureUtil () {
	}

	public static double measureAesthetics (Layoutable tree) {
		clearValues();
		screenWidth = tree.getAssignedWidth();
		screenHeight = tree.getAssignedHeight();
		traverseToLeafNodes(tree);
		double balance = measureBalance() * balanceFactor;
		double equilibrium = measureEquilibrium() * equilibriumFactor;
		double symmetry = measureSymmetry() * symmetryFactor;
		double sequence = measureSequence() * sequenceFactor;
		double cohesion = measureCohesion() * cohesionFactor;
		double unity = measureUnity() * unityFactor;
		double proportion = measureProportion() * proportionFactor;
		double simplicity = measureSimplicity() * simplicityFactor;
		double density = measureDensity() * densityFactor;
		double regularity = measureRegularity() * regularityFactor;
		double economy = measureEconomy() * economyFactor;
		double homogeneity = measureHomogeneity() * homogeneityFactor;
		double rhythm = measureRhythm() * rhythmFactor;

		double orderAndComplexityNumber = balance + equilibrium + symmetry + sequence + cohesion +
				unity + proportion + simplicity + density + regularity + economy + homogeneity + rhythm;
		double orderAndComplexity = measureOrderAndComplexity(orderAndComplexityNumber) * orderAndComplexityFactor;

		return orderAndComplexityNumber + orderAndComplexity;
	}

	public static double measureBalance () {
		double bmVertical = 0;
		double bmHorizontal = 0;
		double wLeft = 0;
		double wRight = 0;
		double wTop = 0;
		double wBottom = 0;

		if (leftAreaList.isEmpty()) {
			return 0;
		}

		// All have the same amount of elements so one for loop is enough
		for (int i = 0; i < getSize(); i++) {
			wLeft += leftAreaList.get(i) / Collections.max(leftAreaList, null);
			wRight += rightAreaList.get(i) / Collections.max(rightAreaList, null);
			wTop += topAreaList.get(i) / Collections.max(topAreaList, null);
			wBottom += bottomAreaList.get(i) / Collections.max(bottomAreaList, null);
		}

		bmVertical = (wLeft - wRight) / Math.max(wLeft, wRight);
		bmHorizontal = (wTop - wBottom) / Math.max(wTop, wBottom);
		return 1 - ((Math.abs(bmVertical) + Math.abs(bmHorizontal)) / 2);
	}

	public static double measureEquilibrium () {
		double upperEmX = 0;
		double emX = 0;
		double upperEmY = 0;
		double emY = 0;
		double sumArea = 0;
		for (int i = 0; i < getSize(); i++) {
			upperEmX += (areaList.get(i) * (centerCoordinateList.get(i).getX() - screenWidth / 2d));
			upperEmY += (areaList.get(i) * (centerCoordinateList.get(i).getY() - screenHeight / 2d));
			sumArea += areaList.get(i);
		}
		emX = (2 * upperEmX) / (getSize() * screenWidth * sumArea);
		emY = (2 * upperEmY) / (getSize() * screenHeight * sumArea);
		return 1 - ((Math.abs(emX) + Math.abs(emY)) / 2);
	}

	// Is it worth it? This much calculation just to see if it is symmetric or not?
	public static double measureSymmetry () {
		SymmetryData symUL = calculateSymmetryData(ulList);
		SymmetryData symUR = calculateSymmetryData(urList);
		SymmetryData symLL = calculateSymmetryData(llList);
		SymmetryData symLR = calculateSymmetryData(lrList);

		// Normalize values for equation
		double xul = normalize(symUL.getX(), symUL.getXMin(), symUL.getXMax());
		double xur = normalize(symUR.getX(), symUR.getXMin(), symUR.getXMax());
		double xll = normalize(symLL.getX(), symLL.getXMin(), symLL.getXMax());
		double xlr = normalize(symLR.getX(), symLR.getXMin(), symLR.getXMax());

		double yul = normalize(symUL.getY(), symUL.getYMin(), symUL.getYMax());
		double yur = normalize(symUR.getY(), symUR.getYMin(), symUR.getYMax());
		double yll = normalize(symLL.getY(), symLL.getYMin(), symLL.getYMax());
		double ylr = normalize(symLR.getY(), symLR.getYMin(), symLR.getYMax());

		double bul = normalize(symUL.getB(), symUL.getBMin(), symUL.getBMax());
		double bur = normalize(symUR.getB(), symUR.getBMin(), symUR.getBMax());
		double bll = normalize(symLL.getB(), symLL.getBMin(), symLL.getBMax());
		double blr = normalize(symLR.getB(), symLR.getBMin(), symLR.getBMax());

		double hul = normalize(symUL.getH(), symUL.getHMin(), symUL.getHMax());
		double hur = normalize(symUR.getH(), symUR.getHMin(), symUR.getHMax());
		double hll = normalize(symLL.getH(), symLL.getHMin(), symLL.getHMax());
		double hlr = normalize(symLR.getH(), symLR.getHMin(), symLR.getHMax());

		double tul = normalize(symUL.getTheta(), symUL.getThetaMin(), symUL.getThetaMax());
		double tur = normalize(symUR.getTheta(), symUR.getThetaMin(), symUR.getThetaMax());
		double tll = normalize(symLL.getTheta(), symLL.getThetaMin(), symLL.getThetaMax());
		double tlr = normalize(symLR.getTheta(), symLR.getThetaMin(), symLR.getThetaMax());

		double rul = normalize(symUL.getR(), symUL.getRMin(), symUL.getRMax());
		double rur = normalize(symUR.getR(), symUR.getRMin(), symUR.getRMax());
		double rll = normalize(symLL.getR(), symLL.getRMin(), symLL.getRMax());
		double rlr = normalize(symLR.getR(), symLR.getRMin(), symLR.getRMax());

		double symVertical = (Math.abs(xul - xur) + Math.abs(xll - xlr) + Math.abs(yul - yur) + Math.abs(yll - ylr) + Math.abs(hul - hur) + Math.abs(hll - hlr) + Math.abs(bul - bur) + Math.abs(bll - blr) + Math.abs(tul - tur) + Math.abs(tll - tlr) + Math.abs(rul - rur) + Math.abs(rll - rlr)) / 12d;
		double symHorizontal = (Math.abs(xul - xll) + Math.abs(xur - xlr) + Math.abs(yul - yll) + Math.abs(yur - ylr) + Math.abs(hul - hll) + Math.abs(hur - hlr) + Math.abs(bul - bll) + Math.abs(bur - blr) + Math.abs(tul - tll) + Math.abs(tur - tlr) + Math.abs(rul - rll) + Math.abs(rur - rlr)) / 12d;
		double symRadial = (Math.abs(xul - xlr) + Math.abs(xur - xll) + Math.abs(yul - ylr) + Math.abs(yur - yll) + Math.abs(hul - hlr) + Math.abs(hur - hll) + Math.abs(bul - blr) + Math.abs(bur - bll) + Math.abs(tul - tlr) + Math.abs(tur - tll) + Math.abs(rul - rlr) + Math.abs(rur - rll)) / 12d;

		return 1 - (Math.abs(symVertical) + Math.abs(symHorizontal) + Math.abs(symRadial)) / 3d;
	}

	public static SymmetryData calculateSymmetryData (List<LayoutComponent> list) {
		SymmetryData sym = new SymmetryData();
		for (int i = 0; i < list.size(); i++) {
			double xijMinusXc = list.get(i).getAssignedX() + (list.get(i).getAssignedWidth() / 2d) + divisionByZeroAvoider - (screenWidth / 2d);
			sym.setX(sym.getX() + Math.abs(xijMinusXc));
			if (Math.abs(xijMinusXc) < sym.getXMin()) {
				sym.setXMin(Math.abs(xijMinusXc));
			}
			if (Math.abs(xijMinusXc) > sym.getXMax()) {
				sym.setXMax(Math.abs(xijMinusXc));
			}

			double yijMinusYc = list.get(i).getAssignedY() + (list.get(i).getAssignedHeight() / 2d) + divisionByZeroAvoider - (screenHeight / 2d);
			sym.setY(sym.getY() + Math.abs(yijMinusYc));
			if (Math.abs(yijMinusYc) < sym.getYMin()) {
				sym.setYMin(Math.abs(yijMinusYc));
			}
			if (Math.abs(yijMinusYc) > sym.getYMax()) {
				sym.setYMax(Math.abs(yijMinusYc));
			}

			sym.setB(sym.getB() + list.get(i).getAssignedWidth());
			if (list.get(i).getAssignedWidth() < sym.getBMin()) {
				sym.setBMin(list.get(i).getAssignedWidth());
			}
			if (list.get(i).getAssignedWidth() > sym.getBMax()) {
				sym.setBMax(list.get(i).getAssignedWidth());
			}

			sym.setH(sym.getH() + list.get(i).getAssignedHeight());
			if (list.get(i).getAssignedHeight() < sym.getHMin()) {
				sym.setHMin(list.get(i).getAssignedHeight());
			}
			if (list.get(i).getAssignedHeight() > sym.getHMax()) {
				sym.setHMax(list.get(i).getAssignedHeight());
			}

			sym.setTheta(sym.getTheta() + Math.abs(yijMinusYc / xijMinusXc));
			if (Math.abs(yijMinusYc / xijMinusXc) < sym.getThetaMin()) {
				sym.setThetaMin(Math.abs(yijMinusYc / xijMinusXc));
			}
			if (Math.abs(yijMinusYc / xijMinusXc) > sym.getThetaMax()) {
				sym.setThetaMax(Math.abs(yijMinusYc / xijMinusXc));
			}

			double r = Math.sqrt(Math.pow(xijMinusXc, 2) + Math.pow(yijMinusYc, 2));
			sym.setR(sym.getR() + r);
			if (r < sym.getRMin()) {
				sym.setRMin(r);
			}
			if (r > sym.getRMax()) {
				sym.setRMax(r);
			}
		}
		return sym;
	}

	public static double measureSequence () {
		// TODO
		return 0;
	}

	public static double measureCohesion () {
		double cmFL = 0;
		double cmLO = 0;
		double fi = 0;
		double ti = 0;
		for (int i = 0; i < getSize(); i++) {
			// FIXME : Assuming screenHeight and screenWidth are the same as layoutHeight and layoutWidth
			if (nodeList.get(i).getAssignedHeight() <= 0 || nodeList.get(i).getAssignedWidth() <= 0) {
				continue;
			}
			ti = (nodeList.get(i).getAssignedHeight() / nodeList.get(i).getAssignedWidth()) / (screenHeight / screenWidth);
			fi += ti <= 1 ? ti : 1 / ti;
		}
		// FIXME : Assuming layout is the same size as frame
		double tFL = 1; // (heightLayout / widthLayout) / (heightFrame / widthFrame)
		cmFL = tFL <= 1 ? tFL : 1 / tFL;
		cmLO = fi / getSize();
		return (Math.abs(cmFL) + Math.abs(cmLO)) / 2;
	}

	// Not Applicable
	public static double measureUnity () {
		// UMSpace = 1 - (areaLayout - totalArea) / (areaFrame - totalArea) which is 0 in our case
		// UMForm = 1 - (numberOfSizes + numberOfColors + numberOfShapes - 3) / 3 * getSize()
		// TODO Find all different sizes
		// 1 - (numberOfSizes - 1) / getSize()
		return 0; //(Math.abs(cmFL)) / 2;
	}

	public static double measureProportion () {
		// TODO
		return 0;
	}

	public static double measureSimplicity () {
		// TODO : What is alignment point in our project?
		return 0; //3 / (sumOfVerticalAlignments + sumOfHorizontalAlignments + getSize());
	}

	// Not Applicable
	public static double measureDensity () {
		// TODO : 1 - (totalArea / totalArea)
		return 0;
	}

	public static double measureRegularity () {
		// TODO : What is alignment point in our project?
		return 0;
	}

	// Not Applicable
	public static double measureEconomy () {
		// TODO : 3 / (numberOfSizes + numberOfColors + numberOfShapes)
		return 0;
	}

	// if an object exists in all four quadrants, will it count for every one of them?
	public static double measureHomogeneity () {
		BigInteger w = factorial(getSize()).divide((factorial(ulList.size()).multiply(factorial(urList.size())).multiply(factorial(llList.size()).multiply(factorial(lrList.size())))));
		BigInteger wMax = factorial(getSize()).divide(factorial(getSize() / 4).multiply(factorial(getSize() / 4)).multiply(factorial(getSize() / 4).multiply(factorial(getSize() / 4))));
		return ((w.divide(wMax))).doubleValue();
	}

	public static double measureRhythm () {
		// RHMArea is 0 since we fill the entire screen
		return 0;
	}

	public static double measureOrderAndComplexity (double orderAndComplexityNumber) {
		return orderAndComplexityNumber / 13;
	}

	private static void traverseToLeafNodes (Layoutable node) {
		if (node instanceof LayoutContainer) {
			for (Layoutable child : ((LayoutContainer) node).getChildren()) {
				traverseToLeafNodes(child);
			}
		}
		if (node instanceof LayoutComponent) {
			findLeftAreaOfComponent((LayoutComponent) node);
			findRightAreaOfComponent((LayoutComponent) node);
			findTopAreaOfComponent((LayoutComponent) node);
			findBottomAreaOfComponent((LayoutComponent) node);
			findAreaOfComponent((LayoutComponent) node);
			findCenterCoordinateOfComponent((LayoutComponent) node);
			addComponent((LayoutComponent) node);
		}
	}

	/**
	 * this method returns the component's area with respect to left side of the screen
	 * ___________
	 * |    |    |
	 * |  X |    |
	 * |____|____|
	 *
	 * @param node - Component that we try to find the area of
	 */
	private static void findLeftAreaOfComponent (LayoutComponent node) {
		if (node.getAssignedX() >= (screenWidth / 2d)) {
			leftAreaList.add(0d);
			return;
		}
		if (node.getAssignedY() < screenHeight / 2d) {
			ulList.add(node);
		} else {
			llList.add(node);
		}
		if (node.getAssignedX() + node.getAssignedWidth() >= (screenWidth / 2d)) {
			leftAreaList.add(Math.abs((screenWidth / 2d - node.getAssignedX()) * node.getAssignedHeight()));
		} else {
			leftAreaList.add(Math.abs((double) (node.getAssignedWidth() * node.getAssignedHeight())));
		}
	}

	/**
	 * this method returns the component's area with respect to right side of the screen
	 * ___________
	 * |    |    |
	 * |    |  X |
	 * |____|____|
	 *
	 * @param node - Component that we try to find the area of
	 */
	private static void findRightAreaOfComponent (LayoutComponent node) {
		if (node.getAssignedX() + node.getAssignedWidth() <= (screenWidth / 2d)) {
			rightAreaList.add(0d);
			return;
		}
		if (node.getAssignedY() < screenHeight / 2d) {
			urList.add(node);
		} else {
			lrList.add(node);
		}
		if (node.getAssignedX() <= (screenWidth / 2d)) {
			rightAreaList.add(Math.abs((node.getAssignedX() + node.getAssignedWidth() - screenWidth / 2d) * node.getAssignedHeight()));
		} else {
			rightAreaList.add(Math.abs((double) (node.getAssignedWidth() * node.getAssignedHeight())));
		}
	}

	/**
	 * this method returns the component's area with respect to top side of the screen
	 * __________
	 * |    X   |
	 * |--------|
	 * |________|
	 *
	 * @param node - Component that we try to find the area of
	 */
	private static void findTopAreaOfComponent (LayoutComponent node) {
		if (node.getAssignedY() >= (screenHeight / 2d)) {
			topAreaList.add(0d);
			return;
		}
		if (node.getAssignedY() + node.getAssignedHeight() >= (screenHeight / 2d)) {
			topAreaList.add(Math.abs((screenHeight / 2d - node.getAssignedY()) * node.getAssignedWidth()));
		} else {
			topAreaList.add(Math.abs((double) (node.getAssignedHeight() * node.getAssignedWidth())));
		}
	}

	/**
	 * this method returns the component's area with respect to top side of the screen
	 * __________
	 * |        |
	 * |--------|
	 * |____X___|
	 *
	 * @param node - Component that we try to find the area of
	 */
	private static void findBottomAreaOfComponent (LayoutComponent node) {
		if (node.getAssignedY() + node.getAssignedHeight() <= (screenHeight / 2d)) {
			bottomAreaList.add(0d);
			return;
		}
		if (node.getAssignedY() <= (screenHeight / 2d)) {
			bottomAreaList.add(Math.abs((node.getAssignedY() + node.getAssignedHeight() - screenHeight / 2d) * node.getAssignedWidth()));
		} else {
			bottomAreaList.add(Math.abs((double) (node.getAssignedHeight() * node.getAssignedWidth())));
		}
	}

	private static void findAreaOfComponent (LayoutComponent node) {
		areaList.add(Math.abs((double) (node.getAssignedWidth() * node.getAssignedHeight())));
	}

	private static void findCenterCoordinateOfComponent (LayoutComponent node) {
		centerCoordinateList.add(new Coordinate(node.getAssignedX() + (node.getAssignedWidth() / 2d), node.getAssignedY() + (node.getAssignedHeight() / 2d)));
	}

	private static void addComponent (LayoutComponent node) {
		nodeList.add(node);
	}

	private static int getSize () {
		return leftAreaList.size();
	}

	private static void clearValues () {
		leftAreaList.clear();
		rightAreaList.clear();
		topAreaList.clear();
		bottomAreaList.clear();
		areaList.clear();
		centerCoordinateList.clear();
		nodeList.clear();
		ulList.clear();
		urList.clear();
		llList.clear();
		lrList.clear();
		screenWidth = 0;
		screenHeight = 0;
	}

	/*
	 * Java method to calculate factorial of a large number
	 * @return BigInteger factorial of given number
	 */
	public static BigInteger factorial (int number) {
		BigInteger factorial = BigInteger.ONE;

		for (int i = number; i > 0; i--) {
			factorial = factorial.multiply(BigInteger.valueOf(i));
		}

		return factorial;
	}

	public static double normalize (double i, double min, double max) {
		return (i - min) / (max + divisionByZeroAvoider - min);
	}

	@Getter
	@Setter
	private static class SymmetryData {
		double x = 0;
		double xMin = Integer.MAX_VALUE;
		double xMax = Integer.MIN_VALUE;
		double y = 0;
		double yMin = Integer.MAX_VALUE;
		double yMax = Integer.MIN_VALUE;
		double h = 0;
		double hMin = Integer.MAX_VALUE;
		double hMax = Integer.MIN_VALUE;
		double b = 0;
		double bMin = Integer.MAX_VALUE;
		double bMax = Integer.MIN_VALUE;
		double theta = 0;
		double thetaMin = Integer.MAX_VALUE;
		double thetaMax = Integer.MIN_VALUE;
		double r = 0;
		double rMin = Integer.MAX_VALUE;
		double rMax = Integer.MIN_VALUE;
	}
}


