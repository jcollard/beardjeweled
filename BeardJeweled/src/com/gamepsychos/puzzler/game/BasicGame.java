package com.gamepsychos.puzzler.game;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.gamepsychos.puzzler.board.Board;
import com.gamepsychos.puzzler.board.Change;
import com.gamepsychos.puzzler.move.MoveResult;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.util.observer.BasicObservable;
import com.gamepsychos.util.observer.Observer;

/**
 * A {@link BasicGame} is a simple implementation of {@link Game} that tracks
 * the score through a specified {@link ScoreCalculator}. Additional moves are
 * rewarded to a player who clears 4 or more jewels at the same time. The number
 * awarded is equal to the number of jewels cleared minus 3.
 * 
 * @author jcollard
 * 
 */
public final class BasicGame implements Game, Observer<MoveResult> {

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
	 * 
	 * @param moves
	 *            the initial number of moves the player receives
	 * @param calculator
	 *            the calculator to use for scoring
	 * @param board
	 *            the board to model
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

	private final ScoreMessage processResult(MoveResult result) {
		runStreak(result);
		runPieces(result);
		int points = runScore(result);
		int moves = runMoves(result);
		return new ScoreMessage(points, moves, latestStreak, result.destroyed()
				.size());
	}

	private final void runStreak(MoveResult result) {
		if (result.isFollowUpMove()) {
			latestStreak++;
		} else {
			latestStreak = 0;
		}
	}

	private final int runMoves(MoveResult result) {
		int change = 0;
		if (result.destroyed().size() > BONUS_THRESHOLD) {
			change = result.destroyed().size() - BONUS_THRESHOLD;
		}

		if (!result.isFollowUpMove())
			change--;

		movesRemaining += change;
		return change;

	}

	private final void runPieces(MoveResult result) {
		piecesCollected.addAll(result.destroyed());
	}

	private final int runScore(MoveResult result) {
		int scoreChange = calculator.getScore(result.removed(), latestStreak);
		if (scoreChange != 0) {
			score += scoreChange;
		}
		return scoreChange;
	}

	@Override
	public void update(MoveResult message) {
		ScoreMessage score = processResult(message);
		GameMessage gameMessage = new BasicGameMessage(message.getChanges(),
				message, score);
		delegateObserver.notifyObservers(gameMessage);
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

	private static final class ScoreMessage {
		private final int points;
		private final int moves;
		private final int streak;
		private final int jewels;

		private ScoreMessage(int points, int moves, int streak, int jewels) {
			this.points = points;
			this.moves = moves;
			this.streak = streak;
			this.jewels = jewels;
		}
	}

	private static final class BasicGameMessage implements GameMessage {

		private final Set<Change> changes;
		private final MoveResult results;
		private final ScoreMessage score;

		private BasicGameMessage(Set<Change> changes, MoveResult results,
				ScoreMessage score) {
			assert changes != null;
			assert results != null;
			assert score != null;

			this.changes = Collections.unmodifiableSet(changes);
			this.results = results;
			this.score = score;

		}

		@Override
		public Set<Change> getChanges() {
			return changes;
		}

		@Override
		public MoveResult getResults() {
			return results;
		}

		@Override
		public int getPointsAwarded() {
			return score.points;
		}

		@Override
		public int getMovesChange() {
			return score.moves;
		}

		@Override
		public int getStreak() {
			return score.streak;
		}

		@Override
		public int getPiecesCollected() {
			return score.jewels;
		}

	}

}
