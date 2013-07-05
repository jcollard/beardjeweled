package com.gamepsychos.puzzler.game;

import java.util.Set;

import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.util.observer.Observable;
import com.gamepsychos.puzzler.board.Board;
import com.gamepsychos.puzzler.game.Game.GameMessage;

public interface Game extends Observable<GameMessage> {

	public static enum GameMessage {
		
		MOVES_CHANGED,
		PIECES_COLLECTED_CHANGED,
		SCORE_CHANGED,
		STREAK_CHANGED;
		
	}
	
	public int getMovesRemaining();
	
	public Set<Piece> getPiecesCollected();
	
	public int getScore();
	
	public int getLatestStreak();
	
	public Board getBoard();
	
}
