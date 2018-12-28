package com.mobiotic.videoplay.demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobiotic.videoplay.demo.R;
import com.mobiotic.videoplay.demo.main_activity.RecyclerItemClickListener;
import com.mobiotic.videoplay.demo.model.VideosListModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.EmployeeViewHolder> {

    private List<VideosListModel> dataList;
    private RecyclerItemClickListener recyclerItemClickListener;
    Context context;
    public VideoListAdapter(Context contxt, List<VideosListModel> dataList , RecyclerItemClickListener recyclerItemClickListener) {
        this.dataList = dataList;
        this.recyclerItemClickListener = recyclerItemClickListener;
        this.context = contxt;
    }


    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_view_row, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtVideoTitle.setText(dataList.get(position).getTitle());
        holder.txtVideoDesc.setText(dataList.get(position).getDescription());
//        holder.txtNoticeFilePath.setText(dataList.get(position).getUrl());
        Picasso.get().load(dataList.get(position).getThumb()).into(holder.videoThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItemClickListener.onItemClick(dataList.get(position),dataList);
//                Intent videoIntent = new Intent(context,VideoPlayActivity.class);
//                videoIntent.putExtra("VIDEO_URL",dataList.get(position).getUrl());
//                videoIntent.putExtra("RELATED_VIDEOS", (Serializable) dataList);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView txtVideoTitle, txtVideoDesc, txtNoticeFilePath;
        ImageView videoThumbnail;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            txtVideoTitle =  itemView.findViewById(R.id.txt_video_title);
            txtVideoDesc =  itemView.findViewById(R.id.txt_notice_brief);
            videoThumbnail = itemView.findViewById(R.id.video_thumbnail);
//            txtNoticeFilePath =  itemView.findViewById(R.id.txt_notice_file_path);

        }
    }
}