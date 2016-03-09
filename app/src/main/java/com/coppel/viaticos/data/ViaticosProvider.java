package com.coppel.viaticos.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.coppel.viaticos.data.ViaticosContract.ViajesEntry;
import com.coppel.viaticos.data.ViaticosContract.ViaticosEntry;
import com.coppel.viaticos.data.ViaticosContract.ConceptosEntry;

/**
 * Created by hgomez on 23/02/2016.
 */
public class ViaticosProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ViaticosDbHelper mOpenHelper;

    static final int VIAJES = 100;
    static final int VIAJE_POR_ID = 101;

    static final int CONCEPTOS = 200;

    static final int VIATICOS = 300;
    static final int VIATICOS_POR_ID = 301;
    static final int VIATICOS_POR_VIAJE = 302;


    private static final SQLiteQueryBuilder sViaticosPorViajeQueryBuilder;

    static{
        sViaticosPorViajeQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join
        sViaticosPorViajeQueryBuilder.setTables(
                ViaticosEntry.TABLE_NAME + " INNER JOIN " +
                        ViajesEntry.TABLE_NAME +
                        " ON " + ViaticosEntry.TABLE_NAME +
                        "." + ViaticosEntry.COLUMN_VIAJE_ID +
                        " = " + ViajesEntry.TABLE_NAME +
                        "." + ViajesEntry._ID  + " INNER JOIN " +
                        ConceptosEntry.TABLE_NAME +
                        " ON " + ViaticosEntry.TABLE_NAME +
                        "." + ViaticosEntry.COLUMN_CONCEPTO_ID +
                        " = " + ConceptosEntry.TABLE_NAME +
                        "." + ConceptosEntry._ID
        );
        Log.d("", "static initializer: " + sViaticosPorViajeQueryBuilder.getTables());
    }

    private static String sViaticosPorViajeSelection =
            ViaticosEntry.TABLE_NAME + "." + ViaticosEntry.COLUMN_VIAJE_ID + " = ?";

    @Override
    public boolean onCreate() {
        mOpenHelper = new ViaticosDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        Cursor retCursor = null;
        switch (match) {
            case VIAJES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ViajesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CONCEPTOS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ConceptosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VIATICOS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ViaticosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
            case VIATICOS_POR_VIAJE:
                retCursor = getViaticosPorViaje(uri,projection,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    public Cursor getViaticosPorViaje(Uri uri, String[] projection, String sortOrder) {
         long viaje_id = ViaticosEntry.getViajeFromUri(uri);
         return sViaticosPorViajeQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                 projection,
                 sViaticosPorViajeSelection,
                 new String[]{String.valueOf(viaje_id)},
                 null,
                 null,
                 sortOrder
         );
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case VIAJES:
                return ViajesEntry.CONTENT_TYPE;
            case VIAJE_POR_ID:
                return ViajesEntry.CONTENT_ITEM_TYPE;
            case CONCEPTOS:
                return ConceptosEntry.CONTENT_TYPE;
            case VIATICOS:
                return ViaticosEntry.CONTENT_TYPE;
            case VIATICOS_POR_ID:
                return ViaticosEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        long _id;
        switch(match) {
            case VIAJES:
                _id = db.insert(ViajesEntry.TABLE_NAME,null,values);
                if( _id>0)
                    returnUri = ViajesEntry.buildViajesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case VIATICOS:
                _id = db.insert(ViaticosEntry.TABLE_NAME,null,values);
                if( _id>0 )
                    returnUri = ViaticosEntry.buildViaticosUri(values.getAsLong("viaje_id"),_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int afectedRows=0;
        switch(match) {
            case VIAJES:
                afectedRows = db.update(ViajesEntry.TABLE_NAME,values, selection, selectionArgs);
                break;
            case VIATICOS:
                afectedRows = db.update(ViaticosEntry.TABLE_NAME,values, selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(afectedRows > 0)
            getContext().getContentResolver().notifyChange(uri,null);
        return afectedRows;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ViaticosContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, ViaticosContract.PATH_VIAJES, VIAJES);
        matcher.addURI(authority, ViaticosContract.PATH_VIAJES + "/#", VIAJE_POR_ID);
        matcher.addURI(authority, ViaticosContract.PATH_CONCEPTOS, CONCEPTOS);
        matcher.addURI(authority, ViaticosContract.PATH_VIATICOS, VIATICOS);
        matcher.addURI(authority, ViaticosContract.PATH_VIATICOS  + "/#", VIATICOS_POR_VIAJE);
        matcher.addURI(authority, ViaticosContract.PATH_VIATICOS  + "/#/#", VIATICOS_POR_ID);

        return matcher;
    }



}
