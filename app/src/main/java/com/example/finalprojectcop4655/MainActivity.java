package com.example.finalprojectcop4655;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.finalprojectcop4655.BuildConfig;
import com.example.finalprojectcop4655.Fragments.Data;
import com.example.finalprojectcop4655.Fragments.MainViewPresenter;
import com.example.finalprojectcop4655.Fragments.RecyclerViewAdapter;
import com.example.finalprojectcop4655.Utility.Common;
import com.example.finalprojectcop4655.Utility.CommonDialogFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CommonDialogFragment.DialogCallback, MainViewPresenter.MainView {

    private int REQUEST_PERMISSIONS_REQUEST_CODE = 10;
    private CommonDialogFragment commonDialogFragment;
    private FragmentManager fm;
    private Location mLastLocation;
    private double mLatitude, mLongitude;

    public RecyclerView mRestaurantRecyclerView;
    private RecyclerViewAdapter mRestaurantAdapter;
    private SearchView mSearchView;
    private Spinner mSpinnerSort, mSpinnerSearch;
    private Button mSearchButton;
    private View mFade;
    private TextView mNo_results;
    private static final String[] Filterby_type = {"Sort By", "Recommended", "Ratings", "Reviews", "Distance"};
    private static final String[] Filterby_categories = {"Search by", "Term", "Location"};
    private FusedLocationProviderClient mFusedLocationClient;
    private MainViewPresenter mMainViewPresenter;
    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRestaurantRecyclerView = findViewById(R.id.restaurant_recylerview);
        mSearchView = findViewById(R.id.searchView);
        mSpinnerSort = findViewById(R.id.spinner_sort);
        mFade = findViewById(R.id.progress_bar);
        mNo_results = findViewById(R.id.no_results);
        mSpinnerSearch = findViewById(R.id.spinner_search);
        mSearchButton = findViewById(R.id.searchButton);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRestaurantRecyclerView.setLayoutManager(mLayoutManager);
        mMainViewPresenter = new MainViewPresenter(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSearchView.setIconified(false);
        initAdapters();
        initListener();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Common.LIST)) {
                mMainViewPresenter.restoreInstance(savedInstanceState);
            } else {
                NoResultVisibility(true);
            }
        }
    }

    private void NoResultVisibility(boolean b) {
        mNo_results.setVisibility(b ? View.VISIBLE : View.GONE);
        mRestaurantRecyclerView.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        mMainViewPresenter.saveState(state);
        super.onSaveInstanceState(state);

    }



    @Override
    protected void onResume() {
        super.onResume();
        mSearchView.clearFocus();
        Common.hideSoftKeyboard(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.
                commonDialogFragment = CommonDialogFragment.newInstance("Enable Location Services", "Please Enable Location Services", this, 2);
                commonDialogFragment.show(fm, "dialog");
            }
        }
    }

    private void initAdapters() {
        ArrayAdapter<String> madapterSort = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, Filterby_type);
        ArrayAdapter<String> madapterSearch = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, Filterby_categories);

        madapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        madapterSearch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerSort.setAdapter(madapterSort);
        mSpinnerSearch.setAdapter(madapterSearch);
    }

    private void initListener() {
        fm = getSupportFragmentManager();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Common.hideSoftKeyboard(MainActivity.this);
                if (!mSearchView.getQuery().toString().isEmpty()) {
                    return mMainViewPresenter.searchSubmit(GetSearchSpinnerItem(), mSearchView.getQuery().toString(), mLatitude, mLongitude, GetSortSpinnerItem());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSpinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mSearchView.setQueryHint("Search by " + Filterby_categories[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideSoftKeyboard(MainActivity.this);
                if (!mSearchView.getQuery().toString().isEmpty()) {
                    mMainViewPresenter.searchSubmitButton(GetSearchSpinnerItem(), mSearchView.getQuery().toString(), mLatitude, mLongitude, GetSortSpinnerItem());
                }
            }
        });

    }

    public String GetSearchSpinnerItem() {
        switch (mSpinnerSearch.getSelectedItemPosition()) {
            case 1:
                return Common.TERM;
            case 2:
                return Common.LOCATION;
            default:
                return Common.TERM;
        }
    }

    public String GetSortSpinnerItem() {
        switch (mSpinnerSort.getSelectedItemPosition()) {
            case 1:
                return Common.BEST_MATCH;
            case 2:
                return Common.RATING;
            case 3:
                return Common.REVIEW_COUNT;
            case 4:
                return Common.DISTANCE;
            default:
                return Common.BEST_MATCH;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale) {
            commonDialogFragment = CommonDialogFragment.newInstance("Enable Location Services", "Please Enable Location Services", this, 1);
            commonDialogFragment.show(fm, "dialog");
        } else {
            startLocationPermissionRequest();
        }
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            mLatitude = mLastLocation.getLatitude();
                            mLongitude = mLastLocation.getLongitude();

                        } else {
                            if(task.getException()!=null) {
                            commonDialogFragment = CommonDialogFragment.newInstance("Error", task.getException().getMessage().toString(), MainActivity.this, 3);
                            commonDialogFragment.show(fm, "dialog");
                        }
                    }
		}
                });

    }

    @Override
    public void onButtonPressed(int type) {
        if (type == 1) {
            startLocationPermissionRequest();
        } else if (type == 2) {
            Intent intent = new Intent();
            intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
        }
    }

    @Override
    public void showProgressBar(boolean b) {
        mFade.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateRecyclerView(ArrayList<Data> dataArrayList) {

        mRestaurantAdapter = new RecyclerViewAdapter(MainActivity.this, dataArrayList);
        mRestaurantRecyclerView.setAdapter(mRestaurantAdapter);
    }

    @Override
    public void noResult() {
        NoResultVisibility(true);
    }

    @Override
    public void hideNoResult() {
        NoResultVisibility(false);
    }


}


