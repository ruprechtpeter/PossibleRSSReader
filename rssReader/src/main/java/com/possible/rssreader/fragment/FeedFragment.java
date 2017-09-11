package com.possible.rssreader.fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.possible.rssreader.R;
import com.possible.rssreader.activity.MainActivity;
import com.possible.rssreader.model.RssFeedModel;
import com.possible.rssreader.util.LinkManager;
import com.possible.rssreader.util.RssAdapter;
import com.possible.rssreader.util.RssParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Rupi on 2017. 09. 10..
 */

public class FeedFragment extends Fragment {

    private EditText et_feed;
    private Button btn_feed;
    private TextView tv_feed_title;
    private TextView tv_feed_link;
    private TextView tv_feed_description;
    private SwipeRefreshLayout sw_swipe;
    private RecyclerView rv_feed_list;
    private Button btn_save;
    private Button btn_load;
    private Button btn_delete;
    private Button btn_share;

    private List<RssFeedModel> rssFeedModelList;
    private static final String TAG = MainActivity.class.getName();

    private LinkManager linkManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        linkManager = new LinkManager(getContext());
        init(view);

        setOnClickListeners();

        return view;
    }

    private void init(View view) {
        et_feed = (EditText) view.findViewById(R.id.et_feed);
        btn_feed = (Button) view.findViewById(R.id.btn_feed);
        tv_feed_title = (TextView) view.findViewById(R.id.tv_feed_title);
        tv_feed_link = (TextView) view.findViewById(R.id.tv_feed_link);
        tv_feed_description = (TextView) view.findViewById(R.id.tv_feed_description);
        sw_swipe = (SwipeRefreshLayout) view.findViewById(R.id.sw_swipe);
        rv_feed_list = (RecyclerView) view.findViewById(R.id.rv_feed_list);
        rv_feed_list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_load = (Button) view.findViewById(R.id.btn_load);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);
        btn_share = (Button) view.findViewById(R.id.btn_share);
    }

    private void setOnClickListeners() {
        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFeed();
            }
        });
        sw_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onClickFeed();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkManager.saveLink(et_feed.getText().toString());
            }
        });
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> links = linkManager.loadLink();
                createDialog(links);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkManager.deleteLink(et_feed.getText().toString());
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkManager.shareLink(et_feed.getText().toString());
            }
        });
    }

    private void createDialog(List<String> links) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Select A Link");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(links);

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_feed.setText(arrayAdapter.getItem(which));
            }
        });
        builderSingle.show();
    }

    private void onClickFeed() {
        new RssFeedTask().execute((Void) null);
    }

    private class RssFeedTask extends AsyncTask<Void, Void, Boolean> {
        private String rssUrl;
        private RssParser rssParser;

        @Override
        protected void onPreExecute() {
            sw_swipe.setRefreshing(true);
            rssParser = new RssParser();
            tv_feed_title.setText("");
            tv_feed_link.setText("");
            tv_feed_description.setText("");
            if (rssFeedModelList != null) {
                rssFeedModelList.clear();
                rv_feed_list.setAdapter(new RssAdapter(rssFeedModelList));
            }
            rssUrl = et_feed.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (TextUtils.isEmpty(rssUrl))
                return false;

            try {
                if(!rssUrl.startsWith("http://") && !rssUrl.startsWith("https://"))
                    rssUrl = "http://" + rssUrl;

                URL url = new URL(rssUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    rssFeedModelList = rssParser.rssParsing(inputStream);
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            sw_swipe.setRefreshing(false);

            if (isSuccess) {
                tv_feed_title.setText(rssParser.rootItem.getTitle());
                tv_feed_link.setText(rssParser.rootItem.getLink());
                tv_feed_description.setText(rssParser.rootItem.getDescription());
                rv_feed_list.setAdapter(new RssAdapter(rssFeedModelList));
            } else {
                Toast.makeText(getContext(),"Enter a valid Rss feed url", Toast.LENGTH_LONG).show();
            }
        }
    }

}



