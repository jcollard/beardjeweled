package com.gamepsychos.puzzler.board;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gamepsychos.puzzler.piece.Piece;

/**
 * A {@code LineChecker} checks for horizontal and vertical lines.
 * 
 * @author jcollard
 * 
 */
public class LineChecker implements BoardChecker {

	private final int length;
	private static final Map<Integer, LineChecker> lineCheckers = new HashMap<Integer, LineChecker>();

	/**
	 * Returns a {@link LineChecker} that checks for horizontal and vertical
	 * lines of the specified length.
	 * 
	 * @param length
	 *            the length to check for
	 * @return a {@link LineChecker} that checks for horizontal and vertical
	 *         lines of the specified length.
	 * @throws IllegalArgumentException
	 *             if {@code length) is less than 2.

	 */
	public static LineChecker getLineChecker(int length) {
		if (length < 2)
			throw new IllegalArgumentException();
		LineChecker checker = lineCheckers.get(length);
		if (checker == null) {
			checker = new LineChecker(length);
			lineCheckers.put(length, checker);
		}
		return checker;
	}

	private LineChecker(int length) {
		assert length > 1;
		this.length = length;
	}

	@Override
	public Set<Location> check(Board board) {
		Set<Location> locations = new HashSet<Location>();
		int rows = board.getRows();
		int cols = board.getColumns();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				checkHorizontal(r, c, board, locations);
				checkVertical(r, c, board, locations);
			}
		}
		return locations;
	}

	private final Set<Piece> toCheckRow(int r, int c, Board board) {
		Set<Piece> toCheck = new HashSet<Piece>();
		int rows = board.getRows();
		int maxrow = r + length;
		for (; r < maxrow && r < rows; r++) {
			Piece p = board.getPiece(Location.getLocation(r, c));
			toCheck.add(p);
		}
		return toCheck;
	}

	private final Set<Piece> toCheckCol(int r, int c, Board board) {
		Set<Piece> toCheck = new HashSet<Piece>();
		int cols = board.getColumns();
		int maxcol = c + length;
		for (; c < maxcol && c < cols; c++) {
			Piece p = board.getPiece(Location.getLocation(r, c));
			toCheck.add(p);
		}
		return toCheck;
	}

	private final void checkHorizontal(int r, int c, Board board,
			Set<Location> locations) {
		Set<Piece> toCheck = toCheckRow(r, c, board);
		int matches = matches(toCheck);

		if (matches == length) {
			int maxcol = c + length;
			for (; c < maxcol; c++)
				locations.add(Location.getLocation(r, c));
		}

	}

	private final void checkVertical(int r, int c, Board board,
			Set<Location> locations) {
		Set<Piece> toCheck = toCheckCol(r, c, board);
		int matches = matches(toCheck);

		if (matches == length) {
			int maxrow = r + length;
			for (; r < maxrow; r++)
				locations.add(Location.getLocation(r, c));
		}
	}

	/**
	 * Returns the number of elements in the Set that match.
	 * 
	 * @param pieces
	 * @return
	 */
	private int matches(Set<Piece> pieces) {
		Iterator<Piece> iterator = pieces.iterator();
		if (!iterator.hasNext())
			return 0;

		Piece p0 = iterator.next();
		int matches = 1;
		while (iterator.hasNext())
			if (p0.matches(iterator.next()))
				matches++;

		return matches;
	}

}
