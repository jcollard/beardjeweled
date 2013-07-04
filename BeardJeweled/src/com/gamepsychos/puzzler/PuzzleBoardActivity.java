package com.gamepsychos.puzzler;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.gamepsychos.puzzler.board.view.BoardView;

public class PuzzleBoardActivity extends Activity {

	private static MediaPlayer player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View board = new BoardView(this);
		setContentView(board);
		
		player = MediaPlayer.create(this, R.raw.percussion_loop);
		
		player.setLooping(true);
		player.start();
		player.setVolume(0.25f, 0.25f);
	}

	@Override
	protected void onStop() {
		super.onStop();
		player.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		player.start();
	}

}
