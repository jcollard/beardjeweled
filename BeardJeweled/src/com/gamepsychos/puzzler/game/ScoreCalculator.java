package com.gamepsychos.puzzler.game;

import java.util.Set;

import com.gamepsychos.puzzler.board.Location;

/**
 * A {@link ScoreCalculator} is used to calculate the number of points received for a particular
 * {@link Set} of cleared {@link Location}s.
 * @author jcollard
 *
 */
public interface ScoreCalculator {

	/**
	 * Calculates the score for the given {@link Location}s
	 * @param locations the {@link Location}s to calculate a score for
	 * @return the score for the {@link Location}s
	 */
	public int getScore(Set<Location> locations, int streak);
	
}
