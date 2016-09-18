package de.braeuer.matthias.photobooth;

import android.provider.BaseColumns;

/**
 * Created by Matze on 21.06.2016.
 */
public final class PhotoBoothDBContract {
    public static abstract class PhotoBoothEntry implements BaseColumns {
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_EMAIL = "email";
    }

    public PhotoBoothDBContract() {
    }
}
