package com.coffeeio.bikeshare;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class FetchRandomName extends AsyncTask<String, Void, String> {

    private String parse(String s){
        //String s has the format  Rides:{rN,rS,rE:}*
        //where rN, rS,rE are strings


        return s;
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
        super.onPostExecute(response);
    }
}

