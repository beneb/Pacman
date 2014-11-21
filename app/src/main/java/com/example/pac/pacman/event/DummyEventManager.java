package com.example.pac.pacman.event;

public class DummyEventManager implements IEventManager {
    @Override
    public <T> void registerObserver(Class<T> event, EventListener<?> listener) {
    }

    @Override
    public <T> void unregisterObserver(Class<T> event, EventListener<T> listener) {
    }

    @Override
    public void fire(Object event) {
    }

    @Override
    public void unregisterAll() {
    }
}
