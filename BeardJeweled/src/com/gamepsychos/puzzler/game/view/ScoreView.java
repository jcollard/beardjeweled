package com.gamepsychos.puzzler.game.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import com.gamepsychos.puzzler.game.Game;
import com.gamepsychos.puzzler.game.Game.GameMessage;
import com.gamepsychos.puzzler.graphics.GraphicResource;
import com.gamepsychos.puzzler.graphics.GraphicResource.Border;
import com.gamepsychos.puzzler.graphics.GraphicResource.Icon;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;

/**
 * A {@link ScoreView} displays the current score of a {@link Game}
 * @author jcollard
 *
 */
public class ScoreView extends View {

	private static final DisplayLocation NO_OFFSET = new DisplayLocation(0, 0);
	private static final float ICON_SCALE = .8f;
	
	private final Game game;
	private final Paint paint;
	private final IconView movesView;
	private final IconView jewelsCollectedView;
	private final IconView scoreView;
	private DisplayLocation jewels_offset;
	private DisplayLocation score_offset;
	private DisplayLocation moves_offset;
	private static final float border_width = 20f;
	private float border_offset;
	private Bitmap border;
	
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
		
		
		score = game.getScore();
		jewels = game.getPiecesCollected().size();
		moves = game.getMovesRemaining();
		
		Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/hurryup.ttf");
		this.paint = new Paint();
		paint.setTypeface(font);
		
		this.movesView = new IconView(this, Icon.MOVES, ICON_SCALE);
		this.movesView.setDisplayString("" + moves);
		this.moves_offset = NO_OFFSET;
		
		this.jewelsCollectedView = new IconView(this, Icon.JEWELS, ICON_SCALE);
		this.jewelsCollectedView.setDisplayString("" + jewels);
		this.jewels_offset = NO_OFFSET;
	
		this.scoreView = new IconView(this, ICON_SCALE);
		this.scoreView.setDisplayString("Score: " + score);
		this.score_offset = NO_OFFSET;		
	}
	
	private int score;
	private int jewels;
	private int moves;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(border != null){
			canvas.drawBitmap(border, 0, border_offset, paint);
			canvas.drawBitmap(border,  0,  0, paint);
		}
		
		movesView.display(canvas, paint, moves_offset);
		
		jewelsCollectedView.display(canvas, paint, jewels_offset);
		
		scoreView.display(canvas, paint, score_offset);
		
	}

	public final void updateScore(GameMessage message){
		score += message.getPointsAwarded();
		jewels += message.getPiecesCollected();
		moves += message.getMovesChange();
		setChildren();
		invalidate();
	}	
	
	public final void syncWithGame(){
		score = game.getScore(); 
		jewels = game.getPiecesCollected().size();
		moves = game.getMovesRemaining();
		setChildren();
		invalidate();
	}
	
	private final void setChildren(){
		movesView.setDisplayString("" + moves);
		jewelsCollectedView.setDisplayString("" + jewels);
		scoreView.setDisplayString("Score: " + score);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		float icon_size_ratio = h*(1/(h/(h*ICON_SCALE)));
		float icon_align_bottom = h-icon_size_ratio;
		GraphicResource resource = GraphicResource.getResource(getContext());
		Bitmap source = resource.getBoard(Border.HORIZONTAL_BAR);
		border = Bitmap.createScaledBitmap(source, w, (int)border_width, false);
		border_offset = h-border.getHeight();
		paint.setTextSize(h*0.5f);
		movesView.changeSize(w, h);
		moves_offset = new DisplayLocation(border_width, icon_align_bottom - border_width);
		jewelsCollectedView.changeSize(w, h);
		float fourth = (w/4)*1.25f;
		jewels_offset = new DisplayLocation(border_width + fourth, icon_align_bottom - border_width);
		scoreView.changeSize(w, h);
		score_offset = new DisplayLocation(border_width + fourth*2, icon_align_bottom - border_width);
	}

}
