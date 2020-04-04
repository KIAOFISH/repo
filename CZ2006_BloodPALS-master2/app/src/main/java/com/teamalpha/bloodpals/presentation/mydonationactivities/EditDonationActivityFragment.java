package com.teamalpha.bloodpals.presentation.mydonationactivities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.user.DonationActivity;
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

public class EditDonationActivityFragment extends DaggerFragment {

    private static final String TAG = "EditDonationActivityFragment";

    private boolean isInProgress = false;

    private EditText inputDonatedDate, inputDonatedTime, inputLocation, inputDonatedAmount;
    private ImageButton buttonDatePicker, buttonTimePicker;
    private Button buttonUpdate;
    private ProgressBar progressSpinner;

    private NavController navController;

    private EditDonationActivityViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_edit_donation_activity, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_donation_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Confirmation");
                builder.setMessage("You are about delete this donation activity. Do you really want to proceed?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteDonationActivity();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: EditDonationActivityFragment was created...");

        inputDonatedDate = view.findViewById(R.id.input_donated_date);
        inputDonatedTime = view.findViewById(R.id.input_donated_time);
        inputLocation = view.findViewById(R.id.input_location);
        inputLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUI();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        inputDonatedAmount = view.findViewById(R.id.input_donated_amount);

        buttonDatePicker = view.findViewById(R.id.button_date_picker);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDatePickerButton();
            }
        });
        buttonTimePicker = view.findViewById(R.id.button_time_picker);
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimePickerButton();
            }
        });
        buttonUpdate = view.findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdateButton();
            }
        });

        progressSpinner = view.findViewById(R.id.progress_spinner);

        viewModel = ViewModelProviders.of(this, providerFactory).get(EditDonationActivityViewModel.class);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        updateUI();

        subscribeToastMessage();

        subscribeIsInProgress();

        subscribeIsUpdated();

        subscribeDonationActivity();

        viewModel.loadDonationActivity(getArguments().getString("donationActivityId",""));
    }

    private void updateUI() {
        if(isInProgress) {
            progressSpinner.setVisibility(View.VISIBLE);
            //inputDonatedDate.setEnabled(false);
            buttonDatePicker.setEnabled(false);
            //inputDonatedTime.setEnabled(false);
            buttonTimePicker.setEnabled(false);
            inputLocation.setEnabled(false);
            inputDonatedAmount.setEnabled(false);
            buttonUpdate.setEnabled(false);
        }
        else {
            progressSpinner.setVisibility(View.GONE);
            //inputDonatedDate.setEnabled(true);
            buttonDatePicker.setEnabled(true);
            //inputDonatedTime.setEnabled(true);
            buttonTimePicker.setEnabled(true);
            inputLocation.setEnabled(true);
            inputDonatedAmount.setEnabled(true);

            int inputDonatedDateLength = inputDonatedDate.getText().toString().trim().length();
            int inputDonatedTimeLength = inputDonatedTime.getText().toString().trim().length();
            int inputLocationLength = inputLocation.getText().toString().trim().length();

            buttonUpdate.setEnabled(inputDonatedDateLength > 0 && inputDonatedTimeLength > 0 && inputLocationLength > 0);
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

    private void subscribeDonationActivity() {
        viewModel.observeDonationActivity().observe(getViewLifecycleOwner(), new Observer<DonationActivity>() {
            @Override
            public void onChanged(DonationActivity donationActivity) {
                if(donationActivity != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                    inputDonatedDate.setText(dateFormat.format(donationActivity.getActivityDateTime()));
                    inputDonatedTime.setText(timeFormat.format(donationActivity.getActivityDateTime()));
                    inputLocation.setText(donationActivity.getLocation());
                    inputDonatedAmount.setText(String.valueOf(donationActivity.getDonatedAmount()));
                }
                else {
                    navController.popBackStack();
                }
            }
        });
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

        final String inputDateText = inputDonatedDate.getText().toString().trim();
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
                inputDonatedDate.setText(formattedDate);
                updateUI();
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    private void handleTimePickerButton() {
        UIKeyboardUtil.hideKeyboard(this);

        int HOUR, MINUTE;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        HOUR = calendar.get(Calendar.HOUR);
        MINUTE = calendar.get(Calendar.MINUTE);

        final String inputDateText = inputDonatedTime.getText().toString().trim();
        if(!inputDateText.isEmpty()) {
            String[] dateArray = inputDateText.split(":");
            HOUR = Integer.parseInt(dateArray[0]);
            MINUTE = Integer.parseInt(dateArray[1]);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String formattedTime = "";
                formattedTime += (hourOfDay <= 9 ? "0" : "") + hourOfDay;
                formattedTime += ":";
                formattedTime += (minute <= 9 ? "0" : "") + minute;
                inputDonatedTime.setText(formattedTime);
                updateUI();
            }
        }, HOUR, MINUTE, true);

        timePickerDialog.show();
    }

    private void handleUpdateButton(){
        boolean validationSuccess = true;
        if(!validateDonatedDate() && validationSuccess) validationSuccess = false;
        if(!validateDonatedTime() && validationSuccess) validationSuccess = false;
        if(!validateLocation() && validationSuccess) validationSuccess = false;
        if(!validateDonatedAmount() && validationSuccess) validationSuccess = false;
        if(validationSuccess) {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            Date donatedDateTime;
            try {
                donatedDateTime = dateTimeFormat.parse(inputDonatedDate.getText() + " " + inputDonatedTime.getText());
            } catch (ParseException e) {
                Log.e(TAG, "handleUpdateButton: ERROR - " + e.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "Format date and time issue!", Toast.LENGTH_LONG).show();
                return;
            }
            String location = inputLocation.getText().toString();
            String donationAmountStr = inputDonatedAmount.getText().toString();
            if(donationAmountStr.isEmpty()) {
                viewModel.updateDonationActivity(donatedDateTime, location, 0);
            }
            else {
                float donationAmount = Float.parseFloat(donationAmountStr);
                viewModel.updateDonationActivity(donatedDateTime, location, donationAmount);
            }
        }
    }

    private boolean validateDonatedDate() {
        String donatedDate = inputDonatedDate.getText().toString().trim();
        inputDonatedDate.setText(donatedDate);

        if(donatedDate.isEmpty()) {
            inputDonatedDate.setError("Date cannot be empty!");
            return false;
        }

        inputDonatedDate.setError(null);
        return true;
    }

    private boolean validateDonatedTime() {
        String donatedTime = inputDonatedTime.getText().toString().trim();
        inputDonatedTime.setText(donatedTime);

        if(donatedTime.isEmpty()) {
            inputDonatedTime.setError("Time cannot be empty!");
            return false;
        }

        inputDonatedTime.setError(null);
        return true;
    }

    private boolean validateLocation() {
        String location = inputLocation.getText().toString().trim();
        inputLocation.setText(location);

        if(location.isEmpty()) {
            inputLocation.setError("Location cannot be empty!");
            return false;
        }

        inputLocation.setError(null);
        return true;
    }

    private boolean validateDonatedAmount() {
        String donatedAmount = inputDonatedAmount.getText().toString().trim();
        inputDonatedAmount.setText(donatedAmount);

        if(!donatedAmount.isEmpty()) {
            try {
                float temp = Float.parseFloat(donatedAmount);
            }
            catch (Exception e) {
                inputDonatedAmount.setError("Donated Amount is invalid!");
                return false;
            }
        }

        inputDonatedAmount.setError(null);
        return true;
    }

}
