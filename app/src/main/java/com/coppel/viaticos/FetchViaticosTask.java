package com.coppel.viaticos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hgomez on 22/02/2016.
 */
public class FetchViaticosTask extends AsyncTask<String, Void,Void> {

    private Context mContext;
    private String LOG_TAG = FetchViaticosTask.class.getSimpleName();
    public FetchViaticosTask ( Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(String ... params) {
        String action = params[0];
        switch (action) {

        }
        return null;
    }

}
