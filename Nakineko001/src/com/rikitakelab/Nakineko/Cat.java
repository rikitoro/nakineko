package com.rikitakelab.Nakineko;

public interface Cat {

	static public enum State {WALKING, SITTING};
	
	public abstract void setState(State state);

	public abstract boolean isTouched(float x, float y);

	public abstract void moveTo(float x, float y);

	public abstract void meows();

	public abstract float getX();

	public abstract float getY();
	

	
}