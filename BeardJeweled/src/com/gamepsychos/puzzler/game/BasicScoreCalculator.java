package com.gamepsychos.puzzler.game;

import java.util.Set;

import com.gamepsychos.puzzler.board.Location;

/**
 * <pre>
 * A {@link BasicScoreCalculator} calculates the score using the following
 * formula:
 * (streak)*25*(locations.size-2)^2
 * </pre>
 * @author jcollard
 *
 */
public class BasicScoreCalculator implements ScoreCalculator {

	@Override
	public int getScore(Set<Location> locations, int streak) {
		if(locations.size() < 3)
			return 0;
		int bonus = locations.size()-2;
		int score = streak*25*bonus*bonus;
		return score;
	}

}
