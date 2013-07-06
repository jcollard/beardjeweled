package com.gamepsychos.puzzler.board.controller;

import java.util.Collections;

import com.gamepsychos.puzzler.board.Board;
import com.gamepsychos.puzzler.board.Boards;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.board.view.BoardView;
import com.gamepsychos.puzzler.game.Game;
import com.gamepsychos.puzzler.move.Move;
import com.gamepsychos.puzzler.move.MoveFactory;
import com.gamepsychos.puzzler.move.MoveResult;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece;
import com.gamepsychos.puzzler.piece.view.PieceResources;

/**
 * A {@code MoveBoardController} is used to control movement of pieces on a {@link Board}.
 * @author jcollard
 *
 */
public class MoveBoardController implements BoardController {
	
	private final Game game;
	private final Board board;
	private final BoardView view;
	private SelectedPiece selected;
	
	/**
	 * Creates a {@code MoveBoardController} that controls movement of pieces
	 * on a {@link Board}.
	 * @param board
	 */
	public MoveBoardController(BoardView view){
		if(view == null)
			throw new NullPointerException(); 
		this.game = view.getGame();
		this.board = this.game.getBoard();
		this.view = view;
	}

	private final Location getLocation(float left, float top){
		int size = PieceResources.getSize();
		int column = (int)(left/size);
		int row = (int)(top/size);
		return Location.getLocation(row, column);
	}
	
	private final DisplayLocation getDisplayLocation(Location location){
		int size = PieceResources.getSize();
		int left = location.getCol()*size;
		int top = location.getRow()*size;
		return new DisplayLocation(left, top);
	}
	
	
	@Override
	public boolean onTouch(float left, float top) {
		if(game.isGameOver()) return false;

		Location loc = getLocation(left, top);
		if(!Boards.inBounds(board, loc))
			return false;
		
		Piece piece = board.getPiece(loc);
		DisplayLocation pieceLocation = getDisplayLocation(loc);
		float offLeft = left - pieceLocation.getLeft();
		float offTop = top - pieceLocation.getTop();
		DisplayLocation offset = new DisplayLocation(offLeft, offTop);
		selected = new SelectedPiece(offset, loc, piece);
		
		return true;
	}

	@Override
	public boolean onRelease(float left, float top) {
		if(game.isGameOver()) return false;
		if(selected == null)
			return false;
		Location location = getLocation(left, top);
		
		//TODO write interface for determining move legality
		if(Location.adjacent(location, selected.selectedLocation) && Boards.inBounds(board, location)){
			MoveFactory factory = board.getMoveFactory();
			Move move = factory.getSwapMove(location, selected.selectedLocation);
			MoveResult result = move.move();
			while(result.followUpMove()){
				move = result.getFollowUpMove();
				result = move.move();
			}
			return true;
		}
		DisplayLocation loc = new DisplayLocation(selected.selectedLocation);
		DisplayablePiece piece = view.getPiece(selected.selectedPiece);
		piece.createAnimator(Collections.singletonList(loc)).start();
		
		return false;
	}

	@Override
	public boolean onMove(float left, float top) {
		if(game.isGameOver()) return false;
		if(selected == null)
			return false;
		DisplayablePiece p = view.getPiece(selected.selectedPiece);
		float offLeft = selected.offset.getLeft();
		float offTop = selected.offset.getTop();
		DisplayLocation location = new DisplayLocation(left-offLeft, top-offTop);
		p.setLocation(location);
		return false;
	}
	
	private final static class SelectedPiece {
		private final DisplayLocation offset;
		private final Location selectedLocation;
		private final Piece selectedPiece;	
		
		private SelectedPiece(DisplayLocation offset, Location selectedLocation, Piece selectedPiece){
			assert offset != null;
			assert selectedLocation != null;
			assert selectedPiece != null;
			this.offset = offset;
			this.selectedLocation = selectedLocation;
			this.selectedPiece = selectedPiece;
		}
		
	}

}
