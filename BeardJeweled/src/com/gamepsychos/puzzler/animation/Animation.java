package com.gamepsychos.puzzler.animation;

import android.animation.ValueAnimator;

/**
 * An {@code Animation} contains information on animating.
 * @author jcollard
 *
 */
public interface Animation {
	
	/**
	 * Returns the {@link ValueAnimator} that can animate.
	 * @return the {@link ValueAnimator} that can animate.
	 */
	public ValueAnimator getAnimator();

}
