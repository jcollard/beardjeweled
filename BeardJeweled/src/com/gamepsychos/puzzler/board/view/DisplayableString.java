package com.gamepsychos.puzzler.board.view;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.gamepsychos.puzzler.animation.Animateable;
import com.gamepsychos.puzzler.animation.Displayable;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;

public class DisplayableString implements Displayable, Animateable {

	private static final long DEFAULT_ANIMATION_DURATION = 300;
	private final String string;
	private DisplayLocation displayLocation;
	private final View toNotify;
	private final int color;

	public DisplayableString(String string, DisplayLocation start, View toNotify, int color) {
		if (string == null || start == null || toNotify == null)
			throw new NullPointerException();
		this.string = string;
		this.displayLocation = start;
		this.toNotify = toNotify;
		this.color = color;
	}

	public final String getString() {
		return string;
	}

	public final DisplayLocation getLocation() {
		return displayLocation;
	}

	public final void setLocation(DisplayLocation location) {
		if (location == null)
			throw new NullPointerException();
		this.displayLocation = location;
		toNotify.invalidate();
	}

	@Override
	public void display(Canvas canvas, Paint paint, DisplayLocation offset) {
		float left = displayLocation.getLeft() + offset.getLeft();
		float top = displayLocation.getTop() + offset.getTop();
		final float textSize = paint.getTextSize();
		final int textColor = paint.getColor();

		paint.setColor(Color.BLACK);
		float outline = textSize * 0.025f;

		float outlineLeft = left - outline;
		float outlineTop = top - outline;
		canvas.drawText(string, outlineLeft, outlineTop, paint);

		outlineLeft += 2 * outline;
		canvas.drawText(string, outlineLeft, outlineTop, paint);

		outlineTop += 2 * outline;
		canvas.drawText(string, outlineLeft, outlineTop, paint);
		
		outlineLeft -= 2*outline;
		canvas.drawText(string, outlineLeft, outlineTop, paint);

		paint.setColor(color);

		canvas.drawText(string, left, top, paint);

		paint.setColor(textColor);
		
	}

	@Override
	public ValueAnimator createAnimator(List<DisplayLocation> locations) {
		if (locations.isEmpty())
			return ObjectAnimator.ofObject(this, "location",
					DisplayLocation.getEvaluator(), displayLocation);
		Object[] locs = locations.toArray();
		ObjectAnimator animator = ObjectAnimator.ofObject(this, "location",
				DisplayLocation.getEvaluator(), locs);
		animator.setDuration(DEFAULT_ANIMATION_DURATION);
		return animator;
	}

}
