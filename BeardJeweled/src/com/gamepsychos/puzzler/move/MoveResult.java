package com.gamepsychos.puzzler.move;

import java.util.Set;

import com.gamepsychos.puzzler.board.Change;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.piece.Piece;


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
	 * Returns the {@link Set} of {@link Piece}s that were removed.
	 * @return the {@link Set} of {@link Piece}s that were removed.
	 */
	public Set<Piece> destroyed();
	
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
	 * Returns {@code true} if this result is from a {@link Move} that was a
	 * follow up to a previous {@link Move} and {@code false} otherwise.
	 * @return {@code true} if this result is from a previous {@link Move}
	 */
	public boolean isFollowUpMove();
	
	/**
	 * Returns the follow up {@link Move} if it exists.
	 * @return the follow up {@link Move} if it exists.
	 * @throws IllegalMoveException if there is no follow up move
	 */
	public Move getFollowUpMove() throws IllegalMoveException;
	
	/**
	 * Returns the {@link Change}s that occurred during this {@link MoveResult}
	 * @return the {@link Change}s that occurred during this {@link MoveResult}
	 */
	public Set<Change> getChanges();
	
}
