package com.teamalpha.bloodpals.presentation.myprofile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;
import com.teamalpha.bloodpals.presentation.utils.StringResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import okhttp3.ResponseBody;

public class MyQRCodeFragment extends DaggerFragment {

    private static final String TAG = "MyQRCodeFragment";

    private ProgressBar progressSpinner;
    private ImageView qrCodeImageView;
    private Button reloadButton;

    private MyQRCodeViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_qr_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: MyQRCodeFragment was created...");

        progressSpinner = view.findViewById(R.id.progress_spinner);
        qrCodeImageView = view.findViewById(R.id.image_qr_code);
        reloadButton = view.findViewById(R.id.button_reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.createQRCode();
            }
        });

        viewModel = ViewModelProviders.of(this, providerFactory).get(MyQRCodeViewModel.class);

        subscribeToastMessage();

        subscribeQRCode();

        viewModel.createQRCode();
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

    private void subscribeQRCode(){
        viewModel.observeCreateQRCodeResponseBody().removeObservers(getViewLifecycleOwner());
        viewModel.observeCreateQRCodeResponseBody().observe(getViewLifecycleOwner(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                Log.d(TAG, "onChanged: response body changed.");
                progressSpinner.setVisibility(View.VISIBLE);
                if(responseBody != null) {
                    updateImageView(responseBody);
                }
                else {
                    qrCodeImageView.setImageResource(android.R.color.transparent);
                    reloadButton.setVisibility(View.VISIBLE);
                }

                progressSpinner.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void updateImageView(ResponseBody responseBody) {
        try {
            Log.d(TAG, "updateImageView: started");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = responseBody.byteStream();
                out = new FileOutputStream(getActivity().getExternalFilesDir(null) + File.separator + "QRCode.jpg");
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }
            catch (IOException e) {
                Log.d(TAG, "updateImageView: ERROR - "+e.toString());
                qrCodeImageView.setImageResource(android.R.color.transparent);
                reloadButton.setVisibility(View.VISIBLE);
                return;
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            Bitmap bitmap = BitmapFactory.decodeFile(getActivity().getExternalFilesDir(null) + File.separator + "QRCode.jpg");

            qrCodeImageView.setImageBitmap(bitmap);

            Log.d(TAG, "updateImageView: completed");
            reloadButton.setVisibility(View.GONE);

        } catch (IOException e) {
            Log.e(TAG,"updateImageView: ERROR - "+e.toString());
            qrCodeImageView.setImageResource(android.R.color.transparent);
            reloadButton.setVisibility(View.VISIBLE);
        }
    }

}
