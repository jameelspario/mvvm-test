package com.example.mvvmtest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.mvvmtest.R;
import com.example.mvvmtest.databinding.ActivityAudioFocusBinding;

import java.io.IOException;

public class AudioFocusActivity extends AppCompatActivity {

    ActivityAudioFocusBinding binding;

    /*
    AUDIOFOCUS_GAIN: if the system grants the audio focus gain, then the playback can be continued after the temporary loss of the audio focus.
    AUDIOFOCUS_LOSS_TRANSIENT: if there is temporary loss of audio focus then the playback of the audio should be paused.
    AUDIOFOCUS_LOSS: if there is permenant loss of the audio then the mediaplayer should be released (completely stopped).
    * */
    /*
    Note: When there is no requirement of audio focus, abandonAudioFocusRequest
    method needs to be called with the AudioManager instance and it requires
    the parameter AudioFocusRequest focusRequest which needs to be passed.

    * */

    // media player instance to playback
    // the media file from the raw folder
    MediaPlayer mediaPlayer;

    // Audio manager instance to manage or
    // handle the audio interruptions
    AudioManager audioManager;

    // Audio attributes instance to set the playback
    // attributes for the media player instance
    // these attributes specify what type of media is
    // to be played and used to callback the audioFocusChangeListener
    AudioAttributes playbackAttributes;

    // media player is handled according to the
    // change in the focus which Android system grants for
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mediaPlayer.release();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_focus);

        // get the audio system service for
        // the audioManger instance
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // initiate the audio playback attributes
        playbackAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        // set the playback attributes for the focus requester
        AudioFocusRequest focusRequest = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(playbackAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(audioFocusChangeListener)
                    .build();


        // request the audio focus and
        // store it in the int variable
        final int audioFocusRequest = audioManager.requestAudioFocus(focusRequest);

        // initiate the media player instance with
        // the media file from the raw folder
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio);

        // handle the PLAY button to play the audio
        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request the audio focus by the Android system
                // if the system grants the permission
                // then start playing the audio file
                if (audioFocusRequest == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer.start();
                }
            }
        });

        // handle the PAUSE button to pause the media player
        binding.pasueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        // handle the STOP button to stop the media player
        binding.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();

                try {
                    // if the mediaplayer is stopped then
                    // it should be again prepared for
                    // next instance of play
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        }
    }


}