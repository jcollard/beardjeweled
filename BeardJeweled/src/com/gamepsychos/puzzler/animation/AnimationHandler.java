package com.gamepsychos.puzzler.animation;

import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

import com.gamepsychos.puzzler.board.Change;
import com.gamepsychos.puzzler.board.view.BoardView;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece;
import com.gamepsychos.puzzler.piece.view.PieceResources;
import com.gamepsychos.util.observer.Observer;

public class AnimationHandler implements AnimatorUpdateListener,
		AnimatorListener, Observer<Set<Change>> {
	
	private final BoardView view;
	private final Deque<Set<Change>> toAnimate;
	private final Set<Animator> waiting;
	
	public AnimationHandler(BoardView view){
		if(view == null)
			throw new NullPointerException();
		this.view = view;		
		this.toAnimate = new LinkedBlockingDeque<Set<Change>>();
		this.waiting  = Collections.synchronizedSet(new HashSet<Animator>());
	}
	
	public final void cancelAllAnimations(){
		synchronized(waiting){
			for(Animator a : waiting)
				a.cancel();
		}
		waiting.clear();
		toAnimate.clear();
	}
	
	public final boolean isBusy(){
		return !waiting.isEmpty() || !toAnimate.isEmpty();
	}
	
	private final void startNext(){
		Set<Change> changes = toAnimate.pop();
		boolean destroyed = false;
		for(Change c : changes){
			
			DisplayablePiece piece = view.getPiece(c.getPiece());
			ValueAnimator animation = null;
			if(c.destroyed()){
				float left = -PieceResources.getSize();
				float top = view.getHeight() + PieceResources.getSize();
				animation = piece.createAnimator(new DisplayLocation(left, top));
				view.destroy(c.getPiece(), animation);
				destroyed = true;
			}else if(c.created()){
				view.addPiece(c.getPiece(), c.getStart());
				piece = view.getPiece(c.getPiece());
				DisplayLocation start = new DisplayLocation(c.getStart());
				DisplayLocation end = new DisplayLocation(c.getEnd());
				animation = piece.createAnimator(start, end);
			}else{
				DisplayLocation end = new DisplayLocation(c.getEnd());
				animation = piece.createAnimator(end);						
			}
			animation.addListener(this);
			animation.addUpdateListener(this);
			animation.start();
			waiting.add(animation);
		}
		if(destroyed)
			AudioResources.playWoosh();
	}
	
	private final void animate(){
		synchronized(waiting){
			if(waiting.isEmpty() && !toAnimate.isEmpty()){
				startNext();
			}
		}
	}
	
	@Override
	public void update(Set<Change> message) {
		toAnimate.add(message);
		animate();
	}
	
	@Override
	public void onAnimationCancel(Animator animation) {
		
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		waiting.remove(animation);
		if(waiting.isEmpty() && !toAnimate.isEmpty())
			startNext();
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
	}

	@Override
	public void onAnimationStart(Animator animation) {
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		//TODO Could probably be clipped per animation
//		view.invalidate();
	}

	

}
