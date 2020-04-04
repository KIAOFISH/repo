package com.teamalpha.bloodpals.presentation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.teamalpha.bloodpals.BaseActivity;
import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.session.SessionResource;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.UIKeyboardUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 123;

    private static final int BACK_PRESSED_TIME_INTERVAL = 2000;
    private long backPressedTime;

    private Group progressBarGroup;
    private TextView navHeaderName, navHeaderEmail;

    private MainViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this, providerFactory).get(MainViewModel.class);

        viewModel.subscribeAuthListener();

        initUI();

        subscribeToastMessage();

        subscribeAuthObservers();

        checkCameraPermission();
    }

    private void initUI() {
        drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                UIKeyboardUtil.hideKeyboard(MainActivity.this);
            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        navigationView = findViewById(R.id.nav_view);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_bloodbanks,
                R.id.nav_login,
                R.id.nav_register,
                R.id.nav_my_profile,
                R.id.nav_my_donation_activities,
                R.id.nav_give_user_points,
                R.id.nav_deduct_user_points,
                R.id.nav_reward
        ).setDrawerLayout(drawer).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        progressBarGroup = (Group) findViewById(R.id.progress_bar_group);

        View headerView = navigationView.getHeaderView(0);
        navHeaderName = (TextView) headerView.findViewById(R.id.nav_header_user_name);
        navHeaderEmail = (TextView) headerView.findViewById(R.id.nav_header_user_email);

        navHeaderName.setText("Muhammad");
        navHeaderEmail.setText("dinocaspero@gmail.com");

        updateNavigationDrawerUI("Guest", "");
    }

    private void subscribeToastMessage() {
        viewModel.observeToastMessage().observe(this, new Observer<StringResource>() {
            @Override
            public void onChanged(StringResource stringResource) {
                if(stringResource != null)
                    Toast.makeText(getApplicationContext(), stringResource.format(getApplicationContext()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void subscribeAuthObservers() {
        viewModel.observeSessionUser().observe(this, new Observer<SessionResource<User>>() {
            @Override
            public void onChanged(SessionResource<User> userSessionResource) {
                if(userSessionResource != null){
                    switch (userSessionResource.status){
                        case LOADING:{
                            showProgressBar(true);
                            break;
                        }

                        case AUTHENTICATED:{
                            showProgressBar(false);
                            updateNavigationMenuUI(true, userSessionResource.data);
                            updateNavigationDrawerUI(userSessionResource.data.getName(), userSessionResource.data.getEmail());

                            navController.popBackStack(R.id.nav_home, false);
                            break;
                        }

                        case NOT_AUTHENTICATED:{
                            showProgressBar(false);
                            updateNavigationMenuUI(false, null);
                            updateNavigationDrawerUI("Guest", "Register/Login for more features");

                            navController.popBackStack(R.id.nav_home, false);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void showProgressBar(boolean isVisible){
        if(isVisible) {
            progressBarGroup.setVisibility(View.VISIBLE);
        } else {
            progressBarGroup.setVisibility(View.GONE);
        }
    }

    private void updateNavigationMenuUI(boolean isAuthenticated, @Nullable User user) {
        if(isAuthenticated) {
            navigationView.getMenu().setGroupVisible(R.id.nav_auth_logged_in_group, true);

            if(user.hasRole("PointGiver"))
                navigationView.getMenu().findItem(R.id.nav_give_user_points).setVisible(true);
            else
                navigationView.getMenu().findItem(R.id.nav_give_user_points).setVisible(false);

            if(user.hasRole("Merchant"))
                navigationView.getMenu().findItem(R.id.nav_deduct_user_points).setVisible(true);
            else
                navigationView.getMenu().findItem(R.id.nav_deduct_user_points).setVisible(false);

            navigationView.getMenu().setGroupVisible(R.id.nav_auth_login_group, false);
            navigationView.getMenu().setGroupVisible(R.id.nav_auth_logout_group, true);
        } else {
            navigationView.getMenu().setGroupVisible(R.id.nav_auth_logged_in_group, false);
            navigationView.getMenu().setGroupVisible(R.id.nav_auth_login_group, true);
            navigationView.getMenu().setGroupVisible(R.id.nav_auth_logout_group, false);
        }
    }

    private void updateNavigationDrawerUI(String name, String email) {
        navHeaderName.setText(name);
        navHeaderEmail.setText(email);
    }

    private void checkCameraPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,  Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean handledByNavigationUI = NavigationUI.onNavDestinationSelected(item, navController);

        if (!handledByNavigationUI) {
            switch (item.getItemId()) {
                case R.id.nav_book_appointment_with_singpass:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://donateblood.hsa.gov.sg"));
                    startActivity(browserIntent);
                    break;
                case R.id.nav_logout:
                    viewModel.authLogout();
                    break;
            }
        }
        else {
            switch (item.getItemId()) {
                case R.id.nav_give_user_points:
                case R.id.nav_deduct_user_points:
                    // check if camera permission is granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), R.string.message_camera_permission_required, Toast.LENGTH_LONG).show();
                        return false;
                    }
                    break;
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return handledByNavigationUI;
    }

    @Override
    public void onBackPressed() {
        //Log.d(TAG, "onBackPressed: ");
        if (backPressedTime + BACK_PRESSED_TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Press back again to quit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.unSubscribeAuthListener();
    }

    @Override
    protected void onRestart() {
        viewModel.subscribeAuthListener();
        super.onRestart();
    }

}
