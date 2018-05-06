package edu.rhit.groupalarm.groupalarm;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {
    private static final int RC_CHOOSE_RINGTONE = 1;
    private User mUser;
    private CheckBox mVibrateCheckBox;
    private TextView mCurrentRingtone;
    private SeekBar mVolumeSeekBar;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUser = getIntent().getParcelableExtra(MainActivity.EXTRA_USER);
        mUserRef= FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getmUid());

        mVibrateCheckBox = findViewById(R.id.vibrate_checkBox);
        mVibrateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mUser.setmVibrate(!mUser.ismVibrate());
                mUserRef.child("mVibrate").setValue(mUser.ismVibrate());
            }
        });

        mCurrentRingtone = findViewById(R.id.current_ringtone_textView);
        mCurrentRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RC_CHOOSE_RINGTONE);
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
                mUserRef.child("mVolume").setValue(volume);
                //TODO volume changes back when leave activity
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_RINGTONE) {
//            Log.d("aaaaaaaaaa",data.getData());
//            mUser.setmRingtone(data.getData());
//            Log.d("aaaaaaaaaa",mUser.getmRingtone().getPath());


            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                mUser.setmRingtoneLocation(uri.toString());
                mUserRef.child("mRingtoneLocation").setValue(uri.toString());
            }
        }
    }
}
