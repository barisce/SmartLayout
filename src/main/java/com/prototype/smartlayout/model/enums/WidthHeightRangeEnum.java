package com.prototype.smartlayout.model.enums;

public enum WidthHeightRangeEnum {
	SINGLE(0),
	HORIZONTAL(1),
	VERTICAL(2),//GRID_LAYOUT
	OTHER(3);

	public final int id;

	WidthHeightRangeEnum (int id) {
		this.id = id;
	}
}
