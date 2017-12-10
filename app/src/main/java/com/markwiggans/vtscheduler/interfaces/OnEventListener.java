package com.markwiggans.vtscheduler.interfaces;

/**
 * Created by Mark Wiggans on 12/10/2017.
 */

public interface OnEventListener<E> {
    public void onSuccess(E object);
    public void onFailure(Exception e);
}
