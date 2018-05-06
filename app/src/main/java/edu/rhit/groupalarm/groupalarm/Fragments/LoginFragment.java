package edu.rhit.groupalarm.groupalarm.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.SignInButton;

import edu.rhit.groupalarm.groupalarm.R;

public class LoginFragment extends Fragment {
    private boolean mLoggingIn;
    private SignInButton mGoogleSignIn;
    private OnLoginListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoggingIn = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mGoogleSignIn = rootView.findViewById(R.id.google_sign_in_button);
        mGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithGoogle();
            }
        });
        return rootView;
    }

    private void loginWithGoogle() {
        if (mLoggingIn) {
            return;
        }
        mLoggingIn = true;
        mListener.onGoogleLogin();
    }

    public void onLoginError(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.login_error))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
        mLoggingIn = false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnLoginListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginListener {
        void onGoogleLogin();
    }

}
