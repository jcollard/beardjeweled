package com.gamepsychos.puzzler.board;

import java.util.Set;

/**
 * A {@link BoardChecker} is used to determine if there are matches on a {@link DisplayableBoard}
 * @author jcollard
 *
 */
public interface BoardChecker {

	/**
	 * Returns a {@link Set} of {@link Location}s that are matches on {@code board}
	 * @param board the {@link DisplayableBoard} to check.
	 * @return a {@link Set} of {@link Location}s that are matches on {@code board}
	 */
	public Set<Location> check(Board board);
	
}
