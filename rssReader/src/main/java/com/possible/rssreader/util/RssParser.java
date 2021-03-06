package com.possible.rssreader.util;

import android.util.Xml;

import com.possible.rssreader.model.RssFeedModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rupi on 2017. 09. 10..
 */

public class RssParser {

    public RssFeedModel rootItem;

    private static final String TAG_TITLE = "title";
    private static final String TAG_LINK = "link";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_RSS = "rss";
    private static final String TAG_ITEM = "item";
    private final String ns = null;

    public RssParser() {
    }

    public List<RssFeedModel> rssParsing(InputStream inputStream) throws  XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    private List<RssFeedModel> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, TAG_RSS);
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;

        List<RssFeedModel> items = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            int eventType = parser.getEventType();
            String name = parser.getName();

            if (name == null) {
                continue;
            }

            if ((eventType == XmlPullParser.START_TAG) && (name.equalsIgnoreCase(TAG_ITEM))) {
                isItem = true;
                continue;
            }

            if ((eventType == XmlPullParser.END_TAG) && (name.equalsIgnoreCase(TAG_ITEM))) {
                isItem = false;
                continue;
            }

            switch (name) {
                case TAG_TITLE:
                    if (title == null) {
                        title = readArticle(parser, TAG_TITLE);
                    }
                    break;
                case TAG_LINK:
                    if (link == null) {
                        link = readArticle(parser, TAG_LINK);
                    }
                    break;
                case TAG_DESCRIPTION:
                    if (description == null) {
                        description = readArticle(parser, TAG_DESCRIPTION);
                    }
                    break;
            }

            if (title != null && link != null && description != null) {
                if (isItem) {
                    RssFeedModel item = new RssFeedModel(title, link, description);
                    items.add(item);
                } else {
                    if (rootItem == null) {
                        rootItem = new RssFeedModel(title, link, description);
                    } else {
                        rootItem.setAll(title, link, description);
                    }
                }

                title = null;
                link = null;
                description = null;
                isItem = false;
            }
        }
        return items;
    }

    private String readArticle(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String article = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return article;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


}
