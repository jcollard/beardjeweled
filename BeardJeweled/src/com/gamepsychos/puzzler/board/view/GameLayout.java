package com.gamepsychos.puzzler.board.view;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.gamepsychos.puzzler.game.Game;

public class GameLayout extends RelativeLayout {
	
	private static final float score_space = 0.20f;
	private static final float board_space = 0.80f;
	
	private final BoardView boardview;
	private final View scoreview;

	public GameLayout(Context context) {
		super(context);
		throw new UnsupportedOperationException();
	}
	
	public GameLayout(Context context, Game game){
		super(context);
		if(game == null)
			throw new NullPointerException();
		
		this.boardview = new BoardView(context, game);
		
		this.scoreview = new ScoreView(context, game);

		boardview.setId(1);
		scoreview.setId(2);
		
		this.addView(boardview);
		this.addView(scoreview);
		
		setLayouts();
		
	}
	
	private final void setLayouts(){
		int width = getWidth();
		int score_height = (int)(score_space*getHeight());
		int board_height = (int)(board_space*getHeight());
		RelativeLayout.LayoutParams scoreLayout = new RelativeLayout.LayoutParams(width, score_height);
		scoreview.setLayoutParams(scoreLayout);
		
		RelativeLayout.LayoutParams boardLayout = new RelativeLayout.LayoutParams(width, board_height);
		boardLayout.addRule(RelativeLayout.BELOW, scoreview.getId());
		boardview.setLayoutParams(boardLayout);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setLayouts();
	}


}
