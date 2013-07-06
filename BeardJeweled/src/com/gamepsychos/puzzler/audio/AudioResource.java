package com.gamepsychos.puzzler.audio;

import java.util.HashMap;
import java.util.Map;

import com.gamepsychos.puzzler.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * {@code AudioResources} is used for playing sound effects and music.
 * @author jcollard
 *
 */
public class AudioResource {
	
	private static final Map<Context, AudioResource> audioLookup;
	
	static {
		audioLookup = new HashMap<Context, AudioResource>();
	}
	
	public static enum Music {
		DRUM_GROOVE(R.raw.drumgroove_loop),
		HIP(R.raw.hip_loop),
		JUNGLE(R.raw.jungle_loop),
		PERCUSSION(R.raw.percussion_loop),
		STRUT(R.raw.strut);
		
		private final int resource_id;
		
		private Music (int resource_id){
			this.resource_id = resource_id;
		}
	}
	
	public static enum SFX {
		WOOSH(R.raw.woosh),
		BOING(R.raw.boing),
		SCRATCH(R.raw.scratch),
		SAD_TROMBONE(R.raw.sad_trombone);
		
		private final int resource_id;
		
		private SFX (int resource_id){
			this.resource_id = resource_id;
		}
	}
	
	private final Context context;
	private MediaPlayer currentBackground;
	
	private float music_volume = 1.0f;
	private float sfx_volume = 1.0f;
	
	/**
	 * Returns an {@code AudioResource} associated with the specified {@link Context}.
	 * @param context the {@link Context} associated with the returned {@code AudioResourcE}
	 * @return an {@code AudioResource} associated with the specified {@link Context}.
	 */
	public static AudioResource getInstance(Context context){
		AudioResource resource = audioLookup.get(context);
		if(resource == null){
			resource = new AudioResource(context);
			audioLookup.put(context, resource);
		}
		return resource;
	}
	
	private AudioResource(Context context){
		this.context = context;
	}
	
	/**
	 * Plays the specified {@link SFX} if there is no other sound effect playing
	 * @param sound the {@link SFX} to play
	 */
	public void play(SFX sound){
		if(sound == null)
			throw new NullPointerException();
		final MediaPlayer sfx = MediaPlayer.create(context, sound.resource_id);
		sfx.setVolume(sfx_volume, sfx_volume);
		sfx.start();
		sfx.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				sfx.release();
			}
		});
	}
	
	public void play(SFX first, SFX ... sound){
		if(sound == null)
			throw new NullPointerException();
		
		final MediaPlayer sfx = MediaPlayer.create(context, first.resource_id);
		sfx.setVolume(sfx_volume, sfx_volume);
		for(SFX s : sound){
			final MediaPlayer next = MediaPlayer.create(context, s.resource_id);
			next.setVolume(sfx_volume, sfx_volume);
			sfx.setNextMediaPlayer(next);
			next.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					next.release();
				}
			});
		}
		sfx.start();
		sfx.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				sfx.release();
			}
		});
	}
	
	private final void stopBackgroundMusic(){
		assert currentBackground != null;
		currentBackground.stop();
		currentBackground.release();
	}
	
	
	/**
	 * Plays the specified {@link Music}
	 * @param music the {@link Music} to play
	 */
	public final void play(Music music){
		if(music == null)
			throw new NullPointerException();
		if(currentBackground != null)
			stopBackgroundMusic();
		currentBackground = MediaPlayer.create(context, music.resource_id);
		currentBackground.setVolume(music_volume, music_volume);
		currentBackground.setLooping(true);
		currentBackground.start();
	}
	
	public final void setMusicVolume(float music_volume){
		if(music_volume < 0 || music_volume > 1)
			throw new IllegalArgumentException();
		this.music_volume = music_volume;
		if(currentBackground == null)
			return;
		currentBackground.setVolume(music_volume, music_volume);
	}
	
	public final float getMusicVolume(){
		return music_volume;
	}
	
	public final void setSFXVolume(float fx_volume){
		if(fx_volume < 0 || fx_volume > 1)
			throw new IllegalArgumentException();
		this.sfx_volume = fx_volume;
	}
	
	public final float getSFXVolume(){
		return sfx_volume;
	}

	public void pauseMusic() {
		if(currentBackground == null) return;
		currentBackground.pause();
	}

	public void resumeMusic() {
		if(currentBackground == null || currentBackground.isLooping()) return;
		currentBackground.start();
	}

}
