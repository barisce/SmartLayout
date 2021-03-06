package com.prototype.smartlayout.utils;

import com.prototype.smartlayout.dictionary.ComponentDictionary;
import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.WidthHeightRange;
import com.prototype.smartlayout.model.enums.WidthHeightRangeEnum;
import java.util.Vector;

public class TestCaseUtils {

	public static Vector<LayoutComponent> components;

	/**
	 * Creates a new component under this layout.
	 *
	 * @param label The label of the new component.
	 * @param range The width and height range of the new component.
	 * @return The component to be created.
	 */
	public static LayoutComponent createComponent (String label, WidthHeightRange range) {
		LayoutComponent c = new LayoutComponent(label, range);
		components.add(c);
		return c;
	}

	/**
	 * Creates a new component from dictionary under this layout.
	 *
	 * @param label The label of the new component.
	 * @param item  The dictionary value that will contain the range info.
	 * @return The component to be created.
	 */
	public static LayoutComponent createComponentFromDictionary (String label, ComponentDictionary item) {
		LayoutComponent c = new LayoutComponent(item.toString(), new WidthHeightRange(WidthHeightRangeEnum.SINGLE, item.minWidth, item.maxWidth, item.minHeight, item.maxHeight));
		components.add(c);
		return c;
	}

	/**
	 * This method executes scenario by given testNumber.
	 *
	 * @param testNumber
	 * @return the root container
	 */
	public static LayoutContainer executeTest (int testNumber) {
		switch (testNumber) {
			case 2:
				return testCase2();
			case 3:
				return testCase3();
			case 4:
				return dictionaryTestCase1();
			case 5:
				return dictionaryTestCase2();
			case 6:
				return dictionaryTestCase3();
			case 1:
			default:
				return testCase1();
		}
	}

	private static LayoutContainer testCase1 () {
		/*
		A diagram to show what this test is about:

		M
		+--------------------------------+--------------------------------+
		|                                |                                |
		Z                                X                                D
		+---------------------+          +----------+----------+
		|                     |          |          |          |
		Y                     G          A          B          C
		+----------+
		|          |
		E          F
		*/

		{ // The braces are for hiding chunks of code.
			LayoutComponent compA =
					createComponent(
							"A",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 50, 250, 50, 250));
			LayoutComponent compB =
					createComponent(
							"B",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 50, 250, 50, 250));
			LayoutComponent compC =
					createComponent(
							"C",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 50, 250, 50, 250));
			LayoutComponent compD =
					createComponent(
							"D",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 400, 200, 400));
			LayoutComponent compE =
					createComponent(
							"E",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 500, 50, 150));
			LayoutComponent compF =
					createComponent(
							"F",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 500, 50, 150));
			LayoutComponent compG =
					createComponent(
							"G",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 400, 100, 400));

			LayoutContainer contY = new LayoutContainer("Y");
			contY.addComponent(compE);
			contY.addComponent(compF);

			LayoutContainer contZ = new LayoutContainer("Z");
			contZ.addComponent(contY);
			contZ.addComponent(compG);

			LayoutContainer contX = new LayoutContainer("X");
			contX.addComponent(compA);
			contX.addComponent(compB);
			contX.addComponent(compC);

			LayoutContainer contM = new LayoutContainer("M");
			contM.addComponent(contZ);
			contM.addComponent(contX);
			contM.addComponent(compD);

			return contM;
		}
	}

	private static LayoutContainer testCase2 () {
		/*
		A diagram to show what this test is about:

		                      M
		           +----------+----------+---------+
		           |                               |
		           K                               Z
		+----------+                    +----------+
		|          |                    |          |
		A          Y                    E          T
				   +----------+                    +----------+
				   |          |                    |          |
				   X          D                    F          G
		+----------+
		|          |
		B          C
		*/

		{ // The braces are for hiding chunks of code.
			LayoutComponent compA =
					createComponent(
							"A",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 50, 250, 50, 250));
			LayoutComponent compB =
					createComponent(
							"B",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 50, 250, 50, 250));
			LayoutComponent compC =
					createComponent(
							"C",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 50, 250, 50, 250));
			LayoutComponent compD =
					createComponent(
							"D",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 450, 200, 450));
			LayoutComponent compE =
					createComponent(
							"E",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 500, 50, 250));
			LayoutComponent compF =
					createComponent(
							"F",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 500, 50, 250));
			LayoutComponent compG =
					createComponent(
							"G",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 150, 450, 150, 350));

			LayoutContainer contX = new LayoutContainer("X");
			contX.addComponent(compB);
			contX.addComponent(compC);

			LayoutContainer contY = new LayoutContainer("Y");
			contY.addComponent(contX);
			contY.addComponent(compD);

			LayoutContainer contK = new LayoutContainer("K");
			contK.addComponent(compA);
			contK.addComponent(contY);

			LayoutContainer contT = new LayoutContainer("T");
			contT.addComponent(compF);
			contT.addComponent(compG);

			LayoutContainer contZ = new LayoutContainer("Z");
			contZ.addComponent(compE);
			contZ.addComponent(contT);


			LayoutContainer contM = new LayoutContainer("M");
			contM.addComponent(contK);
			contM.addComponent(contZ);

			return contM;
		}
	}

	private static LayoutContainer testCase3 () {
		/*
		A diagram to show what this test is about:

		                      M
		           +----------+----------+---------+
		           |          |                    |
		           K          D                    Z
		+----------+                    +----------+----------+
		|          |                    |          |          |
		A          Y                    E          F          G
				   +----------+
				   |          |
				   B          C
		*/

		{ // The braces are for hiding chunks of code.
			LayoutComponent compA =
					createComponent(
							"A",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 250, 150, 400));
			LayoutComponent compB =
					createComponent(
							"B",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 300, 150, 400));
			LayoutComponent compC =
					createComponent(
							"C",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 350, 150, 400));
			LayoutComponent compD =
					createComponent(
							"D",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 150, 300, 250, 500));
			LayoutComponent compE =
					createComponent(
							"E",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 50, 150, 150, 200));
			LayoutComponent compF =
					createComponent(
							"F",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 250, 450, 200, 300));
			LayoutComponent compG =
					createComponent(
							"G",
							new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 300, 250, 300));

			LayoutContainer contY = new LayoutContainer("Y");
			contY.addComponent(compB);
			contY.addComponent(compC);

			LayoutContainer contK = new LayoutContainer("K");
			contK.addComponent(contY);
			contK.addComponent(compA);

			LayoutContainer contZ = new LayoutContainer("Z");
			contZ.addComponent(compE);
			contZ.addComponent(compF);
			contZ.addComponent(compG);


			LayoutContainer contM = new LayoutContainer("M");
			contM.addComponent(contK);
			contM.addComponent(compD);
			contM.addComponent(contZ);

			return contM;
		}
	}

	private static LayoutContainer dictionaryTestCase1 () {
		/*
		A diagram to show what this test is about:

		M
		+--------------------------------+--------------------------------+
		|                                |                                |
		Z                                X                                D
		+---------------------+          +----------+----------+
		|                     |          |          |          |
		Y                     G          A          B          C
		+----------+
		|          |
		E          F
		*/

		{ // The braces are for hiding chunks of code.
			LayoutComponent compA = createComponentFromDictionary("A", ComponentDictionary.TEST_BOX_SMALL_100x100_200x200);
			LayoutComponent compB = createComponentFromDictionary("B", ComponentDictionary.TEST_BOX_MEDIUM_100x100_400x400);
			LayoutComponent compC = createComponentFromDictionary("C", ComponentDictionary.TEST_BOX_MEDIUM_100x100_400x400);
			LayoutComponent compD = createComponentFromDictionary("D", ComponentDictionary.TEST_BOX_LARGE_100x100_600x600);
			LayoutComponent compE = createComponentFromDictionary("E", ComponentDictionary.TEST_BOX_TALL_100x100_200x600);
			LayoutComponent compF = createComponentFromDictionary("F", ComponentDictionary.TEST_BOX_TALL_100x100_200x600);
			LayoutComponent compG = createComponentFromDictionary("G", ComponentDictionary.TEST_BOX_WIDE_100x100_600x200);

			LayoutContainer contY = new LayoutContainer("Y");
			contY.addComponent(compE);
			contY.addComponent(compF);

			LayoutContainer contZ = new LayoutContainer("Z");
			contZ.addComponent(contY);
			contZ.addComponent(compG);

			LayoutContainer contX = new LayoutContainer("X");
			contX.addComponent(compA);
			contX.addComponent(compB);
			contX.addComponent(compC);

			LayoutContainer contM = new LayoutContainer("M");
			contM.addComponent(contZ);
			contM.addComponent(contX);
			contM.addComponent(compD);

			return contM;
		}
	}

	private static LayoutContainer dictionaryTestCase2 () {
		LayoutComponent compA = createComponentFromDictionary("A", ComponentDictionary.TEST_BOX_SMALL_100x100_200x200);
		LayoutComponent compB = createComponentFromDictionary("B", ComponentDictionary.TEST_BOX_MEDIUM_100x100_400x400);
		LayoutComponent compC = createComponentFromDictionary("C", ComponentDictionary.TEST_BOX_MEDIUM_100x100_400x400);
		LayoutComponent compD = createComponentFromDictionary("D", ComponentDictionary.TEST_BOX_LARGE_100x100_600x600);
		LayoutComponent compE = createComponentFromDictionary("E", ComponentDictionary.TEST_BOX_TALL_100x100_200x600);
		LayoutComponent compF = createComponentFromDictionary("F", ComponentDictionary.TEST_BOX_TALL_100x100_200x600);
		LayoutComponent compG = createComponentFromDictionary("G", ComponentDictionary.TEST_BOX_WIDE_100x100_600x200);
		LayoutComponent compH = createComponentFromDictionary("H", ComponentDictionary.TEST_BOX_MEDIUM_100x100_400x400);
		LayoutComponent compI = createComponentFromDictionary("I", ComponentDictionary.TEST_BOX_LARGE_100x100_600x600);
		LayoutComponent compJ = createComponentFromDictionary("J", ComponentDictionary.TEST_BOX_LARGE_100x100_600x600);
		LayoutComponent compK = createComponentFromDictionary("K", ComponentDictionary.TEST_BOX_SMALL_100x100_200x200);
		LayoutComponent compL = createComponentFromDictionary("L", ComponentDictionary.TEST_BOX_SMALL_100x100_200x200);

		LayoutContainer contY = new LayoutContainer("Y");
		contY.addComponent(compE);
		contY.addComponent(compF);

		LayoutContainer contT = new LayoutContainer("T");
		contT.addComponent(compK);
		contT.addComponent(compL);

		LayoutContainer contR = new LayoutContainer("R");
		contR.addComponent(contT);
		contR.addComponent(compJ);

		LayoutContainer contN = new LayoutContainer("N");
		contN.addComponent(compG);
		contN.addComponent(compH);
		contN.addComponent(compI);

		LayoutContainer contZ = new LayoutContainer("Z");
		contZ.addComponent(contY);
		contZ.addComponent(contR);
		contZ.addComponent(contN);

		LayoutContainer contX = new LayoutContainer("X");
		contX.addComponent(compA);
		contX.addComponent(compB);
		contX.addComponent(compC);

		LayoutContainer contM = new LayoutContainer("M");
		contM.addComponent(contZ);
		contM.addComponent(contX);
		contM.addComponent(compD);

		return contM;
	}

	private static LayoutContainer dictionaryTestCase3 () {
		LayoutComponent compA = createComponentFromDictionary("A", ComponentDictionary.TEST_BOX_SMALL_100x100_200x200);
		LayoutComponent compB = createComponentFromDictionary("B", ComponentDictionary.TEST_BOX_MEDIUM_200x200_400x400);
		LayoutComponent compC = createComponentFromDictionary("C", ComponentDictionary.TEST_BOX_MEDIUM_100x100_400x400);
		LayoutComponent compD = createComponentFromDictionary("D", ComponentDictionary.TEST_BOX_LARGE_100x100_600x600);
		LayoutComponent compE = createComponentFromDictionary("E", ComponentDictionary.TEST_BOX_TALL_100x100_200x600);
		LayoutComponent compF = createComponentFromDictionary("F", ComponentDictionary.TEST_BOX_TALL_100x100_200x600);
		LayoutComponent compG = createComponentFromDictionary("G", ComponentDictionary.TEST_BOX_WIDE_100x100_600x200);
		LayoutComponent compH = createComponentFromDictionary("H", ComponentDictionary.TEST_BOX_MEDIUM_100x100_400x400);
		LayoutComponent compI = createComponentFromDictionary("I", ComponentDictionary.TEST_BOX_LARGE_200x200_600x600);
		LayoutComponent compJ = createComponentFromDictionary("J", ComponentDictionary.TEST_BOX_LARGE_200x200_600x600);
		LayoutComponent compK = createComponentFromDictionary("K", ComponentDictionary.TEST_BOX_SMALL_100x100_200x200);
		LayoutComponent compL = createComponentFromDictionary("L", ComponentDictionary.TEST_BOX_SMALL_100x100_200x200);

		LayoutContainer contY = new LayoutContainer("Y");
		contY.addComponent(compE);
		contY.addComponent(compF);

		LayoutContainer contT = new LayoutContainer("T");
		contT.addComponent(compK);
		contT.addComponent(compL);

		LayoutContainer contR = new LayoutContainer("R");
		contR.addComponent(contT);
		contR.addComponent(compJ);

		LayoutContainer contN = new LayoutContainer("N");
		contN.addComponent(compG);
		contN.addComponent(compH);
		contN.addComponent(compI);

		LayoutContainer contZ = new LayoutContainer("Z");
		contZ.addComponent(contY);
		contZ.addComponent(contR);
		contZ.addComponent(contN);

		LayoutContainer contX = new LayoutContainer("X");
		contX.addComponent(compA);
		contX.addComponent(compB);
		contX.addComponent(compC);

		LayoutContainer contM = new LayoutContainer("M");
		contM.addComponent(contZ);
		contM.addComponent(contX);
		contM.addComponent(compD);

		return contM;
	}
}
