package com.gamepsychos.puzzler.board.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.gamepsychos.puzzler.board.Board;

public class ScoreView extends View {

	private final Board board;
	
	public ScoreView(Context context){
		super(context);
		throw new UnsupportedOperationException();
	}
	
	public ScoreView(Context context, Board board) {
		super(context);
		this.board = board;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
