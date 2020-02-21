package com.prototype.smartlayout.templates;

import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.enums.ComponentDimensionEnum;
import com.prototype.smartlayout.utils.TestCaseUtils;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LayoutContainerTemplates {

	public static LayoutContainer createLabelTextFieldContainer (String containerName, String labelName, ComponentDimensionEnum labelSize, String textFieldName, ComponentDimensionEnum textFieldSize) {
		LayoutComponent label = TestCaseUtils.createComponentFromDictionary(labelName, new JLabel(), labelSize);
		LayoutComponent textField = TestCaseUtils.createComponentFromDictionary(textFieldName, new JTextField(), textFieldSize);
		return new LayoutContainer(containerName, label, textField);
	}
}
