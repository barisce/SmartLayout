package com.prototype.smartlayout;

import com.prototype.smartlayout.listeners.KeyInputHandler;
import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.Layoutable;
import com.prototype.smartlayout.model.WidthHeightRange;
import com.prototype.smartlayout.utils.MockUtils;
import com.prototype.smartlayout.utils.TestCaseUtils;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
	private static final Color TRANSPARENT_BLACK = new Color(0f, 0f, 0f, 0.4f);
	private final JPanel panel;
	private JCheckBox showOnlyFeasibleLayouts = new JCheckBox("");
	private Vector<WidthHeightRange> feasibleLayouts = new Vector<>();
	private JTextField txtnum1;
	private JTextField txtnum2;
	private JComboBox comboBox;
	private List<Color> colorList = new ArrayList<>();
	private Layoutable root;
	private BasicStroke correctnessStroke = new BasicStroke(2);
	private BufferedImage buffer;
	private Graphics bufferGraphics;
	private Vector<WidthHeightRange> finalLayoutCases;
	private Map<String, JComponent> applicationComponentMap = new HashMap<>();

	private SmartLayout () {
		super();
		TestCaseUtils.components = new HashMap<>();
		root = null;
		finalLayoutCases = null;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addComponentListener(this);

		JPanel outerPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new FlowLayout());
		panel = new JPanel(null);
		panel.setLayout(null);
		panel.setSize(800, 600);
//		panel.setPreferredSize(new Dimension(800, 600));
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
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// Draw the selected layout
					for (WidthHeightRange finalLayoutCase : finalLayoutCases) {
						if (finalLayoutCase.toString().equals(comboBox.getSelectedItem().toString())) {
							root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), finalLayoutCase);
							log.debug(finalLayoutCase);
							drawLayout();
							resizeComponents();
						}
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
			setSize(root.getAssignedWidth() + 15, root.getAssignedHeight() + 75);
			log.debug(getFeasibleLayout(finalLayoutCases));
			drawLayout();
			resizeComponents();
		});
		topPanel.add(button);
		outerPanel.add(panel, BorderLayout.CENTER);
		// give the test number that you want to execute
		root = TestCaseUtils.executeTest(7);
		createComponentsOfTree();
		outerPanel.add(topPanel, BorderLayout.PAGE_START);

		buffer = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
		bufferGraphics = buffer.createGraphics();

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
		TestCaseUtils.components.forEach((key, ignored) -> colorList.add(new Color(100 + (int) (Math.random() * 100), 100 + (int) (Math.random() * 100), 100 + (int) (Math.random() * 100))));

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
		this.root.layout(0, 0, root.getAssignedWidth() > 0 ? root.getAssignedWidth() : 0, root.getAssignedHeight() > 0 ? root.getAssignedHeight() : 0, getFeasibleLayout(feasibleLayouts));
		log.debug("Root Width: " + (root.getAssignedWidth()) + " Root Height: " + (root.getAssignedHeight()) + " Width: " + (panel.getWidth()) + " Height: " + (panel.getHeight()));

		panel.setSize(root.getAssignedWidth(), root.getAssignedHeight());
		txtnum1.setText(root.getAssignedWidth() + "");
		txtnum2.setText(root.getAssignedHeight() + "");
		drawLayout();
		resizeComponents();
	}

	private void resizeComponents () {
		Insets insets = panel.getInsets();
//		Dimension d = new Dimension(0, 0);
		applicationComponentMap.forEach((name, component) -> {
			LayoutComponent layoutable = ((LayoutContainer) root).findComponent(name);
			if (layoutable != null) {
				component.setBounds(layoutable.getAssignedX() + insets.left, layoutable.getAssignedY() + insets.top,
						layoutable.getAssignedWidth(), layoutable.getAssignedHeight());
//				d.setSize(layoutable.getAssignedWidth(), layoutable.getAssignedHeight());
//				component.setPreferredSize(d);
				component.setBorder(BorderFactory.createLineBorder(layoutable.isFeasible() ? Color.GREEN : Color.RED));
				component.setToolTipText(name);
			}
		});
	}

	private void createComponentsOfTree () {
		Insets insets = panel.getInsets();
		TestCaseUtils.components.forEach((name, component) -> {
			switch (component.getWidthHeightRange().getDict().type) {
				case LABEL:
					JLabel label = new JLabel(MockUtils.generateString((int) (Math.random() * 10.0) + 5));
					label.setBounds(component.getAssignedX() + insets.left, component.getAssignedY() + insets.top,
							component.getAssignedWidth(), component.getAssignedHeight());
					applicationComponentMap.put(name, label);
					panel.add(label);
					break;
				case CANVAS:
					break;
				case TABS:
					break;
				case HEADING:
					break;
				case FOOTER:
					break;
				case TREE:
					break;
				case ACCORDION:
					break;
				case LIST:
					break;
				case FILE_CHOOSER:
					break;
				case FILE_DROP_AREA:
					break;
				case DATATABLE:
					break;
				case TEXT_FIELD:
					JTextField textField = new JTextField();
					textField.setBounds(component.getAssignedX() + insets.left, component.getAssignedY() + insets.top,
							component.getAssignedWidth(), component.getAssignedHeight());
					applicationComponentMap.put(name, textField);
					panel.add(textField);
					break;
				case TEXT_AREA:
					break;
				case TEXT_EDITOR:
					break;
				case COMBO_BOX:
					JComboBox comboBox = new JComboBox();
					comboBox.addItem(MockUtils.generateString((int) (Math.random() * 10.0) + 5));
					comboBox.addItem(MockUtils.generateString((int) (Math.random() * 10.0) + 5));
					comboBox.addItem(MockUtils.generateString((int) (Math.random() * 10.0) + 5));
					comboBox.setBounds(component.getAssignedX() + insets.left, component.getAssignedY() + insets.top,
							component.getAssignedWidth(), component.getAssignedHeight());
					applicationComponentMap.put(name, comboBox);
					panel.add(comboBox);
					break;
				case CHECK_BOX:
					break;
				case BUTTON:
					JButton button = new JButton(MockUtils.generateString((int) (Math.random() * 10.0) + 5));
					button.setBounds(component.getAssignedX() + insets.left, component.getAssignedY() + insets.top,
							component.getAssignedWidth(), component.getAssignedHeight());
					applicationComponentMap.put(name, button);
					panel.add(button);
					break;
				case TOGGLE_BUTTON:
					break;
				case RADIO_BUTTON:
					break;
				case PROGRESS_BAR:
					break;
				case TOOLBAR:
					break;
				case DIALOG:
					break;
				case SIDEBAR:
					break;
				case DATE_PICKER:
					break;
				case CALENDAR:
					break;
				case SLIDER:
					break;
				case KNOB:
					break;
				case PIE_CHART:
					break;
				case GRAPH:
					break;
				case SPACER:
					break;
				case SEPARATOR:
					break;
				case STICKY_MENU:
					break;
				case GOOGLE_MAPS:
					break;
				case VIDEO:
					break;
				case CAPTCHA:
					break;
				case QR_CODE:
					break;
			}
		});
	}

	private void drawBlank () {
		buffer = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
		bufferGraphics = buffer.createGraphics();
		bufferGraphics.setColor(Color.black);
		bufferGraphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());
	}

	/**
	 * Draw the layout on the screen.
	 */
	private void drawLayout () {

		if (root == null) {
			return;
		}

		buffer = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
		bufferGraphics = buffer.createGraphics();

		Object[] components = TestCaseUtils.components.values().toArray();
		for (int i = 0; i < components.length; i++) {
			LayoutComponent c = (LayoutComponent) components[i];
			int x = c.getAssignedX();
			int y = c.getAssignedY();
			int w = c.getAssignedWidth();
			int h = c.getAssignedHeight();
			int minWidth = c.getWidthHeightRange().getMinWidth();
			int maxWidth = c.getWidthHeightRange().getMaxWidth();
			int minHeight = c.getWidthHeightRange().getMinHeight();
			int maxHeight = c.getWidthHeightRange().getMaxHeight();

			// TODO instead of fillRect use images and print them to the UI
//			bufferGraphics.setColor(colorList.get(i));
//			bufferGraphics.fillRect(x, y, w, h);

			if (c.isFeasible()) {
				bufferGraphics.setColor(Color.GREEN);
			} else {
				bufferGraphics.setColor(Color.RED);
			}
			Graphics2D g2 = (Graphics2D) bufferGraphics;
			Stroke oldStroke = g2.getStroke();
			g2.setStroke(correctnessStroke);
			//TODO : Draw infeasible lines not rectangle
			g2.drawRect(x + 1, y + 1, w - 2, h - 2);
			g2.setStroke(oldStroke);

			bufferGraphics.setColor(Color.black);
			bufferGraphics.setFont(new Font("Arial", Font.PLAIN, 14));
			// Coords
			bufferGraphics.drawString(x + " , " + y, x + 5, y + 15);
			// Draw the min,actual,max values of width-height
			// X values
			bufferGraphics.drawString(minWidth + ", " + w + ", " + maxWidth, x + w / 2 - 40, y + h - 10);
			// Y values
			bufferGraphics.drawString("" + minHeight, x + w - 30, y + h / 2 - 25);
			bufferGraphics.drawString("" + h, x + w - 30, y + h / 2);
			bufferGraphics.drawString("" + maxHeight, x + w - 30, y + h / 2 + 25);
			// Draw the leaf node name
//			bufferGraphics.setColor(TRANSPARENT_BLACK);
////			bufferGraphics.setFont(new Font("Arial", Font.PLAIN, ((w / 2) + (h / 2)) / 3));
//			bufferGraphics.drawString(c.getLabel(), x + w / 2 - (h / 2) / 6, y + h / 2 + (w / 2) / 6);
		}
		repaint();
	}

	@Override
	public void paint (Graphics g) {
		super.paint(g);
//		panel.getGraphics().drawImage(buffer, 0, 0, null);
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
