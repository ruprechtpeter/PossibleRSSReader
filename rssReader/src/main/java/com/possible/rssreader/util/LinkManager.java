package com.possible.rssreader.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.possible.rssreader.helper.RssItemDbHelper;

import java.util.List;

/**
 * Created by Rupi on 2017. 09. 11..
 */

public class LinkManager {
    private Context context;
    private RssItemDbHelper dbHelper;

    public LinkManager(Context context) {
        this.context = context;
        dbHelper = new RssItemDbHelper(context);
    }

    public void saveLink(String link) {
        if (!link.equals("")) {
            long id = dbHelper.saveLink(link);
            if (id != -1) {
                Toast.makeText(context, "Rss link saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public List<String> loadLink() {
        return dbHelper.loadLink();
    }

    public void deleteLink(String link) {
        if (!link.equals("")) {
            int id = dbHelper.deleteLink(link);
            if (id == 0) {
                Toast.makeText(context, "Link not found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Link deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void shareLink(String link) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, link);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
