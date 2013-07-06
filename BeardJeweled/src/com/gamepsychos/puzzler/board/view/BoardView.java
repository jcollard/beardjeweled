package com.gamepsychos.puzzler.board.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import com.gamepsychos.puzzler.R;
import com.gamepsychos.puzzler.animation.AnimationHandler;
import com.gamepsychos.puzzler.board.Board;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.board.controller.BoardController;
import com.gamepsychos.puzzler.board.controller.MoveBoardController;
import com.gamepsychos.puzzler.game.Game;
import com.gamepsychos.puzzler.game.view.DisplayableString;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece;
import com.gamepsychos.puzzler.piece.view.PieceResources;

/**
 * A {@code BoardView} is a view that displays the state of a {@link Game}.
 * @author jcollard
 *
 */
public class BoardView extends View {

	private static final DisplayLocation NO_OFFSET = new DisplayLocation(0, 0);
	private AnimationHandler animationHandler;
	private final Paint paint;
	private final Board board;
	private final Game game;
	private final Map<Piece, DisplayablePiece> displayedPieces;
	private final Set<DisplayableString> displayedStrings;
	private BoardController controller;
	private static final int BACKGROUND_SIZE = 100;
	private DisplayLocation offset = new DisplayLocation(0,0);
	private final Bitmap background;
	private final Paint stringPaint;
	private float string_size;

	
	/**
	 * This constructor will throw an {@link UnsupportedOperationException} and should not be used.
	 */
	public BoardView(Context context){
		super(context);
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Creates a {@code BoardView} specifying the {@link Context} and {@link Game}
	 * to use.
	 * @param context the {@link Context} for this {@code BoardView}
	 * @param game the {@link Game} that the created {@code BoardView} will use as a model
	 */
	public BoardView(Context context, Game game) {
		super(context);
		if(game == null)
			throw new NullPointerException();
		this.game = game;
		this.board = game.getBoard();
		Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.concrete_background);
		this.background = Bitmap.createScaledBitmap(background, BACKGROUND_SIZE, BACKGROUND_SIZE, false);
		this.paint = new Paint();
		this.displayedPieces = new HashMap<Piece, DisplayablePiece>();
		this.displayedStrings = new HashSet<DisplayableString>();
		this.controller = new MoveBoardController(this);
		this.stringPaint = new Paint();
		this.string_size = 200.0f;
		intializeFont(); 
		updateAllPieces();
	}
	
	public final float getStringSize(){
		return string_size;
	}
	
	private final void intializeFont(){
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/hurryup.ttf");
		stringPaint.setTypeface(font);
		stringPaint.setTextSize(string_size);
		stringPaint.setColor(Color.YELLOW);
	}
	
	public final void addDisplayableString(DisplayableString toAdd){
		if(toAdd == null)
			throw new NullPointerException();
		displayedStrings.add(toAdd);
	}
	
	public final void removeDisplayableString(DisplayableString toRemove){
		if(toRemove == null)
			throw new NullPointerException();
		displayedStrings.remove(toRemove);
	}
	
	public final void addAnimationHandler(AnimationHandler animationHandler){
		if(animationHandler == null)
			throw new NullPointerException();
		this.animationHandler = animationHandler;
		game.register(animationHandler);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(animationHandler.isBusy())
			return false;

		float left = event.getX() - offset.getLeft();
		float top = event.getY() - offset.getTop();
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			return controller.onTouch(left, top);
		case MotionEvent.ACTION_UP:
			return controller.onRelease(left, top);
		case MotionEvent.ACTION_MOVE:
			return controller.onMove(left, top);
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();

		//Background
		for(int r = 0; r <= height/BACKGROUND_SIZE; r++)
			for(int c = 0; c <= width/BACKGROUND_SIZE; c++)
				canvas.drawBitmap(background, c*BACKGROUND_SIZE, r*BACKGROUND_SIZE, paint);
		
		//Pieces
		for(DisplayablePiece p : displayedPieces.values())
			p.display(canvas, paint, offset);
		
		//Strings
		for(DisplayableString string : displayedStrings)
			string.display(canvas, stringPaint, NO_OFFSET);
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		animationHandler.cancelAllAnimations();
		calculateNewSize(w, h);
		calculateNewFontSize(w, h);
		updateAllPieces();
		invalidate();
	}
	
	private final void calculateNewFontSize(int width, int height){
		
		string_size = height*0.10f;
		stringPaint.setTextSize(string_size);
		
	}
	
	private final void updateAllPieces(){
		displayedPieces.clear();
		int rows = board.getRows();
		int cols = board.getColumns();
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < cols; c++){
				Location loc = Location.getLocation(r, c);
				Piece p = board.getPiece(loc);
				DisplayLocation displayLocation = new DisplayLocation(loc);
				DisplayablePiece dp = new DisplayablePiece(p, displayLocation, this);
				displayedPieces.put(p, dp);
			}
		}
	}
	
	private final void calculateNewSize(int width, int height){
		int w = width/board.getColumns();
		int r = height/board.getRows();
		int size = Math.min(w, r);
		int space_width = size*board.getColumns();
		int space_height = size*board.getRows();
		offset = new DisplayLocation((width-space_width)/2f, (height-space_height)/2f);
		if(size < 1)
			return;
		PieceResources.setSize(size);
	}
	
	public final DisplayablePiece getPiece(Piece p){
		return displayedPieces.get(p);
	}

	/**
	 * Adds a {@link Piece} to be displayed.
	 * @param piece the {@link Piece} to add
	 * @param loc the {@link Location} of the {@code Piece}
	 */
	public final void addPiece(Piece piece, Location loc) {
		DisplayLocation displayLocation = new DisplayLocation(loc);
		DisplayablePiece dp = new DisplayablePiece(piece, displayLocation, this);
		displayedPieces.put(piece, dp);
	}
	
	public final void clearAllStrings(){
		displayedStrings.clear();
	}

	/**
	 * Destroys the specified {@link Piece} when the specified {@link ValueAnimator} has completed.
	 * @param piece the {@link Piece} to be destroyed
	 * @param animation the animation to wait for
	 */
	public void destroy(final Piece piece, ValueAnimator animation) {
		animation.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {}
			
			@Override
			public void onAnimationRepeat(Animator animation) {}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				displayedPieces.remove(piece);
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				displayedPieces.remove(piece);
			}
		});
	}

	public final Game getGame() {
		return game;
	}
	
}