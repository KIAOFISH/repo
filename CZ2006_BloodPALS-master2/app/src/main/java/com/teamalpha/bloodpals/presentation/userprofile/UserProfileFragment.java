package com.teamalpha.bloodpals.presentation.userprofile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;
import com.teamalpha.bloodpals.presentation.utils.StringResource;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class UserProfileFragment extends DaggerFragment {

    private static final String TAG = "UserProfileFragment";

    private boolean isInProgress = false;

    private NavController navController;

    private ProgressBar progressSpinner;

    private TextView displayName, displayEmail, displayPoints;

    private View givePointsDivider;
    private TextView givePointsLabel;
    private Spinner givePointsInput;
    private Button givePointsButton;

    private View deductPointsDivider;
    private TextView deductPointsLabel;
    private EditText deductPointsInput;
    private Button deductPointsButton;

    private UserProfileViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: UserProfileFragment was created...");

        progressSpinner = view.findViewById(R.id.progress_spinner);

        displayName = view.findViewById(R.id.display_name);
        displayEmail = view.findViewById(R.id.display_email);
        displayPoints = view.findViewById(R.id.display_points);

        givePointsDivider = view.findViewById(R.id.divider_give_points);
        givePointsLabel = view.findViewById(R.id.label_give_points);
        givePointsInput = view.findViewById(R.id.input_give_points);
        givePointsButton = view.findViewById(R.id.button_give_points);
        givePointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pointsToGive = Integer.parseInt(givePointsInput.getSelectedItem().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Give Points Confirmation");
                builder.setMessage("You are about give "+pointsToGive+" points to the user. Do you really want to proceed?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.addPoints(pointsToGive);
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

        deductPointsDivider = view.findViewById(R.id.divider_deduct_points);
        deductPointsLabel = view.findViewById(R.id.label_deduct_points);
        deductPointsInput = view.findViewById(R.id.input_deduct_points);
        deductPointsButton = view.findViewById(R.id.button_deduct_points);
        deductPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePointsToDeduct()) {
                    final int pointsToDeduct = Integer.parseInt(deductPointsInput.getText().toString());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Give Points Confirmation");
                    builder.setMessage("You are about deduct "+pointsToDeduct+" points from the user. Do you really want to proceed?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.deductPoints(pointsToDeduct);
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            }
        });

        viewModel = ViewModelProviders.of(this, providerFactory).get(UserProfileViewModel.class);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        subscribeToastMessage();

        subscribeIsInProgress();

        subscribeUser();

        viewModel.loadUserData(getArguments().getString("userId",""));

        initUI();
    }

    private void initUI() {
        if(getArguments().getBoolean("isGivePoints")) {
            givePointsDivider.setVisibility(View.VISIBLE);
            givePointsLabel.setVisibility(View.VISIBLE);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.input_give_points_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            givePointsInput.setAdapter(adapter);
            givePointsInput.setSelection(0);
            givePointsInput.setVisibility(View.VISIBLE);

            givePointsButton.setVisibility(View.VISIBLE);
        }

        if(getArguments().getBoolean("isDeductPoints")) {
            deductPointsDivider.setVisibility(View.VISIBLE);
            deductPointsLabel.setVisibility(View.VISIBLE);
            deductPointsInput.setVisibility(View.VISIBLE);
            deductPointsButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI() {
        if(isInProgress) {
            progressSpinner.setVisibility(View.VISIBLE);
            givePointsInput.setEnabled(false);
            givePointsButton.setEnabled(false);
            deductPointsInput.setEnabled(false);
            deductPointsButton.setEnabled(false);
        }
        else {
            progressSpinner.setVisibility(View.GONE);
            givePointsInput.setEnabled(true);
            givePointsButton.setEnabled(true);
            deductPointsInput.setEnabled(true);
            deductPointsButton.setEnabled(true);
        }
    }

    private boolean validatePointsToDeduct() {
        String pointsToDeductStr = deductPointsInput.getText().toString().trim();

        if(pointsToDeductStr.length() <= 0) {
            deductPointsInput.setError("Points to deduct cannot be empty!!");
            return false;
        }

        int pointsToDeduct = Integer.parseInt(pointsToDeductStr);

        if(pointsToDeduct < 0) {
            deductPointsInput.setError("Points to deduct cannot be negative!");
            return false;
        }
        if(pointsToDeduct == 0) {
            deductPointsInput.setError("Points to deduct cannot be zero!");
            return false;
        }

        deductPointsInput.setError(null);
        return true;
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
                    displayName.setText(user.getName());
                    displayEmail.setText(user.getEmail());
                    displayPoints.setText(String.valueOf(user.getPoints()));
                }
                else {
                    navigateBack();
                }
            }
        });
    }

    private void navigate(int resId, @Nullable Bundle bundle, @Nullable NavOptions navOptions) {
        navController.navigate(resId, bundle, navOptions);
    }

    private void navigateBack() {
        navController.popBackStack();
    }

}
