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

	// 背景色
	private int backgroundColor = Color.GREEN;
	// 鳴いている猫のイメージのリスト
	private List<Bitmap> sittingImages = new ArrayList<Bitmap>();
	
	//　歩いている猫のイメージリスト
	private List<Bitmap> walkingImages = new ArrayList<Bitmap>();
	
	// 鳴き声のリスト
	private List<MediaPlayer> nakigoes = new ArrayList<MediaPlayer>();
	
	// 運命のサイコロ
	private Random dice;
	
	// 猫を掴む手	
	private Hand hand;
	// 猫ちぐら
	private NekoChigura nekoChigura;
	// 猫ちぐらにいる猫
	private Neko watingNeko;
	
	// お外にいる猫たち
	private List<Neko> nekos = new ArrayList<Neko>(); 
	
	private NekoComparator nekoComparator = new NekoComparator();

	public MyView(Context context) {
		super(context);
		setFocusable(true);
		this.myContext = context;
		this.res = this.getContext().getResources();

		// ダイスの設定
		Time time = new Time("Asia/Tokyo");
		time.setToNow();
		this.dice = new Random(time.minute + time.second);
		
		// 手の作成
		this.hand = Hand.getInstance();

		// 猫ちぐら
		Bitmap chiguraImage = BitmapFactory.decodeResource(res, R.drawable.nekochigura);
		this.nekoChigura = new NekoChigura("Chigura", chiguraImage,
				chiguraImage.getWidth()/2, chiguraImage.getHeight()/2);

		// このあたりXMLにファイル名等を載せておき、
		//　その内容をもとに自動的に登録していくようしたい。
		
		//　猫イメージ登録
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
		// 鳴き声の登録
		List<Integer> nakigoeIdList = new ArrayList<Integer>();
		nakigoeIdList.add(R.raw.doneko);
		nakigoeIdList.add(R.raw.reneko);
		nakigoeIdList.add(R.raw.mineko);
		nakigoeIdList.add(R.raw.faneko);
		nakigoeIdList.add(R.raw.soneko);
		nakigoeIdList.add(R.raw.raneko);
		nakigoeIdList.add(R.raw.sineko);
		nakigoeIdList.add(R.raw.ddoneko);
		// 鳴き声リストの登録	
		for(int id: nakigoeIdList) {
			this.nakigoes.add(MediaPlayer.create(myContext, id));
		}
		
		// １匹ちぐらに猫がいます
		this.watingNeko = fetchNewNeko(); 

	}

     
    protected void onDraw(Canvas canvas) {  
        /* 背景色を設定 */  
        canvas.drawColor(this.backgroundColor);
        
        // 箱の猫の描画
        drawNeko(this.watingNeko, canvas);
        
        // 猫ちぐらの描画
        Bitmap chiguraImage = this.nekoChigura.getImage();
        canvas.drawBitmap(chiguraImage, 
        		(float)(nekoChigura.getX() - chiguraImage.getWidth()/2.0),
        		(float)(nekoChigura.getY() - chiguraImage.getHeight()/2.0),
        		this.myPaint);
        
        // お外の猫たちの描画  
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
		
		//　手を移動
		hand.setX(event.getX());
		hand.setY(event.getY());
		// 手の場所に猫がいたら掴みます
		for (ListIterator<Neko> it = nekos.listIterator(nekos.size()); it.hasPrevious(); ) {
			Neko neko = it.previous();
			if(neko.isTouched(hand.getX(), hand.getY())) {
				hand.triesToCatch(neko);
			}
		}
		
		if (hand.isHoldingNeko()) { //猫を掴んでいるときは
			Cat neko = hand.getHoldingNeko();
			// 猫を鳴かせたり
			singingProcedure(neko, event);
			// 歩かせたり
			walkingProcedure(neko, event);
			// 手を離したりするよ
			releasingProcedure(neko, event);
			// ちぐらで手を離すと隠れちゃいます
			dismissingProcedure(neko, event);
		} else { //　猫を掴んでなかったら
			// ちぐらをつつくと猫がでてくるよ
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
				// 箱の猫が外に出ます。
				watingNeko.meows();
				hand.triesToCatch(watingNeko);
				nekos.add(this.watingNeko);
				// 新しい子が箱から顔を出します。
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