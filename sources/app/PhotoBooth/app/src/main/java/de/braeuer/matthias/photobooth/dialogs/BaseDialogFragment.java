package de.braeuer.matthias.photobooth.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;

import de.braeuer.matthias.photobooth.FragmentHolder;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;

/**
 * Created by Matze on 24.06.2016.
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
