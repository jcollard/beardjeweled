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

import com.gamepsychos.puzzler.board.Board.BoardMessage;
import com.gamepsychos.puzzler.board.Change;
import com.gamepsychos.puzzler.board.view.BoardView;
import com.gamepsychos.util.observer.Observer;

/**
 * An {@code AnimationHandler} is used to handle the animations associated with a {@link BoardView}.
 * @author jcollard
 *
 */
public class AnimationHandler implements AnimatorUpdateListener,
		AnimatorListener, Observer<BoardMessage> {
	
	private final BoardView view;
	private final Deque<Set<Change>> toAnimate;
	private final Set<Animator> waiting;
	private final ChangeAnimationFactory factory;
	
	/**
	 * Create an {@link AnimationHandler} that controls the elements on {@code view}
	 * @param view the {@link BoardView} to be animated.
	 */
	public AnimationHandler(BoardView view){
		if(view == null)
			throw new NullPointerException();
		this.view = view;		
		this.toAnimate = new LinkedBlockingDeque<Set<Change>>();
		this.waiting  = Collections.synchronizedSet(new HashSet<Animator>());
		this.factory = new ChangeAnimationFactory(this.view);
	}
	
	/**
	 * Stop all animations. When this method returns {@link AnimationHandler#isBusy()} will return {@code false}
	 */
	public final void cancelAllAnimations(){
		synchronized(waiting){
			for(Animator a : waiting)
				a.cancel();
		}
		waiting.clear();
		toAnimate.clear();
	}
	
	/**
	 * Returns {@code true} if this {@code AnimationHandler} has unfinished animations and {@code false} otherwise.
	 * @return {@code true} if this {@code AnimationHandler} has unfinished animations and {@code false} otherwise.
	 */
	public final boolean isBusy(){
		return !waiting.isEmpty() || !toAnimate.isEmpty();
	}
	
	private final void startNext(){
		Set<Change> changes = toAnimate.pop();
		for(Change c : changes){
			ValueAnimator animation = factory.getAnimation(c).getAnimator();
			animation.addListener(this);
			animation.addUpdateListener(this);
			animation.start();
			waiting.add(animation);
		}
	}
	
	private final void animate(){
		synchronized(waiting){
			if(waiting.isEmpty() && !toAnimate.isEmpty()){
				startNext();
			}
		}
	}
	
	@Override
	public void update(BoardMessage message) {
		if(message.getChanges().isEmpty()) return;
		toAnimate.add(message.getChanges());
		animate();
	}
	
	@Override
	public void onAnimationCancel(Animator animation) {
		waiting.remove(animation);
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
	}

	

}
