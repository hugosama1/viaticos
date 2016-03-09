package com.coppel.viaticos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.coppel.viaticos.data.ViaticosContract;

public class ViaticosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viaticos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabViaticos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViaticosDialogFragment viaticosDialogFragment = new ViaticosDialogFragment();
                viaticosDialogFragment.show(getFragmentManager(),"viaticos");
            }
        });
        Intent intent = getIntent();
        Bundle bundle= intent.getExtras();
        long viaje_id = bundle.getLong("viaje_id");
        String descripcion = bundle.getString("descripcion");
        String fecha_inicio = bundle.getString("fecha_inicio");
        String fecha_fin = bundle.getString("fecha_fin");
        toolbar.setTitle(descripcion + " " + fecha_inicio + " - " + fecha_fin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Cursor viaticosCursor = getContentResolver().query(
                ViaticosContract.ViaticosEntry.buildViaticosViaje(viaje_id),
                new String[]{
                        ViaticosContract.ViaticosEntry.QUERY_COLUMN_ID,
                        ViaticosContract.ViaticosEntry.QUERY_COLUMN_DESCRIPCION,
                        ViaticosContract.ViaticosEntry.COLUMN_CANTIDAD,
                        ViaticosContract.ViaticosEntry.COLUMN_IVA,
                        ViaticosContract.ViaticosEntry.QUERY_COLUMN_CONCEPTO
                },
                null,
                null,
                null
        );

        ViaticosAdapter viaticosAdapter = new ViaticosAdapter(this,viaticosCursor, CursorAdapter.FLAG_AUTO_REQUERY);
        ListView lstViaticos = (ListView) findViewById(R.id.lstViaticos);
        lstViaticos.setAdapter(viaticosAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
