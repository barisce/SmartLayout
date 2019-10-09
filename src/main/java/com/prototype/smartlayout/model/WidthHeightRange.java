package com.prototype.smartlayout.model;

import java.util.Vector;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WidthHeightRange {
    private int minWidth;
    private int maxWidth;
    private int minHeight;
    private int maxHeight;

    // The orientation strategy tells how the children of this container will be layed out.
    private WidthHeightRangeEnum orientationStrategy;

    private Vector<WidthHeightRange> subRanges;

    public WidthHeightRange(
            WidthHeightRangeEnum orientationStrategy,
            int minWidth,
            int maxWidth,
            int minHeight,
            int maxHeight) {
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        subRanges = new Vector<>();
        this.orientationStrategy = orientationStrategy;
    }

    public WidthHeightRange(WidthHeightRange widthHeightRange) {
        minWidth = widthHeightRange.getMinWidth();
        maxWidth = widthHeightRange.getMaxWidth();
        minHeight = widthHeightRange.getMinHeight();
        maxHeight = widthHeightRange.getMaxHeight();
        subRanges = new Vector<>();
        orientationStrategy = widthHeightRange.getOrientationStrategy();
    }

    public void addSubRange(WidthHeightRange whr) {
        subRanges.add(whr);
    }

    public void addSubRanges(Vector<WidthHeightRange> subRangeVector) {
        subRanges.addAll(subRangeVector);
    }

    private String getOrientationStrategyString() {
        if (getOrientationStrategy() == WidthHeightRangeEnum.SINGLE) {
            return "S";
        } else if (getOrientationStrategy() == WidthHeightRangeEnum.HORIZONTAL) {
            return "H";
        } else if (getOrientationStrategy() == WidthHeightRangeEnum.VERTICAL) {
            return "V";
        } else if (getOrientationStrategy() == WidthHeightRangeEnum.OTHER) {
            return "O";
        } else {
            return "!!!";
        }
    }

    private String getOrientationTreeString() {
        StringBuilder builder = new StringBuilder("<");
        builder.append(getOrientationStrategyString());
        if (subRanges.size() > 0) {
            for (WidthHeightRange whr : subRanges) {
                builder.append(whr.getOrientationTreeString());
            }
        }
        builder.append(">");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "{"
                + getOrientationTreeString()
                + "("
                + minWidth
                + ","
                + maxWidth
                + ")-("
                + minHeight
                + ","
                + maxHeight
                + ")}";
    }
}
