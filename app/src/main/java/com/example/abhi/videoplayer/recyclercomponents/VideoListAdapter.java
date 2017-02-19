package com.example.abhi.videoplayer.recyclercomponents;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abhi.videoplayer.R;
import com.example.abhi.videoplayer.youtubedataservice.models.YoutubeData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abhic on 19-02-2017.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {


    private Context context;
    private List<YoutubeData> list;

    public VideoListAdapter(Context context, List<YoutubeData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(context).load(list.get(position).getItems().get(0).getSnippet().getThumbnails().getHigh().getUrl()).into(holder.imageView);
        holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Hi "+ position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        FloatingActionButton floatingActionButton;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.player_view);
            floatingActionButton = (FloatingActionButton) itemView.findViewById(R.id.play_button);
        }
    }
}
