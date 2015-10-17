package com.boo.life.view;

import com.boo.life.model.ModelWorld;
import com.boo.life.model.StatusLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LifePanel extends JPanel implements Runnable {

	private Thread simThread = null;

	private ModelWorld life = null;

	private int updateDelay = 100;

	private int cellSize = 10;

	private int cellGap = 1;

	private static final Color DEAD_CELL_COLOR = new Color(0x505050);

	private static final Color LIFE_CELL_COLOR = new Color(0xFFFFFF);

	public LifePanel() {
		setBackground(Color.BLACK);

		// редактор поля
		MouseAdapter ma = new MouseAdapter() {
			private boolean pressedLeft = false;
			private boolean pressedRight = false;

			@Override
			public void mouseDragged(MouseEvent e) {
				setCell(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					pressedLeft = true;
					pressedRight = false;
					setCell(e);
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					pressedLeft = false;
					pressedRight = true;
					setCell(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					pressedLeft = false;
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					pressedRight = false;
				}
			}

			/**
			 * Устанавливает/стирает клетку.
			 */
			private void setCell(MouseEvent e) {
				if (life != null) {
					synchronized (life) {
						// рассчитываем координаты клетки, на которую указывает курсор мыши
						int x = e.getX() / (cellSize + cellGap);
						int y = e.getY() / (cellSize + cellGap);
						if (x >= 0 && y >= 0 && x < life.getWidth() && y < life.getHeight()) {
							if (pressedLeft == true) {
								life.setStatusLife(x, y, StatusLife.LIFE);
								repaint();
							}
							if (pressedRight == true) {
								life.setStatusLife(x, y, StatusLife.DEAD);
								repaint();
							}
						}
					}
				}
			}
		};
		addMouseListener(ma);
		addMouseMotionListener(ma);
	}

	public ModelWorld getLifeModel() {
		return life;
	}

	public void initialize(int width, int height) {
		life = new ModelWorld(width, height);
	}

	public void setUpdateDelay(int updateDelay) {
		this.updateDelay = updateDelay;
	}

	public void startSimulation() {
		if (simThread == null) {
			simThread = new Thread(this);
			simThread.start();
		}
	}

	public void stopSimulation() {
		simThread = null;
	}

	public boolean isSimulating() {
		return simThread != null;
	}

	@Override
	public void run() {
		repaint();
		while (simThread != null) {
			try {
				Thread.sleep(updateDelay);
			} catch (InterruptedException ignored) {}
			// синхронизация используется для того, чтобы метод paintComponent не выводил на экран
			// содержимое поля, которое в данный момент меняется
			synchronized (life) {
				life.oneCycleLife();
			}
			repaint();
		}
		repaint();
	}

	/*
	 * Возвращает размер панели с учетом размера поля и клеток.
	 */
	@Override
	public Dimension getPreferredSize() {
		if (life != null) {
			Insets b = getInsets();
			return new Dimension((cellSize + cellGap) * life.getWidth() + cellGap + b.left + b.right,
					(cellSize + cellGap) * life.getHeight() + cellGap + b.top + b.bottom);
		} else
			return new Dimension(100, 100);
	}

	/*
	 * Прорисовка содержимого панели.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		if (life != null) {
			synchronized (life) {
				super.paintComponent(g);
				Insets b = getInsets();
				for (int y = 0; y < life.getHeight(); y++) {
					for (int x = 0; x < life.getWidth(); x++) {
						StatusLife c = life.getStatusCell(x, y);
						g.setColor(c.isLife ? LIFE_CELL_COLOR : DEAD_CELL_COLOR);
						g.fillRect(b.left + cellGap + x * (cellSize + cellGap), b.top + cellGap + y
								* (cellSize + cellGap), cellSize, cellSize);
					}
				}
			}
		}
	}

}