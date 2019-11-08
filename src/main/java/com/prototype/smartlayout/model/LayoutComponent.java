package com.prototype.smartlayout.model;

import java.util.Vector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Getter
@Setter
@Log4j
public class LayoutComponent implements Layoutable {
    private final WidthHeightRange widthHeightRange;
    private final String label;
    private boolean isFeasible;
    private int assignedX;
    private int assignedY;
    private int assignedWidth;
    private int assignedHeight;

    public LayoutComponent(String name, WidthHeightRange widthHeightRange) {
        label = name;
        assignedX = 0;
        assignedY = 0;
        this.widthHeightRange = widthHeightRange;
        assignedWidth = widthHeightRange.getMinWidth();
        assignedHeight = widthHeightRange.getMinHeight();
    }

    public void setPosition(int xVal, int yVal) {
        setAssignedX(xVal);
        setAssignedY(yVal);
    }

	public void setSize (int width, int height) {
		if (width > widthHeightRange.getMaxWidth()
				|| width < widthHeightRange.getMinWidth()
				|| height > widthHeightRange.getMaxHeight()
				|| height < widthHeightRange.getMinHeight()) {
			log.error("invalid setSize: (" + width + "," + height + ") on " + widthHeightRange);
		}
		setAssignedWidth(width);
		setAssignedHeight(height);
	}

	@Override
	public String toString () {
		return "['"
				+ label
				+ "'("
				+ assignedX
				+ ","
				+ assignedY
				+ ")."
				+ "("
				+ assignedWidth
				+ ","
				+ assignedHeight
				+ ") < "
				+ widthHeightRange
				+ " >]";
	}

	/**
	 * For a component, this method returns the associated WidthHeightRange object.
	 *
	 * @return The associated WidthHeightRange object.
	 */
	@Override
	public Vector<WidthHeightRange> getRanges () {
		Vector<WidthHeightRange> vec = new Vector<>();
		vec.add(widthHeightRange);
		return vec;
	}

	@Override
	public boolean layout (int x, int y, int w, int h, WidthHeightRange whr) {
		// This is the main method that does the computation of layout
		setAssignedX(x);
		setAssignedY(y);
//		if (w < whr.getMinWidth()) {
//			setAssignedWidth(whr.getMinWidth());
//		} else if (w > whr.getMaxWidth()) {
//			setAssignedWidth(whr.getMaxWidth());
//		} else {
//			setAssignedWidth(w);
//		}
//		if (h < whr.getMinHeight()) {
//			setAssignedHeight(whr.getMinHeight());
//		} else if (h > whr.getMaxHeight()) {
//			setAssignedHeight(whr.getMaxHeight());
//		} else {
//			setAssignedHeight(h);
//		}

		setAssignedWidth(w);
		setAssignedHeight(h);

		setFeasible(w >= whr.getMinWidth() && w <= whr.getMaxWidth() && h >= whr.getMinHeight() && h <= whr.getMaxHeight());
//		log.debug(label + " - X: " + x + " Y: "+ y + " Width: " + w + " Height: " + h);
		return isFeasible;
	}
}
