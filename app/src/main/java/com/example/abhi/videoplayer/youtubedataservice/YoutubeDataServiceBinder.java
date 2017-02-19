package com.example.abhi.videoplayer.youtubedataservice;

import android.os.Binder;

/**
 * Created by abhic on 18-02-2017.
 */

public class YoutubeDataServiceBinder extends Binder {
    private YoutubeDataServiceInputProvider youtubeDataServiceInputProvider ;

    public YoutubeDataServiceBinder(YoutubeDataServiceInputProvider youtubeDataServiceInputProvider)
    {
        this.youtubeDataServiceInputProvider = youtubeDataServiceInputProvider;
    }

    public YoutubeDataServiceInputProvider getYoutubeDataServiceInputProvider() {
        return youtubeDataServiceInputProvider;
    }
}
