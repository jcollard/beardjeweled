package com.gamepsychos.puzzler.animation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.animation.ValueAnimator;

import com.gamepsychos.puzzler.board.Change;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.board.view.BoardView;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece;
import com.gamepsychos.puzzler.piece.view.PieceResources;

public class ChangeAnimationFactory {


	public ChangeAnimation getAnimation(Change c, BoardView view){
		if(c == null || view == null)
			throw new NullPointerException();
		switch(c.getType()){
		case MOVE:
			return new MoveAnimation(c, view);
		case CREATE:
			return new CreateAnimation(c, view);
		case DESTROY:
			return new DestroyAnimation(c, view);
		}
		return null;
	}
	
	private static abstract class BasicAnimation implements ChangeAnimation {
		
		protected final Change change;
		protected final BoardView view;
		
		
		private BasicAnimation(Change change, BoardView view){
			assert change != null;
			assert view != null;
			this.change = change;
			this.view = view;
		}
		
	}
	
	private static final List<DisplayLocation> getLocations(Change change){
		List<DisplayLocation> locations = new LinkedList<DisplayLocation>();
		for(Location l : change.getLocations())
			locations.add(new DisplayLocation(l));
		return locations;
	}
	
	private static final class MoveAnimation extends BasicAnimation {

		private MoveAnimation(Change change, BoardView view){
			super(change, view);
		}
		
		@Override
		public ValueAnimator getAnimator() {
			DisplayablePiece piece = view.getPiece(change.getPiece());
			ValueAnimator animation = piece.createAnimator(getLocations(change));
			return animation;
		}
		
	}
	
	private static final class DestroyAnimation extends BasicAnimation {

		private DestroyAnimation(Change change, BoardView view){
			super(change, view);
		}

		@Override
		public ValueAnimator getAnimator() {
			float left = -PieceResources.getSize();
			float top = view.getHeight()+PieceResources.getSize();
			DisplayablePiece piece = view.getPiece(change.getPiece());
			ValueAnimator animation = piece.createAnimator(Collections.singletonList(new DisplayLocation(left, top)));
			return animation;
		}
		
	}
	
	private static final class CreateAnimation extends BasicAnimation {

		private CreateAnimation(Change change, BoardView view){
			super(change, view);
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
