package com.gamepsychos.puzzler.move;

import com.gamepsychos.puzzler.board.DisplayableBoard;
import com.gamepsychos.puzzler.board.Location;
import com.gamepsychos.puzzler.piece.Piece;

/**
 * A Drop describes the movement of {@link Piece}s that remain on a {@link DisplayableBoard}
 * after pieces have been removed.
 * @author jcollard
 *
 */
public class Drop {
	
	private final Location from;
	private final Location to;
	private final Piece piece;
	
	/**
	 * Returns a {@code Drop} that drops {@code piece} from {@code start} to {@code end}
	 * @param piece the piece to drop
	 * @param start the starting location of this drop
	 * @param end the ending location of this drop
	 * @return a {@code Drop} that starts from {@code start} and ends at {@code end}
	 */
	public Drop(Piece piece, Location start, Location end){
		assert start != null;
		assert end != null;
		this.piece = piece;
		this.from = start;
		this.to = end;
	}
	
	/**
	 * Returns the piece that is dropping.
	 * @return the piece that is dropping.
	 */
	public Piece getPiece(){
		return piece;
	}
	
	/**
	 * Returns the starting location of this {@code Drop}.
	 * @return the starting location of this {@code Drop}.
	 */
	public Location getStart(){
		return from;
	}
	
	/**
	 * Returns the ending location of this {@code Drop}.
	 * @return the ending location of this {@code Drop}.
	 */
	public Location getEnd(){
		return to;
	}

}
