package com.gamepsychos.puzzler.piece.view;

import java.util.HashMap;
import java.util.Map;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamepsychos.puzzler.R;
import com.gamepsychos.puzzler.animation.AnimationHandler;
import com.gamepsychos.puzzler.piece.Piece;

public class DisplayablePiece implements Piece {
	
	public static enum PieceType {
		BLUE(R.drawable.blue_saphire),
		GREEN(R.drawable.green_saphire),
		RED(R.drawable.red_saphire),
		PURPLE(R.drawable.purple_saphire),
		YELLOW(R.drawable.yellow_saphire),
		ORANGE(R.drawable.orange_saphire);
		
		private final int resource_id;
		private Bitmap image;
		
		private PieceType(int resource_id){
			this.resource_id = resource_id;
		}
		
		private void initImage(Resources res){
			if(image == null){
				image = BitmapFactory.decodeResource(res, resource_id);
				Bitmap scaled = Bitmap.createScaledBitmap(image, SIZE, SIZE, false);
				scaledImages.put(this, scaled);
			}
		}
		
	}
	
	private static final long ANIMATION_DELAY = 400;
	private static int SIZE = 100;
	private static final Map<PieceType, Bitmap> scaledImages = new HashMap<DisplayablePiece.PieceType, Bitmap>();
	
	
	public static void setSize(int size){
		SIZE = size;
		for(PieceType p : PieceType.values()){
			if(p.image == null) continue;
			Bitmap scaled = Bitmap.createScaledBitmap(p.image, SIZE, SIZE, false);
			scaledImages.put(p, scaled);
		}
	}
	
	
	private DisplayLocation location;
	private final PieceType type;
	
	public DisplayablePiece(PieceType type){
		if(type == null)
			throw new NullPointerException();
		location = new DisplayLocation(0, 0);
		this.type = type;
	}

	@Override
	public boolean matches(Piece p) {
		if(!(p instanceof DisplayablePiece))
			return false;
		return ((DisplayablePiece)p).type == type;
	}
	
	public PieceType getType(){
		return type;
	}
	
	public void setLocation(DisplayLocation location){
		if(location == null)
			throw new NullPointerException();
		this.location = location;
	}
	
	public DisplayLocation getLocation(){
		return location;
	}
	
	public ObjectAnimator animateTo(DisplayLocation location, AnimationHandler listener){
		ObjectAnimator animator = ObjectAnimator.ofObject(this, "location", DisplayLocation.getEvaluator(), this.location, location);
		animator.setDuration(ANIMATION_DELAY);
		animator.addListener(listener);
		animator.addUpdateListener(listener);
		animator.start();
		return animator;
	}
	
	public ObjectAnimator animateTo(DisplayLocation l0, DisplayLocation l1, AnimationHandler listener){
		ObjectAnimator animator = ObjectAnimator.ofObject(this, "location", DisplayLocation.getEvaluator(), l0, l1);
		animator.setDuration(ANIMATION_DELAY);
		animator.addListener(listener);
		animator.addUpdateListener(listener);
		animator.start();
		return animator;
	}
	
	public void display(Canvas canvas, Paint paint, Resources res){
		type.initImage(res);
		Bitmap image = scaledImages.get(type);
		canvas.drawBitmap(image, location.getLeft(), location.getTop(), paint);
	}

	public static int getSize() {
		return SIZE;
	}

}
