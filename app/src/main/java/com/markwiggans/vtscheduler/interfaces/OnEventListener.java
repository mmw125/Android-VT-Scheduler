package com.markwiggans.vtscheduler.interfaces;

/**
 * Created by Mark Wiggans on 12/10/2017.
 */

public abstract class OnEventListener<E> {
    public abstract void onSuccess(E object);

    public void onFailure(Exception e) {
        onSuccess(null);
    }
}
