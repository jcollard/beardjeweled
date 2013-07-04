package com.gamepsychos.puzzler.board;

/**
 * A {@code Location} describes a position on a {@link Board}
 * @author jcollard
 *
 */
public class Location {

	private final int row;
	private final int col;
	
	private Location(int row, int col){
		this.row = row;
		this.col = col;
	}

	/**
	 * Returns {@code true} if {@code a} and {@code b} are adjacent and false otherwise.
	 * @param a the first location to check
	 * @param b the second location to check
	 * @return {@code true} if {@code a} and {@code b} are adjacent and false otherwise.
	 */
	public static boolean adjacent(Location a, Location b){
		int r0 = a.getRow();
		int r1 = b.getRow();
		int c0 = a.getCol();
		int c1 = b.getCol();
		int distance = Math.abs(r0-r1) + Math.abs(c0-c1);
		return distance == 1;
	}
	
	/**
	 * Returns the {@link Location} that is at ({@code row}, {@code col})
	 * @param row the row of the returned location
	 * @param col the column of the returned location
	 * @return the {@link Location} that is at ({@code row}, {@code col})
	 */
	public static Location getLocation(int row, int col){
		return new Location(row, col);
	}
	
	/**
	 * Returns the row of this {@link Location}
	 * @return the row of this {@link Location}
	 */
	public final int getRow(){
		return row;
	}
	
	/**
	 * Returns the column of this {@link Location}
	 * @return the column of this {@link Location}
	 */
	public final int getCol(){
		return col;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
}
