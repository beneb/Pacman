package com.example.pac.pacman.activities;
import com.example.pac.pacman.event.IEventManager;

import org.joda.time.DateTime;

public class ActionAfterTimeOut {

    private DateTime _timeToFireEvent;
    private Object _eventToFire;
    private IEventManager _eventManager;

    public ActionAfterTimeOut(DateTime timeToFireEvent, Object eventToFire, IEventManager eventManager) {
        _timeToFireEvent = timeToFireEvent;
        _eventToFire = eventToFire;
        _eventManager = eventManager;
    }

    public boolean FireAndForget() {
        if (_timeToFireEvent.isBeforeNow()) {
            _eventManager.fire(_eventToFire);
            return true;
        } else {
            return false;
        }
    }
}
