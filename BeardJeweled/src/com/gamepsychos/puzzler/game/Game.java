package com.gamepsychos.puzzler.game;

import java.util.Set;

import com.gamepsychos.puzzler.piece.Piece;

public interface Game {

	public int movesRemaining();
	
	public Set<Piece> piecesCollected();
	
}
