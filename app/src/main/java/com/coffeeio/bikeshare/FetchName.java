package com.coffeeio.bikeshare;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class FetchName extends AsyncTask<String, Void, ArrayList<Ride>> {
    public FetchName(){

    }

    private ArrayList<Ride> parse(String s){
        //String s has the format  Rides:{rN,rS,rE:}*
        //where rN, rS,rE are strings

        ArrayList<Ride> response=  new ArrayList<>();


        return response;
    }

    @Override
    protected ArrayList<Ride> doInBackground(String... params) {
        String s= "null";
        try {
            s = new NetworkFetcher().getUrlBytes(params[0]);
        } catch (IOException ioe) { Log.i("NetworkFetcher", ioe.toString());  }
        return parse(s);
    }

    @Override
    protected void onPostExecute(ArrayList<Ride> response) {

    }
}

