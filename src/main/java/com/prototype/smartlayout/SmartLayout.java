package com.prototype.smartlayout;

import com.prototype.smartlayout.listeners.ComponentResizeEndListener;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.Layoutable;
import com.prototype.smartlayout.model.WidthHeightRange;
import com.prototype.smartlayout.utils.AestheticMeasureUtil;
import com.prototype.smartlayout.utils.TestCaseUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SmartLayout extends JFrame {
	private static final long serialVersionUID = 6944709955451188697L;
	private JPanel panel;
	private JCheckBox showOnlyFeasibleLayouts = new JCheckBox("");
	private JTextField txtnum1;
	private JTextField txtnum2;
	private JComboBox comboBox;
	private List<Color> colorList = new ArrayList<>();
	private Vector<WidthHeightRange> feasibleLayouts = new Vector<>();
	private Layoutable root;
	private Vector<WidthHeightRange> finalLayoutCases;

	private SmartLayout () {
		super();

		TestCaseUtils.jComponentMap = new HashMap<>();
		root = null;
		finalLayoutCases = null;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addComponentListener(new ComponentResizeEndListener() {
			@Override
			public void resizeTimedOut () {
				frameResized();
			}
		});

		JPanel outerPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new FlowLayout());
		panel = new JPanel(null);
		panel.setLayout(null);
		panel.setSize(800, 600);

		showOnlyFeasibleLayouts.addItemListener(e -> getFinalLayoutCases());
		topPanel.add(showOnlyFeasibleLayouts);

		JLabel lblCombo = new JLabel("Feasible Layouts: ");
		topPanel.add(lblCombo);
		comboBox = new JComboBox();
		comboBox.setPreferredSize(new Dimension(120, 25));
		comboBox.addItemListener(new ItemChangeListener());

		comboBox.addKeyListener(new KeyListener() { // ActionListener did not work. So bound it to Enter Key
			@Override
			public void keyTyped (KeyEvent e) {
			}

			@Override
			public void keyPressed (KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch (keyCode) {
					case KeyEvent.VK_ENTER:
						for (WidthHeightRange finalLayoutCase : finalLayoutCases) {
							if (finalLayoutCase.toString().equals(comboBox.getSelectedItem().toString())) {
								root.setAssignedWidth(root.getAssignedWidth() + 1);
								root.layout(0, 0, Math.max(root.getAssignedWidth(), 0), Math.max(root.getAssignedHeight(), 0), finalLayoutCase);
								log.debug(finalLayoutCase);
								log.debug("Root Width: " + (root.getAssignedWidth()) + " Root Height: " + (root.getAssignedHeight()) + " Width: " + (panel.getWidth()) + " Height: " + (panel.getHeight()));
								setSize(root.getAssignedWidth() + 15, root.getAssignedHeight() + 75);
								panel.setSize(root.getAssignedWidth(), root.getAssignedHeight());
								resizeComponents();
								break;
							}
						}
						break;
					case KeyEvent.VK_LEFT:
						// handle left
						getPrevious(comboBox.getSelectedIndex());
						break;
					case KeyEvent.VK_RIGHT:
						// handle right
						getNext(comboBox.getSelectedIndex());
						break;
				}
			}

			private void getNext (int selectedIndex) {
				if (selectedIndex < 0 || selectedIndex + 1 == comboBox.getItemCount()) {
					return;
				}
				comboBox.setSelectedIndex(selectedIndex + 1);
			}

			private void getPrevious (int selectedIndex) {
				if (selectedIndex <= 0) {
					return;
				}
				comboBox.setSelectedIndex(selectedIndex - 1);
			}


			@Override
			public void keyReleased (KeyEvent e) {
			}
		});
		topPanel.add(comboBox);

		JLabel lbl1 = new JLabel("X: ");
		topPanel.add(lbl1);
		txtnum1 = new JTextField();
		txtnum1.setText("800");
		txtnum1.setPreferredSize(new Dimension(60, 25));
		topPanel.add(txtnum1);

		JLabel lbl2 = new JLabel("Y: ");
		topPanel.add(lbl2);
		txtnum2 = new JTextField();
		txtnum2.setText("600");
		txtnum2.setPreferredSize(new Dimension(60, 25));
		topPanel.add(txtnum2);

		JButton button = new JButton("Resize");
		button.addActionListener(e -> {
			root.setAssignedWidth(Integer.parseInt(txtnum1.getText()) + 1);
			root.setAssignedHeight(Integer.parseInt(txtnum2.getText()) - 1);
//			getFinalLayoutCases();
//			root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), getAestheticLayout(feasibleLayouts));
			setSize(root.getAssignedWidth() + 15, root.getAssignedHeight() + 76);
//			log.debug(getAestheticLayout(feasibleLayouts));
//			resizeComponents();
		});
		topPanel.add(button);
		outerPanel.add(panel, BorderLayout.CENTER);
		// give the test number that you want to execute
		root = TestCaseUtils.executeTest(6);
		TestCaseUtils.createComponentsOfTree(panel);
		outerPanel.add(topPanel, BorderLayout.PAGE_START);

		setContentPane(outerPanel);
		setVisible(true);
	}

	public static void main (String[] args) {

		SmartLayout app = new SmartLayout();
		app.run();
		app.setSize(800, 400);
	}

	private static void resizeComponents () {
		TestCaseUtils.jComponentMap.forEach((lComponent, jComponent) -> {
			if (lComponent != null) {
				jComponent.setBounds(lComponent.getAssignedX(), lComponent.getAssignedY(),
						lComponent.getAssignedWidth(), lComponent.getAssignedHeight());
				jComponent.setBorder(BorderFactory.createLineBorder(lComponent.isFeasible() ? Color.GREEN : Color.RED));
				jComponent.setToolTipText(lComponent.getLabel());
			}
		});
	}

	/**
	 * In this method we only need to check if the minimum values match because if the values exceed maximum we can still fit inside.
	 */
	private static boolean isLayoutFeasible (int w, int h, WidthHeightRange whr) {
		return w >= whr.getMinWidth() && w <= whr.getMaxWidth() && h >= whr.getMinHeight() && h <= whr.getMaxHeight();
	}

	private void layoutAfterComboBoxChanged (int selectedIndex) {
		for (WidthHeightRange finalLayoutCase : finalLayoutCases) {
			if (finalLayoutCase.toString().equals(comboBox.getItemAt(selectedIndex).toString())) {
				long startTime = System.nanoTime();
				root.layout(0, 0, Math.max(root.getAssignedWidth(), 0), Math.max(root.getAssignedHeight(), 0), finalLayoutCase);
				long elapsedTime = System.nanoTime() - startTime;
				log.debug("\nCombo changed layout Execution time in nanosecond: " + elapsedTime + "\nlayout Execution time in microsecond: " + (elapsedTime / 1000) + "\nTotal tree size : " + feasibleLayouts.size() +
						"\nRoot Width: " + (root.getAssignedWidth()) + " Root Height: " + (root.getAssignedHeight()) + " Width: " + (panel.getWidth()) + " Height: " + (panel.getHeight()));
				resizeComponents();
				break;
			}
		}
	}

	/**
	 * Creates a demo layout LayoutComponent and runs the layout algorithm
	 * on the LayoutContainer
	 */
	private void run () {
		log.debug("Starting test...");
		TestCaseUtils.jComponentMap.forEach((key, ignored) -> colorList.add(new Color(100 + (int) (Math.random() * 100), 100 + (int) (Math.random() * 100), 100 + (int) (Math.random() * 100))));

		((LayoutContainer) root).clearMemoization();
		long startTime = System.nanoTime();
		finalLayoutCases = root.getRanges();
		long elapsedTime = System.nanoTime() - startTime;
		log.debug("getRanges Execution time in nanosecond: " + elapsedTime);
		log.debug("getRanges Execution time in microsecond: " + elapsedTime / 1000);

		startTime = System.nanoTime();
		finalLayoutCases = root.getRanges();
		elapsedTime = System.nanoTime() - startTime;
		log.debug("getRanges memoization time in nanosecond: " + elapsedTime);
		log.debug("getRanges memoization time in microsecond: " + elapsedTime / 1000);
		//log.debug(finalLayoutCases);
		root.layout(0, 0, 800, 400, finalLayoutCases.get(0));
	}

	public void frameResized () {
		if (root == null) {
			return;
		}
		setResizeOnRoot();
		getFinalLayoutCases();
		long startTime = System.nanoTime();
		root.layout(0, 0, Math.max(root.getAssignedWidth(), 0), Math.max(root.getAssignedHeight(), 0), getAestheticLayout(feasibleLayouts));
		long elapsedTime = System.nanoTime() - startTime;
		log.debug("\nFrame Resized Layout Execution time in nanosecond: " + elapsedTime + "\nLayout Execution time in microsecond: " + elapsedTime / 1000 + "\nTotal tree size : " + feasibleLayouts.size());

		panel.setSize(root.getAssignedWidth(), root.getAssignedHeight());
		txtnum1.setText(root.getAssignedWidth() + "");
		txtnum2.setText(root.getAssignedHeight() + "");
		resizeComponents();
	}

	/**
	 * This method fills the combobox with feasible layouts so that
	 * we can choose and see how that layout looks like in that resolution.
	 */
	private void getFinalLayoutCases () {
		((LayoutContainer) root).clearMemoization();
		finalLayoutCases = root.getRanges();
		comboBox.removeAllItems();
		feasibleLayouts = new Vector<>();
		for (WidthHeightRange finalLayoutCase : finalLayoutCases) {
			// If the checkbox is selected, then add if and only if layout's minimum requirements are satisfied.
			if (!showOnlyFeasibleLayouts.isSelected() || isLayoutFeasible(root.getAssignedWidth(), root.getAssignedHeight(), finalLayoutCase)) {
				comboBox.addItem(finalLayoutCase);
				feasibleLayouts.add(finalLayoutCase);
			}
		}
	}

	private WidthHeightRange getAestheticLayout (Vector<WidthHeightRange> layouts) {
		if (layouts.isEmpty()) {
			return null;
		}
		long startTime = System.nanoTime();
		Map<Integer, Double> aestheticMeasurementMap = new HashMap<>();
		for (int i = 0; i < layouts.size(); i++) {
			WidthHeightRange layout = layouts.get(i);
			root.layout(0, 0, Math.max(root.getAssignedWidth(), 0), Math.max(root.getAssignedHeight(), 0), layout);
			aestheticMeasurementMap.put(i, AestheticMeasureUtil.measureAesthetics(root, false));
		}
		Optional<Entry<Integer, Double>> maxEntry = aestheticMeasurementMap.entrySet().stream().max(Entry.comparingByValue());
		long elapsedTime = System.nanoTime() - startTime;
		// "\nAesthetic Layout Execution time in nanosecond: " + elapsedTime +
		log.debug("\nAesthetic Execution time in millisecond: " + elapsedTime / 1000000d + "\nTotal tree size : " + feasibleLayouts.size());
		log.info("Selected index : " + maxEntry.get().getKey() + " Selected aesthetic value : " + maxEntry.get().getValue());

		root.layout(0, 0, Math.max(root.getAssignedWidth(), 0), Math.max(root.getAssignedHeight(), 0), maxEntry.isPresent() ? layouts.get(maxEntry.get().getKey()) : layouts.get(0));
		AestheticMeasureUtil.measureAesthetics(root, true);
		return maxEntry.isPresent() ? layouts.get(maxEntry.get().getKey()) : layouts.get(0);
	}

	/**
	 * @deprecated
	 * this method returns the minimum euclidean distance layout to our resolution
	 */
	@Deprecated
	private WidthHeightRange getFeasibleLayout (Vector<WidthHeightRange> layouts) {
		if (layouts.isEmpty()) {
			return null;
		}

		// Start with max values since we try to find minimum values
		double minDiff = Integer.MAX_VALUE;
		WidthHeightRange bestFit = null;
		for (WidthHeightRange range : layouts) {
			// Find closest pair of points according to root point.
			double minsDist = Math.sqrt(Math.pow((double) root.getAssignedWidth() - range.getMinWidth(), 2) + Math.pow((double) root.getAssignedHeight() - range.getMinHeight(), 2));
			double maxsDist = Math.sqrt(Math.pow((double) root.getAssignedWidth() - range.getMaxWidth(), 2) + Math.pow((double) root.getAssignedHeight() - range.getMaxHeight(), 2));
			// if both min and max values of possible layouts are less than our current distance set the new values
			if (Math.max(minsDist, maxsDist) < minDiff) {
				minDiff = Math.max(minsDist, maxsDist);
				bestFit = range;
			}
		}
		return bestFit != null ? bestFit : layouts.get(0);
	}

	private void setResizeOnRoot () {
		root.setAssignedWidth(panel.getWidth());
		root.setAssignedHeight(panel.getHeight());
	}

	class ItemChangeListener implements ItemListener {
		@Override
		public void itemStateChanged (ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				layoutAfterComboBoxChanged(comboBox.getSelectedIndex());
			}
		}
	}
}
