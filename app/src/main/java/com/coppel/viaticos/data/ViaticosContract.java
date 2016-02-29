package com.coppel.viaticos.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by hgomez on 22/02/2016.
 */
public class ViaticosContract {

    public static final String CONTENT_AUTHORITY = "com.coppel.viaticos.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" +  CONTENT_AUTHORITY);

    public static final String PATH_VIAJES = "viajes";
    public static final String PATH_VIATICOS="viaticos";
    public static final String PATH_CONCEPTOS="conceptos";

    public static final class ViajesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIAJES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIAJES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIAJES;

        public static final String TABLE_NAME = "viajes";

        public static final String COLUMN_DESCRIPCION = "descripcion";
        public static final String COLUMN_FECHA_INICIO="fecha_inicio";
        public static final String COLUMN_FECHA_FIN="fecha_fin";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }
    public static final class ConceptosEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONCEPTOS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONCEPTOS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONCEPTOS;

        public static final String TABLE_NAME = "conceptos";

        public static final String COLUMN_DESCRIPCION = "descripcion";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static final class ViaticosEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIATICOS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIATICOS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIATICOS;


        public static final String TABLE_NAME = "viaticos";

        public static final String COLUMN_VIAJE_ID = "viaje_id";
        public static final String COLUMNS_CONCEPTO_ID = "concepto_id";
        public static final String COLUMN_CANTIDAD = "cantidad";
        public static final String COLUMN_IVA = "iva";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";

        public static Uri buildViaticosViaje(long viaje_id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(viaje_id)).build();
        }

    }
    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

}
