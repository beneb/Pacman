package com.example.pac.pacman;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.pac.pacman.event.BigDotEatenEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EventListener;

public class SoundHandler {

    private final MediaPlayer _mediaPlayer;

    public SoundHandler(Context context) {
        _mediaPlayer = MediaPlayer.create(context, R.raw.bite_cookie_fast);
    }

    public EventListener<DotEatenEvent> PlaySoundForEatingADot = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
            if (_mediaPlayer.isPlaying()) {
                _mediaPlayer.stop();
            }
            _mediaPlayer.start();
        }
    };

    public EventListener<BigDotEatenEvent> PlaySoundForEatingABigDot = new EventListener<BigDotEatenEvent>() {
        @Override
        public void onEvent(BigDotEatenEvent event) {
            if (_mediaPlayer.isPlaying()) {
                _mediaPlayer.stop();
            }
            _mediaPlayer.start();
        }
    };
}
