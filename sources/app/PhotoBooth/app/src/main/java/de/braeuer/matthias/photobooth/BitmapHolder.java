package de.braeuer.matthias.photobooth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * This class holds a specific bitmap
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: BitmapHolder.java,v 1.0 2016/09/29 16:57:00 Exp $
 */
public class BitmapHolder {
    public static Bitmap bm;

    public static Bitmap drawTextToBitmap(Context context, Bitmap bm, String text) {
        bm = bm.copy(Bitmap.Config.ARGB_8888, true);

        Canvas c = new Canvas(bm);

        Paint p = new Paint();

        p.setColor(context.getResources().getColor(android.R.color.white));
        p.setTextSize(bm.getHeight() / 10);

        c.drawText(text, 20, bm.getHeight() - 20, p);

        return bm;
    }
}
