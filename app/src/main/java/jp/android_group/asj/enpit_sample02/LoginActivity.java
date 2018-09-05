package jp.android_group.asj.enpit_sample02;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginActivity extends Activity {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
//
//    private String mEmailString;
//    private String mPasswordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        readPassword();

    }

    /**
     * 本来はファイルアクセスは非同期で
     */
    void readPassword() {

        String email = "";
        String password = "";

        File file = new File(getExternalFilesDir(null), "password.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            email = br.readLine();
            password = br.readLine();
            br.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        }

        EditText emailEditText = findViewById(R.id.email);
        emailEditText.setText(email);

        EditText passwordEditText = findViewById(R.id.password);
        passwordEditText.setText(password);

    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean isSave = ((CheckBox) findViewById(R.id.save)).isChecked();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email, password, isSave);
            mAuthTask.execute((Void) null);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final boolean mIsSave;

        UserLoginTask(String email, String password, boolean isSave) {
            mEmail = email;
            mPassword = password;
            mIsSave = isSave;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                //`ファイルにIDとパスワードを保存する
                // Simulate network access.
                Thread.sleep(2000);
                if (mIsSave) savePassword();
            } catch (InterruptedException e) {
                return false;
            }


            // TODO: register the new account here.
            return true;
        }

        /* IDとパスワードをファイルに保存する */
        private void savePassword() {
            File file = new File(getExternalFilesDir(null), "password.txt");
            try {
                PrintWriter pw = new PrintWriter(file);
                pw.println(mEmail);
                pw.println(mPassword);
                pw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            //finish();
            if (success) {
                //画面移動
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

}
