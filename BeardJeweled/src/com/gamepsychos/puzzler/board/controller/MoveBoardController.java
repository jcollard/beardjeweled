package com.gamepsychos.puzzler.board.controller;

import com.gamepsychos.puzzler.board.DisplayableBoard;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece;

/**
 * A {@code MoveBoardController} is used to control movement of pieces on a {@link DisplayableBoard}.
 * @author jcollard
 *
 */
public class MoveBoardController implements BoardController {
	
	private final DisplayableBoard board;
	private SelectedPiece selected;
	
	/**
	 * Creates a {@code MoveBoardController} that controls movement of pieces
	 * on a {@link DisplayableBoard}.
	 * @param board
	 */
	public MoveBoardController(DisplayableBoard board){
		if(board == null)
			throw new NullPointerException();
		this.board = board;
	}

	private final Location getLocation(float left, float top){
		int size = DisplayablePiece.getSize();
		int column = (int)(left/size);
		int row = (int)(top/size);
		return Location.getLocation(row, column);
	}
	
	private final DisplayLocation getDisplayLocation(Location location){
		int size = DisplayablePiece.getSize();
		int left = location.getCol()*size;
		int top = location.getRow()*size;
		return new DisplayLocation(left, top);
	}
	
	private final void releaseSelected(){
		if(selected == null) return;
		DisplayLocation location = getDisplayLocation(selected.selectedLocation);
		selected.selectedPiece.setLocation(location);
		selected = null;
	}
	
	@Override
	public boolean onTouch(float left, float top) {
		if(selected != null)
			releaseSelected();

		Location loc = getLocation(left, top);
		
		
		return false;
	}

	@Override
	public boolean onRelease(float left, float top) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMove(float left, float top) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void releaseController() {
		// TODO Auto-generated method stub

	}

	@Override
	public DisplayableBoard getBoard() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private final static class SelectedPiece {
		private final DisplayLocation offset;
		private final Location selectedLocation;
		private final DisplayablePiece selectedPiece;	
		
		private SelectedPiece(DisplayLocation offset, Location selectedLocation, DisplayablePiece selectedPiece){
			assert offset != null;
			assert selectedLocation != null;
			assert selectedPiece != null;
			this.offset = offset;
			this.selectedLocation = selectedLocation;
			this.selectedPiece = selectedPiece;
		}
		
	}

}
