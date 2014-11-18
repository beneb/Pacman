package com.example.pac.pacman.event;

public interface EventListener<T> {
    void onEvent(T event);
}
