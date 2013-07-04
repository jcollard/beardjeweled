package com.gamepsychos.puzzler.board.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.gamepsychos.puzzler.R;
import com.gamepsychos.puzzler.animation.AnimationHandler;
import com.gamepsychos.puzzler.board.BasicBoard;
import com.gamepsychos.puzzler.board.Board;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.board.controller.BoardController;
import com.gamepsychos.puzzler.board.controller.MoveBoardController;
import com.gamepsychos.puzzler.piece.BasicPieceFactory;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece;
import com.gamepsychos.puzzler.piece.view.PieceResources;

public class BoardView extends View {

	private final AnimationHandler animationHandler;
	private final Paint paint;
	private final Board board;
	private final Map<Piece, DisplayablePiece> displayedPieces;
	private BoardController controller;
	private static final int BACKGROUND_SIZE = 100;
	private DisplayLocation offset = new DisplayLocation(0,0);
	private final Bitmap background;
	
	public BoardView(Context context){
		this(context, new BasicBoard(new BasicPieceFactory(new Random())));
	}
	
	public BoardView(Context context, Board board) {
		super(context);
		if(board == null)
			throw new NullPointerException();
		this.board = board;
		Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.concrete_background);
		this.background = Bitmap.createScaledBitmap(background, BACKGROUND_SIZE, BACKGROUND_SIZE, false);
		this.paint = new Paint();
		this.displayedPieces = new HashMap<Piece, DisplayablePiece>();
		this.animationHandler = new AnimationHandler(this);
		this.controller = new MoveBoardController(board, this);
		board.register(animationHandler);
		updateAllPieces();
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
		
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		animationHandler.cancelAllAnimations();
		calculateNewSize(w, h);
		updateAllPieces();
		invalidate();
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

	public void addPiece(Piece piece, Location loc) {
		DisplayLocation displayLocation = new DisplayLocation(loc);
		DisplayablePiece dp = new DisplayablePiece(piece, displayLocation, this);
		displayedPieces.put(piece, dp);
	}

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
	
}