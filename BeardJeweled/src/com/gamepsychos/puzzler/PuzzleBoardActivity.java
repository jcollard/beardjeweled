package com.gamepsychos.puzzler;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.gamepsychos.puzzler.audio.AudioResource;
import com.gamepsychos.puzzler.audio.AudioResource.Music;
import com.gamepsychos.puzzler.board.BasicBoard;
import com.gamepsychos.puzzler.board.Board;
import com.gamepsychos.puzzler.board.view.GameLayout;
import com.gamepsychos.puzzler.game.BasicGame;
import com.gamepsychos.puzzler.game.BasicScoreCalculator;
import com.gamepsychos.puzzler.game.Game;
import com.gamepsychos.puzzler.piece.BasicPieceFactory;
import com.gamepsychos.puzzler.piece.PieceFactory;
import com.gamepsychos.puzzler.piece.view.PieceResources;

public class PuzzleBoardActivity extends Activity {

	private AudioResource player;
	private GameLayout layout;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		PieceResources.setResources(getResources());
		this.player = AudioResource.getInstance(this);
		
		Random gen = new Random();
		PieceFactory factory = new BasicPieceFactory(gen);
		Board gameBoard = new BasicBoard(factory);
		Game game = new BasicGame(25, new BasicScoreCalculator(), gameBoard);
		layout = new GameLayout(this, game);
		setContentView(layout);
				
		
		player.play(Music.STRUT);
		player.setMusicVolume(0.33f);
	}

	@Override
	protected void onStop() {
		super.onStop();
		player.pauseMusic();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		player.resumeMusic();
	}

}
