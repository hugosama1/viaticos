package com.coppel.viaticos;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coppel.viaticos.data.ViaticosContract;

public class ViajesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajes);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Drawable d=getResources().getDrawable(R.drawable.business_travel);
        myToolbar.setBackgroundDrawable(d);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.viajes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_sync:
                FetchViaticosTask task = new FetchViaticosTask(this);
                task.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addItems(View view) {
        ViajeDialogFragment viaje = new ViajeDialogFragment();
        viaje.show(getFragmentManager(),"viaje");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_viajes, container, false);
            ListView lstViajes = (ListView) rootView.findViewById(R.id.lstViajes);
            Cursor viajesCursor = getActivity().getContentResolver().query(
                    ViaticosContract.ViajesEntry.CONTENT_URI,
                    new String[]{
                            ViaticosContract.ViajesEntry._ID,
                            ViaticosContract.ViajesEntry.COLUMN_FECHA_INICIO,
                            ViaticosContract.ViajesEntry.COLUMN_DESCRIPCION,
                            ViaticosContract.ViajesEntry.COLUMN_FECHA_FIN
                    },
                    null,
                    null,
                    null
                    );
            ViajesAdapter viajesAdapter = new ViajesAdapter(rootView.getContext(),viajesCursor, CursorAdapter.FLAG_AUTO_REQUERY);
            lstViajes.setAdapter(viajesAdapter);
            lstViajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
                    TextView txtFechaInicio = (TextView) view.findViewById(R.id.txtInicioViaje);
                    TextView txtFechaFin = (TextView) view.findViewById(R.id.txtFinViaje);
                    Intent intent = new Intent(view.getContext(), ViaticosActivity.class);
                    intent.putExtra("viaje_id", id);
                    intent.putExtra("descripcion", txtDescripcion.getText().toString());
                    intent.putExtra("fecha_inicio", txtFechaInicio.getText().toString());
                    intent.putExtra("fecha_fin", txtFechaFin.getText().toString());
                    intent.setAction(Intent.ACTION_VIEW);
                    startActivity(intent);
                }
            });
            return rootView;
        }


    }
}
