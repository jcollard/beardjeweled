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

import com.gamepsychos.puzzler.board.view.BoardView;
import com.gamepsychos.puzzler.game.Game.GameMessage;
import com.gamepsychos.puzzler.game.view.GameView;
import com.gamepsychos.util.observer.Observer;

/**
 * An {@code AnimationHandler} is used to handle the animations associated with a {@link BoardView}.
 * @author jcollard
 *
 */
public class AnimationHandler implements AnimatorUpdateListener,
		AnimatorListener, Observer<GameMessage> {
	
	private final GameView view;
	private final Deque<GameMessage> toAnimate;
	private final Set<Animator> waiting;
	private final ChangeAnimationFactory factory;
	
	/**
	 * Create an {@link AnimationHandler} that controls the elements on {@code view}
	 * @param view the {@link GameView} to be animated.
	 */
	public AnimationHandler(GameView view){
		if(view == null)
			throw new NullPointerException();
		this.view = view;		
		this.toAnimate = new LinkedBlockingDeque<GameMessage>();
		this.waiting  = Collections.synchronizedSet(new HashSet<Animator>());
		this.factory = new ChangeAnimationFactory(this.view.getBoardView());
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
		if(toAnimate.isEmpty()) return;
		GameMessage message = toAnimate.pop();
		MoveResultAnimation animations = new MoveResultAnimation(message, factory, view);
		for(ValueAnimator animator : animations.getAnimators()){
			animator.addListener(this);
			animator.addUpdateListener(this);
			animator.start();
			waiting.add(animator);
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
	public void update(GameMessage message) {
		toAnimate.add(message);
		animate();
	}
	
	@Override
	public void onAnimationCancel(Animator animation) {
		waiting.remove(animation);
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		waiting.remove(animation);
		animate();
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
