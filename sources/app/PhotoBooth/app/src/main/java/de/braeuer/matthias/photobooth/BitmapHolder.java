package de.braeuer.matthias.photobooth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Matze on 26.06.2016.
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
