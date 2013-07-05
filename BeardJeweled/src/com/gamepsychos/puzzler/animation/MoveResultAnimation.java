package com.gamepsychos.puzzler.animation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;

import com.gamepsychos.puzzler.board.Change;
import com.gamepsychos.puzzler.board.view.DisplayableString;
import com.gamepsychos.puzzler.board.view.GameLayout;
import com.gamepsychos.puzzler.game.Game.GameMessage;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;

public class MoveResultAnimation {
	
	private final Set<ValueAnimator> animators;
	private final GameMessage message;
	private final GameLayout view;
	private final ChangeAnimationFactory factory;

	public MoveResultAnimation(GameMessage message, ChangeAnimationFactory factory, GameLayout view){
		if(message == null || factory == null || view == null)
			throw new NullPointerException();
		this.message = message;
		this.view = view;
		this.factory = factory;
		animators = new HashSet<ValueAnimator>();
		initSet();
	}
	
	private final void initSet(){
		animateChanges();
		animateCombo();
		animatePoints();
		animateBonusMoves();
		addScoreChangeAnimator();
	}
	
	private final void animateCombo(){
		
	}
	
	private final ValueAnimator getStringAnimation(String string, int color){
		float left = view.getBoardView().getWidth()/2f;
		float top = view.getBoardView().getHeight()/2f;
		float distanceUp = view.getBoardView().getHeight()/4f;
		DisplayLocation start = calculateCenter(left, top, string.length());
		DisplayLocation end = new DisplayLocation(start.getLeft(), start.getTop()-distanceUp);
		DisplayableString ds = new DisplayableString(string, start, view.getBoardView(), color);
		ValueAnimator animation = ds.createAnimator(Collections.singletonList(end));
		animation.addListener(new StringListener(ds));
		return animation;
	}
	
	private final void animateBonusMoves(){
		int moves = message.getMovesChange();
		if(moves > 0){
			ValueAnimator animation = getStringAnimation("+" + moves, Color.GREEN);
			animation.setStartDelay(100);
			animation.setDuration(500);
			animators.add(animation);
		}
	}
	
	private final void animatePoints(){
		int points = message.getPointsAwarded();
		if(points > 0){
			ValueAnimator animation = getStringAnimation(""+points, Color.YELLOW);
			animation.setStartDelay(50);
			animators.add(animation);
		}
	}
	
	private final DisplayLocation calculateCenter(float left, float top, int length){
		float middle = length/2f;
		float stringSize = view.getBoardView().getStringSize();
		top += stringSize/2;
		left -= middle*(stringSize/2);
		return new DisplayLocation(left, top);
	}
	
	private final void animateChanges(){
		for(Change c : message.getChanges()){
			ValueAnimator animation = factory.getAnimation(c).getAnimator();
			animators.add(animation);
		}
	}
	
	//Adds a dummy animator to inform when the score should be updated.
	private final void addScoreChangeAnimator(){
		ValueAnimator animator = new ValueAnimator();
		animator.addListener(new UpdateScoreListener());
		animator.setFloatValues(1.0f);
		animator.setDuration(1);
		animators.add(animator);
	}
	
	public Set<ValueAnimator> getAnimators() {
		return Collections.unmodifiableSet(animators);
	}

	private final class StringListener extends AnimatorListenerAdapter {

		private final DisplayableString string;
		
		private StringListener(DisplayableString string){
			assert string != null;
			this.string = string;
		}
		
		@Override
		public void onAnimationStart(Animator animation) {
			view.getBoardView().addDisplayableString(string);
		}
		
		@Override
		public void onAnimationEnd(Animator animation) {
			view.getBoardView().removeDisplayableString(string);
		}
		
		@Override
		public void onAnimationCancel(Animator animation){
			view.getBoardView().removeDisplayableString(string);
		}
		
	}
	
	private final class UpdateScoreListener extends AnimatorListenerAdapter {
	
		
		@Override
		public void onAnimationStart(Animator animation) {
			view.getScoreView().updateScore(message);
		}
		
	}
	

	

}
