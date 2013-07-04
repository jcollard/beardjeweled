package com.gamepsychos.puzzler.piece.view;

import java.util.HashMap;
import java.util.Map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gamepsychos.puzzler.R;
import com.gamepsychos.puzzler.piece.Piece;
import com.gamepsychos.puzzler.piece.PieceType;

public abstract class PieceResources {
	
	private static final Map<PieceType, Bitmap> pieces;
	private static final Map<PieceType, Bitmap> scaled;
	private static Resources resources;
	private static int size;
	
	static {
		pieces = new HashMap<PieceType, Bitmap>();
		scaled = new HashMap<PieceType, Bitmap>();
	}
	
	/**
	 * Returns a {@link Bitmap} to use when displaying {@link Piece}
	 * @param piece the {@link Piece} to lookup
	 * @return a {@link Bitmap} to use when displaying {@link Piece}
	 */
	public static Bitmap getBitmap(Piece piece){
		if(piece == null)
			throw new NullPointerException();
		PieceType type = piece.getType();
		Bitmap image = scaled.get(type);
		return image;
	}
	
	private static void adjustBitmaps(){
		if(resources == null)
			throw new IllegalStateException("Resources was never set.");
		for(PieceType type : PieceType.values()){
			Bitmap piece = pieces.get(type);
			Bitmap scale = Bitmap.createScaledBitmap(piece, size, size, false);
			scaled.put(type, scale);
		}
	}
	
	public static void setResources(Resources res){
		if(res == null)
			throw new NullPointerException();
		if(resources != null)
			return;
		PieceResources.resources = res;
		
		pieces.put(PieceType.BLUE, BitmapFactory.decodeResource(resources, R.drawable.blue_saphire));
		pieces.put(PieceType.GREEN, BitmapFactory.decodeResource(resources, R.drawable.green_saphire));
		pieces.put(PieceType.RED, BitmapFactory.decodeResource(resources, R.drawable.red_saphire));
		pieces.put(PieceType.YELLOW, BitmapFactory.decodeResource(resources, R.drawable.yellow_saphire));
		pieces.put(PieceType.ORANGE, BitmapFactory.decodeResource(resources, R.drawable.orange_saphire));
		pieces.put(PieceType.PURPLE, BitmapFactory.decodeResource(resources, R.drawable.purple_saphire));
	}
	
	/**
	 * Sets the size for pieces to be displayed at.
	 * @param size
	 */
	public static void setSize(int size){
		if(size < 1)
			throw new IllegalArgumentException();
		PieceResources.size = size;
		adjustBitmaps();
	}
	
	/**
	 * Returns the size pieces are being displayed at
	 * @return
	 */
	public static int getSize(){
		return size;
	}

}
