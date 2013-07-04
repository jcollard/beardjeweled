package com.gamepsychos.puzzler.board;

/**
 * {@code Boards} serves as a utility class for the {@link Board} interface.
 * @author jcollard
 *
 */
public abstract class Boards {

	/**
	 * Given a {@link Board} and a {@link Location} returns {@code true} if the
	 * {@link Location} falls on the {@link Board} and {@code false} otherwise.
	 * @param b the {@link Board} to check
	 * @param loc the {@link Locaiton} to check
	 * @return {@code true} if {@code loc} falls on {@code b} and {@code false} otherwise.
	 */
	public static boolean inBounds(Board b, Location loc){
		return loc.getRow() >= 0 && loc.getCol() >= 0 && loc.getRow() < b.getRows() && loc.getCol() < b.getColumns();
	}
	
}
