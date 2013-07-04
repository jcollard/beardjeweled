package com.gamepsychos.puzzler.piece.view;

import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;

public class DisplayLocation {
	
	private static final TypeEvaluator<DisplayLocation> evaluator = new DisplayLocationEvaluator();
	
	public static TypeEvaluator<DisplayLocation> getEvaluator(){
		return evaluator;
	}
	
	private final float x;
	private final float y;
	
	public DisplayLocation(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float getLeft(){
		return x;
	}
	
	public float getTop(){
		return y;
	}
	
	private static final class DisplayLocationEvaluator implements TypeEvaluator<DisplayLocation>{

		private static final FloatEvaluator evaluator = new FloatEvaluator();
		
		@Override
		public DisplayLocation evaluate(float fraction,
				DisplayLocation startValue, DisplayLocation endValue) {
			float startX = startValue.x;
			float startY = startValue.y;
			float endX = endValue.x;
			float endY = endValue.y;
			float x = evaluator.evaluate(fraction, startX, endX);
			float y = evaluator.evaluate(fraction, startY, endY);
			return new DisplayLocation(x, y);
		}
		
	}

}
