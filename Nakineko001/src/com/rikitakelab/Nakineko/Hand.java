package com.rikitakelab.Nakineko;

public class Hand {
	// singleton
	private static Hand instance = new Hand();
	
	private float x = 0;
	private float y = 0;
	private float holdX = 0; //”L‚Ì‚Ç‚±‚ð’Í‚ñ‚Å‚¢‚é‚©
	private float holdY = 0; //
	private Cat holdingNeko = null;
	
	private Hand()
	{
	}
	
	public static Hand getInstance() {
		return instance;
	}
	public float getX(){
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getHoldX() {
		return holdX;
	}
	
	public float getHoldY(){
		return holdY;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setHoldX(float x) {
		this.holdX = x;
	}
	
	public void setHoldY(float y) {
		this.holdY = y;
	}
	
	public boolean triesToCatch(Cat cat) {
		if (this.holdingNeko == null) {
			this.holdingNeko = cat;
			//
			setHoldX(this.x - cat.getX());
			setHoldY(this.y - cat.getY());
			//
			return true;
		} else {
		return false ;
		}
	}

	public Cat getHoldingNeko() {
		return this.holdingNeko;
	}
	
	public void release() {
		this.holdingNeko = null;
	}

	public boolean isHoldingNeko() {
		if (this.holdingNeko != null) {
			return true;
		} else {
			return false;
		}
	}
}
