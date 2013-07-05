package com.gamepsychos.puzzler.animation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;

import com.gamepsychos.puzzler.audio.AudioResource;
import com.gamepsychos.puzzler.audio.AudioResource.SFX;
import com.gamepsychos.puzzler.board.Change;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.board.view.BoardView;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece;
import com.gamepsychos.puzzler.piece.view.PieceResources;

/**
 * A {@code ChangeAnimgationFactory} is capable of generating animations based on {@link Change}s.
 * @author jcollard
 *
 */
public class ChangeAnimationFactory {

	private final AudioResource audioResource;
	private final BoardView view;
	
	public ChangeAnimationFactory(BoardView view){
		if(view == null)
			throw new NullPointerException();
		this.view = view;
		this.audioResource = AudioResource.getInstance(view.getContext());
	}

	/**
	 * Creates a {@link Animation} from a {@link Change} to be animated on some {@link BoardView}
	 * @param c the {@link Change} to animate
	 * @param view the {@link BoardView} to animate on
	 * @return a {@link Animation}
	 */
	public Animation getAnimation(Change c){
		if(c == null || view == null)
			throw new NullPointerException();
		switch(c.getType()){
		case MOVE:
			return new MoveAnimation(c);
		case CREATE:
			return new CreateAnimation(c);
		case DESTROY:
			return new DestroyAnimation(c);
		}
		return null;
	}
	
	private abstract class BasicAnimation implements Animation {
		
		protected final Change change;		
		
		private BasicAnimation(Change change){
			assert change != null;
			this.change = change;
		}
		
	}
	
	private final List<DisplayLocation> getLocations(Change change){
		List<DisplayLocation> locations = new LinkedList<DisplayLocation>();
		for(Location l : change.getLocations())
			locations.add(new DisplayLocation(l));
		return locations;
	}
	
	private final class MoveAnimation extends BasicAnimation {

		private MoveAnimation(Change change){
			super(change);
		}
		
		@Override
		public ValueAnimator getAnimator() {
			DisplayablePiece piece = view.getPiece(change.getPiece());
			ValueAnimator animation = piece.createAnimator(getLocations(change));
			return animation;
		}
		
	}
	
	private final class DestroyAnimation extends BasicAnimation implements AnimatorListener {

		private DestroyAnimation(Change change){
			super(change);
		}

		@Override
		public ValueAnimator getAnimator() {
			float left = -PieceResources.getSize();
			float top = view.getHeight()+PieceResources.getSize();
			DisplayablePiece piece = view.getPiece(change.getPiece());
			ValueAnimator animation = piece.createAnimator(Collections.singletonList(new DisplayLocation(left, top)));
			animation.setDuration(500);
			animation.addListener(this);
			return animation;
		}

		@Override
		public void onAnimationCancel(Animator animation) {}

		@Override
		public void onAnimationEnd(Animator animation) {}

		@Override
		public void onAnimationRepeat(Animator animation) {}

		@Override
		public void onAnimationStart(Animator animation) {
			audioResource.play(SFX.WOOSH);		
		}
	}
	
	private final class CreateAnimation extends BasicAnimation {

		private CreateAnimation(Change change){
			super(change);
		}

		@Override
		public ValueAnimator getAnimator() {
			int last = change.getLocations().size() - 1;
			view.addPiece(change.getPiece(), change.getLocations().get(last));
			DisplayablePiece piece = view.getPiece(change.getPiece());
			ValueAnimator animation = piece.createAnimator(getLocations(change));
			return animation;
		}
		
	}
	
}
