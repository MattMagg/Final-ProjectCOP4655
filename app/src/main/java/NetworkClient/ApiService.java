package NetworkClient;

import com.example.finalprojectcop4655.Details.DetailsResponse;
import com.example.finalprojectcop4655.Fragments.ReviewResponse;
import com.example.finalprojectcop4655.Fragments.SearchResponse;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("/v3/businesses/search")
    Call<SearchResponse> getSearch(@QueryMap Map<String,String> map);

    @GET("/v3/businesses/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") String id);

    @GET("/v3/businesses/{id}")
    Call<DetailsResponse> getDetails(@Path("id") String id);
}