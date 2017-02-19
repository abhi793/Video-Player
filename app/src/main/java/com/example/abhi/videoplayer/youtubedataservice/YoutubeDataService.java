package com.example.abhi.videoplayer.youtubedataservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.abhi.videoplayer.Constants;
import com.example.abhi.videoplayer.youtubedataservice.models.YoutubeData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by abhic on 18-02-2017.
 */

public class YoutubeDataService extends Service implements YoutubeDataServiceInputProvider {
    private List<YoutubeData> inTheatreList;
    private List<YoutubeData> upcomingList;

    private int responseCount;
    private OnYoutubeDataReceivedListener onYoutubeDataReceivedListener;

    @Override
    public void onCreate() {
        super.onCreate();
        inTheatreList = new ArrayList<>();
        upcomingList = new ArrayList<>();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new YoutubeDataServiceBinder(this);
    }

    private void getInfo(@Constants.ListType int listType) {
        for (String videoId : Constants.getVideoIds(listType)) {
            requestData(videoId);
        }
    }

    @Override
    public void setOnYoutubeDataReceivedListener(OnYoutubeDataReceivedListener onYoutubeDataReceivedListener) {
        this.onYoutubeDataReceivedListener = onYoutubeDataReceivedListener;
    }

    @Override
    public void requestYoutubeData() {
        responseCount = 0;
        getInfo(Constants.IN_THEATRES);
    }

    @Override
    public List<YoutubeData> getIntheatresList() {
        return inTheatreList;
    }

    @Override
    public List<YoutubeData> getUpcomingList() {
        return upcomingList;
    }

    private void requestData(String videoId) {
        ApiCallService apiCallService = callService().create(ApiCallService.class);
        Call<YoutubeData> call = apiCallService.youtubeData(Constants.DEVELOPER_KEY, "snippet", videoId);
        call.enqueue(new Callback<YoutubeData>() {
            @Override
            public void onResponse(Call<YoutubeData> call, Response<YoutubeData> response) {
                if (responseCount < 12) {
                    inTheatreList.add(response.body());
                    if (responseCount == 11) {
                        getInfo(Constants.UPCOMING);
                    }
                } else {
                    upcomingList.add(response.body());
                    if (responseCount == 23) {
                        if (onYoutubeDataReceivedListener != null) {
                            onYoutubeDataReceivedListener.onDataReceived();
                        }
                    }
                }
                responseCount++;
            }

            @Override
            public void onFailure(Call<YoutubeData> call, Throwable t) {

            }
        });
    }

    public interface ApiCallService{
        @GET("youtube/v3/videos")
        Call<YoutubeData> youtubeData(@Query("key") String key, @Query("part") String part, @Query("id") String videoId);
    }

    public static Retrofit callService() {
        return new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
