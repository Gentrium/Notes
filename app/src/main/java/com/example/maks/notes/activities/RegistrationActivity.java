package com.example.maks.notes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.example.maks.notes.R;
import com.example.maks.notes.tools.LoadingCallback;
import com.example.maks.notes.tools.Validator;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Button register = (Button) findViewById(R.id.btn_register);

        View.OnClickListener registerClick = createClickListener();
        register.setOnClickListener(registerClick);
    }

    private View.OnClickListener createClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etName = (EditText) findViewById(R.id.et_reg_name);
                EditText etEmail = (EditText) findViewById(R.id.et_reg_email);
                EditText etPass = (EditText) findViewById(R.id.et_reg_pass);
                EditText etConfirmPass = (EditText) findViewById(R.id.et_confirm_password);

                CharSequence name = etName.getText();
                CharSequence email = etEmail.getText();
                CharSequence password = etPass.getText();
                CharSequence passwordConfirmation = etConfirmPass.getText();

                if (isRegistrationValuesValid(name, email, password, passwordConfirmation)) {
                    LoadingCallback<BackendlessUser> registrationCallback = createRegistrationCallback();

                    registrationCallback.showLoading();
                    registerUser(name.toString(), email.toString(), password.toString(), registrationCallback);
                }
            }
        };
    }

    private void registerUser(String name, String email, String password,
                              AsyncCallback<BackendlessUser> registrationCallback) {
        BackendlessUser user = new BackendlessUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setProperty("name", name);

        Backendless.UserService.register(user, registrationCallback);
    }

    private LoadingCallback<BackendlessUser> createRegistrationCallback() {
        return new LoadingCallback<BackendlessUser>(this, getString(R.string.loading_register)) {
            @Override
            public void handleResponse(BackendlessUser registeredUser) {
                super.handleResponse(registeredUser);
                Intent registrationResult = new Intent();
                registrationResult.putExtra(BackendlessUser.EMAIL_KEY, registeredUser.getEmail());
                setResult(RESULT_OK, registrationResult);
                RegistrationActivity.this.finish();
            }
        };
    }

    private boolean isRegistrationValuesValid(CharSequence name, CharSequence email, CharSequence password,
                                              CharSequence passwordConfirm) {
        return Validator.isNameValid(this, name)
                && Validator.isEmailValid(this, email)
                && Validator.isPasswordValid(this, password)
                && isPasswordsMatch(password, passwordConfirm);
    }

    private boolean isPasswordsMatch(CharSequence password, CharSequence passwordConfirm) {
        if (!TextUtils.equals(password, passwordConfirm)) {
            Toast.makeText(this, getString(R.string.warning_passwords_do_not_match), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
