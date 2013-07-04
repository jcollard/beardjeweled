package com.gamepsychos.puzzler.board;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.gamepsychos.puzzler.move.Drop;
import com.gamepsychos.puzzler.move.IllegalMoveException;
import com.gamepsychos.puzzler.move.Move;
import com.gamepsychos.puzzler.move.MoveFactory;
import com.gamepsychos.puzzler.move.MoveResult;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.puzzler.piece.PieceFactory;
import com.gamepsychos.util.observer.BasicObservable;
import com.gamepsychos.util.observer.Observer;

/**
 * A {@code BasicBoard} is a simple implementation of {@link Board}.
 * 
 * @author jcollard
 * 
 */
public class BasicBoard implements Board {

	private static final Set<BoardChecker> checkers;

	static {
		checkers = new HashSet<BoardChecker>();
		checkers.add(LineChecker.getLineChecker(3));
		checkers.add(LineChecker.getLineChecker(4));
		checkers.add(LineChecker.getLineChecker(5));
	}

	private final PieceFactory pieceFactory;
	private final int rows, cols;
	private final Piece[][] piece;
	private final MoveFactory moveFactory;

	/**
	 * Creates a {@code BasicBoard} that is 8x8 and specifies the
	 * {@link PieceFactory} to use to generate {@link Piece}s
	 * 
	 * @param pieceFactory
	 *            the {@link PieceFactory} to generate {@link Pieces}
	 */
	public BasicBoard(PieceFactory pieceFactory) {
		this(pieceFactory, 8, 6);

	}

	/**
	 * Creates a {@code BasicBoard} that is {@code rows}x{@code cols} and
	 * specifies the {@link PieceFactory} to use to generate {@link Piece}s
	 * 
	 * @param pieceFactory
	 *            the {@link PieceFactory} to generate {@link Pieces}
	 * @param rows
	 *            the number of rows
	 * @param cols
	 *            the number of columns
	 */
	public BasicBoard(PieceFactory pieceFactory, int rows, int cols) {
		if (pieceFactory == null)
			throw new NullPointerException();
		if (rows < 1 || cols < 1)
			throw new IllegalArgumentException();
		this.pieceFactory = pieceFactory;
		this.rows = rows;
		this.cols = cols;
		this.piece = new Piece[rows][cols];
		this.moveFactory = new DefaultMoveFactory();
		init();
	}
	
	private final void init(){
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
				piece[r][c] = pieceFactory.createPiece();
		
		Move clearBoard = new FollowUpMove();
		MoveResult result;
		do{
			result = clearBoard.move();
			if(result.followUpMove())
				clearBoard = result.getFollowUpMove();
		} while(result.followUpMove());
	}

	@Override
	public Piece getPiece(Location loc) {
		return piece[loc.getRow()][loc.getCol()];
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public int getColumns() {
		return cols;
	}

	@Override
	public MoveFactory getMoveFactory() {
		return moveFactory;
	}

	private final void swapPieces(Location a, Location b) {
		Piece temp = piece[a.getRow()][a.getCol()];
		piece[a.getRow()][a.getCol()] = piece[b.getRow()][b.getCol()];
		piece[b.getRow()][b.getCol()] = temp;
	}
	
	private final MoveResult clearBoard(){
		final Set<Location> locations = new HashSet<Location>();
		final Set<Piece> pieces = new HashSet<Piece>();

		for (BoardChecker checker : checkers)
			locations.addAll(checker.check(BasicBoard.this));

		if (locations.isEmpty()) {
			Set<Drop> drops = Collections.emptySet();
			return new BasicMoveResult(locations, null, drops, pieces);
		}

		for (Location l : locations){
			pieces.add(piece[l.getRow()][l.getCol()]);
			piece[l.getRow()][l.getCol()] = null;
		}

		final Set<Drop> drops = findDrops();

		return new BasicMoveResult(locations, new FollowUpMove(), drops, pieces);
	}

	private final Set<Drop> findDrops() {
		Set<Drop> drops = new HashSet<Drop>();
		for (int r = rows-1; r >= 0; r--) { // Start at the bottom and go up.
			for (int c = 0; c < cols; c++) {
				if (piece[r][c] == null) {
					drops.add(executeDrop(r, c));
				}
			}
		}
		return drops;
	}

	private final Drop executeDrop(int row, int col) {
		Location end = Location.getLocation(row, col);
		for (int r = row; r >= 0; r--) {
			if (piece[r][col] != null) {
				Location start = Location.getLocation(r, col);
				piece[row][col] = piece[r][col];
				piece[r][col] = null;
				return new Drop(piece[row][col], start, end);
			}
		}
		piece[row][col] = pieceFactory.createPiece();
		Location start = Location.getLocation(-1, col);
		return new Drop(piece[row][col], start, end);
	}

	// Observable Methods
	private final BasicObservable<Board> delegate = new BasicObservable<Board>();

	@Override
	public boolean register(Observer<Board> observer) {
		return delegate.register(observer);
	}

	private final class DefaultMoveFactory implements MoveFactory {

		@Override
		public Move getSwapMove(Location a, Location b)
				throws IllegalMoveException {
			if (a == null || b == null)
				throw new NullPointerException();
			if (Location.adjacent(a, b))
				return new SwapMove(a, b);
			return new IgnoreMove();
		}

	}

	private final class IgnoreMove implements Move {

		@Override
		public MoveResult move() {
			Set<Location> emptySet = Collections.emptySet();
			Set<Drop> drops = Collections.emptySet();
			Set<Piece> destroyed = Collections.emptySet();
			return new BasicMoveResult(emptySet, null, drops, destroyed);
		}

	}

	private final class SwapMove implements Move {

		private final Location a;
		private final Location b;

		private SwapMove(Location a, Location b) {
			assert a != null;
			assert b != null;
			this.a = a;
			this.b = b;
		}

		@Override
		public MoveResult move() {
			swapPieces(a, b);
			return clearBoard();
		}

	}

	private final class FollowUpMove implements Move {

		@Override
		public MoveResult move() {
			return clearBoard();
		}

	}

	private static final class BasicMoveResult implements MoveResult {

		private final Set<Piece> destroyed;
		private final Set<Location> locations;
		private final Move followUpMove;
		private final Set<Drop> drops;

		private BasicMoveResult(Set<Location> locations, Move followupmove,
				Set<Drop> drops, Set<Piece> destroyed) {
			assert locations != null;
			assert drops != null;
			this.locations = Collections.unmodifiableSet(locations);
			this.followUpMove = followupmove;
			this.drops = Collections.unmodifiableSet(drops);
			this.destroyed = Collections.unmodifiableSet(destroyed);
		}

		@Override
		public Set<Location> removed() {
			return locations;
		}

		@Override
		public boolean followUpMove() {
			return followUpMove != null;
		}

		@Override
		public Move getFollowUpMove() throws IllegalMoveException {
			return followUpMove;
		}

		@Override
		public Set<Drop> drops() {
			return drops;
		}

		@Override
		public Set<Piece> destroyed() {
			return destroyed;
		}

	}

}
