package com.prototype.smartlayout;

import com.prototype.smartlayout.listeners.KeyInputHandler;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.Layoutable;
import com.prototype.smartlayout.model.WidthHeightRange;
import com.prototype.smartlayout.utils.TestCaseUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import lombok.extern.log4j.Log4j;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

@Log4j
public class SmartLayout extends JFrame implements ComponentListener {
	private static final long serialVersionUID = 6944709955451188697L;
	private final JPanel panel;
	private JCheckBox showOnlyFeasibleLayouts = new JCheckBox("");
	private Vector<WidthHeightRange> feasibleLayouts = new Vector<>();
	private JTextField txtnum1;
	private JTextField txtnum2;
	private JComboBox comboBox;
	private List<Color> colorList = new ArrayList<>();
	private Layoutable root;
	private Vector<WidthHeightRange> finalLayoutCases;

	private SmartLayout () {
		super();
		TestCaseUtils.jComponentMap = new HashMap<>();
		root = null;
		finalLayoutCases = null;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addComponentListener(this);

		JPanel outerPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new FlowLayout());
		panel = new JPanel(null);
		panel.setLayout(null);
		panel.setSize(800, 600);
		panel.addMouseListener(new CanvasMouseListener());
		panel.addMouseWheelListener(new CanvasMouseListener());
		panel.addKeyListener(new KeyInputHandler());

		showOnlyFeasibleLayouts.addItemListener(e -> getFinalLayoutCases());
		topPanel.add(showOnlyFeasibleLayouts);

		JLabel lblCombo = new JLabel("Feasible Layouts: ");
		topPanel.add(lblCombo);
		comboBox = new JComboBox();
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
				layoutAfterComboBoxArrowNavigation(selectedIndex + 1);
				comboBox.setSelectedIndex(selectedIndex + 1);
			}

			private void getPrevious (int selectedIndex) {
				if (selectedIndex <= 0) {
					return;
				}
				layoutAfterComboBoxArrowNavigation(selectedIndex - 1);
				comboBox.setSelectedIndex(selectedIndex - 1);
			}

			private void layoutAfterComboBoxArrowNavigation (int selectedIndex) {
				for (WidthHeightRange finalLayoutCase : finalLayoutCases) {
					if (finalLayoutCase.toString().equals(comboBox.getItemAt(selectedIndex).toString())) {
						root.layout(0, 0, Math.max(root.getAssignedWidth(), 0), Math.max(root.getAssignedHeight(), 0), finalLayoutCase);
						log.debug("Root Width: " + (root.getAssignedWidth()) + " Root Height: " + (root.getAssignedHeight()) + " Width: " + (panel.getWidth()) + " Height: " + (panel.getHeight()));
						resizeComponents();
						break;
					}
				}
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
			root.setAssignedWidth(Integer.parseInt(txtnum1.getText()));
			root.setAssignedHeight(Integer.parseInt(txtnum2.getText()));
			getFinalLayoutCases();
			root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), getFeasibleLayout(finalLayoutCases));
			setSize(root.getAssignedWidth() + 15, root.getAssignedHeight() + 76);
			log.debug(getFeasibleLayout(finalLayoutCases));
			resizeComponents();
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
		PropertyConfigurator.configure(
				SmartLayout.class.getProtectionDomain().getCodeSource().getLocation().getPath()
						+ "log4j.properties");

		SmartLayout app = new SmartLayout();
		app.run();
//		app.setSize(app.root.getAssignedWidth() + 50, app.root.getAssignedHeight() + 50);
		app.setSize(1280, 720);
	}

	/**
	 * Creates a demo layout LayoutComponent and runs the layout algorithm
	 * on the LayoutContainer
	 */
	private void run () {
		log.debug("Starting test...");
		TestCaseUtils.jComponentMap.forEach((key, ignored) -> colorList.add(new Color(100 + (int) (Math.random() * 100), 100 + (int) (Math.random() * 100), 100 + (int) (Math.random() * 100))));

		((LayoutContainer) root).clearMemoization();
		finalLayoutCases = root.getRanges();
		log.debug(finalLayoutCases);
		root.layout(0, 0, 800, 300, finalLayoutCases.get(0));
	}

	@Override
	public void componentResized (ComponentEvent componentEvent) {
		if (root == null) {
			return;
		}
		setResizeOnRoot();
		getFinalLayoutCases();
		this.root.layout(0, 0, Math.max(root.getAssignedWidth(), 0), Math.max(root.getAssignedHeight(), 0), getFeasibleLayout(feasibleLayouts));
		log.debug("Root Width: " + (root.getAssignedWidth()) + " Root Height: " + (root.getAssignedHeight()) + " Width: " + (panel.getWidth()) + " Height: " + (panel.getHeight()));

		panel.setSize(root.getAssignedWidth(), root.getAssignedHeight());
		txtnum1.setText(root.getAssignedWidth() + "");
		txtnum2.setText(root.getAssignedHeight() + "");
		resizeComponents();
	}

	private void resizeComponents () {
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

	/**
	 * In this method we only need to check if the minimum values match because if the values exceed maximum we can still fit inside.
	 */
	private boolean isLayoutFeasible (int w, int h, WidthHeightRange whr) {
		return w >= whr.getMinWidth() && w <= whr.getMaxWidth() && h >= whr.getMinHeight() && h <= whr.getMaxHeight();
	}

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

	@Override
	public void componentMoved (ComponentEvent componentEvent) {
	}

	@Override
	public void componentShown (ComponentEvent componentEvent) {
	}

	@Override
	public void componentHidden (ComponentEvent componentEvent) {
	}

	public class CanvasMouseListener implements MouseListener, MouseWheelListener {

		private Logger logger = LogManager.getLogger(CanvasMouseListener.class);
		private int counter = 0;

		@Override
		public void mouseClicked (MouseEvent e) {
			logger.debug("X - " + e.getX() + " Y - " + e.getY());
		}

		@Override
		public void mousePressed (MouseEvent e) {
		}

		@Override
		public void mouseReleased (MouseEvent e) {
		}

		@Override
		public void mouseEntered (MouseEvent e) {
		}

		@Override
		public void mouseExited (MouseEvent e) {
		}

		@Override
		public void mouseWheelMoved (MouseWheelEvent e) {
			incrementCount();
			if (e.getWheelRotation() > 0) {
				// DOWN
				root.setAssignedWidth(Math.max(0, root.getAssignedWidth() - 50));
				root.setAssignedHeight(Math.max(0, root.getAssignedHeight() - 50));
				getFinalLayoutCases();
				root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), finalLayoutCases.get(counter));
			} else {
				// UP
				root.setAssignedWidth(root.getAssignedWidth() + 50);
				root.setAssignedHeight(root.getAssignedHeight() + 50);
				getFinalLayoutCases();
				root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), finalLayoutCases.get(counter));
			}
			setSize(root.getAssignedWidth() + 15, root.getAssignedHeight() + 75);
			logger.debug("Width - " + root.getAssignedWidth() + " Y - " + root.getAssignedHeight());
			log.debug(finalLayoutCases);
			repaint();
		}

		private void incrementCount () {
			counter = (counter + 1) % finalLayoutCases.size();
		}
	}
}
