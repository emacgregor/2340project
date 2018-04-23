package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.AppDatabase;
import edu.gatech.cs2340.app.model.Model;
import edu.gatech.cs2340.app.model.User;

import static java.sql.Types.NULL;

public class BanActivity extends AppCompatActivity {

    private EditText UsernameView;
    private AppDatabase db;
    private int whichOne = 0;

    @Nullable
    private UserBanTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UsernameView = findViewById(R.id.username);
        Button banBtn = findViewById(R.id.button19);
        Button unBBtn = findViewById(R.id.button20);
        Button backBtn = findViewById(R.id.button21);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        banBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whichOne = 1;
                attemptBan(whichOne);
            }
        });

        unBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whichOne = 0;
                attemptBan(whichOne);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainScreen = new Intent(BanActivity.this, MainActivity.class);
                startActivity(mainScreen);
            }
        });

        UsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((id == EditorInfo.IME_ACTION_DONE) || (id == EditorInfo.IME_NULL)) {
                    attemptBan(whichOne);
                    return true;
                }
                return false;
            }
        });
        db = AppDatabase.getAppDatabase(getApplicationContext());
    }

    private void attemptBan(int opCode) {
        if (mAuthTask != null) {
            return;
        }

        UsernameView.setError(null);
        Editable usernameText = UsernameView.getText();
        String username = usernameText.toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            UsernameView.setError("This field is required.");
            focusView = UsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserBanTask(username, opCode, db, this);
            mAuthTask.execute((Void) null);
        }
    }

    static class UserBanTask extends AsyncTask<Void, Void, Boolean> {

        private final WeakReference<BanActivity> activityBan;
        private final String bUsername;
        private final int bOpCode;
        private final AppDatabase bDB;

        UserBanTask(String username, int opCode, AppDatabase db, BanActivity context) {
            bUsername = username;
            activityBan = new WeakReference<>(context);
            bOpCode = opCode;
            bDB = db;
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

            if (bOpCode == 1) {
                return Model.banUser(bUsername, bDB);
            } else {
                return Model.unBanUser(bUsername, bDB);
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            final BanActivity banActivity = activityBan.get();
            banActivity.mAuthTask = null;

            if (success) {
                Intent mainClass = new Intent(banActivity.getApplicationContext(),
                        MainActivity.class);
                banActivity.startActivity(mainClass);
            } else {
                banActivity.UsernameView.setError("This user is not banned.");
                banActivity.UsernameView.requestFocus();
            }
        }
    }
}
