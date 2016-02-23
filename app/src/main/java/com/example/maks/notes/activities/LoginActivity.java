package com.example.maks.notes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.example.maks.notes.constants.Constants;
import com.example.maks.notes.R;
import com.example.maks.notes.tools.LoadingCallback;
import com.example.maks.notes.tools.Validator;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private SharedPreferences dataSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(createLoginButtonListener());
        makeRegistrationLink();
        makeOfflineLink();
        dataSave = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        emailField = (EditText) findViewById(R.id.et_log_email);
        if(dataSave.contains(Constants.LOGIN)){
            emailField.setText(dataSave.getString(Constants.LOGIN,""));
        }

    }

    private void makeRegistrationLink() {
        SpannableString registrationPrompt = new SpannableString(getString(R.string.register_prompt));

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startRegistrationActivity();
            }
        };

        String linkText = getString(R.string.register_link);
        int linkStartIndex = registrationPrompt.toString().indexOf(linkText);
        int linkEndIndex = linkStartIndex + linkText.length();
        registrationPrompt.setSpan(clickableSpan, linkStartIndex, linkEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView registerPromptView = (TextView) findViewById(R.id.tv_register);
        registerPromptView.setText(registrationPrompt);
        registerPromptView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void startRegistrationActivity() {
        Intent registrationIntent = new Intent(this, RegistrationActivity.class);
        startActivityForResult(registrationIntent, Constants.REGISTER_REQUEST_CODE);
    }

    private void makeOfflineLink() {
        SpannableString offlinePrompt = new SpannableString(getString(R.string.enter_offline));

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startOfflineActivity();
            }
        };

        String linkText = getString(R.string.offline_link);
        int linkStartIndex = offlinePrompt.toString().indexOf(linkText);
        int linkEndIndex = linkStartIndex + linkText.length();
        offlinePrompt.setSpan(clickableSpan, linkStartIndex, linkEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView registerPromptView = (TextView) findViewById(R.id.tv_enter_offline);
        registerPromptView.setText(offlinePrompt);
        registerPromptView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void startOfflineActivity() {
        Intent offlineIntent = new Intent(this, OfflineActivity.class);
        startActivityForResult(offlineIntent, Constants.OFFLINE_REQUEST_CODE);
    }

    private void loginUser(String email, String password, AsyncCallback<BackendlessUser> loginCallback) {
        Backendless.UserService.login(email, password, loginCallback);
    }

    private View.OnClickListener createLoginButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText passwordField = (EditText) findViewById(R.id.et_log_pass);

                CharSequence email = emailField.getText();
                CharSequence password = passwordField.getText();

                if (isLoginValuesValid(email, password)) {
                    LoadingCallback<BackendlessUser> loginCallback = createLoginCallback();

                    loginCallback.showLoading();
                    loginUser(email.toString(), password.toString(), loginCallback);
                }
            }
        };
    }

    private boolean isLoginValuesValid(CharSequence email, CharSequence password) {
        return Validator.isEmailValid(this, email) && Validator.isPasswordValid(this, password);
    }

    private LoadingCallback<BackendlessUser> createLoginCallback() {
        return new LoadingCallback<BackendlessUser>(this, getString(R.string.loading_login)) {
            @Override
            public void handleResponse(BackendlessUser loggedInUser) {
                super.handleResponse(loggedInUser);

                SharedPreferences.Editor editor = dataSave.edit();
                editor.putString(Constants.LOGIN, emailField.getText().toString());
                editor.putString(Constants.USER_ID, loggedInUser.getObjectId());
                editor.apply();

                startNotesActivity();
//                Toast.makeText(LoginActivity.this, String.format(getString(R.string.info_logged_in), loggedInUser.getObjectId()), Toast.LENGTH_LONG).show();
                finish();
            }
        };
    }

    private void startNotesActivity() {
        Intent registrationIntent = new Intent(this, NotesActivity.class);
        startActivity(registrationIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REGISTER_REQUEST_CODE:
                    String email = data.getStringExtra(BackendlessUser.EMAIL_KEY);
                    EditText emailField = (EditText) findViewById(R.id.et_log_email);
                    emailField.setText(email);

                    EditText passwordField = (EditText) findViewById(R.id.et_log_pass);
                    passwordField.requestFocus();

                    Toast.makeText(this, getString(R.string.info_registered_success), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
