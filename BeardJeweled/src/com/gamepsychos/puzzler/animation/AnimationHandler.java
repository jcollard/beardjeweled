package com.gamepsychos.puzzler.animation;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;

public class AnimationHandler implements AnimatorUpdateListener,
		AnimatorListener {

	private final View view;
	private final List<Animation> animations;
	private final Map<Animator, Animation> lookup;
	private boolean wait;

	public AnimationHandler(View view) {
		this.view = view;
		this.animations = Collections
				.synchronizedList(new LinkedList<Animation>());
		this.lookup = new ConcurrentHashMap<Animator, AnimationHandler.Animation>();
		this.wait = false;
	}

	public boolean isBusy() {
		return !animations.isEmpty() || wait;
	}
	
	public void animateAndWait(final ValueAnimator animation){
		animate(new Animation() {
			
			@Override
			public void onCompletion() {}
			
			@Override
			public Set<ValueAnimator> getAnimators() {
				Set<ValueAnimator> animations = new HashSet<ValueAnimator>();
				animations.add(animation);
				return animations;
			}
			
			@Override
			public boolean andWait() {
				return true;
			}
		});
	}
	
	public void animate(final ValueAnimator animation){
		animate(new Animation() {
			
			@Override
			public void onCompletion() {}
			
			@Override
			public Set<ValueAnimator> getAnimators() {
				Set<ValueAnimator> animations = new HashSet<ValueAnimator>();
				animations.add(animation);
				return animations;
			}
			
			@Override
			public boolean andWait() {
				return false;
			}
		});
	}

	public void animate(Animation animation) {
		if (isBusy())
			this.animations.add(animation);
		else{
			this.animations.add(animation);
			runAnimations();
		}
			
	}
	
	private void runAnimations(){
		if(animations.isEmpty())
			return;
		Animation a = animations.remove(0);
		startAnimation(a);
	}
	
	private void startAnimation(Animation animation){		
		ValueAnimator[] animators = animation.getAnimators().toArray(new ValueAnimator[0]);
		int last = animators.length-1;
		if(last < 0)
			return;
		if(animation.andWait())
			wait = true;
		lookup.put(animators[last], animation);
		for(ValueAnimator animator : animators){
			animator.addListener(this);
			animator.addUpdateListener(this);
			animator.start();
		}
	}
	
	private final void finishAnimation(Animator animation){
		Animation a = lookup.remove(animation);
		if(a != null){
			wait = false;
			a.onCompletion();
		}
		
		if(!animations.isEmpty()){
			runAnimations();
		}
		
	}

	@Override
	public void onAnimationCancel(Animator animation) {
		finishAnimation(animation);
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		finishAnimation(animation);
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
	}

	@Override
	public void onAnimationStart(Animator animation) {
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		view.invalidate();
	}

	public static interface Animation {

		public Set<ValueAnimator> getAnimators();
		
		public boolean andWait();

		public void onCompletion();

	}

}
