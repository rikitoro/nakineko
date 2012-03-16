package com.rikitakelab.Nakineko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;


public class MyView extends View {

	final private int maxnumOfNeko = 12;
	
	private Context myContext;
	private Resources res;

	
	private Paint myPaint = new Paint();  

	// ”wŒiF
	private int backgroundColor = Color.GREEN;
	// –Â‚¢‚Ä‚¢‚é”L‚ÌƒCƒ[ƒW‚ÌƒŠƒXƒg
	private List<Bitmap> sittingImages = new ArrayList<Bitmap>();
	
	//@•à‚¢‚Ä‚¢‚é”L‚ÌƒCƒ[ƒWƒŠƒXƒg
	private List<Bitmap> walkingImages = new ArrayList<Bitmap>();
	
	// –Â‚«º‚ÌƒŠƒXƒg
	private List<MediaPlayer> nakigoes = new ArrayList<MediaPlayer>();
	
	// ‰^–½‚ÌƒTƒCƒRƒ
	private Random dice;
	
	// ”L‚ğ’Í‚Şè	
	private Hand hand;
	// ”L‚¿‚®‚ç
	private NekoChigura nekoChigura;
	// ”L‚¿‚®‚ç‚É‚¢‚é”L
	private Neko watingNeko;
	
	// ‚¨ŠO‚É‚¢‚é”L‚½‚¿
	private List<Neko> nekos = new ArrayList<Neko>(); 
	
	private NekoComparator nekoComparator = new NekoComparator();

	public MyView(Context context) {
		super(context);
		setFocusable(true);
		this.myContext = context;
		this.res = this.getContext().getResources();

		// ƒ_ƒCƒX‚Ìİ’è
		Time time = new Time("Asia/Tokyo");
		time.setToNow();
		this.dice = new Random(time.minute + time.second);
		
		// è‚Ìì¬
		this.hand = Hand.getInstance();

		// ”L‚¿‚®‚ç
		Bitmap chiguraImage = BitmapFactory.decodeResource(res, R.drawable.nekochigura);
		this.nekoChigura = new NekoChigura("Chigura", chiguraImage,
				chiguraImage.getWidth()/2, chiguraImage.getHeight()/2);

		// ‚±‚Ì‚ ‚½‚èXML‚Éƒtƒ@ƒCƒ‹–¼“™‚ğÚ‚¹‚Ä‚¨‚«A
		//@‚»‚Ì“à—e‚ğ‚à‚Æ‚É©“®“I‚É“o˜^‚µ‚Ä‚¢‚­‚æ‚¤‚µ‚½‚¢B
		
		//@”LƒCƒ[ƒW“o˜^
		List<Integer> sittingImageIdList = new ArrayList<Integer>();
		sittingImageIdList.add(R.drawable.aosong);
		sittingImageIdList.add(R.drawable.buchisong);
		sittingImageIdList.add(R.drawable.kurisong);
		sittingImageIdList.add(R.drawable.kurosong);
		sittingImageIdList.add(R.drawable.shirosong);
		sittingImageIdList.add(R.drawable.torasong);
		for (int id: sittingImageIdList) {
			this.sittingImages.add(BitmapFactory.decodeResource(res, id));
		}
		//
		List<Integer> walkingImageIdList = new ArrayList<Integer>();
		walkingImageIdList.add(R.drawable.aowalk);
		walkingImageIdList.add(R.drawable.buchiwalk);
		walkingImageIdList.add(R.drawable.kuriwalk);
		walkingImageIdList.add(R.drawable.kurowalk);
		walkingImageIdList.add(R.drawable.shirowalk);
		walkingImageIdList.add(R.drawable.torawalk);
		for (int id: walkingImageIdList) {
			this.walkingImages.add(BitmapFactory.decodeResource(res, id));
		}
		// –Â‚«º‚Ì“o˜^
		List<Integer> nakigoeIdList = new ArrayList<Integer>();
		nakigoeIdList.add(R.raw.doneko);
		nakigoeIdList.add(R.raw.reneko);
		nakigoeIdList.add(R.raw.mineko);
		nakigoeIdList.add(R.raw.faneko);
		nakigoeIdList.add(R.raw.soneko);
		nakigoeIdList.add(R.raw.raneko);
		nakigoeIdList.add(R.raw.sineko);
		nakigoeIdList.add(R.raw.ddoneko);
		// –Â‚«ºƒŠƒXƒg‚Ì“o˜^	
		for(int id: nakigoeIdList) {
			this.nakigoes.add(MediaPlayer.create(myContext, id));
		}
		
		// ‚P•C‚¿‚®‚ç‚É”L‚ª‚¢‚Ü‚·
		this.watingNeko = fetchNewNeko(); 

	}

     
    protected void onDraw(Canvas canvas) {  
        /* ”wŒiF‚ğİ’è */  
        canvas.drawColor(this.backgroundColor);
        
        // ” ‚Ì”L‚Ì•`‰æ
        drawNeko(this.watingNeko, canvas);
        
        // ”L‚¿‚®‚ç‚Ì•`‰æ
        Bitmap chiguraImage = this.nekoChigura.getImage();
        canvas.drawBitmap(chiguraImage, 
        		(float)(nekoChigura.getX() - chiguraImage.getWidth()/2.0),
        		(float)(nekoChigura.getY() - chiguraImage.getHeight()/2.0),
        		this.myPaint);
        
        // ‚¨ŠO‚Ì”L‚½‚¿‚Ì•`‰æ  
        for (Neko neko: nekos) {
        	drawNeko(neko, canvas);
        }

    }  

    private void drawNeko(Neko neko, Canvas canvas) {
    	Bitmap image = neko.getImage();
    	canvas.drawBitmap(image, 
    			(float) (neko.getX() - image.getWidth()/2.0), 
    			(float) (neko.getY() - image.getHeight()/2.0), this.myPaint);
	}

	public boolean onTouchEvent(MotionEvent event) {
		
		//@è‚ğˆÚ“®
		hand.setX(event.getX());
		hand.setY(event.getY());
		// è‚ÌêŠ‚É”L‚ª‚¢‚½‚ç’Í‚İ‚Ü‚·
		for (ListIterator<Neko> it = nekos.listIterator(nekos.size()); it.hasPrevious(); ) {
			Neko neko = it.previous();
			if(neko.isTouched(hand.getX(), hand.getY())) {
				hand.triesToCatch(neko);
			}
		}
		
		if (hand.isHoldingNeko()) { //”L‚ğ’Í‚ñ‚Å‚¢‚é‚Æ‚«‚Í
			Cat neko = hand.getHoldingNeko();
			// ”L‚ğ–Â‚©‚¹‚½‚è
			singingProcedure(neko, event);
			// •à‚©‚¹‚½‚è
			walkingProcedure(neko, event);
			// è‚ğ—£‚µ‚½‚è‚·‚é‚æ
			releasingProcedure(neko, event);
			// ‚¿‚®‚ç‚Åè‚ğ—£‚·‚Æ‰B‚ê‚¿‚á‚¢‚Ü‚·
			dismissingProcedure(neko, event);
		} else { //@”L‚ğ’Í‚ñ‚Å‚È‚©‚Á‚½‚ç
			// ‚¿‚®‚ç‚ğ‚Â‚Â‚­‚Æ”L‚ª‚Å‚Ä‚­‚é‚æ
			fetchingProcedure(event);
		}
				
     	invalidate();          
        return true;  
    }

	private void dismissingProcedure(Cat neko, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
        	if (nekoChigura.isTouched(hand.getX(), hand.getY())) {
        		hand.release();
        		nekos.remove(neko);
        	}
		}
	}

	private void fetchingProcedure(MotionEvent event) {
		if ((event.getAction() == MotionEvent.ACTION_DOWN) && 
				(nekos.size() < this.maxnumOfNeko)) 
		{
			if (nekoChigura.isTouched(hand.getX(), hand.getY())) {
				// ” ‚Ì”L‚ªŠO‚Éo‚Ü‚·B
				watingNeko.meows();
				hand.triesToCatch(watingNeko);
				nekos.add(this.watingNeko);
				// V‚µ‚¢q‚ª” ‚©‚çŠç‚ğo‚µ‚Ü‚·B
				this.watingNeko = fetchNewNeko(); // create new Neko (Factory)
			}	
		}
	}
	
	private Neko fetchNewNeko() {
		int imageDice = dice.nextInt(sittingImages.size());
		int nakigoeDice = dice.nextInt(nakigoes.size());
		Bitmap sittingImage = sittingImages.get(imageDice);
		Bitmap walkingImage = walkingImages.get(imageDice);
		MediaPlayer nakigoe = nakigoes.get(nakigoeDice);
		
		return new Neko("hoge", nakigoe, sittingImage, walkingImage,
				nekoChigura.getX(), nekoChigura.getY());
	}


	private void releasingProcedure(Cat neko, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Collections.sort(this.nekos, nekoComparator);
			hand.release();
		}
	}

	private void walkingProcedure(Cat neko, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
        		neko.setState(Cat.State.WALKING);
        		neko.moveTo(hand.getX()-hand.getHoldX(), hand.getY() - hand.getHoldY());
		}		
	}

	private void singingProcedure(Cat neko, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			neko.setState(Cat.State.SITTING);
			neko.meows();
		}	
	}
	
	public void changeBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		invalidate();
	}
}