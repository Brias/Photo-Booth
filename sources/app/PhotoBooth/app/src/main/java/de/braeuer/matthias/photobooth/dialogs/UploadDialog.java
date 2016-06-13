package de.braeuer.matthias.photobooth.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;

/**
 * Created by Matze on 12.06.2016.
 */
public class UploadDialog extends ProgressDialog {

    public UploadDialog(Context context) {
        super(context);

        setTitle(context.getResources().getString(R.string.uploading));
        setCancelable(false);
    }

    @Override
    public void dismiss(){
        if(getOwnerActivity() instanceof OnDialogFragmentClosedListener) {
            ((OnDialogFragmentClosedListener) getOwnerActivity()).onDialogFragmentClosed();
        }

        super.dismiss();
    }
}
