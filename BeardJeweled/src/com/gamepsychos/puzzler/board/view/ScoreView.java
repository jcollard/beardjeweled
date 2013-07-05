package com.gamepsychos.puzzler.board.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import com.gamepsychos.puzzler.game.Game;
import com.gamepsychos.puzzler.game.Game.GameMessage;

/**
 * A {@link ScoreView} displays the current score of a {@link Game}
 * @author jcollard
 *
 */
public class ScoreView extends View {

	private final Game game;
	private final Paint paint;
	
	/**
	 * A call to this constructor will result in an {@link UnsupportedOperationException}
	 */
	public ScoreView(Context context){
		super(context);
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Creates a {@link ScoreView} specifying the {@link Context} for the {@link View} and
	 * the {@link Game} to use as a model.
	 * @param context the {@link Context} for this {@link View}
	 * @param game the {@link Game} to model
	 */
	public ScoreView(Context context, Game game) {
		super(context);
		if(game == null)
			throw new NullPointerException();
		this.game = game;
		this.paint = new Paint();
		Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/hurryup.ttf");
		
		paint.setTypeface(font);
		score = game.getScore();
		jewels = game.getPiecesCollected().size();
		moves = game.getMovesRemaining();
	}
	
	private int score;
	private int jewels;
	private int moves;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float fontSize = 64;
		paint.setTextSize(fontSize);
		
		
		canvas.drawText("Moves Remaining: " + moves, 0, fontSize, paint);
		
		canvas.drawText("Jewels Collected: "  + jewels, 0, fontSize*2, paint);
		
		canvas.drawText("Score: " + score, 0, fontSize*3, paint);
		
	}

	public final void updateScore(GameMessage message){
		score += message.getPointsAwarded();
		jewels += message.getPiecesCollected();
		moves += message.getMovesChange();
		invalidate();
	}	
	
	public final void syncWithGame(){
		score = game.getScore(); 
		jewels = game.getPiecesCollected().size();
		moves = game.getMovesRemaining();
		invalidate();
	}

}
