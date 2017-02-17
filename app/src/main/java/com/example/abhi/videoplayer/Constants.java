package com.example.abhi.videoplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhic on 16-02-2017.
 */

public class Constants {
    public static final String DEVELOPER_KEY = "AIzaSyAHQEDvr8FMzgoJgNb6f_jKXzXcbeTW1Zc";

    private static List<String> videoIds = new ArrayList<>();

    public static void setVideoIds() {
        videoIds.clear();

        videoIds.add("3PyrgGTFp0E");
        videoIds.add("cuA-xqBw4jE");
        videoIds.add("QUlr8Am4zQ0");
        videoIds.add("sZSYYiATFTI");
        videoIds.add("jLBrkK-ZnI0");
        videoIds.add("e3Nl_TCQXuw");
        videoIds.add("CaLNiC-bKHQ");
        videoIds.add("B4LC8GOdfSE");
        videoIds.add("jqftOTvYYd8");
        videoIds.add("DekuSxJgpbY");
        videoIds.add("5kIe6UZHSXw");
        videoIds.add("H0VW6sg50Pk");
    }

    public static List<String> getVideoIds() {
        return videoIds;
    }

    private static void clearVideoIdList() {
        videoIds.clear();
    }
}
