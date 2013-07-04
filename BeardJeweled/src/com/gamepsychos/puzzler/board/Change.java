package com.gamepsychos.puzzler.board;

import com.gamepsychos.puzzler.piece.Piece;

public final class Change {

	private final Piece piece;
	private final Location start;
	private final Location end;
	private final boolean destroyed;
	private final boolean created;

	public Change(Piece piece, Location start, Location end, boolean destroyed, boolean created) {
		if (piece == null || start == null || end == null)
			throw new NullPointerException();
		this.piece = piece;
		this.start = start;
		this.end = end;
		this.destroyed = destroyed;
		this.created = created;
	}

	public final Piece getPiece() {
		return piece;
	}

	public final Location getStart() {
		return start;
	}

	public final Location getEnd() {
		return end;
	}

	public final boolean destroyed() {
		return destroyed;
	}
	
	public final boolean created() {
		return created;
	}

}
