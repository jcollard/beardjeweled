package com.gamepsychos.puzzler.move;

import com.gamepsychos.puzzler.board.Location;

/**
 * A {@link MoveFactory} is used to create {@link Move}s.
 * @author jcollard
 *
 */
public interface MoveFactory {

	/**
	 * Returns a {@link Move} that swaps the two {@link Piece}s at {@link Location} a and b.
	 * @param a the first {@link Location} to swap
	 * @param b the second {@link Location} to swap
	 * @return a {@link Move} that swaps the two {@link Piece}s at {@link Location} a and b.
	 * @throws IllegalMoveException if the move is not a legal move
	 */
	public Move getSwapMove(Location a, Location b) throws IllegalMoveException;
	
}
