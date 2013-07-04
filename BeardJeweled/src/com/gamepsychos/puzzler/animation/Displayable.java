package com.gamepsychos.puzzler.animation;

import com.gamepsychos.puzzler.piece.view.DisplayLocation;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Displayable {

	public void display(Canvas canvas, Paint paint, DisplayLocation offset);
	
}
