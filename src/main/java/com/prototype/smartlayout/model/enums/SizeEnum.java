package com.prototype.smartlayout.model.enums;

public enum SizeEnum {
	TINY(20, 50),
	SMALLER(50, 100),
	SMALL(100, 200),
	SMALL_SLACK(50, 300),
	MEDIUM(200, 300),
	LARGE_SLACK(300, 900),
	LARGE(300, 400),
	LARGER(400, 600),
	HUGE(600, 900),
	GIGANTIC(900, 2000);
	public final int min;
	public final int max;

	SizeEnum (int min, int max) {
		this.min = min;
		this.max = max;
	}
}