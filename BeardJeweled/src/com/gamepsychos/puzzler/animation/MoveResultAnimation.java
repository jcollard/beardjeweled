package com.gamepsychos.puzzler.animation;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;

import com.gamepsychos.puzzler.audio.AudioResource;
import com.gamepsychos.puzzler.audio.AudioResource.Music;
import com.gamepsychos.puzzler.audio.AudioResource.SFX;
import com.gamepsychos.puzzler.board.Change;
import com.gamepsychos.puzzler.game.Game.GameMessage;
import com.gamepsychos.puzzler.game.view.DisplayableString;
import com.gamepsychos.puzzler.game.view.GameView;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;

public class MoveResultAnimation {
	
	private final Set<ValueAnimator> animators;
	private final GameMessage message;
	private final GameView view;
	private final ChangeAnimationFactory factory;

	public MoveResultAnimation(GameMessage message, ChangeAnimationFactory factory, GameView view){
		if(message == null || factory == null || view == null)
			throw new NullPointerException();
		this.message = message;
		this.view = view;
		this.factory = factory;
		animators = new HashSet<ValueAnimator>();
		initSet();
	}
	
	private final void initSet(){
		animateNewGame();
		animateChanges();
		animateCombo();
		animatePoints();
		animateBonusMoves();
		addScoreChangeAnimator();
		animateGameOver();
	}
	
	private final void animateNewGame(){
		if(message.getResults().isStartNewGame()){
			ValueAnimator animator = getDummyAnimator();
			animator.addListener(new NewGame());
			animators.add(animator);
		}
	}
	
	private final void animateGameOver(){
		if(message.isGameOver() && !message.getResults().followUpMove()){
			DisplayableString gameOver = getDisplayString("GAME OVER", Color.MAGENTA);
			float shift = view.getBoardView().getWidth()/10f;
			float left = gameOver.getLocation().getLeft() - shift;
			float right = gameOver.getLocation().getLeft() + shift;
			float top = gameOver.getLocation().getTop();
			float stopTop = view.getBoardView().getHeight()/8f;
			DisplayLocation center = gameOver.getLocation();
			DisplayLocation l = new DisplayLocation(left, top);
			DisplayLocation r = new DisplayLocation(right, top);
			List<DisplayLocation> locations = new LinkedList<DisplayLocation>();
			for(int i = 0; i < 3; i++){
				locations.add(l);
				locations.add(r);
			}
			locations.add(center);
			
			ValueAnimator animation = gameOver.createAnimator(locations);
			animation.setDuration(500);
			animation.setStartDelay(100);
			ValueAnimator animation2 = gameOver.createAnimator(Collections.singletonList(new DisplayLocation(center.getLeft(), stopTop)));
			animation2.setStartDelay(1100);
			animation2.setDuration(3200);
			animation.addListener(new GameOverListener(gameOver));
			animators.add(animation2);
			animators.add(animation);
		}
	}
	
	private final void animateCombo(){
		int streak = message.getStreak()-1;
		if(!message.getResults().followUpMove() && streak > 1){
			DisplayableString string = getDisplayString(streak + "-STREAK", Color.RED);
			ValueAnimator animation = getStringAnimation(string);
			animation.setStartDelay(100);
			animation.setDuration(800);
			animators.add(animation);
		}
	}
	
	private final DisplayableString getDisplayString(String string, int color){
		float left = view.getBoardView().getWidth()/2f;
		float top = view.getBoardView().getHeight()/2f;
		DisplayLocation start = calculateCenter(left, top, string.length());
		DisplayableString ds = new DisplayableString(string, start, view.getBoardView(), color);
		return ds;
	}
	
	private final ValueAnimator getStringAnimation(DisplayableString ds){
		DisplayLocation start = ds.getLocation();
		float distanceUp = view.getBoardView().getHeight()/4f;
		DisplayLocation end = new DisplayLocation(start.getLeft(), start.getTop()-distanceUp);
		ValueAnimator animation = ds.createAnimator(Collections.singletonList(end));
		animation.addListener(new StringListener(ds));
		return animation;
	}
	
	private final void animateBonusMoves(){
		int moves = message.getMovesChange();
		if(moves > 0){
			DisplayableString string = getDisplayString("+" + moves, Color.GREEN);
			ValueAnimator animation = getStringAnimation(string);
			animation.setStartDelay(100);
			animation.setDuration(500);
			animators.add(animation);
		}
	}
	
	private final void animatePoints(){
		int points = message.getPointsAwarded();
		if(points > 0){
			DisplayableString string = getDisplayString(""+points, Color.YELLOW);
			ValueAnimator animation = getStringAnimation(string);
			animators.add(animation);
			animation = getDummyAnimator();
			animation.addListener(new SoundListener(SFX.WOOSH));
			animators.add(animation);
		}
	}
	
	private final ValueAnimator getDummyAnimator(){
		ValueAnimator animator = new ValueAnimator();
		animator.setFloatValues(1.0f);
		animator.setDuration(1);
		return animator;
	}
	
	private final DisplayLocation calculateCenter(float left, float top, int length){
		float middle = length/2f;
		float stringSize = view.getBoardView().getStringSize();
		top += stringSize/2;
		left -= middle*(stringSize/1.7);
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
		ValueAnimator animator = getDummyAnimator();
		animator.addListener(new UpdateScoreListener());
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
	
	private final class GameOverListener extends AnimatorListenerAdapter {
		
		private final DisplayableString string;
		
		private GameOverListener(DisplayableString string){
			assert string != null;
			this.string = string;
		}
		
		@Override
		public void onAnimationStart(Animator animation) {
			view.getBoardView().addDisplayableString(string);
			
			AudioResource resource = AudioResource.getInstance(view.getContext());
			resource.pauseMusic();
			resource.play(SFX.SCRATCH, SFX.SAD_TROMBONE);
		}
	} 
	
	private final class UpdateScoreListener extends AnimatorListenerAdapter {
	
		
		@Override
		public void onAnimationStart(Animator animation) {
			view.getScoreView().updateScore(message);
		}
		
	}
	
	private final class SoundListener extends AnimatorListenerAdapter {
		private final SFX sound;
		
		private SoundListener(SFX sound){
			assert sound != null;
			this.sound = sound;
		}
		
		@Override
		public void onAnimationStart(Animator animation) {
			AudioResource resource = AudioResource.getInstance(view.getContext());
			resource.play(sound);
		}
		
	}
	
	private final class NewGame extends AnimatorListenerAdapter {
		
		@Override
		public void onAnimationStart(Animator animation) {
			view.getBoardView().clearAllStrings();
			view.getScoreView().syncWithGame();
			AudioResource resource = AudioResource.getInstance(view.getContext());
			resource.play(Music.STRUT);
		}
		
	}

	

}
