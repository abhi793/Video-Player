package com.example.abhi.videoplayer.youtubedataservice;

import com.example.abhi.videoplayer.youtubedataservice.models.YoutubeData;

import java.util.List;

/**
 * Created by abhic on 18-02-2017.
 */

public interface YoutubeDataServiceInputProvider {
    void setOnYoutubeDataReceivedListener(OnYoutubeDataReceivedListener onYoutubeDataReceivedListener);
    void requestYoutubeData();
    List<YoutubeData> getIntheatresList();
    List<YoutubeData> getUpcomingList();
}
