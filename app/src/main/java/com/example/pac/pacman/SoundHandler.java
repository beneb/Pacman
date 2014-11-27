package com.example.pac.pacman;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.pac.pacman.event.BigDotEatenEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EventListener;

public class SoundHandler {

    private final SoundPool _soundPool;
    private int _soundIDForEatingCookie;
    private int _soundIDForEatingCookieFast;

    public SoundHandler(Context context) {

        // required API level 21
        // AudioAttributes.Builder aab = new AudioAttributes.Builder();
        // aab.setUsage(AudioAttributes.USAGE_GAME);
        // aab.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        // aab.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
        // SoundPool.Builder spb = new SoundPool.Builder();
        // spb.setAudioAttributes(aab.build());
        // spb.setMaxStreams(R.integer.maxSimultaneousStreams);
        // _soundPool = spb.build();

        // SoundPool constructor is deprecated (since API level 21)
        // ... so we with our good "old ham" smartphones still need this constructor
        _soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        _soundIDForEatingCookie = _soundPool.load(context, R.raw.bite_cookie, 0);
        _soundIDForEatingCookieFast = _soundPool.load(context, R.raw.bite_cookie_fast, 0);
    }

    public EventListener<DotEatenEvent> PlaySoundForEatingADot = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
            if (_soundIDForEatingCookieFast != 0) {
                _soundPool.play(_soundIDForEatingCookieFast, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    };

    public EventListener<BigDotEatenEvent> PlaySoundForEatingABigDot = new EventListener<BigDotEatenEvent>() {
        @Override
        public void onEvent(BigDotEatenEvent event) {
            if (_soundIDForEatingCookie != 0) {
                _soundPool.play(_soundIDForEatingCookie, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    };
}
