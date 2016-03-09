package com.coppel.viaticos;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.coppel.viaticos.data.ViaticosContract;


/**
 * Created by hgomez on 01/03/2016.
 */
public class ViaticosDialogFragment extends DialogFragment {
    private static final String TAG = ViaticosDialogFragment.class.getSimpleName();
    private View mView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_viaticos, null);
        mView = view;
        EditText txtDescViaticos = (EditText) mView.findViewById(R.id.txtDescViaticos);
        Spinner cmbConceptos = (Spinner) mView.findViewById(R.id.cmbConceptos);
        Cursor cursor = getActivity().getContentResolver().query(
                ViaticosContract.ConceptosEntry.CONTENT_URI,
                new String[]{ViaticosContract.ConceptosEntry._ID, ViaticosContract.ConceptosEntry.COLUMN_DESCRIPCION},
                null,
                null,
                null);
        ConceptosAdapter conceptosAdapter = new ConceptosAdapter(getActivity(),cursor, CursorAdapter.FLAG_AUTO_REQUERY);
        cmbConceptos.setAdapter(conceptosAdapter);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ViaticosDialogFragment.this.getDialog().cancel();
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
        final android.support.v7.app.AlertDialog d = (android.support.v7.app.AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = addViaticos();
                    if(wantToCloseDialog)
                        d.dismiss();
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }
    public boolean addViaticos(){
        EditText txtDescViaticos = (EditText)mView.findViewById(R.id.txtDescViaticos);
        Spinner cmbConceptos = (Spinner)mView.findViewById(R.id.cmbConceptos);
        EditText txtCantidad = (EditText)mView.findViewById(R.id.txtCantidad);
        EditText txtIva = (EditText)mView.findViewById(R.id.txtIva);
        long viaje_id = getActivity().getIntent().getExtras().getLong("viaje_id");
        long currentTime = System.currentTimeMillis() / 1000;
        if(     txtDescViaticos.getText().toString().isEmpty() ||
                txtDescViaticos.getText().toString().isEmpty()||
                txtIva.getText().toString().isEmpty()||
                txtCantidad.getText().toString().isEmpty()
                )
            return false;
        ContentValues values = new ContentValues();
        values.put(ViaticosContract.ViaticosEntry.COLUMN_VIAJE_ID,viaje_id);
        values.put(ViaticosContract.ViaticosEntry.COLUMN_CONCEPTO_ID,cmbConceptos.getSelectedItemId());
        values.put(ViaticosContract.ViaticosEntry.COLUMN_DESCRIPCION,txtDescViaticos.getText().toString());
        values.put(ViaticosContract.ViaticosEntry.COLUMN_CANTIDAD, Double.parseDouble(txtCantidad.getText().toString()));
        values.put(ViaticosContract.ViaticosEntry.COLUMN_IVA, Double.parseDouble(txtIva.getText().toString()));
        values.put(ViaticosContract.ViaticosEntry.COLUMN_CREATED_AT, currentTime);
        values.put(ViaticosContract.ViaticosEntry.COLUMN_UPDATED_AT, currentTime);

        Uri viaticoUri = getActivity().getContentResolver().insert(ViaticosContract.ViaticosEntry.CONTENT_URI,values);
        long viatico_id = ContentUris.parseId(viaticoUri);
        Log.d(TAG, "addViaticos: " + viatico_id);
        return true;
    }
}
