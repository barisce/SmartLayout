package com.prototype.smartlayout.model;

import java.util.Vector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Getter
@Setter
@Log4j
public class LayoutContainer implements Layoutable {

	private final Vector<Layoutable> children;
	private int assignedX;
	private int assignedY;
	private int assignedWidth;
	private int assignedHeight;

	public LayoutContainer () {
		children = new Vector<>();
	}

	/**
	 * This method returns all possible layout combinations for this subtree of children. While
	 * computing all layouts, this method does the following:<br>
	 *
	 * <ul>
	 * <li>For each orientation strategy:
	 * <ul>
	 * <li>Get all layouts of all children
	 * <li>Create all layout combinations under this strategy
	 * <li>Store each combination as a possible layout
	 * </ul>
	 * <li>Return all possible layouts
	 * </ul>
	 *
	 * <p>Here is how this works on an example: Let's assume that we have 3 children, A, B and C,
	 * under this container. Let's assume again that A has 2 children, each a component, B is a
	 * component itself and C has two children which are also components. Therefore, A and C can be
	 * layed out in two different ways, namely horizontal or vertical, while B has a single natural
	 * layout. Overall this produces the following combinations of layouts for this container:<br>
	 * H(H(A)-B-H(C)) or H(H(A)-B-V(C)) or H(V(A)-B-H(C)) or H(V(A)-B-V(C)) or V(H(A)-B-H(C)) or
	 * V(H(A)-B-V(C)) or V(V(A)-B-H(C)) or V(V(A)-B-V(C))<br>
	 *
	 * <p>So, based on the orientation strategy of this container along with the orientation
	 * strategies of its children, we get a total of 8 different layouts. The way we compute all
	 * these layouts is as follows:
	 *
	 * <ul>
	 * <li>We first do the following for a horizontal layout strategy of this container:
	 * <ul>
	 * <li>We first insert all possible layouts of A into a vector, namely movingRanges
	 * <li>We than get the single possible layout of B and update movingRanges accordingly.
	 * In other words, we get each layout in movingRanges and attach B to it in a
	 * horizontal fashion, and record the new sizes
	 * <li>Finally, we do the same for C. However, now we have two layouts in movingRanges
	 * and two layouts for C. So, we multiply them to obtain 4 layouts and store them
	 * all in movingRanges
	 * </ul>
	 * <li>Once the horizontal strategy is computed, we do the above steps for the vertical
	 * strategy, which produces four more layouts.
	 * <li>The total of eight layouts are returned as the result.
	 * </ul>
	 *
	 * @return All possible layout combinations for this container.
	 */
	@Override
	public Vector<WidthHeightRange> getRanges () {
		// vec is what we will return at the end.
		// TODO: Neden containerların widthheigthrange objesi yok. Subrangesa iki kere giriyor.
		// movingRanges and tempRanges are used temporarily for creating all possible
		// layouts.

		Vector<WidthHeightRange> movingRanges = new Vector<>();
		Vector<WidthHeightRange> tempRanges = new Vector<>();

		// First, the HORIZONTAL orientation strategy

		// We will iterate over all children and one by one integrate them to the solution
		for (Layoutable layoutable : children) {
			// Get all possible ranges (layouts) for the layoutable child, layoutable
			Vector<WidthHeightRange> compVec = layoutable.getRanges();
			tempRanges.clear();

			if (movingRanges.isEmpty()) {
				// If this is the first component, we simply fill the movingRanges data
				// based on this component

				for (WidthHeightRange whr : compVec) {
					WidthHeightRange newRange = new WidthHeightRange(whr);
					newRange.setOrientationStrategy(WidthHeightRangeEnum.HORIZONTAL);
					newRange.addSubRange(whr);
					movingRanges.add(newRange);
				}
			} else {
				// If this is not the first child, then we already have some ranges computed
				// in movingRanges.
				// For all other children, we compute the product with existing
				// movingRanges data and obtain the new movingRanges.

				for (WidthHeightRange whr : movingRanges) {
					for (WidthHeightRange whrNew : compVec) {
						// Since this is horizontal, we know how to compute the new range:
						int newMinWidth = whr.getMinWidth() + whrNew.getMinWidth();
						int newMaxWidth = whr.getMaxWidth() + whrNew.getMaxWidth();
						int newMinHeight = Math.max(whr.getMinHeight(), whrNew.getMinHeight());
						int newMaxHeight = Math.min(whr.getMaxHeight(), whrNew.getMaxHeight());

						// Make sure that newMaxHeight is greater than newMinHeight.
						// Otherwise we have an infeasible layout. This only should happen
						// if the height ranges of the two children are not intersecting.

						if (newMaxHeight >= newMinHeight) {
							// Now create the new range object and add it to the temp vector:
							WidthHeightRange newRange =
									new WidthHeightRange(
											WidthHeightRangeEnum.HORIZONTAL,
											newMinWidth,
											newMaxWidth,
											newMinHeight,
											newMaxHeight);
							newRange.addSubRanges(whr.getSubRanges());
							newRange.addSubRange(whrNew);
							tempRanges.add(newRange);
						}
					}
				}

				movingRanges.clear();
				movingRanges.addAll(tempRanges);
			}
		}

		Vector<WidthHeightRange> vec = new Vector<>(movingRanges);

		// Now, the VERTICAL orientation strategy

		movingRanges.clear();

		for (Layoutable layoutable : children) {
			Vector<WidthHeightRange> compVec = layoutable.getRanges();
			tempRanges.clear();

			if (movingRanges.isEmpty()) {
				// If this is the first component, we simply fill the movingRanges data
				// based on this component

				for (WidthHeightRange whr : compVec) {
					WidthHeightRange newRange = new WidthHeightRange(whr);
					newRange.setOrientationStrategy(WidthHeightRangeEnum.VERTICAL);
					newRange.addSubRange(whr);
					movingRanges.add(newRange);
				}
			} else {
				// For all other children, we compute the product with existing
				// movingRanges data and obtain the new movingRanges.

				for (WidthHeightRange whr : movingRanges) {
					for (WidthHeightRange whrNew : compVec) {
						// Since this is vertical, we know how to compute the new range:
						int newMinWidth = Math.max(whr.getMinWidth(), whrNew.getMinWidth());
						int newMaxWidth = Math.min(whr.getMaxWidth(), whrNew.getMaxWidth());
						int newMinHeight = whr.getMinHeight() + whrNew.getMinHeight();
						int newMaxHeight = whr.getMaxHeight() + whrNew.getMaxHeight();

						// Make sure that newMaxWidth is greater than newMinWidth.
						// Otherwise we have an infeasible layout. This only should happen
						// if the width ranges of the two children are not intersecting.

						if (newMaxWidth >= newMinWidth) {
							// Now create the new range object and add it to the temp vector:
							WidthHeightRange newRange =
									new WidthHeightRange(
											WidthHeightRangeEnum.VERTICAL,
											newMinWidth,
											newMaxWidth,
											newMinHeight,
											newMaxHeight);
							newRange.addSubRanges(whr.getSubRanges());
							newRange.addSubRange(whrNew);
							tempRanges.add(newRange);
						} // else koşulunda max - min lik bir filler yapılabilir
					}
				}

				movingRanges.clear();
				movingRanges.addAll(tempRanges);
			}
		}

		vec.addAll(movingRanges);

		return vec;
	}

	@Override
	public boolean layout (int x, int y, int w, int h, WidthHeightRange whr) {
		boolean feasible = true;
		// This is the main method that does the computation of layout
		setAssignedX(x);
		setAssignedY(y);
		setAssignedWidth(w);
		setAssignedHeight(h);
		// TODO: width ve height ı kendi içinde düşün ve kendi içinde resize et.
		// First, the HORIZONTAL orientation strategy
		if (whr.getOrientationStrategy() == WidthHeightRangeEnum.HORIZONTAL) {
			Vector<WidthHeightRange> subRanges = whr.getSubRanges();
			int totalWidthOfChildren = 0;
			int[] minWidthValues = new int[subRanges.size()];
			int[] maxWidthValues = new int[subRanges.size()];
			for (int i = 0; i < subRanges.size(); i++) {
				minWidthValues[i] = subRanges.get(i).getMinWidth();
				maxWidthValues[i] = subRanges.get(i).getMaxWidth();
				totalWidthOfChildren += subRanges.get(i).getMaxWidth();
			}

			// find the width ratio according to max values
			float wr = totalWidthOfChildren / (float) w;
			int cumW = 0;
			for (int i = 0; i < subRanges.size(); i++) {
				// Resize the component to fit the window
				int value = (int) Math.ceil(maxWidthValues[i] / wr);
//				if (value < minWidthValues[i]) {
//					// infeasible because doesn't satisfy minimum requirement
//					children.get(i).layout(cumW, y, minWidthValues[i], h, subRanges.get(i));
//					cumW += minWidthValues[i];
//				} else if (value > maxWidthValues[i]) {
//					// infeasible because doesn't satisfy maximum requirement
//					children.get(i).layout(cumW, y, maxWidthValues[i], h, subRanges.get(i));
//					cumW += maxWidthValues[i];
//				} else {
					children.get(i).layout(cumW, y, value, h, subRanges.get(i));
					cumW += value;
//				}
				if (value < minWidthValues[i] || value > maxWidthValues[i]) feasible = false;
			}
		} // Second, the VERTICAL orientation strategy
		else if (whr.getOrientationStrategy() == WidthHeightRangeEnum.VERTICAL) {
			Vector<WidthHeightRange> subRanges = whr.getSubRanges();
			int totalHeightOfChildren = 0;
			int[] minHeightValues = new int[subRanges.size()];
			int[] maxHeightValues = new int[subRanges.size()];
			for (int i = 0; i < subRanges.size(); i++) {
				minHeightValues[i] = subRanges.get(i).getMinHeight();
				maxHeightValues[i] = subRanges.get(i).getMaxHeight();
				totalHeightOfChildren += subRanges.get(i).getMaxHeight();
			}

			// find the height ratio according to max values
			float hr = totalHeightOfChildren / (float) h;

			int cumH = 0;
			for (int i = 0; i < subRanges.size(); i++) {
				int value = (int) Math.ceil(maxHeightValues[i] / hr);
//				if (value < minHeightValues[i]) {
//					// infeasible because doesn't satisfy minimum requirement
//					children.get(i).layout(x, cumH, w, minHeightValues[i], subRanges.get(i));
//					cumH += minHeightValues[i];
//				} else if (value > maxHeightValues[i]) {
//					// infeasible because doesn't satisfy maximum requirement
//					children.get(i).layout(x, cumH, w, maxHeightValues[i], subRanges.get(i));
//					cumH += maxHeightValues[i];
//				} else {
					children.get(i).layout(x, cumH, w, value, subRanges.get(i));
					cumH += value;
//				}
				if (value < minHeightValues[i] || value > maxHeightValues[i]) feasible = false;
			}
		} else {
			log.debug("Shouldn't be here");
		}
		return feasible;
	}

	public void addComponent (Layoutable comp) {
		children.add(comp);
	}
}
