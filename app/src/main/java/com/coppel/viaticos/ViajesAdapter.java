package com.coppel.viaticos;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.coppel.viaticos.data.ViaticosContract;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hugo on 2/27/2016.
 */
public class ViajesAdapter extends CursorAdapter {


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
        TextView txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
        TextView txtFechaInicio = (TextView) view.findViewById(R.id.txtInicioViaje);
        TextView txtFechaFin = (TextView) view.findViewById(R.id.txtFinViaje);
        //extract properties from the cursor
        String descripcion = cursor.getString(cursor.getColumnIndex(ViaticosContract.ViajesEntry.COLUMN_DESCRIPCION));
        long lfechaInicio = cursor.getLong(cursor.getColumnIndex(ViaticosContract.ViajesEntry.COLUMN_FECHA_INICIO));
        long lfechaFin = cursor.getLong(cursor.getColumnIndex(ViaticosContract.ViajesEntry.COLUMN_FECHA_FIN));

        String fechaInicio =  new SimpleDateFormat(ViajeDialogFragment.FORMATO_FECHA).format(new Date(Long.valueOf(lfechaInicio*1000)));
        String fechaFin =  new SimpleDateFormat(ViajeDialogFragment.FORMATO_FECHA).format(new Date(Long.valueOf(lfechaFin*1000)));
        txtDescripcion.setText(descripcion);
        txtFechaInicio.setText(fechaInicio);
        txtFechaFin.setText(fechaFin);
    }
}
