package com.gamepsychos.puzzler.board;

import java.util.List;

import com.gamepsychos.puzzler.piece.Piece;

public final class Change {
	
	public static enum ChangeType {
		MOVE,
		CREATE,
		DESTROY;
	}

	private final Piece piece;
	private final List<Location> locations;
	private final ChangeType type;

	public Change(Piece piece, ChangeType type, List<Location> locations) {
		if (piece == null || locations == null || type == null)
			throw new NullPointerException();
		this.piece = piece;
		this.locations = locations;
		this.type = type;
	}

	public final Piece getPiece() {
		return piece;
	}

	public final ChangeType getType(){
		return this.type;
	}

	public final List<Location> getLocations() {
		return locations;
	}

}
