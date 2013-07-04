package com.gamepsychos.util.observer;

/**
 * An {@code Observer} can be updated with a message of changes to an {@link Observable}.
 * @author jcollard
 *
 * @param <E> the message to receive
 */
public interface Observer<E> {

	public void update(E message);
	
}
