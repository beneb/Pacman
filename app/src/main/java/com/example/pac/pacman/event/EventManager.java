package com.example.pac.pacman.event;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class EventManager {
    protected Map<Class<?>, Set<EventListener<?>>> registrations = new HashMap<Class<?>, Set<EventListener<?>>>();

    public <T> void registerObserver(Class<T> event, EventListener<?> listener) {
        Set<EventListener<?>> observers = registrations.get(event);
        if (observers == null) {
            observers = new HashSet<EventListener<?>>();
            registrations.put(event, observers);
        }
        observers.add(listener);
    }

    public <T> void unregisterObserver(Class<T> event, EventListener<T> listener) {
        final Set<EventListener<?>> observers = registrations.get(event);
        if (observers == null) return;
        observers.remove(listener);
    }

    @SuppressWarnings("unchecked")
    public void fire(Object event) {
        final Set<EventListener<?>> observers = registrations.get(event.getClass());
        if (observers == null) return;

        for (EventListener observer : observers)
            observer.onEvent(event);
    }

    public void unregisterAll() {
        for (Map.Entry<Class<?>, Set<EventListener<?>>> e : registrations.entrySet())
            e.getValue().clear();
        registrations.clear();
    }
}