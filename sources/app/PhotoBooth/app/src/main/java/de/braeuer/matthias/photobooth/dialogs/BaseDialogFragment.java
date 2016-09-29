package de.braeuer.matthias.photobooth.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;

import de.braeuer.matthias.photobooth.FragmentHolder;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;

/**
 * This class defines the Base class for the Dialog classes and provides closing all FragmentDialogs, that are stored
 * in FragmentHolder.java
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: BaseDialogFragment.java,v 1.0 2016/09/29 16:47:00 Exp $
 */
public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void cancel() {
        Activity activity = getActivity();

        if (activity instanceof OnDialogFragmentClosedListener) {
            ((OnDialogFragmentClosedListener) getActivity()).onDialogFragmentClosed();
        }

        dismiss();

        for (int i = FragmentHolder.dialogFragments.size() - 1; i >= 0; i--) {
            DialogFragment df = FragmentHolder.dialogFragments.get(i);

            if (df != null) {
                df.dismiss();
            } else {
                FragmentHolder.dialogFragments.remove(i);
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

        FragmentHolder.dialogFragments.remove(this);
    }
}
