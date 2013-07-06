package com.gamepsychos.puzzler.game.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamepsychos.puzzler.animation.Displayable;
import com.gamepsychos.puzzler.graphics.GraphicResource;
import com.gamepsychos.puzzler.graphics.GraphicResource.Icon;
import com.gamepsychos.puzzler.piece.view.DisplayLocation;

public final class IconView implements Displayable {

	private final ScoreView parent;
	private final Icon icon;
	private final float scale;
	private Bitmap image;
	private String displayString;
	private float scaledHeight;
	
	public IconView(ScoreView parent, float scale){
		this(parent, null, scale);
	}

	public IconView(ScoreView parent, Icon icon, float scale) {
		if (parent == null)
			throw new NullPointerException();
		this.parent = parent;
		this.icon = icon;
		this.scale = scale;
	}
	

	public void changeSize(int width, int height) {
		
		GraphicResource resource = GraphicResource.getResource(parent
				.getContext());
		scaledHeight = height*scale;
		if(icon == null) return;
		Bitmap moves = resource.getIcon(icon);
		float ratio = scaledHeight / moves.getHeight();
		int size = (int) (moves.getWidth() * ratio);
		Bitmap scaled = Bitmap.createScaledBitmap(moves, size, (int)scaledHeight, false);
		
		if (image != null)
			image.recycle();
		image = scaled;
	}
	
	public void setDisplayString(String string){
		this.displayString = string;
	}
	
	public int getHeight(){
		return (int)scaledHeight;
	}
	
	@Override
	public void display(Canvas canvas, Paint paint, DisplayLocation offset) {
		float textSize = paint.getTextSize();
		float center = scaledHeight - ((scaledHeight - textSize)/1.5f) + offset.getTop();
		if(icon == null) {
			canvas.drawText(displayString, offset.getLeft(), center, paint);
			return;
		}
		if(image == null) return;
		canvas.drawBitmap(image, offset.getLeft(), offset.getTop(), paint); 
		canvas.drawText(displayString, image.getWidth()+offset.getLeft()+10, center, paint);
	}

}
