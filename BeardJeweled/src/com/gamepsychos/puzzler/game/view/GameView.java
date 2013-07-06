package com.gamepsychos.puzzler.game.view;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.gamepsychos.puzzler.animation.AnimationHandler;
import com.gamepsychos.puzzler.board.view.BoardView;
import com.gamepsychos.puzzler.game.Game;

/**
 * A {@code GameLayout} is a view that displays a {@link Game}s state.
 * @author jcollard
 *
 */
public class GameView extends RelativeLayout {
	
	private static final float score_space = 0.20f;
	private static final float board_space = 0.80f;
	
	private final BoardView boardview;
	private final ScoreView scoreview;
	private final AnimationHandler handler;

	/**
	 * A call to this constructor will result in an {@link UnsupportedOperationException}
	 */
	public GameView(Context context) {
		super(context);
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Creates a {@code GameLayout} specifying the {@link Context} and {@link Game} to
	 * use.
	 * @param context the {@link Context} for this {@link View}
	 * @param game the {@link Game} to use as a model
	 */
	public GameView(Context context, Game game){
		super(context);
		if(game == null)
			throw new NullPointerException();
		
		this.boardview = new BoardView(context, game);
		this.scoreview = new ScoreView(context, game);
		this.handler = new AnimationHandler(this);
		this.boardview.addAnimationHandler(this.handler);

		boardview.setId(1);
		scoreview.setId(2);
		
		this.addView(boardview);
		this.addView(scoreview);
		
		setLayouts();
		
	}
	
	public final BoardView getBoardView(){
		return boardview;
	}
	
	public final ScoreView getScoreView(){
		return scoreview;
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
