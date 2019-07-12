package com.example.getstarted;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


/**
 * Implementation of App Widget functionality.
 */
public class Quote extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(context,appWidgetManager, appWidgetId );
        Log.i("TAGGER", AppWidgetManager.EXTRA_APPWIDGET_ID);
        AsyncTask<Void, Void, String> lala=retrieveFeedTask.execute();
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quote);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }


    static class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
//        String rurl = "http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1";
        String resultString = "BE PATIENT";
        int appWidgetId;
        Context context;
        AppWidgetManager appWidgetManager;
        Random random = new Random();
        String rurl = "https://www.forbes.com/forbesapi/thought/uri.json?enrich=true&query="+(random.nextInt(10000));
//        String rurl = "https://www.forbes.com/quotes/?";
        public RetrieveFeedTask(Context con, AppWidgetManager apm,
                                int widgetid){
            appWidgetId = widgetid;
            appWidgetManager = apm;
            context = con;
            Log.i("TAG", Integer.toString(appWidgetId));
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(rurl);
                HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
                myConnection.setRequestMethod("GET");
                if (myConnection.getResponseCode()==200) {
                    Log.i("TAG", "This Works"+myConnection.getResponseMessage());
//                    resultString = (String) url.getContent();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                    resultString = bufferedReader.readLine();
                    Log.i("TAG", resultString);
                    try {
                        JSONObject jsonObject = new JSONObject(resultString);
                        Log.i("TAG", "Test "+jsonObject.toString());
//                        resultString = jsonObject.toString();
                        JSONObject thought = jsonObject.getJSONObject("thought");
                        resultString = thought.getString("quote");
                        Log.i("TAGs", "Actual Output "+ resultString);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i("TAG", "Oh Snap! Does not connect."+myConnection.getResponseMessage());
                }
            } catch (MalformedURLException e) {
                return e.getMessage();
            } catch (IOException e) {
                return e.getMessage();
            }
            return resultString;
//        TODO: Add API address etc and return data from it in below return statement
//        TODO: API https://www.forbes.com/forbesapi/thought/uri.json?enrich=true&query=1&relatedlimit=1
//            return resultString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quote);
            views.setTextViewText(R.id.appwidget_text, resultString);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }
}
//    Delete the onEnabled() and onDisabled() method stubs.
//
//    You would use onEnabled() to perform initial setup for a widget (such as opening a new database)
//    when the first instance is initially added to the user's home screen. Even if the user adds multiple widgets,
//    this method is only called once. Use onDisabled(), correspondingly, to clean up any resources that were created
//    in onEnabled() once the last instance of that widget is removed. You won't use either of these methods for this app,
//    so you can delete them.
//
//    @Override
//    public void onEnabled(Context context) {
//        // Enter relevant functionality for when the first widget is created
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        // Enter relevant functionality for when the last widget is disabled
//    }
//}
