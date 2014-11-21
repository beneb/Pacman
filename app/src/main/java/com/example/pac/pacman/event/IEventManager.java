package com.example.pac.pacman.event;

public interface IEventManager {
    <T> void registerObserver(Class<T> event, EventListener<?> listener);

    <T> void unregisterObserver(Class<T> event, EventListener<T> listener);

    @SuppressWarnings("unchecked")
    void fire(Object event);

    void unregisterAll();
}
