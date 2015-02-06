package com.example.pac.pacman.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.example.pac.pacman.R;
import com.example.pac.pacman.event.EnergizerEatenEvent;
import com.example.pac.pacman.event.DotEatenEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.LevelCompleteEvent;

public class SoundHandler {

    private final SoundPool _soundPool;
    private final int _soundExtraPac;
    private int _soundEating;
    private int _soundEatingFast;

    public SoundHandler(Context context) {

        _soundPool = initSoundPool();

        _soundEating = loadSound(context, R.raw.bite_sound);
        _soundEatingFast = loadSound(context, R.raw.bite_sound_fast);
        _soundExtraPac = loadSound(context, R.raw.pacman_extrapac);
    }

    private int loadSound(Context context, int soundId) {
        int streamId = _soundPool.load(context, soundId, 1);
        if (streamId == 0) {
            Log.e("SoundHandler", String.format("Sound ID '%d' could not be loaded", soundId));
        }
        return streamId;
    }

    private static final int MAX_STREAMS = 1; // no mixing

    @SuppressWarnings("deprecation")
    @TargetApi(21)
    private static SoundPool initSoundPool() {

        SoundPool soundPool;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_NOTIFICATION, 0);
        } else {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(MAX_STREAMS);
            AudioAttributes.Builder aab = new AudioAttributes.Builder();
            aab.setUsage(AudioAttributes.USAGE_GAME);
            aab.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            aab.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
            soundPool = builder.build();
        }
        return soundPool;
    }

    private void playSound(int soundID) {
        _soundPool.play(soundID, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public EventListener<DotEatenEvent> PlaySoundForEatingADot = new EventListener<DotEatenEvent>() {
        @Override
        public void onEvent(DotEatenEvent event) {
                playSound(_soundEatingFast);
        }
    };

    public EventListener<EnergizerEatenEvent> PlaySoundForEatingAnEnergizer = new EventListener<EnergizerEatenEvent>() {
        @Override
        public void onEvent(EnergizerEatenEvent event) {
                playSound(_soundEating);
        }
    };

    public EventListener<LevelCompleteEvent> LevelCompleteListener = new EventListener<LevelCompleteEvent>() {
        @Override
        public void onEvent(LevelCompleteEvent event) {
            playSound(_soundExtraPac);
        }
    };
}
