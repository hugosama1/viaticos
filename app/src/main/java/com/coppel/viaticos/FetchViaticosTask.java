package com.coppel.viaticos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.coppel.viaticos.data.ViaticosContract;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hgomez on 22/02/2016.
 */
public class FetchViaticosTask extends AsyncTask<String, Void,Void> {

    private Context mContext;
    private String TAG = FetchViaticosTask.class.getSimpleName();
    public FetchViaticosTask ( Context mContext) {
        this.mContext = mContext;
    }
    private static final String VIAJES_URL = "http://172.20.10.6/viajes";
    @Override
    protected Void doInBackground(String ... params) {
            //if(saveViajes())
                //saveViaticos();
            return null;
    }

    private boolean saveViaticos() {
        boolean success = true;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        JSONObject jsonResponse=null;
        OutputStreamWriter printout;
        Cursor ViaticosCursor = mContext.getContentResolver().query(ViaticosContract.ViaticosEntry.CONTENT_URI,
                new String[]{
                        ViaticosContract.ViaticosEntry._ID,
                        ViaticosContract.ViaticosEntry.COLUMN_DESCRIPCION,
                        ViaticosContract.ViaticosEntry.COLUMN_CANTIDAD,
                        ViaticosContract.ViaticosEntry.COLUMN_CONCEPTO_ID,
                        ViaticosContract.ViaticosEntry.COLUMN_IVA,
                        ViaticosContract.ViaticosEntry.COLUMN_VIAJE_ID

                },
                ViaticosContract.ViajesEntry.COLUMN_SERVER_ID + " = 0",
                null,
                null);
        String token = new UserLoginTask(mContext).getToken();
        Uri uri = Uri.parse(VIAJES_URL).buildUpon().build();
        String basicAuth = "Bearer " + token;
        URL url;
        OutputStream output = null;
        InputStream input = null;
        while( ViaticosCursor.moveToNext() ) {
            try {
                String descripcion =
                        ViaticosCursor.getString(ViaticosCursor.getColumnIndex(ViaticosContract.ViajesEntry.COLUMN_DESCRIPCION));
                int mobile_id = ViaticosCursor.getInt(ViaticosCursor.getColumnIndex(ViaticosContract.ViajesEntry._ID));
                String fechaInicio =
                        ViaticosCursor.getString(ViaticosCursor.getColumnIndex(ViaticosContract.ViajesEntry.COLUMN_FECHA_INICIO));
                String fechaFin =
                        ViaticosCursor.getString(ViaticosCursor.getColumnIndex(ViaticosContract.ViajesEntry.COLUMN_FECHA_FIN));
                JSONObject json = new JSONObject();
                json.put("descripcion",descripcion);
                json.put("fecha_inicio",fechaInicio);
                json.put("fecha_fin",fechaFin);
                json.put("mobile_id",String.valueOf(mobile_id));
                url = new URL(uri.toString());
                urlConnection  = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Authorization", basicAuth);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                printout = new OutputStreamWriter(urlConnection.getOutputStream());
                printout.write(json.toString());
                printout.close();
                int code = urlConnection.getResponseCode();
                if( code >= HttpURLConnection.HTTP_BAD_REQUEST) {
                    input = urlConnection.getErrorStream();
                } else {
                    input = urlConnection.getInputStream();
                }
                StringBuffer buffer = new StringBuffer();
                if (input == null) {
                    // Nothing to do.
                }
                reader = new BufferedReader(new InputStreamReader(input));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                }
                jsonResponse = new JSONObject(buffer.toString());
                int server_id = jsonResponse.getInt("id");
                saveViajeServerId(mobile_id,server_id);
            }catch (Exception ex) {
                Log.e(TAG, "doInBackground: ", ex);
                success = false;
            } finally {
                if( output != null && input != null) {
                    try {
                        output.flush();
                        output.close();
                        input.close();
                    }catch(Exception ex){}
                }
            }
        }
        return success;
    }

    private boolean saveViajes() {
        boolean success = true;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        JSONObject jsonResponse=null;
        OutputStreamWriter printout;
        Cursor viajesCursor = mContext.getContentResolver().query(ViaticosContract.ViajesEntry.CONTENT_URI,
                new String[]{
                        ViaticosContract.ViajesEntry._ID,
                        ViaticosContract.ViajesEntry.COLUMN_DESCRIPCION,
                        "strftime('%Y-%m-%d'," + ViaticosContract.ViajesEntry.COLUMN_FECHA_INICIO + ",'unixepoch') AS " + ViaticosContract.ViajesEntry.COLUMN_FECHA_INICIO,
                        "strftime('%Y-%m-%d'," + ViaticosContract.ViajesEntry.COLUMN_FECHA_FIN + ",'unixepoch') AS " + ViaticosContract.ViajesEntry.COLUMN_FECHA_FIN,
                },
                ViaticosContract.ViajesEntry.COLUMN_SERVER_ID + " = 0",
                null,
                null);
        String token = new UserLoginTask(mContext).getToken();
        Uri uri = Uri.parse(VIAJES_URL).buildUpon().build();
        String basicAuth = "Bearer " + token;
        URL url;
        OutputStream output = null;
        InputStream input = null;
        while( viajesCursor.moveToNext() ) {
            try {
                String descripcion =
                        viajesCursor.getString(viajesCursor.getColumnIndex(ViaticosContract.ViajesEntry.COLUMN_DESCRIPCION));
                int mobile_id = viajesCursor.getInt(viajesCursor.getColumnIndex(ViaticosContract.ViajesEntry._ID));
                String fechaInicio =
                        viajesCursor.getString(viajesCursor.getColumnIndex(ViaticosContract.ViajesEntry.COLUMN_FECHA_INICIO));
                String fechaFin =
                        viajesCursor.getString(viajesCursor.getColumnIndex(ViaticosContract.ViajesEntry.COLUMN_FECHA_FIN));
                JSONObject json = new JSONObject();
                json.put("descripcion",descripcion);
                json.put("fecha_inicio",fechaInicio);
                json.put("fecha_fin",fechaFin);
                json.put("mobile_id",String.valueOf(mobile_id));
                url = new URL(uri.toString());
                urlConnection  = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Authorization", basicAuth);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                printout = new OutputStreamWriter(urlConnection.getOutputStream());
                printout.write(json.toString());
                printout.close();
                int code = urlConnection.getResponseCode();
                if( code >= HttpURLConnection.HTTP_BAD_REQUEST) {
                    input = urlConnection.getErrorStream();
                } else {
                    input = urlConnection.getInputStream();
                }
                StringBuffer buffer = new StringBuffer();
                if (input == null) {
                    // Nothing to do.
                }
                reader = new BufferedReader(new InputStreamReader(input));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                }
                jsonResponse = new JSONObject(buffer.toString());
                int server_id = jsonResponse.getInt("id");
                saveViajeServerId(mobile_id,server_id);
            }catch (Exception ex) {
                Log.e(TAG, "doInBackground: ", ex);
                success = false;
            } finally {
                if( output != null && input != null) {
                    try {
                        output.flush();
                        output.close();
                        input.close();
                    }catch(Exception ex){}
                }
            }
        }
        return success;
    }

    private void saveViajeServerId(int viaje_id,int server_id) {
        ContentValues values = new ContentValues();
        values.put("server_id",server_id);
        mContext.getContentResolver().update(
                ViaticosContract.ViajesEntry.CONTENT_URI,
                values,
                ViaticosContract.ViajesEntry._ID + "=?",
                new String[]{String.valueOf(viaje_id)}
        );
    }

}
