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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends Activity {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;


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

        readPassword();
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

        //ファイル入力
        byte[] buff = new byte[1024 * 4];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        File file = new File(getExternalFilesDir(null), "password.txt");

        try {
            FileInputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);

            int n = bis.read(buff);
            while (n != -1) {
                out.write(buff, 0, n);
                n = bis.read(buff);
            }
            is.close();

        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }

        //復号化
        byte[] binary = out.toByteArray();

        Des des = new Des();
        des.init();
        byte[] s = des.decrypt(binary);
        if (s == null) return;

        //文字分割
        String text = new String(s);

        int x1 = text.indexOf("HIMITU_PASSWORD=");
        email = text.substring("HIIMITU_LOGINID=".length(), x1);
        password = text.substring("HIMITU_PASSWORD=".length() + x1);

        //Viewへ書き込み
        EditText emailEditText = findViewById(R.id.email);
        emailEditText.setText(email);

        EditText passwordEditText = findViewById(R.id.password);
        passwordEditText.setText(password);

    }

    /* IDとパスワードをファイルに保存する */
    void writePassword(String email, String password) {

        //文字連結して暗号化する
        String text = "HIIMITU_LOGINID=" + email + "HIMITU_PASSWORD=" + password;

        //暗号化
        Des des = new Des();
        des.init();
        byte[] binary = des.encrypt(text.getBytes());

        //ファイル書き込み
        File file = new File(getExternalFilesDir(null), "password.txt");
        try {
            FileOutputStream fs = new FileOutputStream(file);
            fs.write(binary);
            fs.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                if (mIsSave) writePassword(mEmail, mPassword);
            } catch (InterruptedException e) {
                return false;
            }


            // TODO: register the new account here.
            return true;
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
