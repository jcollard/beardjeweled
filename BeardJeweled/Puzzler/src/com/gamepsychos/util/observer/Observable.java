package com.gamepsychos.util.observer;


/**
 * An {@code Observable} stores a set of {@link Observer}s that it notifies when its
 * internal state changes.
 * @author jcollard
 *
 * @param <E> The message type of this {@link Observable}
 */
public interface Observable<E> {

	public boolean register(Observer<E> observer);
	
}
