package com.gamepsychos.puzzler.game;

import java.util.Set;

import com.gamepsychos.puzzler.board.Board;
import com.gamepsychos.puzzler.board.Change;
import com.gamepsychos.puzzler.game.Game.GameMessage;
import com.gamepsychos.puzzler.move.MoveResult;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.util.observer.Observable;

/**
 * A {@link Game} contains data on the number of moves a player may
 * take, the number of pieces collected, the score, and the length of
 * the most recent streak.
 * @author jcollard
 *
 */
public interface Game extends Observable<GameMessage> {

	public interface GameMessage {
	
		public Set<Change> getChanges();
		
		public MoveResult getResults();
		
		public int getPointsAwarded();
		
		public int getMovesChange();
		
		public int getPiecesCollected();
		
		public int getStreak();
		
	}
	
	public int getMovesRemaining();
	
	public Set<Piece> getPiecesCollected();
	
	public int getScore();
	
	public int getLatestStreak();
	
	public Board getBoard();
	
}
