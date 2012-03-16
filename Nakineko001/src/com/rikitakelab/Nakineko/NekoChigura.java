package com.rikitakelab.Nakineko;

import android.graphics.Bitmap;

public class NekoChigura {
	final private String name;
	final private Bitmap image;
	float x;
	float y;
	
	public NekoChigura(String name, Bitmap image, float x, float y) {
		this.name = name;
		this.image = image;
		this.x = x;
		this.y = y;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Bitmap getImage() {
		return this.image;
	}
	
	public float getX(){
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public boolean isTouched(float x, float y) {
		float deltaX = (float) (this.image.getWidth()/2.0);
		float deltaY = (float) (this.image.getHeight()/2.0);
		
		boolean conditionForX = (this.x - deltaX < x) && (x < this.x + deltaX);
		boolean conditionForY = (this.y - deltaY < y) && (y < this.y + deltaX);
		
		return conditionForX && conditionForY;
		
	}
}
