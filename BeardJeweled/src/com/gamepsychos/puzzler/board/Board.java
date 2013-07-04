package com.gamepsychos.puzzler.board;

import java.util.Set;

import com.gamepsychos.puzzler.move.Move;
import com.gamepsychos.puzzler.move.MoveFactory;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.util.observer.Observable;

/**
 * A {@link Board} is a grid of locations.
 * @author jcollard
 *
 */
public interface Board extends Observable<Set<Change>> {

	/**
	 * Returns the piece stored at {@code loc}. If the loc is not on the board,
	 * an {@link IllegalLocationException} is thrown.
	 * @param loc the {@link Location} to check.
	 * @return
	 */
	public Piece getPiece(Location loc);
	
	/**
	 * Returns the number of rows on this {@link Board}
	 * @return the number of rows on this {@link Board}
	 */
	public int getRows();
	
	/**
	 * Returns the number of columns on this {@link Board}
	 * @return the number of columns on this {@link Board}
	 */
	public int getColumns();
	
	/**
	 * Returns the {@link MoveFactory} that is capable of creating {@link Move}s for
	 * this {@link Board}
	 * @return the {@link MoveFactory} that is capable of creating {@link Move}s for
	 * this {@link Board}
	 */
	public MoveFactory getMoveFactory();
	
}
