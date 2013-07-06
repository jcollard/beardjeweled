package com.gamepsychos.puzzler.game;

import java.util.Set;

import com.gamepsychos.puzzler.board.Location;

/**
 * <pre>
 * A {@link BasicScoreCalculator} calculates the score using the following
 * formula:
 * 25*(locations.size-2)*(streak^2)
 * </pre>
 * @author jcollard
 *
 */
public class BasicScoreCalculator implements ScoreCalculator {

	@Override
	public int getScore(Set<Location> locations, int streak) {
		if(locations.size() < 3)
			return 0;
		int bonus = getBaseScore(locations.size()-2, 25);
		int score = bonus*streak*streak;
		return score;
	}
	
	private final int getBaseScore(int size, int score){
		if(size <= 1)
			return score;
		return score+(getBaseScore(size-1, score*2));
	}

}
