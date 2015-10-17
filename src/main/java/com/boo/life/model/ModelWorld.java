package com.boo.life.model;

/**
 * The data model "Life."
 * For the simulation, the principle of double buffering : data is taken from the main array of world,
 * after the calculation result is added to the auxiliary array tempWorld.
 * At the end of the calculation one step links to these arrays are swapped.
 * The arrays are stored values: StatusLife.
 */
public class ModelWorld {

	private StatusLife[][] world;

	private int[][] neighborXYOffsets = new int[][] {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};

	/**
	 * Width of the world.
	 */
	private int width;

	/**
	 * Height of the world.
	 */
	private int height;

	/**
	 * The minimum number of neighbors.
	 */
	private static final int MIN_NEIGHBORS = 2;

	/**
	 * The maximum number of neighbors.
	 */
	private static final int MAX_NEIGHBORS = 3;

	/**
	 * The number of neighbors of one cell.
	 */
	private static final int NEIGHBORS_ONE_CELL = 8;

	/**
	 * The number of neighbors for life
	 */
	private static final int NEIGHBORS_FOR_LIFE = 3;

	/**
	 * Initialization of the world.
	 *
	 * @param width width of the world.
	 * @param height height of the world.
	 */
	public ModelWorld(int width, int height) {
		this.width = width;
		this.height = height;

		world = new StatusLife[width][height];

		clearWorld();
	}

	/**
	 * One cycle of life.
	 */
	public void oneCycleLife() {
		StatusLife[][] tempWorld = new StatusLife[width][height];

		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				tempWorld[i][j] = getNewStatusLife(world[i][j], countBorderNeighbors(i, j));
			}
		}
		world = tempWorld;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Get status cell.
	 *
	 * @param x Location in the world.
	 * @param y Location in the world.
	 * @return life status cell
	 * @throws ArrayIndexOutOfBoundsException in the case of the World
	 */
	public StatusLife getStatusCell(int x, int y) throws ArrayIndexOutOfBoundsException {
		return world[x][y];
	}

	/**
	 * Set the state of the cell
	 *
	 * @param x Location in the world.
	 * @param y Location in the world.
	 * @param statusLife state : life(true) / dead(false)
	 * @throws ArrayIndexOutOfBoundsException in the case of the World
	 */
	public void setStatusLife(int x, int y, StatusLife statusLife) throws ArrayIndexOutOfBoundsException {
		world[x][y] = statusLife;
	}

	/**
	 * Clear the world (kill all the cells).
	 */
	public void clearWorld() {
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				world[i][j] = StatusLife.DEAD;
			}
		}
	}

	/**
	 * Counting with the neighbors for the boundary cells.
	 *
	 * @param x Location in the world.
	 * @param y Location in the world.
	 * @return count neighbors.
	 */
	private byte countBorderNeighbors(int x, int y) {
		byte n = 0;
		for (int i = 0; i < NEIGHBORS_ONE_CELL; i++) {
			int bx = (x + neighborXYOffsets[i][0] + width) % width;
			int by = (y + neighborXYOffsets[i][1] + height) % height;
			if (world[bx][by].isLife) {
				n++;
			}
		}
		return n;
	}

	/**
	 * Certain life status.
	 *
	 * @param currentStatusLife status : life(true) / dead(false).
	 * @param neighbors count neighbors.
	 * @return new life status.
	 */
	private StatusLife getNewStatusLife(StatusLife currentStatusLife, byte neighbors) {
		if (currentStatusLife.isLife) {
			if (neighbors >= MIN_NEIGHBORS && neighbors <= MAX_NEIGHBORS) {// если у живой клетки есть две или три живые соседки, то эта клетка продолжает жить;
				return StatusLife.LIFE;
			}
		} else {
			if (neighbors == NEIGHBORS_FOR_LIFE) {// мёртвой клетке, рядом с которой ровно три живые клетки, зарождается жизнь;
				return StatusLife.LIFE;
			}
		}
		return StatusLife.DEAD;
	}

	@Override
	public String toString() {
		for (int i = 0; i < width; ++i) {
			System.out.println();
			for (int j = 0; j < height; ++j) {
				if (world[i][j].isLife) {
					System.out.print("O | ");
				} else {
					System.out.print("X | ");
				}
			}
		}
		return "";
	}
}
