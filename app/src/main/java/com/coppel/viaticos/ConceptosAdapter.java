package com.coppel.viaticos;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.coppel.viaticos.data.ViaticosContract;

/**
 * Created by hgomez on 01/03/2016.
 */
public class ConceptosAdapter extends CursorAdapter {

    public ConceptosAdapter(Context context, Cursor c, int flags) {
        super(context,c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_item_viaticos,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtCmbConceptos = (TextView) view.findViewById(R.id.txtCmbConceptos);
        txtCmbConceptos.setText(cursor.getString(cursor.getColumnIndex(ViaticosContract.ConceptosEntry.COLUMN_DESCRIPCION)));
    }
}
