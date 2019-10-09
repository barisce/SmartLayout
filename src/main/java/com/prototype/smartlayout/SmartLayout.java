package com.prototype.smartlayout;

import com.prototype.smartlayout.model.LayoutComponent;
import com.prototype.smartlayout.model.LayoutContainer;
import com.prototype.smartlayout.model.Layoutable;
import com.prototype.smartlayout.model.WidthHeightRange;
import com.prototype.smartlayout.model.WidthHeightRangeEnum;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.PropertyConfigurator;

@Log4j
public class SmartLayout extends JFrame implements ComponentListener {
    private static final long serialVersionUID = 6944709955451188697L;
    private final Vector<LayoutComponent> components;
    private final Canvas canv;
    private final JPanel panel;
    private Layoutable root;
    private BufferedImage buffer;
    private Graphics bufferGraphics;
    private Vector<WidthHeightRange> finalLayoutCases;

    private SmartLayout() {
        super();
        components = new Vector<>();
        root = null;
        finalLayoutCases = null;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(this);

        canv = new Canvas();
        canv.setSize(100, 100);
        panel = new JPanel(new FlowLayout());
        panel.add(canv);

        buffer = new BufferedImage(canv.getWidth(), canv.getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.createGraphics();

        setContentPane(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure(
                SmartLayout.class.getProtectionDomain().getCodeSource().getLocation().getPath()
                        + "log4j.properties");
        //        PropertyConfigurator.configure("log4j.properties");

        SmartLayout app = new SmartLayout();
        app.run();
        app.setSize(app.root.getAssignedWidth() + 50, app.root.getAssignedHeight() + 50);
    }

    /**
     * Creates a new component under this layout.
     *
     * @param label The label of the new component.
     * @param range The width and height range of the new component.
     * @return The component to be created.
     */
    private LayoutComponent createComponent(String label, WidthHeightRange range) {
        LayoutComponent c = new LayoutComponent(label, range);
        components.add(c);
        return c;
    }

    /**
     * Creates a demo layout main.java.com.prototype.smartlayout.model and runs the layout algorithm
     * on the main.java.com.prototype.smartlayout.model.
     */
    private void run() {
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
                        "A", new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 200, 100, 100));
        LayoutComponent compB =
                createComponent(
                        "B", new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 200, 100, 100));
        LayoutComponent compC =
                createComponent(
                        "C", new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 100, 200, 100, 100));
        LayoutComponent compD =
                createComponent(
                        "D", new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 400, 200, 400));
        LayoutComponent compE =
                createComponent(
                        "E", new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 300, 400, 100, 150));
        LayoutComponent compF =
                createComponent(
                        "F", new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 300, 400, 100, 150));
        LayoutComponent compG =
                createComponent(
                        "G", new WidthHeightRange(WidthHeightRangeEnum.SINGLE, 200, 300, 200, 300));

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

    /** Draw the layout on the screen. */
    private void drawLayout() {
        if (root == null) {
            return;
        }

        for (LayoutComponent c : components) {
            int x = c.getAssignedX();
            int y = c.getAssignedY();
            int w = c.getAssignedWidth();
            int h = c.getAssignedHeight();
            int red = 100 + (int) (Math.random() * 100);
            int gre = 100 + (int) (Math.random() * 100);
            int blu = 100 + (int) (Math.random() * 100);
            //			System.out.println(c + " " + red + " " + gre + " " + blu);

            bufferGraphics.setColor(new Color(red, gre, blu));
            bufferGraphics.fillRect(x, y, w, h);

            bufferGraphics.setColor(Color.black);
            bufferGraphics.drawString(c.getLabel(), x + w / 2, y + h / 2);
            bufferGraphics.drawString(x + " , " + y, x + 5, y + 15);
            bufferGraphics.drawString("" + w, x + w / 2, y + h - 10);
            bufferGraphics.drawString("" + h, x + w - 30, y + h / 2);
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        canv.getGraphics().drawImage(buffer, 0, 0, null);
    }

    /** */
    @Override
    public void componentResized(ComponentEvent componentEvent) {
        if (root == null) {
            return;
        }
        //		this.root.layout(0, 0, this.getWidth() - 50, this.getHeight() - 50, finalLayoutCases
        //				.get(0));
        canv.setSize(new Dimension(root.getAssignedWidth(), root.getAssignedHeight()));
        buffer = new BufferedImage(canv.getWidth(), canv.getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferGraphics = buffer.createGraphics();
        drawLayout();
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {}

    @Override
    public void componentShown(ComponentEvent componentEvent) {}

    @Override
    public void componentHidden(ComponentEvent componentEvent) {}
}
