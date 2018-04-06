package edu.gatech.cs2340.app.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.AppDatabase;
import edu.gatech.cs2340.app.model.Model;

/**
 * A login screen that offers login via username/password.
 */
public class LoginScreen extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    @Nullable
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private int wrongAttempts;
    private boolean lockedOut = false;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        // Set up the login form.
        mUsernameView = findViewById(R.id.username);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((id == EditorInfo.IME_ACTION_DONE) || (id == EditorInfo.IME_NULL)) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mUsernameCancelButton = findViewById(R.id.cancel_button);
        mUsernameCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        db = AppDatabase.getAppDatabase(getApplicationContext());
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        Editable usernameText = mUsernameView.getText();
        Editable passwordText = mPasswordView.getText();
        String username = usernameText.toString();
        String password = passwordText.toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }
    private void cancel() {
        if (lockedOut) {
            Snackbar waitBar = Snackbar.make(findViewById(R.id.username_login_form),
                    "You are locked out. You cannot leave this screen.",
                    Snackbar.LENGTH_SHORT);
            waitBar.show();
            return;
        }
        Intent welcomeScreen =  new Intent(LoginScreen.this, WelcomeScreen.class);
        startActivity(welcomeScreen);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        Resources resources = getResources();
        int shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private boolean userFound = false;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final int WAIT_TIME = 2;
            try {
                // Simulate network access.
                Thread.sleep(WAIT_TIME * 1000);
            } catch (InterruptedException e) {
                return false;
            }

            Model model = Model.getInstance();
            userFound = model.userExists(mUsername, db);
            return model.checkCredentials(mUsername, mPassword, db);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (lockedOut) {
                Snackbar waitBar = Snackbar.make(findViewById(R.id.username_login_form),
                        "You are locked out.",
                        Snackbar.LENGTH_SHORT);
                waitBar.show();
                return;
            }

            if (success) {
                Intent mainClass =  new Intent(LoginScreen.this, MainActivity.class);
                startActivity(mainClass);
            } else {
                if (userFound) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                } else {
                    mUsernameView.setError("This username is not registered");
                    mUsernameView.requestFocus();
                }
                wrongAttempts++;
                Log.d("I am here", "" + wrongAttempts);
                if(wrongAttempts > 2) {
                    wrongAttempts = 0;
                    lockedOut = true;
                    final int NUM_SECONDS_LOCKED_OUT = 60;
                    final int NOTIFICATION_INTERVAL = 10;
                    CountDownTimer lockoutTimer = new CountDownTimer(
                            NUM_SECONDS_LOCKED_OUT * 1000,
                            NOTIFICATION_INTERVAL * 1000) {
                        int secondsLeft = NUM_SECONDS_LOCKED_OUT;

                        @Override
                        public void onTick(long l) {
                            Snackbar waitBar = Snackbar.make(findViewById(R.id.username_login_form),
                                    "You are locked out for " + secondsLeft + " seconds.",
                                    Snackbar.LENGTH_SHORT);
                            secondsLeft -= 10;
                            waitBar.show();
                        }

                        @Override
                        public void onFinish() {
                            Snackbar waitBar = Snackbar.make(findViewById(R.id.username_login_form),
                                    "You are no longer locked out.",
                                    Snackbar.LENGTH_SHORT);
                            waitBar.show();
                            lockedOut = false;
                        }
                    };
                    lockoutTimer.start();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    @Override
    public void onBackPressed() {
        if(!lockedOut) {
            super.onBackPressed();
        } else {
            Snackbar waitBar = Snackbar.make(findViewById(R.id.username_login_form),
                    "You are locked out. You cannot leave this screen.",
                    Snackbar.LENGTH_SHORT);
            waitBar.show();
        }
    }
}

