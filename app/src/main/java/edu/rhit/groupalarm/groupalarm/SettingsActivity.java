package edu.rhit.groupalarm.groupalarm;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private User mUser;
    private CheckBox mVibrateCheckBox;
    private TextView mCurrentRingtone;
    private SeekBar mVolumeSeekBar;
    private TextView mCurrentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUser = getIntent().getParcelableExtra(MainActivity.EXTRA_USER);
        mVibrateCheckBox = findViewById(R.id.vibrate_checkBox);
        mVibrateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mUser.setmVibrate(!mUser.ismVibrate());
            }
        });
        mCurrentRingtone = findViewById(R.id.current_ringtone_textView);
        mCurrentRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);

        mVolumeSeekBar = findViewById(R.id.volume_seekBar);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        double progress = currentVolume * 100 / maxVolume;
        mVolumeSeekBar.setProgress((int) progress);
        mVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double volume = maxVolume * progress / 100.0;
                mUser.setmVolume((int) volume);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, (int) volume, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mCurrentLanguage = findViewById(R.id.current_language_textView);
        mCurrentLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
