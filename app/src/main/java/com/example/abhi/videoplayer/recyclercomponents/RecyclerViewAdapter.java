package com.example.abhi.videoplayer.recyclercomponents;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhi.videoplayer.Constants;
import com.example.abhi.videoplayer.R;
import com.example.abhi.videoplayer.uicomponents.LoaderView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

/**
 * Created by abhic on 16-02-2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final int UNINITIALIZED = 1;
    private final int INITIALIZING = 2;
    private final int INITIALIZED = 3;

    private List<String> list;

    public RecyclerViewAdapter() {
        list = Constants.getVideoIds();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        String videoId = list.get(position);

        //holder.textView.setText(videoId);
        holder.youTubeThumbnailView.setTag(R.id.videoid, videoId);

        int state = (int) holder.youTubeThumbnailView.getTag(R.id.initialize);

        if (state == UNINITIALIZED){
            holder.initialize();
        } else if (state == INITIALIZED){
            YouTubeThumbnailLoader loader = (YouTubeThumbnailLoader) holder.youTubeThumbnailView.getTag(R.id.thumbnailloader);
            loader.setVideo(videoId);

            Log.e("dfhbfkdjnvlj", "Loading " + position);
            holder.youTubeThumbnailView.setImageResource(0);
            holder.loaderView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        YouTubeThumbnailView youTubeThumbnailView;
        TextView textView;
        LoaderView loaderView;

        ViewHolder(View itemView) {
            super(itemView);

            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.thumbnail);
            textView = (TextView) itemView.findViewById(R.id.text);
            loaderView = (LoaderView) itemView.findViewById(R.id.loader_view);

            initialize();
        }

        public void initialize(){
            youTubeThumbnailView.setTag(R.id.initialize, INITIALIZING);
            youTubeThumbnailView.setTag(R.id.thumbnailloader, null);
            youTubeThumbnailView.setTag(R.id.videoid, "");

            youTubeThumbnailView.initialize(Constants.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailView.setTag(R.id.initialize, INITIALIZED);
                    youTubeThumbnailView.setTag(R.id.thumbnailloader, youTubeThumbnailLoader);

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String loadedVideoId) {
                            Log.e("dfhbfkdjnvlj", "Loaded " + Constants.getVideoIds().indexOf(loadedVideoId));
                            loaderView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                        }
                    });

                    String videoId = (String) youTubeThumbnailView.getTag(R.id.videoid);
                    if(videoId != null && !videoId.isEmpty()){
                        youTubeThumbnailLoader.setVideo(videoId);
                        Log.e("dfhbfkdjnvlj", "Loading " + Constants.getVideoIds().indexOf(videoId));
                        youTubeThumbnailView.setImageResource(0);
                        loaderView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    youTubeThumbnailView.setTag(R.id.initialize, UNINITIALIZED);
                }
            });
        }
    }
}
