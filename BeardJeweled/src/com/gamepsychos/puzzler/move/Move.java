package com.gamepsychos.puzzler.move;

import com.gamepsychos.puzzler.board.Board;

/**
 * A {@link Move} is an action that modifies the state of a {@link Board}
 * @author jcollard
 *
 */
public interface Move {

	/**
	 * Attempts to perform this {@link Move}. If the move was successful,
	 * True is returned. Otherwise, False is returned.
	 * @return True if the move was successful and False otherwise.
	 */
	public MoveResult move();
	
}
