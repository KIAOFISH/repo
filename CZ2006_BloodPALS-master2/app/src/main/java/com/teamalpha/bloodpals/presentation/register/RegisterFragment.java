package com.teamalpha.bloodpals.presentation.register;

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
import com.teamalpha.bloodpals.presentation.utils.RegexPattern;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.UIKeyboardUtil;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class RegisterFragment extends DaggerFragment implements View.OnClickListener {

    private static final String TAG = "RegisterFragment";

    private boolean isInProgress = false;

    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private Button buttonRegister;
    private ProgressBar progressSpinner;

    private RegisterViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: RegisterFragment was created...");

        inputName = view.findViewById(R.id.input_name);
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUI();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
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
        inputConfirmPassword = view.findViewById(R.id.input_confirm_password);
        inputConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUI();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        buttonRegister = view.findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(this);
        progressSpinner = view.findViewById(R.id.progress_spinner);

        viewModel = ViewModelProviders.of(this, providerFactory).get(RegisterViewModel.class);

        updateUI();

        subscribeToastMessage();

        subscribeIsInProgress();
    }

    private void updateUI() {
        if(isInProgress) {
            progressSpinner.setVisibility(View.VISIBLE);
            inputName.setEnabled(false);
            inputEmail.setEnabled(false);
            inputPassword.setEnabled(false);
            inputConfirmPassword.setEnabled(false);
            buttonRegister.setEnabled(false);

        }
        else {
            progressSpinner.setVisibility(View.INVISIBLE);
            inputName.setEnabled(true);
            inputEmail.setEnabled(true);
            inputPassword.setEnabled(true);
            inputConfirmPassword.setEnabled(true);
            int inputNameLength = inputName.getText().toString().trim().length();
            int inputEmailLength = inputEmail.getText().toString().trim().length();
            int inputPasswordLength = inputPassword.getText().toString().trim().length();
            int inputConfirmPasswordLength = inputConfirmPassword.getText().toString().trim().length();
            buttonRegister.setEnabled(inputNameLength > 0 && inputEmailLength > 0 && inputPasswordLength > 0 && inputConfirmPasswordLength > 0);
        }
    }

    private void subscribeToastMessage() {
        viewModel.observeToastMessage().observe(getViewLifecycleOwner(), new Observer<StringResource>() {
            @Override
            public void onChanged(StringResource stringResource) {
                if(stringResource != null)
                    Toast.makeText(getContext(), stringResource.format(getActivity().getApplicationContext()), Toast.LENGTH_LONG).show();
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
        switch (v.getId()){
            case R.id.button_register:
                UIKeyboardUtil.hideKeyboard(this);

                boolean validationSuccess = true;
                if(!validateName() && validationSuccess) validationSuccess = false;
                if(!validateEmail() && validationSuccess) validationSuccess = false;
                if(!validatePassword() && validationSuccess) validationSuccess = false;
                if(!validateConfirmPassword() && validationSuccess) validationSuccess = false;
                if(validationSuccess)
                    viewModel.register(
                            inputName.getText().toString(),
                            inputEmail.getText().toString(),
                            inputPassword.getText().toString());

                break;
        }
    }

    private boolean validateName() {
        String name = inputName.getText().toString().trim();
        inputName.setText(name);

        if(name.isEmpty()) {
            inputName.setError("Name cannot be empty!");
            return false;
        }
        else if (name.length() < 3) {
            inputName.setError("Name must be at least 3 characters!");
            return false;
        }

        inputName.setError(null);
        return true;
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
        else if (!RegexPattern.PASSWORD_PATTERN.matcher(password).matches()) {
            inputPassword.setError("Password does not meet the requirements!");
            return false;
        }

        inputPassword.setError(null);
        return true;
    }

    private boolean validateConfirmPassword() {
        String confirmPassword = inputConfirmPassword.getText().toString().trim();
        inputConfirmPassword.setText(confirmPassword);

        if(confirmPassword.isEmpty()) {
            inputConfirmPassword.setError("Confirm Password cannot be empty!");
            return false;
        }
        else if (!confirmPassword.equals(inputPassword.getText().toString())) {
            inputConfirmPassword.setError("Confirm Password does not match!");
            return false;
        }

        inputConfirmPassword.setError(null);
        return true;
    }

}
