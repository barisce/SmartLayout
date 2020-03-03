package com.prototype.smartlayout.templates;

import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.enums.ComponentDimensionEnum;
import com.prototype.smartlayout.utils.TestCaseUtils;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class LayoutContainerTemplates {

	public static LayoutContainer createLabelTextFieldContainer (String containerName, String labelName, ComponentDimensionEnum labelSize, String textFieldName, ComponentDimensionEnum textFieldSize) {
		LayoutComponent label = TestCaseUtils.createComponentFromDictionary(labelName, new JLabel(), labelSize);
		LayoutComponent textField = TestCaseUtils.createComponentFromDictionary(textFieldName, new JTextField(), textFieldSize);
		return new LayoutContainer(containerName, label, textField);
	}

	public static LayoutComponent createCheckboxComponent (String checkboxName, ComponentDimensionEnum checkboxSize) {
		return TestCaseUtils.createComponentFromDictionary(checkboxName, new JCheckBox(), checkboxSize);
	}

	public static LayoutContainer createCheckboxLabelContainer (String containerName, String checkboxName, ComponentDimensionEnum checkboxSize, String labelName, ComponentDimensionEnum labelSize) {
		LayoutComponent label = TestCaseUtils.createComponentFromDictionary(labelName, new JLabel(), labelSize);
		LayoutComponent checkBox = TestCaseUtils.createComponentFromDictionary(checkboxName, new JCheckBox(), checkboxSize);
		return new LayoutContainer(containerName, label, checkBox);
	}

	public static LayoutComponent createRadioButtonComponent (ButtonGroup group, String radioButtonName, ComponentDimensionEnum radioButtonSize) {
		JRadioButton radioButton = new JRadioButton();
		if (group != null) {
			group.add(radioButton);
		}
		return TestCaseUtils.createComponentFromDictionary(radioButtonName, radioButton, radioButtonSize);
	}

	public static LayoutContainer createRadioButtonLabelContainer (String containerName, ButtonGroup group, String radioButtonName, ComponentDimensionEnum radioButtonSize, String labelName, ComponentDimensionEnum labelSize) {
		LayoutComponent label = TestCaseUtils.createComponentFromDictionary(labelName, new JLabel(), labelSize);
		JRadioButton radioButton = new JRadioButton();
		LayoutComponent radioButtonComponent = TestCaseUtils.createComponentFromDictionary(radioButtonName, radioButton, radioButtonSize);
		if (group != null) {
			group.add(radioButton);
		}
		return new LayoutContainer(containerName, label, radioButtonComponent);
	}
}
