package com.example.finalprojectcop4655.Details;

import androidx.annotation.NonNull;

import java.util.List;

import NetworkClient.ApiService;
import NetworkClient.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsViewPresenter {

    private DetailsView view;
    private static ApiService apiService;


    public DetailsViewPresenter(DetailsView detailsView, String id) {
        this.view = detailsView;
        apiService = NetworkClient.getInstance().getClient();
        if (id != null) {
            detailSubmit(id);
        }
    }

    private boolean detailSubmit(String id) {
        view.showProgressBar(true);
        apiService.getDetails(id).enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailsResponse> call, @NonNull Response<DetailsResponse> response) {
                if (response.isSuccessful()) {
                    final DetailsResponse detailsResponse = response.body();
                    if (detailsResponse != null) {
                        view.updateText(detailsResponse.name, detailsResponse.rating, detailsResponse.phone);
                        if (detailsResponse.photos != null && detailsResponse.photos.size() > 0)
                            view.updateRecyclerView(detailsResponse.photos);
                    }
                }
                view.showProgressBar(false);
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                view.showProgressBar(false);
            }
        });
        return true;
    }

    public interface DetailsView {

        void showProgressBar(boolean b);

        void updateRecyclerView(List<String> photos);

        void updateText(String title, String hours, String phone);
    }
}
