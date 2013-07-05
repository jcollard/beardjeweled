package com.gamepsychos.puzzler.piece.view;

import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;

import com.gamepsychos.puzzler.board.Location;

/**
 * A {@link DisplayLocation} contains data about a point on the screen.
 * @author jcollard
 *
 */
public class DisplayLocation {
	
	private static final TypeEvaluator<DisplayLocation> evaluator = new DisplayLocationEvaluator();
	
	/**
	 * Returns a {@link TypeEvaluator} that can evaluate between two {@link DisplayLocation}s.
	 * @return a {@link TypeEvaluator} that can evaluate between two {@link DisplayLocation}s.
	 */
	public static TypeEvaluator<DisplayLocation> getEvaluator(){
		return evaluator;
	}
	
	private final float left;
	private final float top;
	
	/**
	 * Creates a new {@link DisplayLocation} converting the specified {@link Location}
	 * based on the current size of {@link Piece}s as determined by {@link PieceResources#getSize()}
	 * @param loc the {@link Location} to translate
	 */
	public DisplayLocation(Location loc){
		if(loc == null)
			throw new NullPointerException();
		int size = PieceResources.getSize();
		this.top = loc.getRow()*size;
		this.left = loc.getCol()*size;
	}
	
	/**
	 * Creates a new {@link DisplayLocation} at the specified coordinates
	 * @param left the distance from the left most side of the screen
	 * @param top the distance from the top most side of the screen
	 */
	public DisplayLocation(float left, float top){
		this.left = left;
		this.top = top;
	}
	
	public float getLeft(){
		return left;
	}
	
	public float getTop(){
		return top;
	}
	
	private static final class DisplayLocationEvaluator implements TypeEvaluator<DisplayLocation>{

		private static final FloatEvaluator evaluator = new FloatEvaluator();
		
		@Override
		public DisplayLocation evaluate(float fraction,
				DisplayLocation startValue, DisplayLocation endValue) {
			float startX = startValue.left;
			float startY = startValue.top;
			float endX = endValue.left;
			float endY = endValue.top;
			float x = evaluator.evaluate(fraction, startX, endX);
			float y = evaluator.evaluate(fraction, startY, endY);
			return new DisplayLocation(x, y);
		}
		
	}

}
