package com.gamepsychos.puzzler.animation;

import com.gamepsychos.puzzler.R;

import android.content.Context;
import android.media.MediaPlayer;

public abstract class AudioResources {
	
	private static MediaPlayer woosh;
	
	public static void initialize(Context context){
		woosh = MediaPlayer.create(context, R.raw.woosh);
	}
	
	public static void playWoosh(){
		if(woosh == null)
			throw new IllegalStateException("Audio Resources never initialized!");
		if(woosh.isPlaying()) return;
		woosh.start();
	}

}
