package com.gamepsychos.puzzler.piece;

import java.util.Random;

public class BasicPieceFactory implements PieceFactory {

	private final Random gen;
	
	public BasicPieceFactory(Random gen){
		if(gen == null)
			throw new NullPointerException();
		this.gen = gen;
	}
	
	@Override
	public Piece createPiece() {
		PieceType[] types = PieceType.values();
		int piece = gen.nextInt(types.length);
		return new BasicPiece(types[piece]);
	}

}
