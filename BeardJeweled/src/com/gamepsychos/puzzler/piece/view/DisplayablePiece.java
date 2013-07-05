package com.gamepsychos.puzzler.piece.view;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.gamepsychos.puzzler.animation.Animateable;
import com.gamepsychos.puzzler.animation.Displayable;
import com.gamepsychos.puzzler.piece.Piece;

/**
 * A {@link DisplayablePiece} models a {@link Piece}, displays itself and 
 * can be animated.
 * @author jcollard
 *
 */
public class DisplayablePiece implements Displayable, Animateable {

	private static final long DEFAULT_ANIMATION_DURATION = 300;
	
	private DisplayLocation displayLocation;
	private final Piece model;
	private final View view;
	
	/**
	 * Creates a {@link DisplayablePiece} initializing the {@link Piece}, initial
	 * {@link DisplayLocation}, and {@link View} to update upon changing.
	 * @param model the {@link Piece} to model
	 * @param displayLocation the {@link DisplayLocaiton} to start at
	 * @param view the {@link View} to notify upon update.
	 */
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
	public void display(Canvas canvas, Paint paint, DisplayLocation offset) {
		if(canvas == null || paint == null)
			throw new NullPointerException();
		Bitmap bitmap = PieceResources.getBitmap(model);
		float left = displayLocation.getLeft() + offset.getLeft();
		float top = displayLocation.getTop() + offset.getTop();
		canvas.drawBitmap(bitmap, left, top, paint);
	}

	@Override
	public ValueAnimator createAnimator(List<DisplayLocation> locations){
		if(locations.isEmpty()) return ObjectAnimator.ofObject(this, "location", DisplayLocation.getEvaluator(), displayLocation);
		Object[] locs = locations.toArray();
		ObjectAnimator animator = ObjectAnimator.ofObject(this, "location", DisplayLocation.getEvaluator(), locs);
		animator.setDuration(DEFAULT_ANIMATION_DURATION);
		return animator;
	}

}
