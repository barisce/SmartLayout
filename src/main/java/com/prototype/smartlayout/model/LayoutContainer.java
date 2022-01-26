package com.prototype.smartlayout.model;

import com.prototype.smartlayout.model.enums.WidthHeightRangeEnum;
import com.prototype.smartlayout.utils.ArrayIndexComparator;
import com.prototype.smartlayout.utils.LayoutContainerUtils;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Vector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class LayoutContainer implements Layoutable {

	private final Vector<Layoutable> children;
	private String id;
	private int assignedX;
	private int assignedY;
	private int assignedWidth;
	private int assignedHeight;
	private Vector<WidthHeightRange> memo = null;

	public LayoutContainer (String id) {
		this.id = id;
		children = new Vector<>();
	}

	public LayoutContainer (String id, Layoutable... layoutables) {
		this(id);
		children.addAll(Arrays.asList(layoutables));
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
		// movingRanges and tempRanges are used temporarily for creating all possible layouts.

		// if we already calculated it no need to do it again.
		if (memo != null) {
			return memo;
		}

		Vector<WidthHeightRange> movingRanges = new Vector<>();
		Vector<WidthHeightRange> tempRanges = new Vector<>();

		// First, the HORIZONTAL orientation strategy

		// We will iterate over all children and one by one integrate them to the solution
		for (Layoutable layoutable : children) {
			// Get all possible ranges (layouts) for the layoutable child, layoutable
			Vector<WidthHeightRange> compVec = layoutable.getRanges(); // inside recursion, keep the calculated values.
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
						} // else koşulunda max - min lik bir filler yapılabilir
					}
				}

				//==============
				// Check if tempRAnges is empty
				// If so, we cannot feasibly layout this container.
				// We hould immediately stop the horizontal layout process. and also do not update vec below.
				movingRanges.clear();
				movingRanges.addAll(tempRanges);
			}
		}

		// If the above check failed, do not update vec.
		Vector<WidthHeightRange> vec = new Vector<>(movingRanges);

		// Now, the VERTICAL orientation strategy

		movingRanges.clear();

		for (Layoutable layoutable : children) {
			Vector<WidthHeightRange> compVec = layoutable.getRanges(); // inside recursion, keep the calculated values.
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

				//==============
				// Similaryl check feasibility and only update if feasible layouts exist
				movingRanges.clear();
				movingRanges.addAll(tempRanges);
			}
		}

		// Update only if feasiblity checks returned true
		vec.addAll(movingRanges);
		memo = vec;

		return vec;
	}

	public void clearMemoization () {
		for (Layoutable layoutable : children) {
			if (layoutable instanceof LayoutContainer) {
				((LayoutContainer) layoutable).clearMemoization();
				((LayoutContainer) layoutable).memo = null;
			}
		}
	}

	@Override
	public boolean layout (int x, int y, int w, int h, WidthHeightRange whr) {
		if (whr == null || (whr.getMinHeight() > h && whr.getMaxHeight() < h && whr.getMinWidth() > w && whr.getMaxWidth() < w)) {
			return false;
		}
		// This is the main method that does the computation of layout
		setAssignedX(x);
		setAssignedY(y);
		setAssignedWidth(w);
		setAssignedHeight(h);

		Vector<WidthHeightRange> subRanges = whr.getSubRanges();
		int[] minWidthValues = new int[subRanges.size()];
		int[] maxWidthValues = new int[subRanges.size()];
		int totalMinWidthOfChildren = 0;
		int totalMaxWidthOfChildren = 0;
		int[] minHeightValues = new int[subRanges.size()];
		int[] maxHeightValues = new int[subRanges.size()];
		int totalMinHeightOfChildren = 0;
		int totalMaxHeightOfChildren = 0;
		for (int i = 0; i < subRanges.size(); i++) {
			minHeightValues[i] = subRanges.get(i).getMinHeight();
			maxHeightValues[i] = subRanges.get(i).getMaxHeight();
			totalMinHeightOfChildren += subRanges.get(i).getMinHeight();
			totalMaxHeightOfChildren += subRanges.get(i).getMaxHeight();

			minWidthValues[i] = subRanges.get(i).getMinWidth();
			maxWidthValues[i] = subRanges.get(i).getMaxWidth();
			totalMinWidthOfChildren += subRanges.get(i).getMinWidth();
			totalMaxWidthOfChildren += subRanges.get(i).getMaxWidth();
		}


		boolean feasible = false;
		if (whr.getOrientationStrategy() == WidthHeightRangeEnum.HORIZONTAL || whr.getOrientationStrategy() == WidthHeightRangeEnum.VERTICAL) {
			// Weight strategy by max values
//			feasible = strategyWeight(x, y, isHorizontal(whr) ? totalMaxWidthOfChildren : totalMaxHeightOfChildren, subRanges, whr.getOrientationStrategy(), w, h, isHorizontal(whr) ? maxWidthValues : maxHeightValues);
			// Weight strategy by min values
//			feasible = strategyWeight(x, y, isHorizontal(whr) ? totalMinWidthOfChildren : totalMinHeightOfChildren, subRanges, whr.getOrientationStrategy(), w, h, isHorizontal(whr) ? minWidthValues : minHeightValues);

//			// this stream gets the statistics of the int array so we can find minimum or maximum values of this array.
//			IntSummaryStatistics statWidth = Arrays.stream(maxWidthValues).summaryStatistics();
//			IntSummaryStatistics statHeight = Arrays.stream(maxHeightValues).summaryStatistics();
//			// Max values strategy
//			feasible = strategyValues(x, y, subRanges, whr.getOrientationStrategy(), (w > statWidth.getMin() ? statWidth.getMin() : w), (h > statHeight.getMin() ? statHeight.getMin() : h), isHorizontal(whr) ? maxWidthValues : maxHeightValues);
//			// Min values strategy
//			feasible = strategyValues(x, y, subRanges, whr.getOrientationStrategy(), (w > statWidth.getMin() ? statWidth.getMin() : w), (h > statHeight.getMin() ? statHeight.getMin() : h), isHorizontal(whr) ? minWidthValues : minHeightValues);

			// Balance min
//			feasible = strategyFair(x, y, subRanges, whr.getOrientationStrategy(), w, h, isHorizontal(whr) ? minWidthValues : minHeightValues);
			// Balance max
//			feasible = strategyFair(x, y, subRanges, whr.getOrientationStrategy(), w, h, isHorizontal(whr) ? minWidthValues : minHeightValues, isHorizontal(whr) ? maxWidthValues : maxHeightValues);

			feasible = strategyBalance(x, y, subRanges, whr.getOrientationStrategy(), w, h, isHorizontal(whr) ? minWidthValues : minHeightValues, isHorizontal(whr) ? maxWidthValues : maxHeightValues);
		} else {
			log.debug("Shouldn't be here - Probably infeasible layout. ID: " + this.id);
		}

		// Just check if the given boundaries match with given width & height.
		return feasible;
	}

	private boolean isHorizontal (WidthHeightRange whr) {
		return WidthHeightRangeEnum.HORIZONTAL.equals(whr.getOrientationStrategy());
	}

	/**
	 * This strategy gives the components values according to values parameter.
	 *
	 * @param x                   x position of Component/Container
	 * @param y                   y position of Component/Container
	 * @param subRanges           sub ranges of given root node
	 * @param orientationStrategy horizontal or vertical
	 * @param w                   actual width value
	 * @param h                   actual height value
	 * @param values              values to consider while distributing weight ratio
	 */
	private boolean strategyValues (int x, int y, Vector<WidthHeightRange> subRanges, WidthHeightRangeEnum orientationStrategy, int w, int h, int[] values) {
		int cum = 0;
		return layoutRecursively(x, y, subRanges, orientationStrategy, w, h, cum, values);
	}

	/**
	 * This method shares remaining values as the minimum of the components.
	 *
	 * @param x
	 * @param y
	 * @param subRanges
	 * @param orientationStrategy
	 * @param w
	 * @param h
	 * @param minValues
	 * @param capacityValues
	 */
	private boolean strategyFair (int x, int y, Vector<WidthHeightRange> subRanges, WidthHeightRangeEnum orientationStrategy, int w, int h, int[] minValues, int[] capacityValues) {
		int length = capacityValues.length;
		int cum = 0;
		int[] distribution = new int[subRanges.size()];
		boolean[] removedIndex = new boolean[subRanges.size()];
		int remaining = WidthHeightRangeEnum.HORIZONTAL.equals(orientationStrategy) ? w : h;
		for (int i = 0; i < subRanges.size(); i++) {
			distribution[i] = minValues[i];
			remaining -= minValues[i];
			capacityValues[i] -= minValues[i];
			length = LayoutContainerUtils.checkDistributionComplete(length, removedIndex, i, capacityValues[i]);
			if (remaining <= 0) {
				break;
			}
		}

		//while it can still be distributed, distribute
		while (remaining > 0) {
			// get the smallest number after 0 since 0 means that node is distributed.
			IntSummaryStatistics stats = Arrays.stream(capacityValues).filter(num -> num > 0).summaryStatistics();
			// if total remaining over number of children less than minimum value a child can get, distribute this value instead of minimum value
			if (remaining / length <= stats.getMin()) {
				// if there is still something left to distribute in this node
				int remainingOverLength = remaining / length;
				for (int i = 0; i < subRanges.size(); i++) {
					if (!removedIndex[i]) {
						// distribute and subtract the amount from remaining
						if (remainingOverLength == 0) { // TODO : remaining <= length - add one by one
							distribution[i] += remaining;
							remaining = 0;
							break;
						} else {
							remaining = LayoutContainerUtils.distribute(capacityValues, distribution, remaining, remainingOverLength, i);
							length = LayoutContainerUtils.checkDistributionComplete(length, removedIndex, i, capacityValues[i]);
						}
					}
				}
			} else {
				int remainingOverLength = remaining / length;
				for (int i = 0; i < subRanges.size(); i++) {
					if (!removedIndex[i]) {
						// distribute and subtract the amount from remaining
						if (remainingOverLength == 0) {
							distribution[i] += remaining;
							remaining = 0;
						} else {
							remaining = LayoutContainerUtils.distribute(capacityValues, distribution, remaining, stats.getMin(), i);
							length = LayoutContainerUtils.checkDistributionComplete(length, removedIndex, i, capacityValues[i]);
						}
					}
				}
			}
			if (remaining < 0) {
				log.error("Remaining can't be negative!");
				break;
			}
			if (length <= 0) {
//				log.error("All is distributed. Remaining: " + remaining);
			}
		}

		LayoutContainerUtils.checkForDistributionCompletedSuccessfully(removedIndex, id);

		return layoutRecursively(x, y, subRanges, orientationStrategy, w, h, cum, distribution);
	}

	// TODO: private boolean strategyWeighted();

	/**
	 * This method acts like a water scale. adds minimum then adds only to least numbers
	 *
	 * @param x
	 * @param y
	 * @param subRanges
	 * @param orientationStrategy
	 * @param w
	 * @param h
	 * @param minValues
	 * @param capacityValues
	 * @return
	 */
	private boolean strategyBalance (int x, int y, Vector<WidthHeightRange> subRanges, WidthHeightRangeEnum orientationStrategy, int w, int h, int[] minValues, int[] capacityValues) {
		int cum = 0;
		int[] maxValues = new int[subRanges.size()];
		int[] distribution = new int[subRanges.size()];
		int remaining = WidthHeightRangeEnum.HORIZONTAL.equals(orientationStrategy) ? w : h;
		for (int i = 0; i < subRanges.size(); i++) {
			maxValues[i] = WidthHeightRangeEnum.HORIZONTAL.equals(orientationStrategy) ? subRanges.get(i).getMaxWidth() : subRanges.get(i).getMaxHeight();
			distribution[i] = minValues[i];
			remaining -= minValues[i];
			capacityValues[i] -= minValues[i];
			if (remaining <= 0) {
				break;
			}
		}
		// This creates the index array of distribution array and then sorts the distribution
		// and returns as indexes so that we know the positions of sorted array and keep the original array.
		ArrayIndexComparator comparator = new ArrayIndexComparator(distribution);
		Integer[] indexOrder = comparator.createIndexArray();
		Arrays.sort(indexOrder, comparator);

		//while it can still be distributed, distribute
		while (remaining > 0) {
			if (distribution[indexOrder[0]] == maxValues[indexOrder[0]]) {
				// This slot is full, remove it from indexOrder
				indexOrder = Arrays.copyOfRange(indexOrder, 1, indexOrder.length);
			} else {
				// This slot can still be filled
				// Increase by 1.
				// Can we increase more? Maybe, but requires very smart computation
				distribution[indexOrder[0]] = distribution[indexOrder[0]] + 1;
				remaining--;
				int p = 0;
				int q = 1;
				// Now we move this item up the list if this is not the smallest
				// item anymore. This is basically one step of bubble sort
				// takes O(n) in the worst case. But n is small here.
				while (q < indexOrder.length && distribution[indexOrder[p]] > distribution[indexOrder[q]]) {
					// swap indexOrder[p] with indexOrder[q] and increment pointers.
					int temp = indexOrder[p];
					indexOrder[p] = indexOrder[q];
					indexOrder[q] = temp;
					p = q;
					q = p + 1;
				}
			}
			if (indexOrder.length < 1) {
				log.trace("Layout's constraints exceeds max values for components!");
				break;
			}
			if (remaining < 0) {
				log.error("Remaining can't be negative!");
				break;
			}
		}

		return layoutRecursively(x, y, subRanges, orientationStrategy, w, h, cum, distribution);
	}

	private boolean layoutRecursively (int x, int y, Vector<WidthHeightRange> subRanges, WidthHeightRangeEnum orientationStrategy, int w, int h, int cum, int[] distribution) {
		for (int i = 0; i < subRanges.size(); i++) {
			// Resize the component to fit the window
			if (WidthHeightRangeEnum.HORIZONTAL.equals(orientationStrategy)) {
				if (!children.get(i).layout(x + cum, y, distribution[i], h, subRanges.get(i))) {
					return false;
				}
			} else {
				if (!children.get(i).layout(x, y + cum, w, distribution[i], subRanges.get(i))) {
					return false;
				}
			}
			cum += distribution[i];
		}
		return true;
	}

	/**
	 * This strategy weights the components according to the values parameter.
	 * To distribute according to maxValues give values maxValues array.
	 * To distribute according to minValues give values minValues array.
	 *
	 * @param x                   x position of Component/Container
	 * @param y                   y position of Component/Container
	 * @param totalOfChildren     total pixels occupied according to orientationStrategy
	 * @param subRanges           sub ranges of given root node
	 * @param orientationStrategy horizontal or vertical
	 * @param w                   actual width value
	 * @param h                   actual height value
	 * @param values              values to consider while distributing weight ratio
	 */
	private boolean strategyWeight (int x, int y, int totalOfChildren, Vector<WidthHeightRange> subRanges, WidthHeightRangeEnum orientationStrategy, int w, int h, int[] values) {
		int cum = 0;
		// find the width ratio according to max values
		float ratio = totalOfChildren / (WidthHeightRangeEnum.HORIZONTAL.equals(orientationStrategy) ? (float) w : (float) h);
		for (int i = 0; i < subRanges.size(); i++) {
			// Resize the component to fit the window
			int value = (int) Math.ceil(values[i] / ratio);
			if (WidthHeightRangeEnum.HORIZONTAL.equals(orientationStrategy)) {
				if (!children.get(i).layout(x + cum, y, value, h, subRanges.get(i))) {
					return false;
				}
			} else {
				if (!children.get(i).layout(x, y + cum, w, value, subRanges.get(i))) {
					return false;
				}
			}
			cum += value;
			// TODO width height tan çıkara çıkara ilerlenebilir. Böylelikle 2-3 pixel artık kalmaz
		}
		return true;
	}

	public void addComponent (Layoutable... comp) {
		children.addAll(Arrays.asList(comp));
	}

	public LayoutComponent findComponent (String label) {
		for (Layoutable component : children) {
			if (component instanceof LayoutComponent && label.equals(((LayoutComponent) component).getLabel())) {
				return (LayoutComponent) component;
			} else if (component instanceof LayoutContainer) {
				LayoutComponent comp = ((LayoutContainer) component).findComponent(label);
				if (comp != null) {
					return comp;
				}
			}
		}
		return null;
	}
}