package com.teamalpha.bloodpals.presentation.userprofile;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.zxing.Result;
import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;
import com.teamalpha.bloodpals.presentation.utils.StringResource;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRCodeFragment extends DaggerFragment implements ZXingScannerView.ResultHandler {

    private static final String TAG = "ScanQRCodeFragment";

    private NavController navController;

    private ZXingScannerView scannerView;

    private ScanQRCodeViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_scan_qr_code, container, false);
        scannerView = new ZXingScannerView(getActivity());
        return scannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ScanQRCodeFragment was created...");

        viewModel = ViewModelProviders.of(this, providerFactory).get(ScanQRCodeViewModel.class);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        subscribeToastMessage();
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

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.d(TAG, "handleResult: " + "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString());

        if(viewModel.validateScanResult(rawResult.getText(), getCurrentDestinationId())) {
            Bundle bundle = new Bundle();
            bundle.putString("userId", rawResult.getText().trim());
            bundle.putBoolean("isGivePoints", getCurrentDestinationId() == R.id.nav_give_user_points);
            bundle.putBoolean("isDeductPoints", getCurrentDestinationId() == R.id.nav_deduct_user_points);
            navigate(getNavigateActionId(), bundle, null);
        }

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scannerView.resumeCameraPreview(ScanQRCodeFragment.this);
            }
        }, 2000);
    }

    private int getNavigateActionId() {
        if(getCurrentDestinationId() == R.id.nav_give_user_points)
            return R.id.action_nav_give_user_points_to_nav_user_profile;
        return R.id.action_nav_deduct_user_points_to_nav_user_profile;
    }

    private int getCurrentDestinationId() {
        return navController.getCurrentDestination().getId();
    }

    private void navigate(int resId, @Nullable Bundle bundle, @Nullable NavOptions navOptions) {
        navController.navigate(resId, bundle, navOptions);
    }
}
