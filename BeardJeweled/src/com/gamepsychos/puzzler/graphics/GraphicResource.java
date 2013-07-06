package com.gamepsychos.puzzler.graphics;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gamepsychos.puzzler.R;

public class GraphicResource {

	public static enum Jewel {
		BLUE(R.drawable.blue_saphire),
		RED(R.drawable.red_saphire),
		GREEN(R.drawable.green_saphire),
		ORANGE(R.drawable.orange_saphire),
		PURPLE(R.drawable.purple_saphire),
		YELLOW(R.drawable.yellow_saphire);
		
		private final int resource_id;
		private Bitmap image;
		
		private Jewel(int resource_id){
			this.resource_id = resource_id;
		}
	}
	
	private static final Map<Context, GraphicResource> resources;
	
	static {
		resources = new HashMap<Context, GraphicResource>();
	}
	
	public static GraphicResource getResource(Context context){
		if(context == null)
			throw new NullPointerException();
		GraphicResource resource = resources.get(context);
		if(resource == null){
			resource = new GraphicResource(context);
			resources.put(context, resource);
		}
		return resource;
	}
	
	private final Context context;
	
	private GraphicResource(Context context){
		assert context != null;
		this.context = context;
	}
	
	public Bitmap getJewel(Jewel jewel){
		if(jewel.image == null)
			jewel.image = BitmapFactory.decodeResource(context.getResources(), jewel.resource_id);
		return jewel.image;
	}
	
	public static enum Icon {
		MOVES(R.drawable.moves),
		JEWELS(R.drawable.collected),
		SCORE(R.drawable.score);
		
		private final int resource_id;
		private Bitmap image;
		
		private Icon(int resource_id){
			this.resource_id = resource_id;
		}
	}
	
	public Bitmap getIcon(Icon icon){
		if(icon.image == null)
			icon.image = BitmapFactory.decodeResource(context.getResources(), icon.resource_id);
		return icon.image;
	}
	
	public static enum Border {
		GREY_DASHED(R.drawable.border),
		HORIZONTAL_BAR(R.drawable.horizontal_bar);
		
		private final int resource_id;
		private Bitmap image;
		
		private Border(int resource_id){
			this.resource_id = resource_id;
		}
	}
	
	public Bitmap getBoard(Border border){
		if(border.image == null)
			border.image = BitmapFactory.decodeResource(context.getResources(), border.resource_id);
		return border.image;
	}
	
}
