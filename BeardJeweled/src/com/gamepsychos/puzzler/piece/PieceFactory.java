package com.gamepsychos.puzzler.piece;

/**
 * A PieceFactory is used to generate new {@link Piece}s.
 * @author jcollard
 *
 */
public interface PieceFactory {

	/**
	 * Returns the next {@link Piece}
	 * @return the next {@link Piece}
	 */
	public Piece createPiece();
	
}
