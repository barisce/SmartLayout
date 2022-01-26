package com.prototype.smartlayout.model.enums;

@Deprecated
public enum ComponentTypeEnum {

	LABEL(0),
	CANVAS(1),
	TABS(2), // TODO: These need to be at top always
	HEADING(3),
	FOOTER(4),
	TREE(5), // TODO: How to handle dynamic structures like Accordion or Tree,
	ACCORDION(6), // give min 100 but max a lot like 1000
	LIST(7),
	FILE_CHOOSER(8),
	FILE_DROP_AREA(9),
	DATATABLE(10), // TODO: What if endless scroller?
	TEXT_FIELD(11),
	TEXT_AREA(12),
	TEXT_EDITOR(13),
	COMBO_BOX(14), // Dropdown
	CHECK_BOX(15),
	BUTTON(16),
	TOGGLE_BUTTON(17),
	RADIO_BUTTON(18),
	PROGRESS_BAR(19),
	TOOLBAR(20),
	DIALOG(21),
	SIDEBAR(22), // or sidemenu
	DATE_PICKER(23),
	CALENDAR(24),
	SLIDER(25), // Ex: Slide value from 0 to 100
	KNOB(26), // Ex: Slider as pie chart gauge,
	PIE_CHART(27), // Graph but more squareish
	GRAPH(28),
	SPACER(29),
	SEPARATOR(30),
	STICKY_MENU(31), // TODO: What if we want menu to stick to top of the page?
	GOOGLE_MAPS(32),
	VIDEO(33),
	CAPTCHA(34), // 300x75 / max +50
	QR_CODE(35); // 125x125 90-150

	public final int id;

	ComponentTypeEnum (int id) {
		this.id = id;
	}
}
