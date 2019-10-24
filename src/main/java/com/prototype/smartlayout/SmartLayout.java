package com.prototype.smartlayout;

import com.prototype.smartlayout.listeners.KeyInputHandler;
import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.Layoutable;
import com.prototype.smartlayout.model.WidthHeightRange;
import com.prototype.smartlayout.model.WidthHeightRangeEnum;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.JButton;
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
	private static final Color TRANSPARENT_BLACK = new Color(0f, 0f, 0f, 0.4f);
	private final Vector<LayoutComponent> components;
	private final JPanel panel;
	private JLabel lblFeasible;
	private JTextField txtnum1;
	private JTextField txtnum2;
	private JComboBox comboBox;
	private Layoutable root;
	private BufferedImage buffer;
	private Graphics bufferGraphics;
	private Vector<WidthHeightRange> finalLayoutCases;

	private SmartLayout () {
		super();
		components = new Vector<>();
		root = null;
		finalLayoutCases = null;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addComponentListener(this);

		JPanel outerPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new FlowLayout());
		panel = new JPanel(new FlowLayout());
		panel.setSize(800, 600);
		panel.addMouseListener(new CanvasMouseListener());
		panel.addMouseWheelListener(new CanvasMouseListener());
		panel.addKeyListener(new KeyInputHandler());

		lblFeasible = new JLabel("Fea");
		topPanel.add(lblFeasible);

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
					finalLayoutCases = root.getRanges();
					if (root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), finalLayoutCases.get(comboBox.getSelectedIndex()))) {
						lblFeasible.setForeground(Color.BLUE);
						lblFeasible.setText("Fea");
					} else {
						lblFeasible.setForeground(Color.RED);
						lblFeasible.setText("Inf");
					}
					drawLayout();
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
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				root.setAssignedWidth(Integer.parseInt(txtnum1.getText()));
				root.setAssignedHeight(Integer.parseInt(txtnum2.getText()));
				getFinalLayoutCases();
				if (root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), getFeasibleLayout(finalLayoutCases))) {
					lblFeasible.setForeground(Color.BLUE);
					lblFeasible.setText("Fea");
				} else {
					lblFeasible.setForeground(Color.RED);
					lblFeasible.setText("Inf");
				}
				drawLayout();
			}
		});
		topPanel.add(button);
		outerPanel.add(panel, BorderLayout.CENTER);
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
		app.setSize(1300, 800);
	}

	/**
	 * Creates a new component under this layout.
	 *
	 * @param label The label of the new component.
	 * @param range The width and height range of the new component.
	 * @return The component to be created.
	 */
	private LayoutComponent createComponent (String label, WidthHeightRange range) {
		LayoutComponent c = new LayoutComponent(label, range);
		components.add(c);
		return c;
	}

	/**
	 * Creates a demo layout LayoutComponent and runs the layout algorithm
	 * on the LayoutContainer
	 */
	private void run () {
		log.debug("Starting test...");

        /*
        A diagram to show what this test is about:

        M
        +--------------------------------+--------------------------------+
        |                                |                                |
        Z                                X                                D
        +---------------------+          +----------+----------+
        |                     |          |          |          |
        Y                     G          A          B          C
        +----------+
        |          |
        E          F
         */

		LayoutComponent compA =
				createComponent(
						"A",
						new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 200, 100, 100));
		LayoutComponent compB =
				createComponent(
						"B",
						new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 200, 100, 100));
		LayoutComponent compC =
				createComponent(
						"C",
						new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 200, 100, 100));
		LayoutComponent compD =
				createComponent(
						"D",
						new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 400, 200, 400));
		LayoutComponent compE =
				createComponent(
						"E",
						new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 300, 400, 100, 150));
		LayoutComponent compF =
				createComponent(
						"F",
						new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 300, 400, 100, 150));
		LayoutComponent compG =
				createComponent(
						"G",
						new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 300, 200, 300));

		LayoutContainer contY = new LayoutContainer();
		contY.addComponent(compE);
		contY.addComponent(compF);

		LayoutContainer contZ = new LayoutContainer();
		contZ.addComponent(contY);
		contZ.addComponent(compG);

		LayoutContainer contX = new LayoutContainer();
		contX.addComponent(compA);
		contX.addComponent(compB);
		contX.addComponent(compC);

		LayoutContainer contM = new LayoutContainer();
		contM.addComponent(contZ);
		contM.addComponent(contX);
		contM.addComponent(compD);

		root = contM;

		finalLayoutCases = root.getRanges();
		log.debug(finalLayoutCases);
		if (root.layout(0, 0, 800, 300, finalLayoutCases.get(0))) {
			lblFeasible.setForeground(Color.BLUE);
			lblFeasible.setText("Fea");
		} else {
			lblFeasible.setForeground(Color.RED);
			lblFeasible.setText("Inf");
		}
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

		for (LayoutComponent c : components) {
			int x = c.getAssignedX();
			int y = c.getAssignedY();
			int w = c.getAssignedWidth();
			int h = c.getAssignedHeight();
			int minWidth = c.getWidthHeightRange().getMinWidth();
			int maxWidth = c.getWidthHeightRange().getMaxWidth();
			int minHeight = c.getWidthHeightRange().getMinHeight();
			int maxHeight = c.getWidthHeightRange().getMaxHeight();
			int red = 100 + (int) (Math.random() * 100);
			int gre = 100 + (int) (Math.random() * 100);
			int blu = 100 + (int) (Math.random() * 100);
			// System.out.println(c + " " + red + " " + gre + " " + blu);

			bufferGraphics.setColor(new Color(red, gre, blu));
			bufferGraphics.fillRect(x, y, w, h);

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
			bufferGraphics.setColor(TRANSPARENT_BLACK);
			bufferGraphics.setFont(new Font("Arial", Font.PLAIN, ((w / 2) + (h / 2)) / 3));
			bufferGraphics.drawString(c.getLabel(), x + w / 2 - (h / 2) / 6, y + h / 2 + (w / 2) / 6);
		}
		repaint();
	}

	@Override
	public void paint (Graphics g) {
		super.paint(g);
		panel.getGraphics().drawImage(buffer, 0, 0, null);
	}

	private void getFinalLayoutCases () {
		finalLayoutCases = root.getRanges();
		comboBox.removeAllItems();
		for (int i = 0; i < finalLayoutCases.size(); i++) {
			comboBox.addItem(finalLayoutCases.get(i));
		}
	}

	@Override
	public void componentResized (ComponentEvent componentEvent) {
		if (root == null) {
			return;
		}
		setResizeOnRoot();
		getFinalLayoutCases();
		boolean feasible = this.root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), getFeasibleLayout(finalLayoutCases));
		log.debug("Root Width: " + (root.getAssignedWidth()) + " Root Height: " + (root.getAssignedHeight()) + " Width: " + (panel.getWidth()) + " Height: " + (panel.getHeight()));

		panel.setSize(root.getAssignedWidth(), root.getAssignedHeight());
		drawLayout();
		if (feasible) {
			lblFeasible.setForeground(Color.BLUE);
			lblFeasible.setText("Fea");
//			drawLayout();
		} else {
			lblFeasible.setForeground(Color.RED);
			lblFeasible.setText("Inf");
//			drawBlank();
		}
	}

	private WidthHeightRange getFeasibleLayout (Vector<WidthHeightRange> layouts) {
		if (layouts.isEmpty()) {
			return null;
		}

		// Start with max values since we try to find minimum values
		double minWidthDiff = Integer.MAX_VALUE;
		double minHeightDiff = Integer.MAX_VALUE;
		WidthHeightRange bestFit = null;

		for (WidthHeightRange range : layouts) {
			// Find closest pair of points according to root point.
			double minsDist = Math.sqrt(Math.pow(root.getAssignedWidth() - range.getMinWidth(), 2) + Math.pow(root.getAssignedHeight() - range.getMinHeight(), 2));
			double maxsDist = Math.sqrt(Math.pow(root.getAssignedWidth() - range.getMaxWidth(), 2) + Math.pow(root.getAssignedHeight() - range.getMaxHeight(), 2));
			// if both min and max values of possible layouts are less than our current distance set the new values
			if (minsDist < minWidthDiff && maxsDist < minHeightDiff) {
				minWidthDiff = minsDist;
				minHeightDiff = maxsDist;
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
				panel.setSize(root.getAssignedWidth(), root.getAssignedHeight());
				getFinalLayoutCases();
				root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), finalLayoutCases.get(counter));
			} else {
				// UP
				root.setAssignedWidth(root.getAssignedWidth() + 50);
				root.setAssignedHeight(root.getAssignedHeight() + 50);
				panel.setSize(root.getAssignedWidth(), root.getAssignedHeight());
				getFinalLayoutCases();
				root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), finalLayoutCases.get(counter));
			}
			logger.debug("Width - " + root.getAssignedWidth() + " Y - " + root.getAssignedHeight());
			log.debug(finalLayoutCases);
			repaint();
		}

		private void incrementCount () {
			counter = (counter + 1) % finalLayoutCases.size();
		}
	}
}
