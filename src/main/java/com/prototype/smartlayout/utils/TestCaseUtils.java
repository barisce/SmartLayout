package com.prototype.smartlayout.utils;

import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.WidthHeightRange;
import com.prototype.smartlayout.model.enums.ComponentDimensionEnum;
import com.prototype.smartlayout.model.enums.WidthHeightRangeEnum;
import com.prototype.smartlayout.templates.LayoutContainerTemplates;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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

	/**
	 * This Schengen Visa Form part contains 84 components in the tree.
	 * It has unnecessary grouping and not used containers just to stress test the application
	 *
	 * @return the root of the Schengen Visa Application tree
	 */
	private static LayoutContainer schengenVisaApplicationForm () {
		LayoutContainer surnameField = LayoutContainerTemplates.createLabelTextFieldContainer("Surname Field", "Surname", ComponentDimensionEnum.SMALL_SMALLER, "Enter Surname", ComponentDimensionEnum.SMALL_SMALLER);
		LayoutContainer surnameAtBirthField = LayoutContainerTemplates.createLabelTextFieldContainer("Surname At Birth Field", "<html>Surname<br/>at<br/>Birth</html>", ComponentDimensionEnum.SMALL_SMALLER, "Enter Surname at Birth", ComponentDimensionEnum.SMALL_SMALLER);
		LayoutContainer firstNameField = LayoutContainerTemplates.createLabelTextFieldContainer("First Name Field", "First Name", ComponentDimensionEnum.SMALL_SMALLER, "Enter First Name", ComponentDimensionEnum.SMALL_SMALLER);

		LayoutContainer leftUpperGroup = new LayoutContainer("Left Upper Group", surnameField, surnameAtBirthField, firstNameField);

		LayoutComponent noticeLabel = createComponentFromDictionary("<html>FOR OFFICIAL<br/>USE ONLY</html>", new JLabel(), ComponentDimensionEnum.SMALL_SLACK_SMALLER);
		LayoutContainer dateOfApplicationField = LayoutContainerTemplates.createLabelTextFieldContainer("Date of Application Field", "<html>Date of<br/>Application</html>", ComponentDimensionEnum.SMALLER_SMALLER, "Enter Date of Application", ComponentDimensionEnum.SMALL_SMALLER);
		LayoutContainer applicationNumberField = LayoutContainerTemplates.createLabelTextFieldContainer("Application Number Field", "<html>Application<br/>Number</html>", ComponentDimensionEnum.SMALLER_SMALLER, "Enter Application Number", ComponentDimensionEnum.SMALL_SMALLER);

		LayoutContainer rightUpperGroup = new LayoutContainer("Right Upper Group", noticeLabel, dateOfApplicationField, applicationNumberField);

		LayoutContainer birthDateField = LayoutContainerTemplates.createLabelTextFieldContainer("Birth Date Field", "<html>Date of Birth<br/>(dd-mm-yyyy)</html>", ComponentDimensionEnum.SMALL_SMALLER, "Enter Birth Date", ComponentDimensionEnum.SMALL_SMALLER);

		LayoutContainer placeOfBirthField = LayoutContainerTemplates.createLabelTextFieldContainer("Place of Birth Field", "Place of Birth", ComponentDimensionEnum.SMALL_SMALLER, "Enter Place of Birth", ComponentDimensionEnum.SMALL_SMALLER);
		LayoutContainer countryOfBirthField = LayoutContainerTemplates.createLabelTextFieldContainer("Country of Birth Field", "Country of Birth", ComponentDimensionEnum.SMALL_SMALLER, "Enter Country of Birth", ComponentDimensionEnum.SMALL_SMALLER);
		LayoutContainer birthPlaceGroup = new LayoutContainer("Birth Place Group", placeOfBirthField, countryOfBirthField);

		LayoutContainer nationalityField = LayoutContainerTemplates.createLabelTextFieldContainer("Current Nationality Field", "Current Nationality: ", ComponentDimensionEnum.MEDIUM_SMALLER, "Enter Current Nationality", ComponentDimensionEnum.MEDIUM_SMALLER);
		LayoutContainer nationalityAtBirthField = LayoutContainerTemplates.createLabelTextFieldContainer("Nationality at birth, if different", "Nationality at Birth: ", ComponentDimensionEnum.MEDIUM_SMALLER, "Enter Nationality at Birth", ComponentDimensionEnum.MEDIUM_SMALLER);
		LayoutContainer otherNationalitiesField = LayoutContainerTemplates.createLabelTextFieldContainer("Other Nationalities Field", "Other Nationalities: ", ComponentDimensionEnum.MEDIUM_SMALLER, "Enter Other nationalities", ComponentDimensionEnum.MEDIUM_SMALLER);
		LayoutContainer nationalityGroup = new LayoutContainer("Nationality Group", nationalityField, nationalityAtBirthField, otherNationalitiesField);

		LayoutContainer leftUpperMiddleGroup = new LayoutContainer("Left Upper Middle Group", birthDateField, birthPlaceGroup, nationalityGroup);

		LayoutComponent genderLabel = createComponentFromDictionary("Sex: ", new JLabel(), ComponentDimensionEnum.SMALL_SMALLER);
		ButtonGroup genderButtonGroup = new ButtonGroup();
		LayoutContainer genderMaleCheckbox = LayoutContainerTemplates.createRadioButtonLabelContainer("Gender Male", genderButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Male", ComponentDimensionEnum.SMALLER_TINY);
		LayoutContainer genderFemaleCheckbox = LayoutContainerTemplates.createRadioButtonLabelContainer("Gender Female", genderButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Female", ComponentDimensionEnum.SMALLER_TINY);
		LayoutContainer genderChoicesGroup = new LayoutContainer("Gender Choices Group", genderMaleCheckbox, genderFemaleCheckbox);

		LayoutContainer genderGroup = new LayoutContainer("Gender Group", genderLabel, genderChoicesGroup);

		LayoutComponent civilStatusLabel = createComponentFromDictionary("Civil Status: ", new JLabel(), ComponentDimensionEnum.SMALL_SMALLER);
		ButtonGroup civilStatusButtonGroup = new ButtonGroup();
		LayoutContainer singleRadioButton = LayoutContainerTemplates.createRadioButtonLabelContainer("Single", civilStatusButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Single", ComponentDimensionEnum.SMALL_TINY);
		LayoutContainer marriedRadioButton = LayoutContainerTemplates.createRadioButtonLabelContainer("Married", civilStatusButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Married", ComponentDimensionEnum.SMALL_TINY);
		LayoutContainer divorcedRadioButton = LayoutContainerTemplates.createRadioButtonLabelContainer("Divorced", civilStatusButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Divorced", ComponentDimensionEnum.SMALL_TINY);
		LayoutContainer seperatedRadioButton = LayoutContainerTemplates.createRadioButtonLabelContainer("Seperated", civilStatusButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Seperated", ComponentDimensionEnum.SMALL_TINY);
		LayoutContainer civilStatusChoicesGroup = new LayoutContainer("Civil Status Choices Group", singleRadioButton, marriedRadioButton, divorcedRadioButton, seperatedRadioButton);

		LayoutContainer civilStatusGroup = new LayoutContainer("Civil Status Group", civilStatusLabel, civilStatusChoicesGroup);

		LayoutComponent lodgedLabel = createComponentFromDictionary("Application lodged at: ", new JLabel(), ComponentDimensionEnum.MEDIUM_TINY);
		ButtonGroup lodgePlaceButtonGroup = new ButtonGroup();
		LayoutContainer embassyRadioButton = LayoutContainerTemplates.createRadioButtonLabelContainer("Embassy", lodgePlaceButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Embassy", ComponentDimensionEnum.MEDIUM_TINY);
		LayoutContainer serviceProviderRadioButton = LayoutContainerTemplates.createRadioButtonLabelContainer("Service Provider", lodgePlaceButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Service Provider", ComponentDimensionEnum.MEDIUM_TINY);
		LayoutContainer commercialIntermediaryRadioButton = LayoutContainerTemplates.createRadioButtonLabelContainer("Commercial Intermediary", lodgePlaceButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Commercial Intermediary", ComponentDimensionEnum.MEDIUM_TINY);
		LayoutContainer borderRadioButton = LayoutContainerTemplates.createRadioButtonLabelContainer("Border", lodgePlaceButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Border", ComponentDimensionEnum.MEDIUM_TINY);
		LayoutContainer otherRadioButton = LayoutContainerTemplates.createRadioButtonLabelContainer("Other", lodgePlaceButtonGroup, "", ComponentDimensionEnum.TINY_TINY, "Other", ComponentDimensionEnum.MEDIUM_TINY);
		LayoutContainer lodgePlaceChoicesGroup = new LayoutContainer("Lodge Place Choices Group", embassyRadioButton, serviceProviderRadioButton, commercialIntermediaryRadioButton, borderRadioButton, otherRadioButton);

		LayoutContainer lodgedAtGroup = new LayoutContainer("Lodged At Group", lodgedLabel, lodgePlaceChoicesGroup);

		LayoutContainer firstLine = new LayoutContainer("First Line", leftUpperGroup, leftUpperMiddleGroup, rightUpperGroup);
		LayoutContainer secondLine = new LayoutContainer("Second Line", genderGroup, civilStatusGroup, lodgedAtGroup);

		return new LayoutContainer("M", firstLine, secondLine);
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
			} else if (jComponent instanceof JCheckBox) {
				((JCheckBox) jComponent).setText(lComponent.getLabel()/*MockUtils.generateString((int) (Math.random() * 10.0) + 5)*/);
			} else if (jComponent instanceof JRadioButton) {
				((JRadioButton) jComponent).setText(lComponent.getLabel()/*MockUtils.generateString((int) (Math.random() * 10.0) + 5)*/);
				((JRadioButton) jComponent).setHorizontalAlignment(SwingConstants.CENTER);
			} else if (jComponent instanceof JButton) {
				((JButton) jComponent).setText(lComponent.getLabel()/*MockUtils.generateString((int) (Math.random() * 10.0) + 5)*/);
			} else {
				// Custom made component
			}
			panel.add(jComponent);
		});
	}
}
