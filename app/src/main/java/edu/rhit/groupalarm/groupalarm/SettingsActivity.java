package edu.rhit.groupalarm.groupalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SettingsActivity extends AppCompatActivity {
    private static final int RC_CHOOSE_RINGTONE = 1;
    private User mUser;
    private CheckBox mVibrateCheckBox;
    private TextView mCurrentRingtone;
    private SeekBar mVolumeSeekBar;
    private DatabaseReference mUserRef;
    private StorageReference mStorageRef;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mUser = getIntent().getParcelableExtra(MainActivity.EXTRA_USER);
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getmUid());
        mStorageRef = FirebaseStorage.getInstance().getReference().child(mUser.getmUid());

        mVibrateCheckBox = findViewById(R.id.vibrate_checkBox);
        if (mUser.ismVibrate()) {
            mVibrateCheckBox.setChecked(true);
        } else {
            mVibrateCheckBox.setChecked(false);
        }
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
        String location = mUser.getmRingtoneLocation();
        int i = 0;
        for (i = 0; i < location.length(); i++) {
            if (location.charAt(i) == '/') {
                break;
            }
        }
        mCurrentRingtone.setText(mUser.getmRingtoneLocation().substring(i + 1));

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
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                Ringtone ringtone = RingtoneManager.getRingtone(this, uri);

                final String title = ringtone.getTitle(this);
                UploadTask uploadTask = mStorageRef.child(title).putFile(uri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mUserRef.child("mRingtoneLocation").setValue(mUser.getmUid() + "/" + title);
                        mUser.setmRingtoneLocation(mUser.getmUid() + "/" + title);
                        mCurrentRingtone.setText(title);
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
