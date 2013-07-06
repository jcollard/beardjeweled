package com.gamepsychos.puzzler.board;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.gamepsychos.puzzler.board.Change.ChangeType;
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
	 * Creates a {@code BasicBoard} that is 7x6 and specifies the
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
		
		FollowUpMove clearBoard = new FollowUpMove();
		MoveResult result;
		do{
			result = clearBoard.silentMove();
			if(result.followUpMove())
				clearBoard = (FollowUpMove)result.getFollowUpMove();
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

	private final MoveResult swapPieces(Location a, Location b) {
		Set<Change> changes = new HashSet<Change>();
		Change change0 = new Change(piece[a.getRow()][a.getCol()], ChangeType.MOVE, Collections.singletonList(b));
		Change change1 = new Change(piece[b.getRow()][b.getCol()], ChangeType.MOVE, Collections.singletonList(a));
		changes.add(change0);
		changes.add(change1);
		Piece temp = piece[a.getRow()][a.getCol()];
		piece[a.getRow()][a.getCol()] = piece[b.getRow()][b.getCol()];
		piece[b.getRow()][b.getCol()] = temp;
		Set<Location> locations = Collections.emptySet();
		Set<Drop> drops = Collections.emptySet();
		Set<Piece> destroyed = Collections.emptySet();
		Move followupmove = new FollowUpMove();
		return new BasicMoveResult(locations, followupmove, drops, destroyed, false, changes, false);
	}
	
	
	//TODO Write interface for clearing a board
	private final MoveResult clearBoard(boolean isFollowUp){
		final Set<Location> locations = new HashSet<Location>();
		final Set<Piece> pieces = new HashSet<Piece>();

		for (BoardChecker checker : checkers)
			locations.addAll(checker.check(BasicBoard.this));

		if (locations.isEmpty()) {
			Set<Drop> drops = Collections.emptySet();
			Set<Change> changes = Collections.emptySet();
			return new BasicMoveResult(locations, null, drops, pieces, isFollowUp, changes, false);
		}
		
		Set<Change> changes = new HashSet<Change>();
		for (Location l : locations){
			Piece p = piece[l.getRow()][l.getCol()];
			pieces.add(p);
			List<Location> emptyList = Collections.emptyList();
			Change destroy = new Change(p, ChangeType.DESTROY, emptyList);
			piece[l.getRow()][l.getCol()] = null;
			changes.add(destroy);
		}

		final Set<Drop> drops = findDrops();
		
		for(Drop d : drops){
			boolean created = !Boards.inBounds(this, d.getStart());
			
			List<Location> ls = new LinkedList<Location>();
			ls.add(d.getStart());
			ls.add(d.getEnd());
			ChangeType type = created ? ChangeType.CREATE : ChangeType.MOVE;
			Change drop = new Change(d.getPiece(), type, ls);
			changes.add(drop);
		}
		
		return new BasicMoveResult(locations, new FollowUpMove(), drops, pieces, isFollowUp, changes, false);
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
	private final BasicObservable<MoveResult> delegate = new BasicObservable<MoveResult>();

	@Override
	public boolean register(Observer<MoveResult> observer) {
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
	
	private final class ResetMove implements Move {

		@Override
		public MoveResult move() {
			
			Set<Change> changes = new HashSet<Change>();
			for(int r = 0; r < rows; r++){
				for(int c = 0; c < cols; c++){
					Piece p = piece[r][c];
					List<Location> locations = Collections.singletonList(Location.getLocation(0, 0));
					Change remove = new Change(p, ChangeType.REMOVE, locations);
					changes.add(remove);
				}
			}
			
			Set<Piece> destroyed = Collections.emptySet();
			Set<Location> locations = Collections.emptySet();
			Set<Drop> drops = Collections.emptySet();
			
			return new BasicMoveResult(locations, new SetupMove(), drops, destroyed, false, changes, false);
		}
		
	}
	
	private final class SetupMove implements Move {

		@Override
		public MoveResult move() {

			Set<Change> changes = new HashSet<Change>();
			init();
			for(int r = 0; r < rows; r++){
				for(int c = 0; c < cols; c++){
					Piece p = piece[r][c];
					List<Location> locations = new LinkedList<Location>();
					locations.add(Location.getLocation(-1, c));
					locations.add(Location.getLocation(r, c));
					Change create = new Change(p, ChangeType.CREATE, locations);
					changes.add(create);
				}
			}
			
			Set<Piece> destroyed = Collections.emptySet();
			Set<Location> locations = Collections.emptySet();
			Set<Drop> drops = Collections.emptySet();
			
			return new BasicMoveResult(locations, new SetupMove(), drops, destroyed, false, changes, true);
		}
		
	}

	private final class IgnoreMove implements Move {

		@Override
		public MoveResult move() {
			Set<Location> emptySet = Collections.emptySet();
			Set<Drop> drops = Collections.emptySet();
			Set<Piece> destroyed = Collections.emptySet();
			Set<Change> changes = Collections.emptySet();
			return new BasicMoveResult(emptySet, null, drops, destroyed, false, changes, false);
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
			MoveResult result = swapPieces(a, b);
			delegate.notifyObservers(result);
			return result;
		}

	}

	private final class FollowUpMove implements Move {

		@Override
		public MoveResult move() {
			MoveResult result = clearBoard(true);
			delegate.notifyObservers(result);
			return result;
		}
		
		private MoveResult silentMove() {
			MoveResult result = clearBoard(true);
			return result;
		}

	}

	//TODO Create Builder w/ default values
	private static final class BasicMoveResult implements MoveResult {

		private final Set<Piece> destroyed;
		private final Set<Location> locations;
		private final Move followUpMove;
		private final Set<Drop> drops;
		private final boolean isFollowUp;
		private final Set<Change> changes;
		private final boolean startNewGame;

		//TODO Create Builder w/ default values
		private BasicMoveResult(Set<Location> locations, Move followupmove,
				Set<Drop> drops, Set<Piece> destroyed, boolean isFollowUp, Set<Change> changes, boolean startNewGame) {
			assert locations != null;
			assert drops != null;
			assert changes != null;
			this.locations = Collections.unmodifiableSet(locations);
			this.followUpMove = followupmove;
			this.drops = Collections.unmodifiableSet(drops);
			this.destroyed = Collections.unmodifiableSet(destroyed);
			this.isFollowUp = isFollowUp;
			this.changes = Collections.unmodifiableSet(changes);
			this.startNewGame = startNewGame;
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

		@Override
		public boolean isFollowUpMove() {
			return isFollowUp;
		}

		@Override
		public Set<Change> getChanges() {
			return changes;
		}
		
		@Override
		public boolean isStartNewGame() {
			return startNewGame;
		}

	}

	@Override
	public final void resetBoard() {
		MoveResult resetMove = new ResetMove().move();
		delegate.notifyObservers(resetMove); 
		MoveResult setupMove = new SetupMove().move();
		delegate.notifyObservers(setupMove);
	}
	
}
