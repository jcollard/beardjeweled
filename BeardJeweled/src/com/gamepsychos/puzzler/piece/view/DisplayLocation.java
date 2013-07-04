package com.gamepsychos.puzzler.piece.view;

import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;

import com.gamepsychos.puzzler.board.Location;

public class DisplayLocation {
	
	private static final TypeEvaluator<DisplayLocation> evaluator = new DisplayLocationEvaluator();
	
	public static TypeEvaluator<DisplayLocation> getEvaluator(){
		return evaluator;
	}
	
	private final float left;
	private final float top;
	
	public DisplayLocation(Location loc){
		if(loc == null)
			throw new NullPointerException();
		int size = PieceResources.getSize();
		this.top = loc.getRow()*size;
		this.left = loc.getCol()*size;
	}
	
	public DisplayLocation(float x, float y){
		this.left = x;
		this.top = y;
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
