package com.prototype.smartlayout.dictionary;

import com.prototype.smartlayout.model.enums.ComponentDimensionEnum;
import javax.swing.JComponent;

public class ComponentDictionary {
	JComponent component;
	public final int minWidth;
	public final int maxWidth;
	public final int minHeight;
	public final int maxHeight;

	ComponentDictionary (JComponent component, ComponentDimensionEnum sizeType) {
		this.component = component;
		this.minWidth = sizeType.minWidth;
		this.maxWidth = sizeType.maxWidth;
		this.minHeight = sizeType.minHeight;
		this.maxHeight = sizeType.maxHeight;
	}
}
