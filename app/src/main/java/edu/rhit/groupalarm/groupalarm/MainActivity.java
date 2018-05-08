package edu.rhit.groupalarm.groupalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import edu.rhit.groupalarm.groupalarm.Fragments.AlarmFragment;
import edu.rhit.groupalarm.groupalarm.Fragments.LoginFragment;
import edu.rhit.groupalarm.groupalarm.Fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener, GoogleApiClient.OnConnectionFailedListener, MainFragment.OnFragmentInteractionListener, AlarmFragment.LogoutListener {

    public static final String EXTRA_USER = "EXTRA_USER";
    public static final String ALARM = "ALARM";
    public static final String USER = "USER";
    public static final String UID = "UID";
    private static final int RC_GOOGLE_LOG_IN = 1;
    private static User mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private OnCompleteListener<AuthResult> mOnCompleteListener;
    private GoogleApiClient mGoogleApiClient;
    private TabLayout tabs;

    public static User getUserInstance() {
        return mUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs = findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        initializeListeners();
        setupGoogleSignIn();
    }

    private void initializeListeners() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUser = new User(user.getDisplayName(), user.getUid(), MainActivity.this);
                    switchToAlarmFragment(user.getDisplayName(), user.getUid());
                } else {
                    mUser = null;
                    switchToLoginFragment();
                }
            }
        };
        mOnCompleteListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    showLoginError("Authentication failed");
                } else {
                    //TODO show if the user is a new user
                    task.getResult().getAdditionalUserInfo().isNewUser();
                }
            }
        };
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestId()
                .build();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void switchToLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_fragments, new LoginFragment(), "login");
        ft.commit();
        tabs.setVisibility(View.GONE);
    }

    private void switchToAlarmFragment(String username, String uid) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_fragments, MainFragment.newInstance(username, uid));
        ft.commit();
    }

    private void showLoginError(String error) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("Login");
        loginFragment.onLoginError(error);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_GOOGLE_LOG_IN && resultCode == Activity.RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(this.mOnCompleteListener);
            } else {
                showLoginError("Google Authentication failed.");
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("LOGIN", "Connection to Google failed.");
    }

    @Override
    public void onGoogleLogin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_GOOGLE_LOG_IN);
    }

    @Override
    public void OnFragmentCreated(MainFragment fragment) {
        tabs.setVisibility(View.VISIBLE);
        ViewPager viewPager = fragment.getmViewPager();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    @Override
    public void logout() {
        mAuth.signOut();
    }
}
