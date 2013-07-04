package com.gamepsychos.puzzler.piece;

import com.gamepsychos.puzzler.board.Board;

/**
 * A Piece is an element that can occupy a space on a {@link Board}
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
	
	/**
	 * Returns the {link PieceType} for this {@code Piece}
	 * @return the {link PieceType} for this {@code Piece}
	 */
	public PieceType getType();
	
}
