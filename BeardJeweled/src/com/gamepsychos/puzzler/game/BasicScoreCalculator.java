package com.gamepsychos.puzzler.game;

import java.util.Set;

import com.gamepsychos.puzzler.board.Location;

public class BasicScoreCalculator implements ScoreCalculator {

	@Override
	public int getScore(Set<Location> locations, int streak) {
		if(locations.size() < 3)
			return 0;
		int bonus = locations.size()-2;
		int score = (streak+1)*25*bonus*bonus;
		return score;
	}

}
