package com.gamepsychos.puzzler.board.controller;

import com.gamepsychos.puzzler.board.Board;

/**
 * A {@code BoardController} is used to handle input events on some underlying {@link Board}.
 * @author jcollard
 *
 */
public interface BoardController {

	/**
	 * This event is fired when the user touches the screen at coordinates left, top
	 * @param left the distance from the left of the board
	 * @param top the distance from the top of the board
	 * @return {@code true} if the touch caused the state of the underlying {@link Board} and {@code false} otherwise.
	 */
	public boolean onTouch(float left, float top);
	
	/**
	 * This event is fired when the user releases the screen at coordinates left, top
	 * @param left the distance from the left of the board
	 * @param top the distance from the top of the board
	 * @return {@code true} if the release caused the state of the underlying {@link Board} and {@code false} otherwise.
	 */
	public boolean onRelease(float left, float top);
	
	
	/**
	 * This event is fired when the user moves across the screen to coordinates left, top.
	 * @param left the distance from the left of the board
	 * @param top the distance from the top of the board
	 * @return {@code true} if the move caused the state of the underlying {@link Board} and {@code false} otherwise.
	 */
	public boolean onMove(float left, float top);
	
}
