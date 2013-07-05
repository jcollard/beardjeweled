package com.gamepsychos.puzzler.board.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.gamepsychos.puzzler.game.Game;
import com.gamepsychos.puzzler.game.Game.GameMessage;
import com.gamepsychos.util.observer.Observer;

public class ScoreView extends View implements Observer<GameMessage> {

	private final Game game;
	private final Paint paint;
	
	public ScoreView(Context context){
		super(context);
		throw new UnsupportedOperationException();
	}
	
	public ScoreView(Context context, Game game) {
		super(context);
		if(game == null)
			throw new NullPointerException();
		this.game = game;
		this.game.register(this);
		this.paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int score = game.getScore();
		int jewels = game.getPiecesCollected().size();
		int moves = game.getMovesRemaining();
		int streak = game.getLatestStreak();
		float fontSize = 64;
		paint.setTextSize(fontSize);
		
		canvas.drawText("Moves Remaining: " + moves, 0, fontSize, paint);
		
		canvas.drawText("Jewels Collected: "  + jewels, 0, fontSize*2, paint);
		
		canvas.drawText("Score: " + score, 0, fontSize*3, paint);
		
		canvas.drawText("Latest Streak: " + streak, 0, fontSize*4, paint);
		
	}

	@Override
	public void update(GameMessage message) {
		invalidate();
	}

}
