package com.boo.life;


import com.boo.life.view.LifeWindow;

import javax.swing.*;

public class LifeRun  {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LifeWindow("LifeRun");
			}
		});
	}

}