package de.braeuer.matthias.photobooth;

import android.provider.BaseColumns;

/**
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: PhotoBoothDBContract.java,v 1.0 2016/09/29 16:56:00 Exp $
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
