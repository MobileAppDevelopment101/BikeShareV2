package com.coffeeio.bikeshare;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class FetchName extends AsyncTask<String, Void, String> {
    SessionStorage ss = null;
    public FetchName(Context context){
        ss = SessionStorage.get(context);
    }

    private String parse(String s){
        //String s has the format  Rides:{rN,rS,rE:}*
        //where rN, rS,rE are strings

        Log.d("bikeParse", s);

        String name = null;

        try {
            JSONObject mainObject = new JSONObject(s);
            JSONArray resObject = mainObject.getJSONArray("results");
            JSONObject entry1 = resObject.getJSONObject(0);
            JSONObject nameObject = entry1.getJSONObject("name");
            name = nameObject.getString("first");
        } catch (Exception ex) {
            Log.d("bikeparse", ex.toString());
        }

        Log.d("bikeparse", "post parsing");
        Log.d("bikeparse", name);

        return name;
    }

    @Override
    protected String doInBackground(String... params) {
        String s= "null";
        try {
            s = new NetworkFetcher().getUrlBytes(params[0]);
        } catch (IOException ioe) { Log.i("NetworkFetcher", ioe.toString());  }
        return parse(s);
    }

    @Override
    protected void onPostExecute(String response) {
        ss.setGenName(response);
    }
}

