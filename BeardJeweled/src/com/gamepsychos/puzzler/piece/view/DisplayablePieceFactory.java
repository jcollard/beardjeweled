package com.gamepsychos.puzzler.piece.view;

import java.util.Random;

import com.gamepsychos.puzzler.piece.PieceFactory;
import com.gamepsychos.puzzler.piece.view.DisplayablePiece.PieceType;

public class DisplayablePieceFactory implements PieceFactory {

	private final Random gen;
	
	public DisplayablePieceFactory(Random gen){
		this.gen = gen;
	}
	
	@Override
	public DisplayablePiece createPiece() {
		PieceType[] types = PieceType.values();
		int type = gen.nextInt(types.length);
		return new DisplayablePiece(types[type]);
	}

}
