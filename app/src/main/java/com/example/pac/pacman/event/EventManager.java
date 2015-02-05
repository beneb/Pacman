package com.example.pac.pacman.event;

import android.os.Handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class EventManager implements IEventManager {
    protected Map<Class<?>, Set<EventListener<?>>> registrations = new HashMap<>();

    @Override
    public <T> void registerObserver(Class<T> event, EventListener<?> listener) {
        Set<EventListener<?>> observers = registrations.get(event);
        if (observers == null) {
            observers = new HashSet<>();
            registrations.put(event, observers);
        }
        observers.add(listener);
    }

    @Override
    public <T> void unregisterObserver(Class<T> event, EventListener<T> listener) {
        final Set<EventListener<?>> observers = registrations.get(event);
        if (observers == null) return;
        observers.remove(listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void fire(Object event) {
        final Set<EventListener<?>> observers = registrations.get(event.getClass());
        if (observers == null) return;

        for (EventListener observer : observers)
            observer.onEvent(event);
    }

    private final Handler _handler = new Handler();

    @Override
    public void fire(final Object event, long delay) {
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fire(event);
            }
        }, delay);
    }

    @Override
    public void unregisterAll() {
        for (Map.Entry<Class<?>, Set<EventListener<?>>> e : registrations.entrySet())
            e.getValue().clear();
        registrations.clear();
    }
}