package com.coppel.viaticos.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coppel.viaticos.data.ViaticosContract.ConceptosEntry;
import com.coppel.viaticos.data.ViaticosContract.ViajesEntry;
import com.coppel.viaticos.data.ViaticosContract.ViaticosEntry;
/**
 * Created by hgomez on 23/02/2016.
 */
public class ViaticosDbHelper extends SQLiteOpenHelper {

    //si cambias la BD debes incrementar la versión
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "viaticos.db";

    public ViaticosDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_VIAJES_TABLE = "CREATE TABLE " + ViajesEntry.TABLE_NAME + "(" +
                ViajesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                ViajesEntry.COLUMN_DESCRIPCION + " TEXT NOT NULL , " +
                ViajesEntry.COLUMN_FECHA_INICIO + " INTEGER NOT NULL , " +
                ViajesEntry.COLUMN_FECHA_FIN + " INTEGER NOT NULL , " +
                ViajesEntry.COLUMN_SERVER_ID + " INTEGER DEFAULT(0) , " +
                ViajesEntry.COLUMN_CREATED_AT + " INTEGER NOT NULL , " +
                ViajesEntry.COLUMN_UPDATED_AT + " INTEGER NOT NULL" +
                ")";

        final String SQL_CREATE_CONCEPTOS_TABLE = "CREATE TABLE " +  ConceptosEntry.TABLE_NAME + "(" +
                ConceptosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                ConceptosEntry.COLUMN_DESCRIPCION + " TEXT NOT NULL , " +
                ConceptosEntry.COLUMN_CREATED_AT + " INTEGER NOT NULL , " +
                ConceptosEntry.COLUMN_UPDATED_AT + " INTEGER NOT NULL" +
                ")";

        final String SQL_CREATE_VIATICOS_TABLE = "CREATE TABLE " +  ViaticosEntry.TABLE_NAME + "(" +
                ViaticosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                ViaticosEntry.COLUMN_VIAJE_ID + " INTEGER NOT NULL , " +
                ViaticosEntry.COLUMN_CONCEPTO_ID + " INTEGER NOT NULL , " +
                ViaticosEntry.COLUMN_DESCRIPCION + " TEXT NOT NULL , " +
                ViaticosEntry.COLUMN_CANTIDAD + " REAL NOT NULL , " +
                ViaticosEntry.COLUMN_IVA + " REAL NOT NULL , " +
                ViaticosEntry.COLUMN_SERVER_ID + " INTEGER DEFAULT(0) , " +
                ViaticosEntry.COLUMN_CREATED_AT + " INTEGER NOT NULL , " +
                ViaticosEntry.COLUMN_UPDATED_AT + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + ViaticosEntry.COLUMN_VIAJE_ID + ") REFERENCES " +
                ViajesEntry.TABLE_NAME + " (" + ViajesEntry._ID + "), " +

                " FOREIGN KEY (" + ViaticosEntry.COLUMN_CONCEPTO_ID + ") REFERENCES " +
                ConceptosEntry.TABLE_NAME + " (" + ConceptosEntry._ID + ") " +
                ")";
        db.execSQL(SQL_CREATE_CONCEPTOS_TABLE);
        db.execSQL(SQL_CREATE_VIAJES_TABLE);
        db.execSQL(SQL_CREATE_VIATICOS_TABLE);
        //INSERTANDO CONCEPTOS
        ContentValues values = new ContentValues();
        long currentTime = System.currentTimeMillis()/1000;
        values.put("descripcion","AVION");
        values.put("created_at",currentTime);
        values.put("updated_at",currentTime);
        db.insert(ConceptosEntry.TABLE_NAME, null, values);
        values.put("descripcion", "TRANSPORTE");
        db.insert(ConceptosEntry.TABLE_NAME, null, values);
        values.put("descripcion", "HOSPEDAJE");
        db.insert(ConceptosEntry.TABLE_NAME, null, values);
        values.put("descripcion", "ALIMENTOS");
        db.insert(ConceptosEntry.TABLE_NAME, null, values);
        values.put("descripcion", "OTROS");
        db.insert(ConceptosEntry.TABLE_NAME, null, values);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



    }
}
