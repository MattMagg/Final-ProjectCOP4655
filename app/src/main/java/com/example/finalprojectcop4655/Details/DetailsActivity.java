package com.example.finalprojectcop4655.Details;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectcop4655.R;
import com.example.finalprojectcop4655.Utility.Common;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements DetailsViewPresenter.DetailsView {
    String ID = Common.EMPTY_VALUE;
    public DetailsViewPresenter mDetailsViewPresenter;
    private RecyclerView mDetailsReyclerView;
    private TextView mTitle, mPhone, mHours;
    private View mFade;
    private DetailsViewAdapter mDetailsViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        mTitle = findViewById(R.id.title);
        mPhone = findViewById(R.id.phone_text);
        mHours = findViewById(R.id.hours_text);
        mFade = findViewById(R.id.progress_bar);
        mDetailsReyclerView = findViewById(R.id.restaurant_details_recylerview);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mDetailsReyclerView.setLayoutManager(layoutManager);
        mDetailsViewPresenter = new DetailsViewPresenter(this, getIntent().getStringExtra(Common.RESTURANT_ID));
    }


    @Override
    public void showProgressBar(boolean b) {
        mFade.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateRecyclerView(List<String> photos) {
        mDetailsViewAdapter = new DetailsViewAdapter(this, photos);
        mDetailsReyclerView.setAdapter(mDetailsViewAdapter);
    }

    @Override
    public void updateText(String title, String hours, String phone) {
        if (hours != null) {
            mHours.setText(hours);
        }
        if (phone != null) {
            mPhone.setText(phone);
        }
        if(title != null){
            mTitle.setText(title);
        }
    }
}
