package com.coppel.viaticos;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coppel.viaticos.data.ViaticosContract;

import java.text.SimpleDateFormat;

/**
 * Created by Hugo on 2/27/2016.
 */
public class ViajeDialogFragment extends DialogFragment {

    public static String FORMATO_FECHA = "dd/MM/yyyy";
    private static String TAG = ViajeDialogFragment.class.getSimpleName();
    private View mView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_viajes, null);
        EditText txtDescViaje = (EditText) mView.findViewById(R.id.txtDescViaje);
        EditText dtInicioViaje = (EditText) mView.findViewById(R.id.dtInicioViaje);
        EditText dtFinViaje = (EditText) mView.findViewById(R.id.dtFinViaje);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(mView)
                // Add action buttons
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ViajeDialogFragment.this.getDialog().cancel();
                    }
                });
        dtInicioViaje.addTextChangedListener(new TextValidator(dtInicioViaje) {
            @Override
            public void validate(TextView textView, String text) {
                if (!validateDate(textView.getText().toString())) {
                    textView.setError(getString(R.string.formato_fecha)+ FORMATO_FECHA);
                }
            }
        });
        dtFinViaje.addTextChangedListener(new TextValidator(dtFinViaje) {
            @Override public void validate(TextView textView, String text) {
                if(!validateDate(textView.getText().toString())) {
                    textView.setError(getString(R.string.formato_fecha)+ FORMATO_FECHA);
                }
            }
        });

        return builder.create();
    }

    /**
     * Agregado para sobreescribir el cierre del dialogo al dar al botón aceptar
     */
    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = addViaje();
                    if(wantToCloseDialog)
                        d.dismiss();
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }

    private boolean validateDate(String date) {
        boolean success = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
            sdf.parse(date);
        }catch(Exception ex) {
            success = false;
        }
        return success;
    }

    public boolean addViaje() {
        ContentValues viajesValues = new ContentValues();
        EditText txtDescViaje = (EditText) mView.findViewById(R.id.txtDescViaje);
        EditText dtInicioViaje = (EditText) mView.findViewById(R.id.dtInicioViaje);
        EditText dtFinViaje = (EditText) mView.findViewById(R.id.dtFinViaje);
        long currentTime = System.currentTimeMillis() / 1000;
        if(     dtFinViaje.getError() != null ||
                dtInicioViaje.getError() != null ||
                dtFinViaje.getText().toString().isEmpty()||
                dtInicioViaje.getText().toString().isEmpty()||
                txtDescViaje.getText().toString().isEmpty()
            )
            return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
            long fechaInicio = sdf.parse(dtInicioViaje.getText().toString()).getTime() / 1000;
            long fechaFin = sdf.parse(dtFinViaje.getText().toString()).getTime() / 1000;
            viajesValues.put(ViaticosContract.ViajesEntry.COLUMN_DESCRIPCION,txtDescViaje.getText().toString());
            viajesValues.put(ViaticosContract.ViajesEntry.COLUMN_FECHA_INICIO, fechaInicio);
            viajesValues.put(ViaticosContract.ViajesEntry.COLUMN_FECHA_FIN, fechaFin);
            viajesValues.put(ViaticosContract.ViajesEntry.COLUMN_CREATED_AT, currentTime);
            viajesValues.put(ViaticosContract.ViajesEntry.COLUMN_UPDATED_AT, currentTime);
            Uri inserted = getActivity().getContentResolver().insert(ViaticosContract.ViajesEntry.CONTENT_URI,viajesValues);
            Log.d(TAG, "addViaje: id : " + ContentUris.parseId(inserted));
        }catch(Exception ex) {
            Log.d(TAG, "addViaje: " + ex.getMessage());
        }
        return true;
    }
}
