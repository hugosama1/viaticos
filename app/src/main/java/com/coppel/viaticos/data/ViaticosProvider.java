package com.coppel.viaticos.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

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


    private static final SQLiteQueryBuilder sSeleccionViaticosPorViaje;

    static{
        sSeleccionViaticosPorViaje = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sSeleccionViaticosPorViaje.setTables(
                ViaticosEntry.TABLE_NAME + " INNER JOIN " +
                        ViajesEntry.TABLE_NAME +
                        " ON " + ViaticosEntry.TABLE_NAME +
                        "." + ViaticosEntry.COLUMN_VIAJE_ID +
                        " = " + ViajesEntry.TABLE_NAME +
                        "." + ViajesEntry._ID);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ViaticosDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
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

        switch(match) {
            case VIAJES:


        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ViaticosContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, ViaticosContract.PATH_VIAJES, VIAJES);
        matcher.addURI(authority, ViaticosContract.PATH_VIAJES + "/#", VIAJE_POR_ID);
        matcher.addURI(authority, ViaticosContract.PATH_CONCEPTOS, CONCEPTOS);
        matcher.addURI(authority, ViaticosContract.PATH_VIATICOS  + "/#", VIATICOS);
        matcher.addURI(authority, ViaticosContract.PATH_VIATICOS  + "/#/#", VIATICOS_POR_ID);

        return matcher;
    }



}
