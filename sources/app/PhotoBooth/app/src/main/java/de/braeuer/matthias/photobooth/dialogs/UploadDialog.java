package de.braeuer.matthias.photobooth.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;

/**
 * This class shows an loading spinner in a fragment dialog
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: UploadDialog.java,v 1.0 2016/09/29 16:55:00 Exp $
 */
public class UploadDialog extends ProgressDialog {

    public UploadDialog(Context context) {
        super(context);

        setTitle(context.getResources().getString(R.string.uploading));
        setCancelable(false);
    }

    @Override
    public void dismiss() {
        if (getOwnerActivity() instanceof OnDialogFragmentClosedListener) {
            ((OnDialogFragmentClosedListener) getOwnerActivity()).onDialogFragmentClosed();
        }

        super.dismiss();
    }
}
