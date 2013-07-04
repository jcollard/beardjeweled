package com.gamepsychos.puzzler.piece;

import com.gamepsychos.puzzler.board.DisplayableBoard;

/**
 * A Piece is an element that can occupy a space on a {@link DisplayableBoard}
 * @author jcollard
 *
 */
public interface Piece {

	/**
	 * Returns true if the {@code p} matches with this {@link Piece}
	 * @param p the {@link Piece} to check
	 * @return true if {@code p} matches with this and false otherwise.
	 */
	public boolean matches(Piece p);
	
}
