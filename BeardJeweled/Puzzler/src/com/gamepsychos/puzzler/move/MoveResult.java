package com.gamepsychos.puzzler.move;

import java.util.Set;

import com.gamepsychos.puzzler.board.Location;


/**
 * A MoveResult contains information on the result of performing a {@link Move}.
 * @author jcollard
 *
 */
public interface MoveResult {
	
	/**
	 * Returns the {@link Set} of {@link Location}s that were removed.
	 * @return the {@link Set} of {@link Location}s that were removed.
	 */
	public Set<Location> removed();
	
	/**
	 * Returns {@code true} if there is a follow up move based on the results and false otherwise.
	 * @return {@code true} if there is a follow up move based on the results and false otherwise.
	 */
	public boolean followUpMove();
	
	/**
	 * Returns the {@link Drop}s that resulted.
	 * @return the {@link Drop}s that resulted.
	 */
	public Set<Drop> drops();
	
	/**
	 * Returns the follow up {@link Move} if it exists.
	 * @return the follow up {@link Move} if it exists.
	 * @throws IllegalMoveException if there is no follow up move
	 */
	public Move getFollowUpMove() throws IllegalMoveException;
	
}
