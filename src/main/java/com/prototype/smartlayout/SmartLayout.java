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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.JButton;
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
	private final Vector<LayoutComponent> components;
	private final JPanel panel;
	private JTextField txtnum1;
	private JTextField txtnum2;
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
				finalLayoutCases = root.getRanges();
				root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), getFeasibleLayout(finalLayoutCases));
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
		root.layout(0, 0, 800, 300, finalLayoutCases.get(0));
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
			int red = 100 + (int) (Math.random() * 100);
			int gre = 100 + (int) (Math.random() * 100);
			int blu = 100 + (int) (Math.random() * 100);
			// System.out.println(c + " " + red + " " + gre + " " + blu);

			bufferGraphics.setColor(new Color(red, gre, blu));
			bufferGraphics.fillRect(x, y, w, h);

			bufferGraphics.setColor(Color.black);
			bufferGraphics.setFont(new Font("Arial", Font.PLAIN, ((w / 2) + (h / 2)) / 3));
			bufferGraphics.drawString(c.getLabel(), x + w / 2 - (h / 2) / 6, y + h / 2 + (w / 2) / 6);
			bufferGraphics.setFont(new Font("Arial", Font.PLAIN, 14));
			bufferGraphics.drawString(x + " , " + y, x + 5, y + 15);
			bufferGraphics.drawString("" + w, x + w / 2, y + h - 10);
			bufferGraphics.drawString("" + h, x + w - 30, y + h / 2);
		}
		repaint();
	}

	@Override
	public void paint (Graphics g) {
		super.paint(g);
		panel.getGraphics().drawImage(buffer, 0, 0, null);
	}

	@Override
	public void componentResized (ComponentEvent componentEvent) {
		if (root == null) {
			return;
		}
		setResizeOnRoot();
		finalLayoutCases = root.getRanges();
		this.root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), getFeasibleLayout(finalLayoutCases));
		log.debug("Root Width: " + (root.getAssignedWidth() - 50) + " Root Height: " + (root.getAssignedHeight() - 50) + " Width: " + (panel.getWidth() - 50) + " Height: " + (panel.getHeight() - 50));

		panel.setSize(root.getAssignedWidth(), root.getAssignedHeight());
		drawLayout();
	}

	private WidthHeightRange getFeasibleLayout (Vector<WidthHeightRange> layouts) {
		if (layouts.isEmpty()) {
			return null;
		}

		int minWidthDiff = Integer.MAX_VALUE;
		int minHeightDiff = Integer.MAX_VALUE;
		WidthHeightRange bestFit = null;

		for (WidthHeightRange range : layouts) {
			int widthDiff = Math.min(root.getAssignedWidth() - range.getMinWidth(), Math.abs(root.getAssignedWidth() - range.getMaxWidth()));
			int heightDiff = Math.min(root.getAssignedHeight() - range.getMinHeight(), Math.abs(root.getAssignedHeight() - range.getMaxHeight()));
			if (widthDiff >= 0 && heightDiff >= 0) {
				// Feasible
				if (widthDiff < minWidthDiff && heightDiff < minHeightDiff) {
					minWidthDiff = widthDiff;
					minHeightDiff = heightDiff;
					bestFit = range;
				}
			}
		}
		if (bestFit != null) {
			return bestFit;
		} else {
			return layouts.get(0);
		}
	}

	private void setResizeOnRoot () {
		root.setAssignedWidth(panel.getWidth() - 50);
		root.setAssignedHeight(panel.getHeight() - 50);
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
				finalLayoutCases = root.getRanges();
				root.layout(0, 0, root.getAssignedWidth(), root.getAssignedHeight(), finalLayoutCases.get(counter));
			} else {
				// UP
				root.setAssignedWidth(root.getAssignedWidth() + 50);
				root.setAssignedHeight(root.getAssignedHeight() + 50);
				panel.setSize(root.getAssignedWidth(), root.getAssignedHeight());
				finalLayoutCases = root.getRanges();
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
