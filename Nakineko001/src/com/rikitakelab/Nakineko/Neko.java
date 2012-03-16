package com.rikitakelab.Nakineko;

import android.graphics.Bitmap;
import android.media.MediaPlayer;

public class Neko implements Cat {



	private String name;
	private Bitmap sittingImage;
	private Bitmap walkingImage;
	private MediaPlayer nakigoe;
	private float x;
	private float y;

	//private boolean isWalking = false;
	private State state = State.SITTING;
	
	public Neko(String name, MediaPlayer nakigoe,
			Bitmap sittingImage, Bitmap walkingImage, float x, float y) {
		this.name = name;
		this.nakigoe = nakigoe;
		this.sittingImage = sittingImage;
		this.walkingImage = walkingImage;
		this.x = x;
		this.y = y;
	}
	
	public String getName () {
		return this.name;
	}
	
	public float getX() {
		return x;		
	}
	
	public float getY() {
		return y;
	}
	
	public State getState(){
		return this.state;
	}
	
	public Bitmap getImage() {
		Bitmap image;
		if (getState() == State.SITTING) {
			image = this.sittingImage;
		} else {
			image = this.walkingImage;
		}
		return image;
	}
	
	
	/* (non-Javadoc)
	 * @see com.rikitakelab.Nakineko.Cat#setState(com.rikitakelab.Nakineko.Neko.State)
	 */
	@Override
	public void setState(State state) {
		this.state = state;
	}
	
	/* (non-Javadoc)
	 * @see com.rikitakelab.Nakineko.Cat#isTouched(float, float)
	 */
	@Override
	public boolean isTouched(float x, float y) {
		final float imageR = (float) (getImage().getWidth()/3.0);
		
		final float r = (float) Math.sqrt((x - this.x)*(x - this.x) + (y - this.y)*(y - this.y));
		
		if (r < imageR) {
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.rikitakelab.Nakineko.Cat#moveTo(float, float)
	 */
	@Override
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/* (non-Javadoc)
	 * @see com.rikitakelab.Nakineko.Cat#meows()
	 */
	@Override
	public void meows() {
		getNakigoe().stop();
		try {
			getNakigoe().prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		getNakigoe().seekTo(0);
		getNakigoe().start();
	}

	private MediaPlayer getNakigoe() {
		return this.nakigoe;
	}


}
