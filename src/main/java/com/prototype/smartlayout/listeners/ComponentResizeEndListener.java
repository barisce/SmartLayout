package com.prototype.smartlayout.listeners;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.Timer;

public abstract class ComponentResizeEndListener extends ComponentAdapter implements ActionListener {

	private final Timer timer;

	public ComponentResizeEndListener () {
		this(150);
	}

	public ComponentResizeEndListener (int delayMS) {
		timer = new Timer(delayMS, this);
		timer.setRepeats(false);
		timer.setCoalesce(false);
	}

	@Override
	public void componentResized (ComponentEvent e) {
		timer.restart();
	}

	@Override
	public void actionPerformed (ActionEvent e) {
		resizeTimedOut();
	}

	public abstract void resizeTimedOut ();
}
