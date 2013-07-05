package com.gamepsychos.puzzler.animation;

import com.gamepsychos.puzzler.piece.view.DisplayLocation;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * A {@code Displayable} is capable of displaying itself.
 * @author jcollard
 *
 */
public interface Displayable {

	/**
	 * Display this {@code Displayable} on the specified {@link Canvas} at some
	 * specified offset and using the specified {@link Paint}
	 * @param canvas the {@link Canvas} to display on
	 * @param paint the {@link Paint} to use
	 * @param offset the offset to display at.
	 */
	public void display(Canvas canvas, Paint paint, DisplayLocation offset);
	
}
