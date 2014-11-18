package com.example.pac.pacman;

import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.EventManager;

import junit.framework.TestCase;

class EventOne {
}

class EventTwo {
}

class EventHandler {
    public int EventOneCallCount;
    public int EventTwoCallCount;
    public EventListener<EventOne> EventOneHandler = new EventListener<EventOne>() {
        @Override
        public void onEvent(EventOne event) {
            EventOneCallCount++;
        }
    };

    public EventListener<EventTwo> EventTwoHandler = new EventListener<EventTwo>() {
        @Override
        public void onEvent(EventTwo event) {
            EventTwoCallCount++;
        }
    };
}

public class EventManagerTests extends TestCase {

    private EventManager _eventManager;
    private EventHandler _handler;

    @Override
    public void setUp() {
        _eventManager = new EventManager();
        _handler = new EventHandler();
    }

    public void testRegistration() {
        _eventManager.registerObserver(EventOne.class, _handler.EventOneHandler);
        _eventManager.registerObserver(EventTwo.class, _handler.EventTwoHandler);

        _eventManager.fire(new EventOne());

        assertEquals(1, _handler.EventOneCallCount);
        assertEquals(0, _handler.EventTwoCallCount);

        _eventManager.fire(new EventTwo());

        assertEquals(1, _handler.EventOneCallCount);
        assertEquals(1, _handler.EventTwoCallCount);

        _eventManager.unregisterObserver(EventOne.class, _handler.EventOneHandler);
        _eventManager.unregisterObserver(EventTwo.class, _handler.EventTwoHandler);

        _eventManager.fire(new EventOne());

        assertEquals(1, _handler.EventOneCallCount);
        assertEquals(1, _handler.EventTwoCallCount);
    }
}