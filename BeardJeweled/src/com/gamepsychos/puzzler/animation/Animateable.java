package com.gamepsychos.puzzler.animation;

import com.gamepsychos.puzzler.piece.view.DisplayLocation;

import android.animation.ValueAnimator;

public interface Animateable {
	
	public ValueAnimator createAnimator(DisplayLocation firstLocation, DisplayLocation ... displayLocations);

}
