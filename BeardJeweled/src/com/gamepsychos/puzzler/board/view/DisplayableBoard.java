package com.gamepsychos.puzzler.board.view;

import com.gamepsychos.puzzler.board.BasicBoard;
import com.gamepsychos.puzzler.board.Board;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.move.MoveFactory;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece;
import com.gamepsychos.puzzler.piece.view.DisplayablePieceFactory;
import com.gamepsychos.util.observer.Observer;

public class DisplayableBoard implements Board {
	
	private final BasicBoard delegate;
	
	public DisplayableBoard(DisplayablePieceFactory pieceFactory){
		if(pieceFactory == null)
			throw new NullPointerException();
		this.delegate = new BasicBoard(pieceFactory);
	}

	@Override
	public boolean register(Observer<Board> observer) {
		return delegate.register(observer);
	}

	@Override
	public int getColumns() {
		return delegate.getColumns();
	}

	@Override
	public MoveFactory getMoveFactory() {
		return delegate.getMoveFactory();
	}

	@Override
	public DisplayablePiece getPiece(Location loc) {
		//Guaranteed by DisplayablePieceFactory
		return (DisplayablePiece)delegate.getPiece(loc);
	}

	@Override
	public int getRows() {
		return delegate.getRows();
	}

}
