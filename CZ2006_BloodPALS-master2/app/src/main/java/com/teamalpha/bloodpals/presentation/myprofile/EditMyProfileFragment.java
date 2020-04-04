package com.teamalpha.bloodpals.presentation.myprofile;

import android.app.DatePickerDialog;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.UIKeyboardUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class EditMyProfileFragment extends DaggerFragment {

    private static final String TAG = "EditMyProfileFragment";

    private boolean isInProgress = false;

    private EditText inputName, inputDOB;
    private Spinner inputBloodType;
    private ImageButton buttonDatePicker;
    private Button buttonUpdate;

    private ProgressBar progressSpinner;

    private NavController navController;

    private EditMyProfileViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: EditMyProfileFragment was created...");

        progressSpinner = view.findViewById(R.id.progress_spinner);

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
        inputDOB = view.findViewById(R.id.input_dob);
        inputBloodType = view.findViewById(R.id.input_blood_type);

        buttonDatePicker = view.findViewById(R.id.button_date_picker);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDatePickerButton();
            }
        });
        buttonUpdate = view.findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdateButton();
            }
        });

        viewModel = ViewModelProviders.of(this, providerFactory).get(EditMyProfileViewModel.class);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        initUI();

        updateUI();

        subscribeToastMessage();

        subscribeIsInProgress();

        subscribeUser();

        subscribeIsUpdated();

        viewModel.loadUserData();
    }

    private void initUI() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.input_blood_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputBloodType.setAdapter(adapter);
        inputBloodType.setSelection(0);
    }

    private void updateUI() {
        if(isInProgress) {
            progressSpinner.setVisibility(View.VISIBLE);
            inputName.setEnabled(false);
            buttonDatePicker.setEnabled(false);
            inputBloodType.setEnabled(false);
            buttonUpdate.setEnabled(false);
        }
        else {
            progressSpinner.setVisibility(View.GONE);
            inputName.setEnabled(true);
            buttonDatePicker.setEnabled(true);
            inputBloodType.setEnabled(true);

            int inputNameLength = inputName.getText().toString().trim().length();

            buttonUpdate.setEnabled(inputNameLength > 0);
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

    private void subscribeUser() {
        viewModel.observeUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null) {
                    inputName.setText(user.getName());

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                    dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    if(user.getDateOfBirth() != null) inputDOB.setText(dateFormatter.format(user.getDateOfBirth()));

                    if(user.getBloodType() != null) inputBloodType.setSelection(getBloodTypeArrayPosition(user.getBloodType()));
                }
                else {
                    navController.popBackStack();
                }
            }
        });
    }

    private int getBloodTypeArrayPosition(String bloodType) {
        String[] bloodTypeArray = getResources().getStringArray(R.array.input_blood_types_array);

        for(int i = 0; i < bloodTypeArray.length; i++) {
            if(bloodTypeArray[i].trim().toLowerCase().equals(bloodType.trim().toLowerCase()))
                return i;
        }

        return 0;
    }

    private void subscribeIsUpdated() {
        viewModel.observeIsUpdated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    navController.popBackStack();
                }
            }
        });
    }

    private void handleDatePickerButton() {
        UIKeyboardUtil.hideKeyboard(this);

        int YEAR, MONTH, DATE;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DATE = calendar.get(Calendar.DATE);

        final String inputDateText = inputDOB.getText().toString().trim();
        if(!inputDateText.isEmpty()) {
            String[] dateArray = inputDateText.split("/");
            YEAR = Integer.parseInt(dateArray[2]);
            MONTH = Integer.parseInt(dateArray[1])-1;
            DATE = Integer.parseInt(dateArray[0]);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String formattedDate = "";
                formattedDate += (dayOfMonth <= 9 ? "0" : "") + dayOfMonth;
                formattedDate += "/";
                formattedDate += (month+1 <= 9 ? "0" : "") + (month+1);
                formattedDate += "/";
                formattedDate += year;
                inputDOB.setText(formattedDate);
                updateUI();
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    private void handleUpdateButton() {
        boolean validationSuccess = true;
        if(!validateName() && validationSuccess) validationSuccess = false;
        if(!validateDOB() && validationSuccess) validationSuccess = false;
        if(validationSuccess) {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            Date dob = null;
            try {
                String dobStr = inputDOB.getText().toString();
                if(!dobStr.isEmpty())
                    dob = dateTimeFormat.parse(dobStr);
            } catch (ParseException e) {
                Log.e(TAG, "handleUpdateButton: ERROR - " + e.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "Format date issue!", Toast.LENGTH_LONG).show();
                return;
            }

            String name = inputName.getText().toString();

            String bloodType = null;
            if(inputBloodType.getSelectedItemId() > 0)
                bloodType = inputBloodType.getSelectedItem().toString();

            viewModel.updateUserData(name, dob, bloodType);
        }
    }

    private boolean validateName() {
        String location = inputName.getText().toString().trim();
        inputName.setText(location);

        if(location.isEmpty()) {
            inputName.setError("Name cannot be empty!");
            return false;
        }

        inputName.setError(null);
        return true;
    }

    private boolean validateDOB() {
        String donatedDate = inputDOB.getText().toString().trim();
        inputDOB.setText(donatedDate);

        /*if(donatedDate.isEmpty()) {
            inputDOB.setError("Date cannot be empty!");
            return false;
        }*/

        inputDOB.setError(null);
        return true;
    }

}
