package edu.gatech.cs2340.app.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.AppDatabase;
import edu.gatech.cs2340.app.model.Model;

/**
 * A login screen that offers login via username/password.
 */
@SuppressWarnings("CyclicClassDependency")
public class RegistrationScreen extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    @Nullable
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private Spinner adminSpinner;
    private View mProgressView;
    private View mLoginFormView;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        // Set up the login form.
        mUsernameView = findViewById(R.id.email);

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

        adminSpinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"User", "Admin"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adminSpinner.setAdapter(adapter);

        Button mRegistrationButton = findViewById(R.id.reg_button);
        mRegistrationButton.setOnClickListener(new OnClickListener() {
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

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

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
            mAuthTask = new UserLoginTask(username, password, this);
            mAuthTask.execute((Void) null);
        }
    }
    private void cancel() {
        Intent welcomeScreen =  new Intent(RegistrationScreen.this, WelcomeScreen.class);
        startActivity(welcomeScreen);
    }

    private boolean isPasswordValid(CharSequence password) {
        return password.length() > 6;
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
        ViewPropertyAnimator animator = mLoginFormView.animate();
        animator = animator.setDuration(shortAnimTime);
        animator = animator.alpha(show? 0 : 1);
        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        ViewPropertyAnimator animator1 = mProgressView.animate();
        animator1 = animator1.setDuration(shortAnimTime);
        animator1 = animator1.alpha(show? 1 : 0);
        animator1.setListener(new AnimatorListenerAdapter() {
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
    @SuppressWarnings("CyclicClassDependency")
    public static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final WeakReference<RegistrationScreen> activityReference;
        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password, RegistrationScreen context) {
            mUsername = username;
            mPassword = password;
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final RegistrationScreen registrationScreen = activityReference.get();
            final int WAIT_TIME = 2;
            try {
                // Simulate network access.
                Thread.sleep(WAIT_TIME * 1000);
            } catch (InterruptedException e) {
                return false;
            }

            return Model.addUser(mUsername, mPassword,
                    (String) registrationScreen.adminSpinner.getSelectedItem(),
                    registrationScreen.db);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            final RegistrationScreen registrationScreen = activityReference.get();
            registrationScreen.mAuthTask = null;
            registrationScreen.showProgress(false);

            if (success) {
                Intent mainClass =  new Intent(registrationScreen.getApplicationContext(),
                        MainActivity.class);
                registrationScreen.startActivity(mainClass);
            } else {
                registrationScreen.mUsernameView.setError("Username is already registered.");
                registrationScreen.mUsernameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            final RegistrationScreen registrationScreen = activityReference.get();
            registrationScreen.mAuthTask = null;
            registrationScreen.showProgress(false);
        }
    }
}

