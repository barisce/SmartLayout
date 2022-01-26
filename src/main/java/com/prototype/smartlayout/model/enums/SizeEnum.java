package com.prototype.smartlayout.model.enums;

public enum SizeEnum {
	TINY(20, 50),
	SMALLER(40, 110),
	SMALL(90, 210),
	SMALL_SLACK(40, 310),
	MEDIUM(150, 310),
	LARGE_SLACK(190, 910),
	LARGE(290, 410),
	LARGER(590, 610),
	HUGE(590, 910),
	GIGANTIC(890, 2010);
	public final int min;
	public final int max;

	SizeEnum (int min, int max) {
		this.min = min;
		this.max = max;
	}
}