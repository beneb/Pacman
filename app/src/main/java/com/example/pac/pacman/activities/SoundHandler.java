package com.example.pac.pacman.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.pac.pacman.R;
import com.example.pac.pacman.event.EnergizerEatenEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.LevelCompleteEvent;

public class SoundHandler {

    private final SoundPool _soundPool;
    private final int _soundExtraPac;
    private int _soundIDForEating;
    private int _soundIDForEatingFast;

    @SuppressWarnings("deprecation")
    @TargetApi(21)
    public SoundHandler(Context context) {

        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            _soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        } else {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(8);
            AudioAttributes.Builder aab = new AudioAttributes.Builder();
            aab.setUsage(AudioAttributes.USAGE_GAME);
            aab.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            aab.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
            _soundPool = builder.build();
        }
        _soundIDForEating = _soundPool.load(context, R.raw.bite_sound, 0);
        _soundIDForEatingFast = _soundPool.load(context, R.raw.bite_sound_fast, 0);
        _soundExtraPac = _soundPool.load(context, R.raw.pacman_extrapac, 0);
    }

    public EventListener<DotEatenEvent> PlaySoundForEatingADot = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
            if (_soundIDForEatingFast != 0) {
                _soundPool.play(_soundIDForEatingFast, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    };

    public EventListener<EnergizerEatenEvent> PlaySoundForEatingAnEnergizer = new EventListener<EnergizerEatenEvent>() {
        @Override
        public void onEvent(EnergizerEatenEvent event) {
            if (_soundIDForEating != 0) {
                _soundPool.play(_soundIDForEating, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    };


    public EventListener<LevelCompleteEvent> LevelCompleteListener = new EventListener<LevelCompleteEvent>() {
        @Override
        public void onEvent(LevelCompleteEvent event) {
            if (_soundExtraPac != 0) {
                _soundPool.play(_soundExtraPac, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    };
}
