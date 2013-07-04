package com.gamepsychos.puzzler.piece;


public class BasicPiece implements Piece {

	private final PieceType type;
	
	public BasicPiece(PieceType type){
		if(type == null)
			throw new NullPointerException();
		this.type = type;
	}
	
	@Override
	public boolean matches(Piece p) {
		return p.getType() == type;
	}

	@Override
	public PieceType getType() {
		return type;
	}


}
