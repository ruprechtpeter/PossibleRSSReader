package com.possible.rssreader.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.possible.rssreader.R;
import com.possible.rssreader.model.RssFeedModel;

import java.util.List;

/**
 * Created by Rupi on 2017. 09. 10..
 */

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.RssFeedModelViewHolder> {

    private List<RssFeedModel> rssFeedModelList;

    public RssAdapter(List<RssFeedModel> rssFeedModelList) {
        this.rssFeedModelList = rssFeedModelList;
    }

    @Override
    public RssFeedModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_feed_item, parent, false);

        return new RssFeedModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RssFeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = rssFeedModelList.get(position);
        ((TextView)holder.view.findViewById(R.id.tv_title)).setText(rssFeedModel.getTitle());
        ((TextView)holder.view.findViewById(R.id.tv_link)).setText(rssFeedModel.getLink());
        ((TextView)holder.view.findViewById(R.id.tv_description)).setText(rssFeedModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return rssFeedModelList.size();
    }

    static class RssFeedModelViewHolder extends RecyclerView.ViewHolder {
        private View view;

        RssFeedModelViewHolder(View v) {
            super(v);

            this.view = v;
        }
    }
}
