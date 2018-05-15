package edu.rhit.groupalarm.groupalarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AlarmRingActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private Alarm mAlarm;
    private DatabaseReference mRef;
    private DatabaseReference mAlarmRef;
    private DatabaseReference mUserRef;
    private User mUser;
    private Window wind;
    private MediaPlayer mMediaPlayer;
    private Vibrator mVibrator;
    private GestureDetectorCompat mDetector;
    private StorageReference mRingtoneStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        mAlarm = getIntent().getParcelableExtra(MainActivity.ALARM);
        mRef = FirebaseDatabase.getInstance().getReference();
        mUserRef = mRef.child("users").child(mAlarm.getOwnerId());
        mAlarmRef = mRef.child("alarms").child(mAlarm.getmKey());
        mRingtoneStorage = FirebaseStorage.getInstance().getReference();


        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                playMedia();


                mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (mUser.ismVibrate()) {
                    mVibrator.vibrate(3000);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDetector = new GestureDetectorCompat(AlarmRingActivity.this, new MyGestureDetector());

        View swipeView = findViewById(R.id.swipe_stop_textView);
        swipeView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        mAlarm.setmOpen(false);
        mAlarmRef.child("mOpen").setValue(false);
        mAlarmRef.child("mRinging").setValue(true);
    }

    private void playMedia() {
//Log.d("aaaaaaaaaaaa",Environment.getExternalStorageDirectory().getPath());
//        String PATH_TO_FILE = Environment.getExternalStorageDirectory().getPath()+ "青空.mp3";
//        mMediaPlayer = new  MediaPlayer();
//        if (Build.VERSION.SDK_INT >= 21) {
//            AudioAttributes aa = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build();
//            mMediaPlayer.setAudioAttributes(aa);
//        } else {
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
//        }
//
//        try {
//            mMediaPlayer.setDataSource(PATH_TO_FILE);
//            mMediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mMediaPlayer.start();




        mRingtoneStorage.child(mUser.getmRingtoneLocation()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.reset();

                //https://stackoverflow.com/questions/33961439/how-to-play-a-ringtone-using-the-alarm-volume-with-setaudioattributes
                if (Build.VERSION.SDK_INT >= 21) {
                    AudioAttributes aa = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();
                    mMediaPlayer.setAudioAttributes(aa);
                } else {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                }

                try {
                    mMediaPlayer.setDataSource(uri.toString());
                    mMediaPlayer.setOnPreparedListener(AlarmRingActivity.this);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mAlarmRef.child("mRinging").setValue(false);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            finish();
            return true;
        }
    }

}
