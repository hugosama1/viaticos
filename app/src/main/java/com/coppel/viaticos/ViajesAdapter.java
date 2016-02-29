package com.coppel.viaticos;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Hugo on 2/27/2016.
 */
public class ViajesAdapter extends CursorAdapter{


    public ViajesAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_viaje, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
