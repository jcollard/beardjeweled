package com.gamepsychos.puzzler.piece.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.gamepsychos.puzzler.animation.Animateable;
import com.gamepsychos.puzzler.animation.Displayable;
import com.gamepsychos.puzzler.piece.Piece;

public class DisplayablePiece implements Displayable, Animateable {

	private static final long ANIMATION_DURATION = 300;
	
	private DisplayLocation displayLocation;
	private final Piece model;
	private final View view;
	
	public DisplayablePiece(Piece model, DisplayLocation displayLocation, View view){
		if(model == null || displayLocation == null || view == null)
			throw new NullPointerException();
		this.model = model;
		this.displayLocation = displayLocation;
		this.view = view;
	}
	
	public DisplayLocation getLocation(){
		return displayLocation;
	}
	
	public final void setLocation(DisplayLocation location){
		if(location == null)
			throw new NullPointerException();
		this.displayLocation = location;
		view.invalidate();
	}
	
	@Override
	public void display(Canvas canvas, Paint paint) {
		if(canvas == null || paint == null)
			throw new NullPointerException();
		Bitmap bitmap = PieceResources.getBitmap(model);
		float left = displayLocation.getLeft();
		float top = displayLocation.getTop();
		canvas.drawBitmap(bitmap, left, top, paint);
	}

	@Override
	public ValueAnimator createAnimator(DisplayLocation first, DisplayLocation ... displayLocations) {
		Object[] locations = new Object[displayLocations.length+1];
		locations[0] = first;
		int i = 1;
		for(DisplayLocation loc : displayLocations)
			locations[i++] = loc;
		ObjectAnimator animator = ObjectAnimator.ofObject(this, "location", DisplayLocation.getEvaluator(), locations);
		
		animator.setDuration(ANIMATION_DURATION);
		return animator;
	}

}
