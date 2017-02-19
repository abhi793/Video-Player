package com.example.abhi.videoplayer.recyclercomponents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhi.videoplayer.Constants;
import com.example.abhi.videoplayer.R;
import com.example.abhi.videoplayer.uicomponents.LoaderView;
import com.example.abhi.videoplayer.youtubedataservice.models.YoutubeData;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abhic on 16-02-2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<YoutubeData> list;

    public RecyclerViewAdapter(Context context, List<YoutubeData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        if (list != null) {
            holder.textView.setText(list.get(position).getItems().get(0).getSnippet().getTitle());
            Picasso.with(context).load(list.get(position).getItems().get(0).getSnippet().getThumbnails().getHigh().getUrl()).into(holder.imageView);

            holder.loaderView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 12;
        }
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LoaderView loaderView;

        ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            textView = (TextView) itemView.findViewById(R.id.text);
            loaderView = (LoaderView) itemView.findViewById(R.id.loader_view);

            if (list == null) {
                loaderView.setVisibility(View.VISIBLE);
            }
        }
    }
}
