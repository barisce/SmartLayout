package com.prototype.smartlayout.model;

public enum WidthHeightRangeEnum {
  SINGLE(0),
  HORIZONTAL(1),
  VERTICAL(2),
  OTHER(3);

  public final int id;

  WidthHeightRangeEnum(int id) {
    this.id = id;
  }
}
