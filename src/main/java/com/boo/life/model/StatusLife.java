package com.boo.life.model;

/**

 */
public enum StatusLife {

	LIFE(true),

	DEAD(false);

	public boolean isLife;

	private StatusLife(boolean isLife) {
		this.isLife = isLife;
	}
}
