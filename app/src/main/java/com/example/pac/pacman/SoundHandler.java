package com.example.pac.pacman;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.EventManager;

public class SoundHandler {

    private final EventManager _eventManager;
    private final MediaPlayer _mediaPlayer;

    public SoundHandler (Context context, EventManager eventManager) {
        _eventManager = eventManager;
        _eventManager.registerObserver(DotEatenEvent.class, PlaySoundForEatingADot);
        _mediaPlayer = MediaPlayer.create(context, R.raw.bite_cookie);
    }

    public EventListener<DotEatenEvent> PlaySoundForEatingADot = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
            _mediaPlayer.start();
        }
    };
}
