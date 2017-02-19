package com.example.abhi.videoplayer;

import android.support.annotation.IntDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhic on 16-02-2017.
 */

public class Constants {
    public static final String DEVELOPER_KEY = "AIzaSyC07_BZrr6aC1A9NtWv33DopINljpsBdjI";

    private static List<String> inTheatreVideoIds = new ArrayList<>();
    private static List<String> upcomingVideoIds = new ArrayList<>();

    @IntDef({IN_THEATRES, UPCOMING})
    public @interface ListType {

    }

    public static final int IN_THEATRES = 0, UPCOMING = 1;

    public static void setUpcomingVideoIds()
    {
        upcomingVideoIds.clear();

        upcomingVideoIds.add("5wfrDhgUMGI");
        upcomingVideoIds.add("ziOLGzKq6oo");
        upcomingVideoIds.add("8q-7trUd8Vw");
        upcomingVideoIds.add("gf0tYL9nlOs");
        upcomingVideoIds.add("S2DwrJ9S3f4");
        upcomingVideoIds.add("ClcQUlXcCKw");
        upcomingVideoIds.add("uQrj2M-2Uiw");
        upcomingVideoIds.add("ykwSo64RYao");
        upcomingVideoIds.add("IqrgxZLd_gE");
        upcomingVideoIds.add("dA7JW9Zj2-g");
        upcomingVideoIds.add("6JnFaltqnAY");
        upcomingVideoIds.add("ksLZEepQ0nA");
    }
    public static void setVideoIds() {
        inTheatreVideoIds.clear();

        inTheatreVideoIds.add("3PyrgGTFp0E");
        inTheatreVideoIds.add("cuA-xqBw4jE");
        inTheatreVideoIds.add("QUlr8Am4zQ0");
        inTheatreVideoIds.add("sZSYYiATFTI");
        inTheatreVideoIds.add("jLBrkK-ZnI0");
        inTheatreVideoIds.add("e3Nl_TCQXuw");
        inTheatreVideoIds.add("CaLNiC-bKHQ");
        inTheatreVideoIds.add("B4LC8GOdfSE");
        inTheatreVideoIds.add("jqftOTvYYd8");
        inTheatreVideoIds.add("DekuSxJgpbY");
        inTheatreVideoIds.add("5kIe6UZHSXw");
        inTheatreVideoIds.add("H0VW6sg50Pk");
    }

    public static List<String> getVideoIds(@ListType int listType) {
        switch (listType) {
            case IN_THEATRES:
                return inTheatreVideoIds;
            case UPCOMING:
                return upcomingVideoIds;
            default:
                return null;
        }
    }
}
