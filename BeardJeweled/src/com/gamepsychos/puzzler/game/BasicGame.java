package com.gamepsychos.puzzler.game;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.gamepsychos.puzzler.board.Board;
import com.gamepsychos.puzzler.board.Board.BoardMessage;
import com.gamepsychos.puzzler.move.MoveResult;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.util.observer.BasicObservable;
import com.gamepsychos.util.observer.Observer;

/**
 * A {@link BasicGame} is a simple implementation of {@link Game} that
 * tracks the score through a specified {@link ScoreCalculator}. Additional
 * moves are rewarded to a player who clears 4 or more jewels at the same time.
 * The number awarded is equal to the number of jewels cleared minus 3.
 * @author jcollard
 *
 */
public final class BasicGame implements Game, Observer<BoardMessage> {

	private static final int BONUS_THRESHOLD = 3;
	private int score;
	private int movesRemaining;
	private final Set<Piece> piecesCollected;
	private final BasicObservable<GameMessage> delegateObserver;
	private final ScoreCalculator calculator;
	private final Board board;
	private int latestStreak = 0;

	/**
	 * Creates a {@link BasicGame} that allows the user to take {@code moves}
	 * calculates the score with {@code calculator} and models {@code board}
	 * @param moves the initial number of moves the player receives
	 * @param calculator the calculator to use for scoring
	 * @param board the board to model
	 */
	public BasicGame(int moves, ScoreCalculator calculator, Board board) {
		if (moves < 0)
			throw new IllegalArgumentException();
		if (calculator == null || board == null)
			throw new NullPointerException();

		this.movesRemaining = moves;
		this.piecesCollected = new HashSet<Piece>();
		this.delegateObserver = new BasicObservable<Game.GameMessage>();
		this.calculator = calculator;
		this.board = board;
		board.register(this);
	}

	@Override
	public final int getMovesRemaining() {
		return movesRemaining;
	}

	@Override
	public final Set<Piece> getPiecesCollected() {
		return Collections.unmodifiableSet(piecesCollected);
	}

	private final void processResult(MoveResult result) {
		runStreak(result);
		runPieces(result);
		runScore(result);
		runMoves(result);
	}
	
	private final void runStreak(MoveResult result){
		if(result.isFollowUpMove()){
			latestStreak++;
		}else{
			latestStreak = 0;
		}
	}

	private final void runMoves(MoveResult result) {
		int change = 0;
		if (result.destroyed().size() > BONUS_THRESHOLD) {
			change = result.destroyed().size() - BONUS_THRESHOLD;
		}

		if(!result.isFollowUpMove())
			change--;
		
		if (change == 0)
			return;

		movesRemaining += change;
		delegateObserver.notifyObservers(GameMessage.MOVES_CHANGED);

	}

	private final void runPieces(MoveResult result) {
		piecesCollected.addAll(result.destroyed());
		if (!result.destroyed().isEmpty())
			delegateObserver
					.notifyObservers(GameMessage.PIECES_COLLECTED_CHANGED);
	}

	private final void runScore(MoveResult result) {
		int scoreChange = calculator.getScore(result.removed(), latestStreak);
		if (scoreChange != 0) {
			score += scoreChange;
			delegateObserver.notifyObservers(GameMessage.SCORE_CHANGED);
		}
	}

	@Override
	public void update(BoardMessage message) {
		Set<MoveResult> results = message.getResults();
		if (results.isEmpty())
			return;
		for (MoveResult result : results)
			processResult(result);
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public boolean register(Observer<GameMessage> observer) {
		return delegateObserver.register(observer);
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public int getLatestStreak() {
		return latestStreak;
	}

}
