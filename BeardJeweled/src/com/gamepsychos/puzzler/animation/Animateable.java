package com.gamepsychos.puzzler.animation;

import java.util.List;

import com.gamepsychos.puzzler.piece.view.DisplayLocation;

import android.animation.ValueAnimator;

/**
 * An {@code Animateable} is an element that can be animated between {@link DisplayLocation}s.
 * @author jcollard
 *
 */
public interface Animateable {
	
	/**
	 * Creates a {@link ValueAnimator} that moves from the first location to each subsequent locations.
	 * If only the first location is specified, the result is that the element will be animated from its.
	 * The animation is not started. If no locations are specified the animator does nothing.
	 * current location to the first location
	 * @param toAnimate a list of locations to animate between.
	 * @return a {@link ValueAnimator} which controls the animation.
	 */
	public ValueAnimator createAnimator(List<DisplayLocation> toAnimate);

}
