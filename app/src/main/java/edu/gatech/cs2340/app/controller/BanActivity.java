package edu.gatech.cs2340.app.controller;

import android.content.Intent;
import android.os.Bundle;
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

import edu.gatech.cs2340.app.R;
import edu.gatech.cs2340.app.model.AppDatabase;
import edu.gatech.cs2340.app.model.Model;
import edu.gatech.cs2340.app.model.User;

public class BanActivity extends AppCompatActivity {

    private EditText UsernameView;
    private AppDatabase db;

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
                Intent mainScreen = new Intent(BanActivity.this, MainActivity.class);
                startActivity(mainScreen);
            }
        });

        unBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainScreen = new Intent(BanActivity.this, MainActivity.class);
                startActivity(mainScreen);
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
                    banUser();
                    return true;
                }
                return false;
            }
        });
        db = AppDatabase.getAppDatabase(getApplicationContext());
    }

    private void banUser() {

        UsernameView.setError(null);
        Editable usernameText = UsernameView.getText();
        String username = usernameText.toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            UsernameView.setError(getString(R.string.error_field_required));
            focusView = UsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            db.userDao().banUser(username);
        }

    }

}
