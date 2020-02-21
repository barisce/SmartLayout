package com.prototype.smartlayout.utils;

import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.WidthHeightRange;
import com.prototype.smartlayout.model.enums.ComponentDimensionEnum;
import com.prototype.smartlayout.model.enums.WidthHeightRangeEnum;
import com.prototype.smartlayout.templates.LayoutContainerTemplates;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TestCaseUtils {

	public static Map<LayoutComponent, JComponent> jComponentMap;

	/**
	 * Creates a new component from dictionary under this layout.
	 *
	 * @param label     The label of the new component.
	 * @param dimension The enum value that will contain the range info.
	 * @return The component to be created.
	 */
	public static LayoutComponent createComponentFromDictionary (String label, JComponent component, ComponentDimensionEnum dimension) {
		LayoutComponent c = new LayoutComponent(label, new WidthHeightRange(WidthHeightRangeEnum.SINGLE, dimension));
		jComponentMap.put(c, component);
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
			case 1:
				return dictionaryTestCase1();
			case 2:
				return dictionaryTestCase2();
			case 3:
				return dictionaryTestCase3();
			case 4:
				return dictionaryTestCaseWithComponents1();
			case 5:
				return dictionaryTestCaseWithComponents2();
			case 6:
			default:
				return schengenVisaApplicationForm();
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
			LayoutComponent compA = createComponentFromDictionary("A", new JLabel(), ComponentDimensionEnum.SMALL_SMALL);
			LayoutComponent compB = createComponentFromDictionary("B", new JButton(), ComponentDimensionEnum.SMALL_SLACK_SMALL_SLACK);
			LayoutComponent compC = createComponentFromDictionary("C", new JButton(), ComponentDimensionEnum.SMALL_SLACK_SMALL_SLACK);
			LayoutComponent compD = createComponentFromDictionary("D", new JTextField(), ComponentDimensionEnum.LARGE_SLACK_LARGE_SLACK);
			LayoutComponent compE = createComponentFromDictionary("E", new JTextArea(), ComponentDimensionEnum.SMALL_LARGE_SLACK);
			LayoutComponent compF = createComponentFromDictionary("F", new JComboBox<String>(), ComponentDimensionEnum.SMALL_LARGE_SLACK);
			LayoutComponent compG = createComponentFromDictionary("G", new JRadioButton(), ComponentDimensionEnum.LARGE_SLACK_SMALL);

			LayoutContainer contY = new LayoutContainer("Y", compE, compF);
			LayoutContainer contZ = new LayoutContainer("Z", contY, compG);
			LayoutContainer contX = new LayoutContainer("X", compA, compB, compC);
			return new LayoutContainer("M", contZ, contX, compD);
		}
	}

	private static LayoutContainer dictionaryTestCase2 () {
		LayoutComponent compA = createComponentFromDictionary("A", new JLabel(), ComponentDimensionEnum.SMALL_SMALL);
		LayoutComponent compB = createComponentFromDictionary("B", new JButton(), ComponentDimensionEnum.SMALL_SLACK_SMALL_SLACK);
		LayoutComponent compC = createComponentFromDictionary("C", new JButton(), ComponentDimensionEnum.SMALL_SLACK_SMALL_SLACK);
		LayoutComponent compD = createComponentFromDictionary("D", new JTextField(), ComponentDimensionEnum.LARGE_SLACK_LARGE_SLACK);
		LayoutComponent compE = createComponentFromDictionary("E", new JTextArea(), ComponentDimensionEnum.SMALL_LARGE_SLACK);
		LayoutComponent compF = createComponentFromDictionary("F", new JComboBox<String>(), ComponentDimensionEnum.SMALL_LARGE_SLACK);
		LayoutComponent compG = createComponentFromDictionary("G", new JRadioButton(), ComponentDimensionEnum.LARGE_SLACK_SMALL);
		LayoutComponent compH = createComponentFromDictionary("H", new JLabel(), ComponentDimensionEnum.SMALL_SLACK_SMALL_SLACK);
		LayoutComponent compI = createComponentFromDictionary("I", new JButton(), ComponentDimensionEnum.LARGE_SLACK_LARGE_SLACK);
		LayoutComponent compJ = createComponentFromDictionary("J", new JButton(), ComponentDimensionEnum.LARGE_SLACK_LARGE_SLACK);
		LayoutComponent compK = createComponentFromDictionary("K", new JTextField(), ComponentDimensionEnum.SMALL_SMALL);
		LayoutComponent compL = createComponentFromDictionary("L", new JTextArea(), ComponentDimensionEnum.SMALL_SMALL);

		LayoutContainer contY = new LayoutContainer("Y", compE, compF);
		LayoutContainer contT = new LayoutContainer("T", compK, compL);
		LayoutContainer contR = new LayoutContainer("R", contT, compJ);
		LayoutContainer contN = new LayoutContainer("N", compG, compH, compI);
		LayoutContainer contZ = new LayoutContainer("Z", contY, contR, contN);
		LayoutContainer contX = new LayoutContainer("X", compA, compB, compC);
		return new LayoutContainer("M", contZ, contX, compD);
	}

	private static LayoutContainer dictionaryTestCase3 () {
		LayoutComponent compA = createComponentFromDictionary("A", new JLabel(), ComponentDimensionEnum.SMALL_SMALL);
		LayoutComponent compB = createComponentFromDictionary("B", new JButton(), ComponentDimensionEnum.LARGE_LARGE);
		LayoutComponent compC = createComponentFromDictionary("C", new JButton(), ComponentDimensionEnum.SMALL_SLACK_SMALL_SLACK);
		LayoutComponent compD = createComponentFromDictionary("D", new JTextField(), ComponentDimensionEnum.LARGE_SLACK_LARGE_SLACK);
		LayoutComponent compE = createComponentFromDictionary("E", new JTextArea(), ComponentDimensionEnum.SMALL_LARGE_SLACK);
		LayoutComponent compF = createComponentFromDictionary("F", new JComboBox<String>(), ComponentDimensionEnum.SMALL_LARGE_SLACK);
		LayoutComponent compG = createComponentFromDictionary("G", new JRadioButton(), ComponentDimensionEnum.LARGE_SLACK_SMALL);
		LayoutComponent compH = createComponentFromDictionary("H", new JLabel(), ComponentDimensionEnum.SMALL_SLACK_SMALL_SLACK);
		LayoutComponent compI = createComponentFromDictionary("I", new JButton(), ComponentDimensionEnum.LARGE_SLACK_LARGE_SLACK);
		LayoutComponent compJ = createComponentFromDictionary("J", new JButton(), ComponentDimensionEnum.LARGE_SLACK_LARGE_SLACK);
		LayoutComponent compK = createComponentFromDictionary("K", new JTextField(), ComponentDimensionEnum.SMALL_SMALL);
		LayoutComponent compL = createComponentFromDictionary("L", new JTextArea(), ComponentDimensionEnum.SMALL_SMALL);

		LayoutContainer contY = new LayoutContainer("Y", compE, compF);
		LayoutContainer contT = new LayoutContainer("T", compK, compL);
		LayoutContainer contR = new LayoutContainer("R", contT, compJ);
		LayoutContainer contN = new LayoutContainer("N", compG, compH, compI);
		LayoutContainer contZ = new LayoutContainer("Z", contY, contR, contN);
		LayoutContainer contX = new LayoutContainer("X", compA, compB, compC);
		return new LayoutContainer("M", contZ, contX, compD);
	}

	private static LayoutContainer dictionaryTestCaseWithComponents1 () {
		LayoutComponent compA = createComponentFromDictionary("A", new JLabel(), ComponentDimensionEnum.SMALLER_TINY);
		LayoutComponent compB = createComponentFromDictionary("B", new JTextField(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compC = createComponentFromDictionary("C", new JComboBox<>(), ComponentDimensionEnum.MEDIUM_TINY);
		LayoutComponent compD = createComponentFromDictionary("D", new JTextField(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compE = createComponentFromDictionary("E", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compF = createComponentFromDictionary("F", new JComboBox<>(), ComponentDimensionEnum.MEDIUM_TINY);
		LayoutComponent compG = createComponentFromDictionary("G", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compH = createComponentFromDictionary("H", new JTextField(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compI = createComponentFromDictionary("I", new JButton(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compJ = createComponentFromDictionary("J", new JButton(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compK = createComponentFromDictionary("K", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compL = createComponentFromDictionary("L", new JTextField(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compM = createComponentFromDictionary("M", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compN = createComponentFromDictionary("N", new JTextField(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compO = createComponentFromDictionary("O", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compP = createComponentFromDictionary("P", new JComboBox<>(), ComponentDimensionEnum.MEDIUM_TINY);
		LayoutComponent compQ = createComponentFromDictionary("Q", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compR = createComponentFromDictionary("R", new JTextField(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compS = createComponentFromDictionary("S", new JButton(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compT = createComponentFromDictionary("T", new JButton(), ComponentDimensionEnum.SMALL_SLACK_TINY);

		LayoutContainer cont1 = new LayoutContainer("1", compA, compB, compC);
		LayoutContainer cont2 = new LayoutContainer("2", compE, compD);
		LayoutContainer cont3 = new LayoutContainer("3", cont1, cont2);
		LayoutContainer cont4 = new LayoutContainer("4", compG, compF);
		LayoutContainer cont5 = new LayoutContainer("5", compH, compI);
		LayoutContainer cont6 = new LayoutContainer("6", cont4, cont5, compJ);
		LayoutContainer cont7 = new LayoutContainer("7", cont3, cont6);
		LayoutContainer cont8 = new LayoutContainer("8", compK, compL);
		LayoutContainer cont9 = new LayoutContainer("9", compM, compN);
		LayoutContainer cont10 = new LayoutContainer("10", compO, compP);
		LayoutContainer cont11 = new LayoutContainer("11", cont8, cont9, cont10);
		LayoutContainer cont12 = new LayoutContainer("12", compQ, compR);
		LayoutContainer cont13 = new LayoutContainer("13", cont12, compS, compT);
		return new LayoutContainer("Root", cont7, cont11, cont13);
	}

	private static LayoutContainer dictionaryTestCaseWithComponents2 () {
		LayoutComponent compA = createComponentFromDictionary("A", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compB = createComponentFromDictionary("B", new JTextField(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compC = createComponentFromDictionary("C", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compD = createComponentFromDictionary("D", new JTextField(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compE = createComponentFromDictionary("E", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compF = createComponentFromDictionary("F", new JComboBox<>(), ComponentDimensionEnum.MEDIUM_TINY);
		LayoutComponent compG = createComponentFromDictionary("G", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL_SLACK);
		LayoutComponent compH = createComponentFromDictionary("H", new JTextField(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compI = createComponentFromDictionary("I", new JButton(), ComponentDimensionEnum.SMALL_SLACK_TINY);
		LayoutComponent compJ = createComponentFromDictionary("J", new JButton(), ComponentDimensionEnum.SMALL_SLACK_TINY);

		// first line
		LayoutContainer contY = new LayoutContainer("Y", compA, compB);
		// second line
		LayoutContainer contT = new LayoutContainer("T", compC, compD);
		// third line
		LayoutContainer contR = new LayoutContainer("R", compE, compF);
		// fourth line
		LayoutContainer contN = new LayoutContainer("N", compG, compH);
		// buttons
		LayoutContainer contZ = new LayoutContainer("Z", compI, compJ);
		// first two lines
		LayoutContainer contX = new LayoutContainer("X", contY, contT);
		// last two lines
		LayoutContainer contQ = new LayoutContainer("Q", contR, contN);
		// all lines
		LayoutContainer contU = new LayoutContainer("U", contX, contQ);
		// all lines and buttons
		return new LayoutContainer("M", contU, contZ);
	}

	private static LayoutContainer schengenVisaApplicationForm () {
		LayoutContainer surnameField = LayoutContainerTemplates.createLabelTextFieldContainer("Surname Field", "Surname", ComponentDimensionEnum.TINY_SMALL_SLACK, "Enter Surname", ComponentDimensionEnum.TINY_LARGE_SLACK);
		LayoutContainer surnameAtBirthField = LayoutContainerTemplates.createLabelTextFieldContainer("Surname At Birth Field", "Surname at Birth", ComponentDimensionEnum.TINY_SMALL_SLACK, "Enter Surname at Birth", ComponentDimensionEnum.TINY_LARGE_SLACK);
		LayoutContainer firstNameField = LayoutContainerTemplates.createLabelTextFieldContainer("First Name Field", "First Name", ComponentDimensionEnum.SMALL_SMALL_SLACK, "Enter First Name", ComponentDimensionEnum.SMALL_LARGE_SLACK);

		LayoutContainer leftUpperGroup = new LayoutContainer("Left Upper Group", surnameField, surnameAtBirthField, firstNameField);

		LayoutComponent noticeLabel = createComponentFromDictionary("FOR OFFICIAL USE ONLY", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL);
		LayoutComponent dateOfApplicationLabel = createComponentFromDictionary("Date of Application", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL);
		LayoutComponent applicationNumberLabel = createComponentFromDictionary("Application number", new JLabel(), ComponentDimensionEnum.SMALLER_SMALL);
		LayoutContainer rightUpperGroup = new LayoutContainer("Right Upper Group", noticeLabel, dateOfApplicationLabel, applicationNumberLabel);

		LayoutContainer birthDateField = LayoutContainerTemplates.createLabelTextFieldContainer("Birth Date Field", "Date of Birth (day-month-year)", ComponentDimensionEnum.SMALL_TINY, "Enter Birth Date", ComponentDimensionEnum.LARGE_SLACK_SMALL);

		LayoutContainer placeOfBirthField = LayoutContainerTemplates.createLabelTextFieldContainer("Place of Birth Field", "Place of Birth", ComponentDimensionEnum.TINY_TINY, "Enter Place of Birth", ComponentDimensionEnum.TINY_TINY);
		LayoutContainer countryOfBirthField = LayoutContainerTemplates.createLabelTextFieldContainer("Country of Birth Field", "Country of Birth", ComponentDimensionEnum.TINY_TINY, "Enter Country of Birth", ComponentDimensionEnum.TINY_SMALLER);
		LayoutContainer birthPlaceGroup = new LayoutContainer("Birth Place Group", placeOfBirthField, countryOfBirthField);

		LayoutComponent nationalityLabel = createComponentFromDictionary("Current nationality: ", new JLabel(), ComponentDimensionEnum.TINY_TINY);
		LayoutComponent nationalityAtBirthLabel = createComponentFromDictionary("Nationality at birth,\nif different", new JLabel(), ComponentDimensionEnum.TINY_SMALLER);
		LayoutComponent otherNationalitiesLabel = createComponentFromDictionary("Other nationalities: ", new JLabel(), ComponentDimensionEnum.TINY_TINY);
		LayoutContainer nationalityGroup = new LayoutContainer("Nationality Group", nationalityLabel, nationalityAtBirthLabel, otherNationalitiesLabel);
		LayoutContainer leftUpperMiddleGroup = new LayoutContainer("Left Upper Middle Group", birthDateField, birthPlaceGroup, nationalityGroup);

		return new LayoutContainer("M", leftUpperGroup, leftUpperMiddleGroup, rightUpperGroup);
	}

	public static void createComponentsOfTree (JPanel panel) {
		jComponentMap.forEach((lComponent, jComponent) -> {
			jComponent.setBounds(lComponent.getAssignedX(), lComponent.getAssignedY(),
					lComponent.getAssignedWidth(), lComponent.getAssignedHeight());
			jComponent.setToolTipText(lComponent.getLabel());
			if (jComponent instanceof JLabel) {
				((JLabel) jComponent).setText(lComponent.getLabel()/*MockUtils.generateString((int) (Math.random() * 10.0) + 5)*/);
			} else if (jComponent instanceof JComboBox) {
				((JComboBox) jComponent).addItem(lComponent.getLabel()/*MockUtils.generateString((int) (Math.random() * 10.0) + 5)*/);
				((JComboBox) jComponent).addItem("Item 2"/*MockUtils.generateString((int) (Math.random() * 10.0) + 5)*/);
				((JComboBox) jComponent).addItem("Item 3"/*MockUtils.generateString((int) (Math.random() * 10.0) + 5)*/);
			} else if (jComponent instanceof JButton) {
				((JButton) jComponent).setText(lComponent.getLabel()/*MockUtils.generateString((int) (Math.random() * 10.0) + 5)*/);
			} else {
				// Custom made component
			}
			panel.add(jComponent);
		});
	}
}
