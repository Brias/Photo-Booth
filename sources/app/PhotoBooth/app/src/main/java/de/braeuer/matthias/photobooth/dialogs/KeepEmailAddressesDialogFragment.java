package de.braeuer.matthias.photobooth.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.braeuer.matthias.photobooth.EmailAddressManager;
import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;

/**
 * This class is responsible for deleting email addresses
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: KeepEmailAddressesDialogFragment.java,v 1.0 2016/09/29 16:53:00 Exp $
 */
public class KeepEmailAddressesDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String KEEP_EMAIL_ADDRESSES_DIALOG_FRAGMENT = "KeepEmailAddressesDialogFragment";

    public static KeepEmailAddressesDialogFragment newInstance() {
        KeepEmailAddressesDialogFragment kdf = new KeepEmailAddressesDialogFragment();

        Bundle bundle = new Bundle();

        kdf.setArguments(bundle);

        return kdf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getString(R.string.http_request_success));
        setCancelable(false);

        View v = inflater.inflate(R.layout.keep_email_addresses_dialog_fragment_layout, container, false);

        setListener(v);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_keep_email_addresses:
                keepEmailAddresses();
                break;
            case R.id.btn_delete_email_addresses:
                deleteEmailAddresses();
                break;
        }
    }

    private void setListener(View v) {
        Button btnKeepEmailAddresses = (Button) v.findViewById(R.id.btn_keep_email_addresses);
        Button btnDeleteEmailAddresses = (Button) v.findViewById(R.id.btn_delete_email_addresses);

        btnKeepEmailAddresses.setOnClickListener(this);
        btnDeleteEmailAddresses.setOnClickListener(this);
    }

    private void deleteEmailAddresses() {
        EmailAddressManager.reset();

        if (getActivity() instanceof OnDialogFragmentClosedListener) {
            ((OnDialogFragmentClosedListener) getActivity()).onDialogFragmentClosed();
        }

        cancel();
    }

    private void keepEmailAddresses() {
        if (getActivity() instanceof OnDialogFragmentClosedListener) {
            ((OnDialogFragmentClosedListener) getActivity()).onDialogFragmentClosed();
        }

        cancel();
    }
}
