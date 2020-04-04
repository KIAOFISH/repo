package com.teamalpha.bloodpals.presentation.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.UIKeyboardUtil;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class LoginFragment extends DaggerFragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";

    private boolean isInProgress = false;

    private EditText inputEmail;
    private EditText inputPassword;
    private Button buttonLogin;
    private ProgressBar progressSpinner;

    private LoginViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: LoginFragment was created...");

        inputEmail = view.findViewById(R.id.input_email);
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUI();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        inputPassword = view.findViewById(R.id.input_password);
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUI();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        buttonLogin = view.findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this);
        progressSpinner = view.findViewById(R.id.progress_spinner);

        viewModel = ViewModelProviders.of(this, providerFactory).get(LoginViewModel.class);

        updateUI();

        subscribeToastMessage();

        subscribeIsInProgress();
    }

    private void updateUI() {
        if(isInProgress) {
            progressSpinner.setVisibility(View.VISIBLE);
            inputEmail.setEnabled(false);
            inputPassword.setEnabled(false);
            buttonLogin.setEnabled(false);
        }
        else {
            progressSpinner.setVisibility(View.INVISIBLE);
            inputEmail.setEnabled(true);
            inputPassword.setEnabled(true);
            int inputEmailLength = inputEmail.getText().toString().trim().length();
            int inputPasswordLength = inputPassword.getText().toString().trim().length();
            buttonLogin.setEnabled(inputEmailLength > 0 && inputPasswordLength > 0);
        }
    }

    private void subscribeToastMessage() {
        viewModel.observeToastMessage().observe(getViewLifecycleOwner(), new Observer<StringResource>() {
            @Override
            public void onChanged(StringResource stringResource) {
                if(stringResource != null)
                    Toast.makeText(getActivity().getApplicationContext(), stringResource.format(getActivity().getApplicationContext()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void subscribeIsInProgress() {
        viewModel.observeIsInProgress().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInProgress = aBoolean;
                updateUI();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                UIKeyboardUtil.hideKeyboard(this);

                if(validateEmail() && validatePassword())
                    viewModel.login(inputEmail.getText().toString(), inputPassword.getText().toString());

                break;
        }
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();
        inputEmail.setText(email);

        if(email.isEmpty()) {
            inputEmail.setError("Email cannot be empty!");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Invalid email address!");
            return false;
        }

        inputEmail.setError(null);
        return true;
    }

    private boolean validatePassword() {
        String password = inputPassword.getText().toString().trim();
        inputPassword.setText(password);

        if(password.isEmpty()) {
            inputPassword.setError("Password cannot be empty!");
            return false;
        }

        inputPassword.setError(null);
        return true;
    }
}
