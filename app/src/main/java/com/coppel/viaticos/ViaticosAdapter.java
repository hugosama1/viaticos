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
 * Created by hgomez on 02/03/2016.
 */
public class ViaticosAdapter extends CursorAdapter {

    public ViaticosAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_viaticos,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
        TextView txtConcepto = (TextView) view.findViewById(R.id.txtConcepto);
        TextView txtCantidad = (TextView) view.findViewById(R.id.txtCantidad);
        TextView txtIva = (TextView) view.findViewById(R.id.txtIva);

        txtDescripcion.setText(cursor.getString(cursor.getColumnIndex(ViaticosContract.ViaticosEntry.COLUMN_DESCRIPCION)));
        txtConcepto.setText(cursor.getString(cursor.getColumnIndex(ViaticosContract.ViaticosEntry.COLUMN_CONCEPTO_ALT)));
        txtCantidad.setText(cursor.getString(cursor.getColumnIndex(ViaticosContract.ViaticosEntry.COLUMN_CANTIDAD)));
        txtIva.setText(cursor.getString(cursor.getColumnIndex(ViaticosContract.ViaticosEntry.COLUMN_IVA)));

    }
}
