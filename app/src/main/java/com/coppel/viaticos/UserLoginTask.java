package com.coppel.viaticos;

import android.content.Context;
import android.content.SharedPreferences;
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
 * Created by hgomez on 04/03/2016.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;
    private Context mContext;
    final String LOGIN_URL = "http://172.20.10.6/auth/authenticate?";
    private String LOG_TAG = UserLoginTask.class.getSimpleName();
    public static String USER_DETAILS = "userdetails";

    UserLoginTask(Context context,String email, String password) {
        mEmail = email;
        mPassword = password;
        mContext = context;
    }

    UserLoginTask(Context context) {
        mContext = context;
        SharedPreferences preferences = mContext.getSharedPreferences(USER_DETAILS, mContext.MODE_PRIVATE);
        mEmail = preferences.getString("email", "");
        mPassword = preferences.getString("password", "");
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return saveToken();
    }

    public boolean isTokenSet() {
        SharedPreferences userPreferences = mContext.getSharedPreferences(USER_DETAILS,mContext.MODE_PRIVATE);
        String token = userPreferences.getString("token",null);
        return token != null;
    }

    /**
     * llamar solo en métodos que no se encuentren en el hilo principal de la aplicación
     * @return un token válido
     */
    public String getToken() {
        String token = null;
        if(saveToken()) {
            SharedPreferences preferences = mContext.getSharedPreferences(USER_DETAILS, mContext.MODE_PRIVATE);
            token = preferences.getString("token", "");
        }
        return token;
    }

    private boolean saveToken() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        JSONObject jsonResponse=null;
        // Will contain the raw JSON response as a string.
        String response = null;

        try {
            final String USER_PARAM = "email";
            final String PASSWORD_PARAM = "password";

            Uri builtUri = Uri.parse(LOGIN_URL).buildUpon()
                    .appendQueryParameter(USER_PARAM, mEmail)
                    .appendQueryParameter(PASSWORD_PARAM, mPassword)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            InputStream inputStream = null;
            // Read the input stream into a String
            int code = urlConnection.getResponseCode();
            if( code >= HttpURLConnection.HTTP_BAD_REQUEST) {
                return false;
            } else {
                inputStream = urlConnection.getInputStream();
            }
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return false;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return false;
            }
            jsonResponse = new JSONObject(buffer.toString());
            String token = jsonResponse.getString("token");
            SharedPreferences userDetails =  mContext.getSharedPreferences(USER_DETAILS, mContext.MODE_PRIVATE);
            SharedPreferences.Editor edit = userDetails.edit();
            edit.clear();
            edit.putString("email", mEmail);
            edit.putString("password", mPassword);
            edit.putString("token", token);
            edit.commit();
            return true;
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "ERROR : " + e.getMessage(), e);
        }catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return false;
    }

}