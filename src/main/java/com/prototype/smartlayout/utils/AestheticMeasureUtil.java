package com.prototype.smartlayout.utils;

import com.prototype.smartlayout.model.Coordinate;
import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.Layoutable;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * These measures does not include color shape etc.
 * formulas gained from <site>http://www.mi.sanu.ac.rs/vismath/ngo/index.html</site>
 */
public class AestheticMeasureUtil {
	private static final double balanceFactor = 1;
	private static final double equilibriumFactor = 1;
	private static final double symmetryFactor = 1;
	private static final double sequenceFactor = 1;
	private static final double cohesionFactor = 0.5;
	private static final double unityFactor = 1;
	private static final double proportionFactor = 1;
	private static final double simplicityFactor = 1;
	private static final double densityFactor = 1;
	private static final double regularityFactor = 1;
	private static final double economyFactor = 1;
	private static final double homogeneityFactor = 1;
	private static final double rhythmFactor = 1;
	private static final double orderAndComplexityFactor = 1;

	// This is not multi thread safe
	private static List<Double> leftAreaList = new ArrayList<>();
	private static List<Double> rightAreaList = new ArrayList<>();
	private static List<Double> topAreaList = new ArrayList<>();
	private static List<Double> bottomAreaList = new ArrayList<>();
	private static List<Double> areaList = new ArrayList<>();
	private static List<Coordinate> centerCoordinateList = new ArrayList<>();
	private static List<LayoutComponent> nodeList = new ArrayList<>();
	private static List<Coordinate> widthHeightList = new ArrayList<>();
	private static List<LayoutComponent> ulList = new ArrayList<>();
	private static List<LayoutComponent> urList = new ArrayList<>();
	private static List<LayoutComponent> llList = new ArrayList<>();
	private static List<LayoutComponent> lrList = new ArrayList<>();
	private static double frameWidth = 0;
	private static double frameHeight = 0;
	private static double screenWidth = 0;
	private static double screenHeight = 0;

	// https://stackoverflow.com/questions/49377139/performance-of-set-in-java-vs-list-in-java
	private static List<Integer> unorganizedHAlignmentPoints = new ArrayList<>();
	private static List<Integer> horizontalAlignmentPoints = new ArrayList<>();
	private static List<Integer> unorganizedVAlignmentPoints = new ArrayList<>();
	private static List<Integer> verticalAlignmentPoints = new ArrayList<>();
	// nSpacing looks at the unique spacing of row AND columns so we hold a single list of them
	// FIXME : change this into count and hold an int not HashSet
	private static HashSet<Integer> distinctDistances = new HashSet<>();
	private static int distinctAreaCount = 0;

	private static final double[] rectangleRatios = {1, 1/1.414, 1/1.618, 1/1.732, 1/2.0};

	private AestheticMeasureUtil () {
	}

	public static double measureAesthetics (Layoutable tree) {
		clearValues();
		frameWidth = tree.getAssignedWidth();
		frameHeight = tree.getAssignedHeight();
		// gets total screen size, supports multi-monitors as long as they all are the same resolution
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		screenWidth = gd.getDisplayMode().getWidth();
		screenHeight = gd.getDisplayMode().getHeight();
		traverseToLeafNodes(tree);
		getAlignmentPoints(unorganizedHAlignmentPoints, horizontalAlignmentPoints);
		getAlignmentPoints(unorganizedVAlignmentPoints, verticalAlignmentPoints);
		getDistinctDistances(horizontalAlignmentPoints, distinctDistances);
		getDistinctDistances(verticalAlignmentPoints, distinctDistances);
		getDistinctAreas();

		double balance = measureBalance();
		double equilibrium = measureEquilibrium();
		double symmetry = measureSymmetry();
		double sequence = measureSequence();
		double cohesion = measureCohesion();
		double unity = measureUnity();
		double proportion = measureProportion();
		double simplicity = measureSimplicity();
		double density = measureDensity();
		double regularity = measureRegularity();
		double economy = measureEconomy();
		double homogeneity = measureHomogeneity();
		double rhythm = measureRhythm();

		balance = Double.isNaN(balance) ? 0 : balance * balanceFactor;
		equilibrium = Double.isNaN(equilibrium) ? 0 : equilibrium * equilibriumFactor;
		symmetry = Double.isNaN(symmetry) ? 0 : symmetry * symmetryFactor;
		sequence = Double.isNaN(sequence) ? 0 : sequence * sequenceFactor;
		cohesion = Double.isNaN(cohesion) ? 0 : cohesion * cohesionFactor;
		unity = Double.isNaN(unity) ? 0 : unity * unityFactor;
		proportion = Double.isNaN(proportion) ? 0 : proportion * proportionFactor;
		simplicity = Double.isNaN(simplicity) ? 0 : simplicity * simplicityFactor;
		density = Double.isNaN(density) ? 0 : density * densityFactor;
		regularity = Double.isNaN(regularity) ? 0 : regularity * regularityFactor;
		economy = Double.isNaN(economy) ? 0 : economy * economyFactor;
		homogeneity = Double.isNaN(homogeneity) ? 0 : homogeneity * homogeneityFactor;
		rhythm = Double.isNaN(rhythm) ? 0 : rhythm * rhythmFactor;

		double orderAndComplexityNumber = balance + equilibrium + symmetry + sequence + cohesion +
				unity + proportion + simplicity + density + regularity + economy + homogeneity + rhythm;
		double orderAndComplexity = measureOrderAndComplexity(orderAndComplexityNumber) * orderAndComplexityFactor;

		return orderAndComplexityNumber + orderAndComplexity;
	}

	public static double measureBalance () {
		double bmVertical;
		double bmHorizontal;
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
			upperEmX += (areaList.get(i) * (centerCoordinateList.get(i).getX() - frameWidth / 2d));
			upperEmY += (areaList.get(i) * (centerCoordinateList.get(i).getY() - frameHeight / 2d));
			sumArea += areaList.get(i);
		}
		emX = (2 * upperEmX) / (getSize() * frameWidth * sumArea);
		emY = (2 * upperEmY) / (getSize() * frameHeight * sumArea);
		return 1 - ((Math.abs(emX) + Math.abs(emY)) / 2);
	}

	// Is it worth it? This much calculation just to see if it is symmetric or not?
	public static double measureSymmetry () {
		SymmetryData symUL = calculateSymmetryData(ulList);
		SymmetryData symUR = calculateSymmetryData(urList);
		SymmetryData symLL = calculateSymmetryData(llList);
		SymmetryData symLR = calculateSymmetryData(lrList);

		// Normalize values for equation
		double[] xArr = {symUL.getX(), symUR.getX(), symLL.getX(), symLR.getX()};
		Arrays.sort(xArr);
		double xul = normalize(symUL.getX(), xArr[0], xArr[xArr.length - 1]);
		double xur = normalize(symUR.getX(), xArr[0], xArr[xArr.length - 1]);
		double xll = normalize(symLL.getX(), xArr[0], xArr[xArr.length - 1]);
		double xlr = normalize(symLR.getX(), xArr[0], xArr[xArr.length - 1]);

		double[] yArr = {symUL.getY(), symUR.getY(), symLL.getY(), symLR.getY()};
		Arrays.sort(yArr);
		double yul = normalize(symUL.getY(), yArr[0], yArr[yArr.length - 1]);
		double yur = normalize(symUR.getY(), yArr[0], yArr[yArr.length - 1]);
		double yll = normalize(symLL.getY(), yArr[0], yArr[yArr.length - 1]);
		double ylr = normalize(symLR.getY(), yArr[0], yArr[yArr.length - 1]);

		double[] bArr = {symUL.getB(), symUR.getB(), symLL.getB(), symLR.getB()};
		Arrays.sort(bArr);
		double bul = normalize(symUL.getB(), bArr[0], bArr[bArr.length - 1]);
		double bur = normalize(symUR.getB(), bArr[0], bArr[bArr.length - 1]);
		double bll = normalize(symLL.getB(), bArr[0], bArr[bArr.length - 1]);
		double blr = normalize(symLR.getB(), bArr[0], bArr[bArr.length - 1]);

		double[] hArr = {symUL.getH(), symUR.getH(), symLL.getH(), symLR.getH()};
		Arrays.sort(hArr);
		double hul = normalize(symUL.getH(), hArr[0], hArr[hArr.length - 1]);
		double hur = normalize(symUR.getH(), hArr[0], hArr[hArr.length - 1]);
		double hll = normalize(symLL.getH(), hArr[0], hArr[hArr.length - 1]);
		double hlr = normalize(symLR.getH(), hArr[0], hArr[hArr.length - 1]);

		double[] tArr = {symUL.getTheta(), symUR.getTheta(), symLL.getTheta(), symLR.getTheta()};
		Arrays.sort(tArr);
		double tul = normalize(symUL.getTheta(), tArr[0], tArr[tArr.length - 1]);
		double tur = normalize(symUR.getTheta(), tArr[0], tArr[tArr.length - 1]);
		double tll = normalize(symLL.getTheta(), tArr[0], tArr[tArr.length - 1]);
		double tlr = normalize(symLR.getTheta(), tArr[0], tArr[tArr.length - 1]);

		double[] rArr = {symUL.getR(), symUR.getR(), symLL.getR(), symLR.getR()};
		Arrays.sort(rArr);
		double rul = normalize(symUL.getR(), rArr[0], rArr[rArr.length - 1]);
		double rur = normalize(symUR.getR(), rArr[0], rArr[rArr.length - 1]);
		double rll = normalize(symLL.getR(), rArr[0], rArr[rArr.length - 1]);
		double rlr = normalize(symLR.getR(), rArr[0], rArr[rArr.length - 1]);

		double symVertical = (Math.abs(xul - xur) + Math.abs(xll - xlr) + Math.abs(yul - yur) + Math.abs(yll - ylr) + Math.abs(hul - hur) + Math.abs(hll - hlr) + Math.abs(bul - bur) + Math.abs(bll - blr) + Math.abs(tul - tur) + Math.abs(tll - tlr) + Math.abs(rul - rur) + Math.abs(rll - rlr)) / 12d;
		double symHorizontal = (Math.abs(xul - xll) + Math.abs(xur - xlr) + Math.abs(yul - yll) + Math.abs(yur - ylr) + Math.abs(hul - hll) + Math.abs(hur - hlr) + Math.abs(bul - bll) + Math.abs(bur - blr) + Math.abs(tul - tll) + Math.abs(tur - tlr) + Math.abs(rul - rll) + Math.abs(rur - rlr)) / 12d;
		double symRadial = (Math.abs(xul - xlr) + Math.abs(xur - xll) + Math.abs(yul - ylr) + Math.abs(yur - yll) + Math.abs(hul - hlr) + Math.abs(hur - hll) + Math.abs(bul - blr) + Math.abs(bur - bll) + Math.abs(tul - tlr) + Math.abs(tur - tll) + Math.abs(rul - rlr) + Math.abs(rur - rll)) / 12d;

		return 1 - (Math.abs(symVertical) + Math.abs(symHorizontal) + Math.abs(symRadial)) / 3d;
	}

	public static SymmetryData calculateSymmetryData (List<LayoutComponent> list) {
		SymmetryData sym = new SymmetryData();
		for (int i = 0; i < list.size(); i++) {
			double xijMinusXc = list.get(i).getAssignedX() + (list.get(i).getAssignedWidth() / 2d) - (frameWidth / 2d);
			sym.setX(sym.getX() + Math.abs(xijMinusXc));
			if (Math.abs(xijMinusXc) < sym.getXMin()) {
				sym.setXMin(Math.abs(xijMinusXc));
			}
			if (Math.abs(xijMinusXc) > sym.getXMax()) {
				sym.setXMax(Math.abs(xijMinusXc));
			}

			double yijMinusYc = list.get(i).getAssignedY() + (list.get(i).getAssignedHeight() / 2d) - (frameHeight / 2d);
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
		return 1 - (0) / 8d;
	}

	public static double measureCohesion () {
		double cmFL;
		double cmLO;
		double fi = 0;
		double ti;
		for (int i = 0; i < getSize(); i++) {
			if (nodeList.get(i).getAssignedHeight() <= 0 || nodeList.get(i).getAssignedWidth() <= 0) {
				continue;
			}
			ti = ((double)nodeList.get(i).getAssignedHeight() / nodeList.get(i).getAssignedWidth()) / (frameHeight / frameWidth);
			fi += ti <= 1 ? ti : 1 / ti;
		}
		double tFL = (frameHeight / frameWidth) / (screenHeight / screenWidth);
		cmFL = tFL <= 1 ? tFL : 1 / tFL;
		cmLO = fi / getSize();
		return (Math.abs(cmFL) + Math.abs(cmLO)) / 2;
	}

	// ???
	public static double measureUnity () {
		double umForm = 1 - (distinctAreaCount - 1) / getSize();
		// UMSpace = 1 - (areaLayout - totalArea) / (areaFrame - totalArea) which is 0 in our case
		// 1 - (numberOfSizes - 1) / getSize()
		return umForm;
	}

	public static double measureProportion () {
		double pmObject = 0;
		double pmLayout = 0;

		for (LayoutComponent layoutComponent : nodeList) {
			double ri = (double) layoutComponent.getAssignedHeight() / layoutComponent.getAssignedWidth();
			double pi = ri <= 1 ? ri : 1 / ri;
			double[] pjMinusPi = {Math.abs(rectangleRatios[0] - pi), Math.abs(rectangleRatios[1] - pi), Math.abs(rectangleRatios[2] - pi), Math.abs(rectangleRatios[3] - pi), Math.abs(rectangleRatios[4] - pi)};
			Arrays.sort(pjMinusPi);
			pmObject += 1 - (pjMinusPi[0]) / 0.5;
		}
		pmObject = pmObject / getSize();

		double rl = frameHeight / frameWidth; // Assuming layout is as big as frame
		double pl = rl <= 1 ? rl : 1/rl;
		double[] pjMinusPl = {Math.abs(rectangleRatios[0] - pl), Math.abs(rectangleRatios[1] - pl), Math.abs(rectangleRatios[2] - pl), Math.abs(rectangleRatios[3] - pl), Math.abs(rectangleRatios[4] - pl)};
		Arrays.sort(pjMinusPl);
		pmLayout = 1 - (pjMinusPl[0]) / 0.5;

		return (Math.abs(pmObject) + Math.abs(pmLayout)) / 2d;
	}

	public static double measureSimplicity () {
		return 3d / (verticalAlignmentPoints.size() + horizontalAlignmentPoints.size() + getSize());
	}

	// Not Applicable
	public static double measureDensity () {
		// TODO : 1 - (totalArea / totalArea)
		return 0;
	}

	public static double measureRegularity () {
		double rmAlignment = 1 - (verticalAlignmentPoints.size() + horizontalAlignmentPoints.size()) / (2d * getSize());
		double rmSpacing = getSize() == 1 ? 1 : 1 - (distinctDistances.size() - 1) / (2d * getSize() - 1);

		return (Math.abs(rmAlignment) + Math.abs(rmSpacing)) / 2d;
	}

	public static double measureEconomy () {
		return 1d / distinctAreaCount;
	}

	/**
	 * Hm = (1 - Ht)^2
	 * Ht = (sum j of |n/4 - nj| / (n/4)) / 6
	 * where j is the quadrant upperLeft, upperRight, lowerLeft, lowerRight
	 * and n is the number of elements
	 *
	 * @return
	 */
	public static double measureHomogeneity () {
		double ht = Math.abs(getSize() / 4d - ulList.size()) / (getSize() / 4d);
		ht += Math.abs(getSize() / 4d - urList.size()) / (getSize() / 4d);
		ht += Math.abs(getSize() / 4d - llList.size()) / (getSize() / 4d);
		ht += Math.abs(getSize() / 4d - lrList.size()) / (getSize() / 4d);
		ht = ht / 6d;
		return Math.pow(1 - ht, 2);
	}

	public static double measureRhythm () {
		// TODO : RHMArea is 0 since we fill the entire screen
		return 0;
	}

	public static double measureOrderAndComplexity (double orderAndComplexityNumber) {
		return orderAndComplexityNumber / 13d;
	}

	private static void traverseToLeafNodes (Layoutable node) {
		if (node instanceof LayoutContainer) {
			for (Layoutable child : ((LayoutContainer) node).getChildren()) {
				traverseToLeafNodes(child);
			}
		}
		if (node instanceof LayoutComponent) {
			findAligmentPoints(node);
			findLeftAreaOfComponent((LayoutComponent) node);
			findRightAreaOfComponent((LayoutComponent) node);
			findTopAreaOfComponent((LayoutComponent) node);
			findBottomAreaOfComponent((LayoutComponent) node);
			findAreaOfComponent((LayoutComponent) node);
			findCenterCoordinateOfComponent((LayoutComponent) node);
			addComponentAndSize((LayoutComponent) node);
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
		if (node.getAssignedX() >= (frameWidth / 2d)) {
			leftAreaList.add(0d);
			return;
		}
		if (node.getAssignedY() < frameHeight / 2d) {
			ulList.add(node);
		} else {
			llList.add(node);
		}
		if (node.getAssignedX() + node.getAssignedWidth() >= (frameWidth / 2d)) {
			leftAreaList.add(Math.abs((frameWidth / 2d - node.getAssignedX()) * node.getAssignedHeight()));
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
		if (node.getAssignedX() + node.getAssignedWidth() <= (frameWidth / 2d)) {
			rightAreaList.add(0d);
			return;
		}
		if (node.getAssignedY() < frameHeight / 2d) {
			urList.add(node);
		} else {
			lrList.add(node);
		}
		if (node.getAssignedX() <= (frameWidth / 2d)) {
			rightAreaList.add(Math.abs((node.getAssignedX() + node.getAssignedWidth() - frameWidth / 2d) * node.getAssignedHeight()));
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
		if (node.getAssignedY() >= (frameHeight / 2d)) {
			topAreaList.add(0d);
			return;
		}
		if (node.getAssignedY() + node.getAssignedHeight() >= (frameHeight / 2d)) {
			topAreaList.add(Math.abs((frameHeight / 2d - node.getAssignedY()) * node.getAssignedWidth()));
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
		if (node.getAssignedY() + node.getAssignedHeight() <= (frameHeight / 2d)) {
			bottomAreaList.add(0d);
			return;
		}
		if (node.getAssignedY() <= (frameHeight / 2d)) {
			bottomAreaList.add(Math.abs((node.getAssignedY() + node.getAssignedHeight() - frameHeight / 2d) * node.getAssignedWidth()));
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

	/**
	 * This method gets horizontal and vertical alignment points in a duplicated manner
	 *
	 * @param node
	 */
	private static void findAligmentPoints (Layoutable node) {
		unorganizedHAlignmentPoints.add(node.getAssignedX());
		unorganizedHAlignmentPoints.add(node.getAssignedX() + node.getAssignedWidth());
		unorganizedVAlignmentPoints.add(node.getAssignedY());
		unorganizedVAlignmentPoints.add(node.getAssignedY() + node.getAssignedHeight());
	}

	/**
	 * This method organizes alignment points and makes a new unique list of these points
	 *
	 * @param alignmentPoints    - unorganized alignment points
	 * @param newAlignmentPoints - organized alignment points
	 */
	private static void getAlignmentPoints (List<Integer> alignmentPoints, List<Integer> newAlignmentPoints) {
		Collections.sort(alignmentPoints);
		// Traverse the sorted array
		for (int i = 0; i < alignmentPoints.size(); i++) {
			// Move the index ahead while there are duplicates
			while (i < alignmentPoints.size() - 1 && alignmentPoints.get(i).equals(alignmentPoints.get(i + 1))) {
				i++;
			}
			// Add the unique value to a new list
			newAlignmentPoints.add(alignmentPoints.get(i));
		}
	}

	/**
	 * This method calculates distances of alignment points and stores the unique values in a set
	 * <site>https://stackoverflow.com/questions/17985029/hashset-vs-arraylist</site>
	 *
	 * @param alignmentPoints   - organizedAlignmentPoints
	 * @param distinctDistances - distinctDistances
	 */
	private static void getDistinctDistances (List<Integer> alignmentPoints, HashSet<Integer> distinctDistances) {
		if (alignmentPoints.size() < 2) {
			return;
		}
		for (int i = 1; i < alignmentPoints.size(); i++) {
			distinctDistances.add(alignmentPoints.get(i) - alignmentPoints.get(i - 1));
		}
	}

	private static void addComponentAndSize (LayoutComponent node) {
		nodeList.add(node);
		widthHeightList.add(new Coordinate(node.getAssignedWidth(), node.getAssignedHeight()));
	}

	private static void getDistinctAreas () {
		Collections.sort(widthHeightList);
		// Traverse the sorted array
		for (int i = 0; i < widthHeightList.size(); i++) {
			// Move the index ahead while there are duplicates
			while (i < widthHeightList.size() - 1 && widthHeightList.get(i).getX() == widthHeightList.get(i + 1).getX() && widthHeightList.get(i).getY() == widthHeightList.get(i + 1).getY()) {
				i++;
			}
			// Add the unique value to a new list
			distinctAreaCount++;
		}
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
		widthHeightList.clear();
		ulList.clear();
		urList.clear();
		llList.clear();
		lrList.clear();
		frameWidth = 0;
		frameHeight = 0;
		unorganizedHAlignmentPoints.clear();
		unorganizedVAlignmentPoints.clear();
		horizontalAlignmentPoints.clear();
		verticalAlignmentPoints.clear();
		distinctDistances.clear();
		distinctAreaCount = 0;
	}

	public static double normalize (double i, double min, double max) {
		return (i - min) / (max - min);
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


