package com.gamepsychos.puzzler.board;

import java.util.Collections;
import java.util.List;

import com.gamepsychos.puzzler.piece.Piece;

/**
 * A {@link Change} represents the change of a {@link Piece} after some {@link Move}
 * @author jcollard
 *
 */
public final class Change {
	
	public static enum ChangeType {
		MOVE,
		CREATE,
		DESTROY,
		REMOVE;
	}

	private final Piece piece;
	private final List<Location> locations;
	private final ChangeType type;

	/**
	 * Creates a {@link Change} specifying the {@link Piece} that was changed, the
	 * type of change, and a {@link List} of {@link Location}s that the piece occupied.
	 * @param piece the {@link Piece} that changed
	 * @param type the {@link ChangeType} for this {@link Change}
	 * @param locations the {@link List} of {@link Location}s that the piece occupied
	 */
	public Change(Piece piece, ChangeType type, List<Location> locations) {
		if (piece == null || locations == null || type == null)
			throw new NullPointerException();
		this.piece = piece;
		this.locations = Collections.unmodifiableList(locations);
		this.type = type;
	}

	public final Piece getPiece() {
		return piece;
	}

	public final ChangeType getType(){
		return this.type;
	}

	/**
	 * Returns an immutable view of the {@link Location}s occupied by this {@link Change}
	 * @return
	 */
	public final List<Location> getLocations() {
		return locations;
	}

}
