package com.example.finalprojectcop4655.Fragments;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import NetworkClient.ApiService;
import NetworkClient.NetworkClient;

import NetworkClient.NetWorkCallback;
import com.example.finalprojectcop4655.Utility.Common;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainViewPresenter {
    int iterator = 0;
    private ArrayList<SearchResponse.Business> businesses;
    private MainView view;
    private static ApiService apiService;
    private ArrayList<Data> data_list;


    public MainViewPresenter(MainView mainView) {
        this.view = mainView;
        apiService = NetworkClient.getInstance().getClient();
    }

    public void searchSubmitButton(final String searchType, String query, double mLatitude, double mLongitude, String sortType) {
        callSearch(searchType,query,mLatitude,mLongitude,sortType);
    }



        public boolean searchSubmit(final String searchType, String query, double mLatitude, double mLongitude, String sortType) {
        callSearch(searchType,query,mLatitude,mLongitude,sortType);
        return true;
    }


    public void callSearch(final String searchType, String query, double mLatitude, double mLongitude, String sortType){
        view.showProgressBar(true);
        HashMap<String, String> map = new HashMap<>();
        map.put(searchType, query);
        map.put(Common.LATITUDE, mLatitude + "");
        map.put(Common.LONGITUDE, mLongitude + "");
        map.put(Common.CATEGORY, Common.RESTAURANTS_ALL);
        map.put(Common.SORT_BY, sortType);
        apiService.getSearch(map).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull final Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                final SearchResponse searchResponse = response.body();
                businesses = new ArrayList<>();
                if (searchResponse != null && searchResponse.businesses.size() > 0) {
                    view.hideNoResult();
                    if (searchResponse.businesses.size() >= 10) {
                        for (int i = 0; i < 10; i++) {
                            businesses.add(searchResponse.businesses.get(i));
                        }
                    } else {
                        businesses.addAll(searchResponse.businesses);
                    }
                    callReviews(new NetWorkCallback() {
                        @Override
                        public void finish() {
                             data_list = new ArrayList<>();
                            if (searchType.equals(Common.LOCATION)) {
                                businesses = sortList(businesses);
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                for (SearchResponse.Business business : businesses) {
                                    if (business.categories.get(0).title != null) {
                                        if (!hashMap.containsKey(business.categories.get(0).title)) {
                                            hashMap.put(business.categories.get(0).title, "1");
                                        } else {
                                            int new_val = Integer.valueOf(Objects.requireNonNull(hashMap.get(business.categories.get(0).title))) + 1;
                                            hashMap.put(business.categories.get(0).title, new_val + "");
                                        }
                                    }
                                }
                                //  Add data to list
                                String header = Common.HEADER;
                                Data data;
                                Data header_data;
                                for (int i = 0; i < businesses.size(); i++) {
                                    if (!header.equals(businesses.get(i).categories.get(0).title)) {
                                        header_data = new Data(Common.HEADER, businesses.get(i).categories.get(0).title, hashMap.get(businesses.get(i).categories.get(0).title));
                                        data_list.add(header_data);
                                        header = businesses.get(i).categories.get(0).title;
                                    }
                                    data = new Data(businesses.get(i).name, Common.CATEGORY, businesses.get(i).id, businesses.get(i).reviews, businesses.get(i).image_url);
                                    data_list.add(data);
                                }


                            } else {
                                for (SearchResponse.Business business : businesses) {
                                    Data data = new Data(business.name, Common.CATEGORY, business.id, business.reviews, business.image_url);
                                    data_list.add(data);
                                }
                            }
                            view.updateRecyclerView(data_list);
                            view.showProgressBar(false);
                        }

                        @Override
                        public void failure() {
                            view.showProgressBar(false);
                        }
                    }, businesses);
                }else{
                    view.noResult();
                    view.showProgressBar(false);
                }


            }


            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                view.showProgressBar(false);
            }
        });
    }

    private void callReviews(final NetWorkCallback callback,
                             final ArrayList<SearchResponse.Business> business) {
        iterator = 0;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (iterator < business.size()) {
                    apiService.getReviews(business.get(iterator).id).enqueue(new Callback<ReviewResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                            if (response.isSuccessful()) {
                                ReviewResponse reviewResponse = response.body();
                                assert reviewResponse != null;
                                if (iterator < 10) {
                                    business.get(iterator).reviews = reviewResponse.reviews.get(0).text;
                                    iterator++;
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                            callback.failure();
                        }
                    });
                    handler.postDelayed(this, 200);
                } else {
                    callback.finish();
                }
            }
        };
        handler.post(runnable);
    }

    //to sort the list on the basis of category
    private ArrayList<SearchResponse.Business> sortList(ArrayList<SearchResponse.Business> businesses) {
        Collections.sort(businesses, new Comparator<SearchResponse.Business>() {
            @Override
            public int compare(SearchResponse.Business o1, SearchResponse.Business o2) {
                return o1.categories.get(0).title.compareToIgnoreCase(o2.categories.get(0).title);
            }
        });
        return businesses;
    }

    public void saveState(Bundle state) {
        if(data_list!=null && data_list.size()>0)
        state.putParcelableArrayList(Common.LIST, data_list);
    }

    public void restoreInstance(Bundle savedInstanceState) {
        data_list = savedInstanceState.getParcelableArrayList(Common.LIST);
        view.updateRecyclerView(data_list);
    }

    public interface MainView {
        void showProgressBar(boolean b);

        void updateRecyclerView(ArrayList<Data> dataArrayList);

        void noResult();

        void hideNoResult();
    }
}
