package com.gamepsychos.util.observer;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@code BasicObservable} is a simple implementation of {@link Observable} that uses
 * a {@link Set} to maintain its registered {@link Observers}.
 * @author jcollard
 *
 */
public class BasicObservable<E> implements Observable<E> {

	private final Set<Observer<E>> observers;
	
	public BasicObservable(){
		observers = new HashSet<Observer<E>>();
	}
	
	@Override
	public boolean register(Observer<E> observer) {
		if(observer == null)
			throw new NullPointerException();
		return observers.add(observer);
	}
	
	public final void notifyObservers(E message){
		for(Observer<E> observer : observers)
			observer.update(message);
	}
	
}
