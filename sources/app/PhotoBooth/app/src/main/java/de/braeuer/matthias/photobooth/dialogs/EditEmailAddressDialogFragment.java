package de.braeuer.matthias.photobooth.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.braeuer.matthias.photobooth.R;

/**
 * Created by Matze on 09.06.2016.
 */
public class EditEmailAddressDialogFragment extends DialogFragment {

    public static EditEmailAddressDialogFragment newInstance() {
        EditEmailAddressDialogFragment edf = new EditEmailAddressDialogFragment();

        Bundle bundle = new Bundle();

        edf.setArguments(bundle);

        return edf;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View v = inflater.inflate(R.layout.edit_email_address_dialog_fragment_layout, container, false);

        return v;
    }
}
