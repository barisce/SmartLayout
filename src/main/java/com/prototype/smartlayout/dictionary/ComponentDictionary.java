package com.prototype.smartlayout.dictionary;

import com.prototype.smartlayout.model.enums.ComponentSizeEnum;
import com.prototype.smartlayout.model.enums.ComponentTypeEnum;

@SuppressWarnings("unused")
public enum ComponentDictionary {
	// TODO : Make a language that parses a string such as
	// L = {{c[1], c[2]},{c[2], c[3], c[1]}} where c[1] = ComponentDictionary.get(1);
	// L = H{V{c[1], c[2]},{c[2], c[3], c[1]}} - Vertical and Horizontal forced cases may appear.

	// ComponentSizeEnum can be programmatically get and multiplier such as Tall multiplies maxHeight by 2 and minHeight by 1.2
	TEST_BOX_SMALL_100x100_200x200(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.SMALL, 100, 100, 200, 200),
	TEST_BOX_SMALL_200x200_200x200(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.SMALL, 200, 200, 200, 200),
	TEST_BOX_MEDIUM_100x100_400x400(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.MEDIUM, 100, 100, 400, 400),
	TEST_BOX_MEDIUM_200x200_400x400(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.MEDIUM, 200, 200, 400, 400),
	TEST_BOX_LARGE_100x100_600x600(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.LARGE, 100, 100, 600, 600),
	TEST_BOX_LARGE_200x200_600x600(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.LARGE, 200, 200, 600, 600),
	TEST_BOX_WIDE_100x100_600x200(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.WIDE, 100, 100, 600, 200),
	TEST_BOX_TALL_100x100_200x600(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.TALL, 100, 100, 200, 600),

	LABEL_SMALL(ComponentTypeEnum.LABEL, ComponentSizeEnum.SMALL, 35, 20, 75, 30),
	LABEL_MEDIUM(ComponentTypeEnum.LABEL, ComponentSizeEnum.MEDIUM, 50, 20, 120, 40),
	LABEL_LARGE(ComponentTypeEnum.LABEL, ComponentSizeEnum.LARGE, 100, 20, 350, 50),
	LABEL_WIDE(ComponentTypeEnum.LABEL, ComponentSizeEnum.WIDE, 35, 20, 400, 30),
	LABEL_TALL(ComponentTypeEnum.LABEL, ComponentSizeEnum.TALL, 35, 20, 100, 170),

	CANVAS_SMALL(ComponentTypeEnum.CANVAS, ComponentSizeEnum.SMALL, 200, 200, 1200, 700),
	CANVAS_MEDIUM(ComponentTypeEnum.CANVAS, ComponentSizeEnum.MEDIUM, 300, 300, 1200, 700),
	CANVAS_LARGE(ComponentTypeEnum.CANVAS, ComponentSizeEnum.LARGE, 500, 500, 1200, 700),
	CANVAS_WIDE(ComponentTypeEnum.CANVAS, ComponentSizeEnum.WIDE, 400, 200, 1200, 700),
	CANVAS_TALL(ComponentTypeEnum.CANVAS, ComponentSizeEnum.TALL, 200, 400, 1200, 700),

	BUTTON_SMALL(ComponentTypeEnum.BUTTON, ComponentSizeEnum.SMALL, 20, 20, 80, 50),
	BUTTON_MEDIUM(ComponentTypeEnum.BUTTON, ComponentSizeEnum.MEDIUM, 35, 35, 120, 80),
	BUTTON_LARGE(ComponentTypeEnum.BUTTON, ComponentSizeEnum.LARGE, 50, 50, 150, 100),
	BUTTON_WIDE(ComponentTypeEnum.BUTTON, ComponentSizeEnum.WIDE, 50, 20, 100, 50),
	BUTTON_TALL(ComponentTypeEnum.BUTTON, ComponentSizeEnum.TALL, 20, 50, 50, 100),

	TOGGLE_BUTTON_SMALL(ComponentTypeEnum.TOGGLE_BUTTON, ComponentSizeEnum.SMALL, 40, 20, 60, 30),
	TOGGLE_BUTTON_MEDIUM(ComponentTypeEnum.TOGGLE_BUTTON, ComponentSizeEnum.MEDIUM, 55, 30, 85, 40),
	TOGGLE_BUTTON_LARGE(ComponentTypeEnum.TOGGLE_BUTTON, ComponentSizeEnum.LARGE, 70, 45, 125, 55),
	TOGGLE_BUTTON_WIDE(ComponentTypeEnum.TOGGLE_BUTTON, ComponentSizeEnum.WIDE, 100, 30, 250, 60),
	TOGGLE_BUTTON_TALL(ComponentTypeEnum.TOGGLE_BUTTON, ComponentSizeEnum.TALL, 20, 40, 40, 85),

	RADIO_BUTTON_SMALL(ComponentTypeEnum.RADIO_BUTTON, ComponentSizeEnum.SMALL, 20, 20, 250, 40),
	RADIO_BUTTON_MEDIUM(ComponentTypeEnum.RADIO_BUTTON, ComponentSizeEnum.MEDIUM, 20, 20, 350, 45),
	RADIO_BUTTON_LARGE(ComponentTypeEnum.RADIO_BUTTON, ComponentSizeEnum.LARGE, 20, 20, 500, 45),
	RADIO_BUTTON_WIDE(ComponentTypeEnum.RADIO_BUTTON, ComponentSizeEnum.WIDE, 40, 20, 500, 40),
	RADIO_BUTTON_TALL(ComponentTypeEnum.RADIO_BUTTON, ComponentSizeEnum.TALL, 20, 40, 250, 70),

	CHECK_BOX_SMALL(ComponentTypeEnum.CHECK_BOX, ComponentSizeEnum.SMALL, 20, 20, 100, 60),
	CHECK_BOX_MEDIUM(ComponentTypeEnum.CHECK_BOX, ComponentSizeEnum.MEDIUM, 35, 35, 200, 60),
	CHECK_BOX_LARGE(ComponentTypeEnum.CHECK_BOX, ComponentSizeEnum.LARGE, 45, 45, 300, 60),
	CHECK_BOX_WIDE(ComponentTypeEnum.CHECK_BOX, ComponentSizeEnum.WIDE, 100, 20, 400, 60),
	CHECK_BOX_TALL(ComponentTypeEnum.CHECK_BOX, ComponentSizeEnum.TALL, 50, 40, 200, 70),

	COMBO_BOX_SMALL(ComponentTypeEnum.COMBO_BOX, ComponentSizeEnum.SMALL, 45, 25, 150, 50),
	COMBO_BOX_MEDIUM(ComponentTypeEnum.COMBO_BOX, ComponentSizeEnum.MEDIUM, 200, 25, 400, 50),
	COMBO_BOX_LARGE(ComponentTypeEnum.COMBO_BOX, ComponentSizeEnum.LARGE, 400, 25, 800, 50),
	COMBO_BOX_WIDE(ComponentTypeEnum.COMBO_BOX, ComponentSizeEnum.WIDE, 200, 25, 1000, 50),
	COMBO_BOX_TALL(ComponentTypeEnum.COMBO_BOX, ComponentSizeEnum.TALL, 45, 25, 150, 60),

	TEXT_FIELD_SMALL(ComponentTypeEnum.TEXT_FIELD, ComponentSizeEnum.SMALL, 40, 25, 150, 40),
	TEXT_FIELD_MEDIUM(ComponentTypeEnum.TEXT_FIELD, ComponentSizeEnum.MEDIUM, 100, 30, 300, 50),
	TEXT_FIELD_LARGE(ComponentTypeEnum.TEXT_FIELD, ComponentSizeEnum.LARGE, 100, 30, 600, 60),
	TEXT_FIELD_WIDE(ComponentTypeEnum.TEXT_FIELD, ComponentSizeEnum.WIDE, 40, 25, 700, 60),
	TEXT_FIELD_TALL(ComponentTypeEnum.TEXT_FIELD, ComponentSizeEnum.TALL, 40, 25, 300, 100),

	TEXT_AREA_SMALL(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.SMALL, 40, 25, 250, 100),
	TEXT_AREA_MEDIUM(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.MEDIUM, 40, 30, 500, 300),
	TEXT_AREA_LARGE(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.LARGE, 40, 30, 800, 600),
	TEXT_AREA_WIDE(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.WIDE, 40, 25, 700, 300),
	TEXT_AREA_TALL(ComponentTypeEnum.TEXT_AREA, ComponentSizeEnum.TALL, 40, 25, 300, 600),

	// TODO
	TEXT_EDITOR_SMALL(ComponentTypeEnum.TEXT_EDITOR, ComponentSizeEnum.SMALL, 0, 0, 0, 0),

	DATATABLE_SMALL(ComponentTypeEnum.DATATABLE, ComponentSizeEnum.SMALL, 180, 80, 300, 150),
	DATATABLE_MEDIUM(ComponentTypeEnum.DATATABLE, ComponentSizeEnum.MEDIUM, 180, 80, 600, 300),
	DATATABLE_LARGE(ComponentTypeEnum.DATATABLE, ComponentSizeEnum.LARGE, 180, 80, 1200, 800),
	DATATABLE_WIDE(ComponentTypeEnum.DATATABLE, ComponentSizeEnum.WIDE, 180, 80, 700, 300),
	DATATABLE_TALL(ComponentTypeEnum.DATATABLE, ComponentSizeEnum.TALL, 180, 150, 500, 600),

	GRAPH_SMALL(ComponentTypeEnum.GRAPH, ComponentSizeEnum.SMALL, 150, 150, 200, 150),
	GRAPH_MEDIUM(ComponentTypeEnum.GRAPH, ComponentSizeEnum.MEDIUM, 150, 150, 500, 300),
	GRAPH_LARGE(ComponentTypeEnum.GRAPH, ComponentSizeEnum.LARGE, 150, 150, 1200, 800),
	GRAPH_WIDE(ComponentTypeEnum.GRAPH, ComponentSizeEnum.WIDE, 150, 150, 1600, 400),
	GRAPH_TALL(ComponentTypeEnum.GRAPH, ComponentSizeEnum.TALL, 150, 150, 800, 900),

	PIE_CHART_SMALL(ComponentTypeEnum.PIE_CHART, ComponentSizeEnum.SMALL, 150, 150, 200, 200),
	PIE_CHART_MEDIUM(ComponentTypeEnum.PIE_CHART, ComponentSizeEnum.MEDIUM, 150, 150, 500, 500),
	PIE_CHART_LARGE(ComponentTypeEnum.PIE_CHART, ComponentSizeEnum.LARGE, 150, 150, 900, 900),
	PIE_CHART_WIDE(ComponentTypeEnum.PIE_CHART, ComponentSizeEnum.WIDE, 150, 150, 1600, 400),
	PIE_CHART_TALL(ComponentTypeEnum.PIE_CHART, ComponentSizeEnum.TALL, 150, 150, 800, 900),

	// TODO
	DIALOG_SMALL(ComponentTypeEnum.DIALOG, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	FILE_CHOOSER_SMALL(ComponentTypeEnum.FILE_CHOOSER, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	FILE_DROP_AREA_SMALL(ComponentTypeEnum.FILE_DROP_AREA, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	TOOLBAR_SMALL(ComponentTypeEnum.TOOLBAR, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	SIDEBAR_SMALL(ComponentTypeEnum.SIDEBAR, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	DATE_PICKER_SMALL(ComponentTypeEnum.DATE_PICKER, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	CALENDAR_SMALL(ComponentTypeEnum.CALENDAR, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	SLIDER_SMALL(ComponentTypeEnum.SLIDER, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	KNOB_SMALL(ComponentTypeEnum.KNOB, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	SPACER_SMALL(ComponentTypeEnum.SPACER, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	SEPARATOR_SMALL(ComponentTypeEnum.SEPARATOR, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	STICKY_MENU_SMALL(ComponentTypeEnum.STICKY_MENU, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	GOOGLE_MAPS_SMALL(ComponentTypeEnum.GOOGLE_MAPS, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	VIDEO_SMALL(ComponentTypeEnum.VIDEO, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	CAPTCHA_SMALL(ComponentTypeEnum.CAPTCHA, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	QR_CODE_SMALL(ComponentTypeEnum.QR_CODE, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	PROGRESS_BAR_SMALL(ComponentTypeEnum.PROGRESS_BAR, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	TABS_SMALL(ComponentTypeEnum.TABS, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	HEADING_SMALL(ComponentTypeEnum.HEADING, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	FOOTER_SMALL(ComponentTypeEnum.FOOTER, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	TREE_SMALL(ComponentTypeEnum.TREE, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	ACCORDION_SMALL(ComponentTypeEnum.ACCORDION, ComponentSizeEnum.SMALL, 0, 0, 0, 0),
	LIST_SMALL(ComponentTypeEnum.LIST, ComponentSizeEnum.SMALL, 0, 0, 0, 0);

	public final ComponentTypeEnum type;
	public final ComponentSizeEnum sizeType;
	public final int minWidth;
	public final int maxWidth;
	public final int minHeight;
	public final int maxHeight;

	ComponentDictionary (ComponentTypeEnum type, ComponentSizeEnum sizeType, int minWidth, int minHeight, int maxWidth, int maxHeight) {
		this.type = type;
		this.sizeType = sizeType;
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
}
