package com.gamepsychos.puzzler.board.view;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import com.gamepsychos.puzzler.R;
import com.gamepsychos.puzzler.animation.AnimationHandler;
import com.gamepsychos.puzzler.animation.AnimationHandler.Animation;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.move.Drop;
import com.gamepsychos.puzzler.move.Move;
import com.gamepsychos.puzzler.move.MoveFactory;
import com.gamepsychos.puzzler.move.MoveResult;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece;
import com.gamepsychos.puzzler.piece.view.DisplayablePieceFactory;

public class BoardView extends View {
	
	private final Bitmap background;
	private final int BACKGROUND_SIZE = 100;
	private final DisplayableBoard board;
	private final Paint paint;
	private final AnimationHandler animationHandler;
	private final Set<DisplayablePiece> destroyed;
	
	private final MediaPlayer woosh;

	public BoardView(Context context) {
		super(context);
		Random gen = new Random();
		this.board = new DisplayableBoard(new DisplayablePieceFactory(gen));
		this.paint = new Paint();
		this.animationHandler = new AnimationHandler(this);
		this.destroyed = new HashSet<DisplayablePiece>();
		Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.concrete_background);
		this.background = Bitmap.createScaledBitmap(background, BACKGROUND_SIZE, BACKGROUND_SIZE, false);
		woosh = MediaPlayer.create(context, R.raw.woosh);
		woosh.setVolume(1f, 1f);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int rows = board.getRows();
		int cols = board.getColumns();
		int width = w/cols;
		int height = h/rows;
		int size = Math.min(width, height);
		DisplayablePiece.setSize(size);
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < cols; c++){
				Location loc = Location.getLocation(r, c);
				DisplayablePiece piece = board.getPiece(loc);
				DisplayLocation location = new DisplayLocation(c*size, r*size);
				piece.setLocation(location);
			}
		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		
		for(int r = 0; r <= height/BACKGROUND_SIZE; r++){
			for(int c = 0; c <= width/BACKGROUND_SIZE; c++){
				canvas.drawBitmap(background, c*BACKGROUND_SIZE, r*BACKGROUND_SIZE, paint);
			}
		}
		
		int rows = board.getRows();
		int cols = board.getColumns();
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < cols; c++){
				Location loc = Location.getLocation(r, c);
				DisplayablePiece piece = board.getPiece(loc);
				piece.display(canvas, paint, getResources());
			}
		}
		
		for(DisplayablePiece piece : destroyed){ 
			piece.display(canvas, paint, getResources());
		}
		
		if(selected != null){
			selected.display(canvas, paint, getResources());
		}
		
	}

	private DisplayablePiece selected = null;
	private Location selectedLocation = null;
	private float offsetX;
	private float offsetY;

	private final boolean onTouch(MotionEvent event){
		if(animationHandler.isBusy())
			return false;
		if(selected == null){
			selectedLocation = getLocation(event.getX(), event.getY());
			if(inBounds(selectedLocation)){
				selected = board.getPiece(selectedLocation);
				setOffset(event.getX(), event.getY(), selectedLocation);
				return true;
			}
			selectedLocation = null;
		}
		
		return false;
	}
	
	private final void setOffset(float x, float y, Location loc){
		int size = DisplayablePiece.getSize();
		offsetX = x - (loc.getCol()*size);
		offsetY = y - (loc.getRow()*size);	
	}
	
	private final boolean inBounds(Location loc){
		return loc.getCol() >= 0 && loc.getCol() < board.getColumns() && loc.getRow() >= 0 && loc.getRow() < board.getRows();
	}
	
	private final Location getLocation(float x, float y){
		int size = DisplayablePiece.getSize();
		int c = ((int)x)/size;
		int r = ((int)y)/size;
		return Location.getLocation(r, c);
	}
	
	private final void setPiece(Location loc){
		if(!inBounds(loc))
			return;
		DisplayablePiece piece = board.getPiece(loc);
		int size = DisplayablePiece.getSize();
		float x = loc.getCol()*size;
		float y = loc.getRow()*size;
		DisplayLocation location = new DisplayLocation(x, y);
		ValueAnimator animator = piece.animateTo(location, animationHandler);
		animationHandler.animate(animator);
	}
	
	private void animateResults(final MoveResult results){
		Set<Drop> drops = results.drops();
		final Set<ValueAnimator> animations = new HashSet<ValueAnimator>();
		for(Piece p : results.destroyed()){
			//Guaranteed by DisplayableBoard
			DisplayablePiece piece = (DisplayablePiece)p;
			destroyed.add(piece);
			int size = DisplayablePiece.getSize();
			int bottom = getHeight() + size;
			DisplayLocation location = new DisplayLocation(-size, bottom);
			ValueAnimator animator = piece.animateTo(location, animationHandler);
			animations.add(animator);
		}
		
		for(Drop drop : drops){
			DisplayablePiece piece = board.getPiece(drop.getEnd());
			float startX = drop.getStart().getCol()*DisplayablePiece.getSize();
			float startY = drop.getStart().getRow()*DisplayablePiece.getSize();
			float endX = drop.getEnd().getCol()*DisplayablePiece.getSize();
			float endY = drop.getEnd().getRow()*DisplayablePiece.getSize();
			DisplayLocation start = new DisplayLocation(startX, startY);
			DisplayLocation end = new DisplayLocation(endX, endY);
			ValueAnimator animator = piece.animateTo(start, end, animationHandler);
			animations.add(animator);
		}
		Animation animation = new Animation() {

			@Override
			public Set<ValueAnimator> getAnimators() {
				if(!destroyed.isEmpty() && !woosh.isPlaying()){
					woosh.seekTo(0);
					woosh.start();
				}
				return animations;
			}

			@Override
			public boolean andWait() {
				return true;
			}

			@Override
			public void onCompletion() {
				if(results.followUpMove()){
					destroyed.clear();
					Move m = results.getFollowUpMove();
					MoveResult result = m.move();
					animateResults(result);
				}
			}
		
		};
		animationHandler.animate(animation);	
	}
	
	private final void animateSwap(final Location a, final Location b){
		int size = DisplayablePiece.getSize();
		DisplayLocation first = new DisplayLocation(a.getCol()*size, a.getRow()*size);
		DisplayLocation second = new DisplayLocation(b.getCol()*size, b.getRow()*size);
		DisplayablePiece p0 = board.getPiece(a);
		DisplayablePiece p1 = board.getPiece(b);
		ValueAnimator animation0 = p0.animateTo(second, animationHandler);
		ValueAnimator animation1 = p1.animateTo(first, animationHandler);
		final Set<ValueAnimator> animations = new HashSet<ValueAnimator>(2);
		animations.add(animation0);
		animations.add(animation1);
		animationHandler.animate(new Animation() {
			
			@Override
			public void onCompletion() {
				MoveFactory factory = board.getMoveFactory();
				Move swap = factory.getSwapMove(a, b);
				MoveResult results = swap.move();
				animateResults(results);
			}
			
			@Override
			public Set<ValueAnimator> getAnimators() {
				return animations;
			}
			
			@Override
			public boolean andWait() {
				return true;
			}
		});
	}
	
	private final boolean onRelease(MotionEvent event){
		boolean rv = false;
		if(selected != null){
			Location loc = getLocation(event.getX(), event.getY());
			if(inBounds(loc) && Location.adjacent(loc, selectedLocation)){
				animateSwap(loc, selectedLocation);
				rv = true;				
			}else{
				setPiece(selectedLocation);
			}
		}
		selected = null;
		selectedLocation = null;
		return rv;
	}
	
	private final boolean onMove(MotionEvent event){
		if(animationHandler.isBusy()){
			if(selectedLocation != null && selected != null){
				setPiece(selectedLocation);
				selected = null;
				selectedLocation = null;
			}
			return false;
		}
		if(selected == null)
			return false;
		DisplayLocation location = new DisplayLocation(event.getX() - offsetX, event.getY() - offsetY);
		selected.setLocation(location);
		invalidate();
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			return onTouch(event);
		case MotionEvent.ACTION_UP:
			return onRelease(event);
		case MotionEvent.ACTION_MOVE:
			return onMove(event);
		}
		return false;
	}
	

}