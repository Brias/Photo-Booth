package de.braeuer.matthias.photobooth.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.braeuer.matthias.photobooth.R;

/**
 * This class shows an error fragment dialog, depending on the passed arguments
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: ErrorDialogFragment.java,v 1.0 2016/09/29 16:51:00 Exp $
 */
public class ErrorDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String ERROR_DIALOG_FRAGMENT = "ErrorDialogFragment";

    private static final String TITLE_BUNDLE_KEY = "title_key";
    private static final String ERROR_MSG_BUNDLE_KEY = "error_message_key";
    private static final String CALL_ON_CLOSED_LISTENER_BUNDLE_KEY = "call_on_closed_listener_key";

    public static ErrorDialogFragment newInstance(String title, String errorMsg, boolean callOnClosedListener) {
        ErrorDialogFragment edf = new ErrorDialogFragment();

        Bundle bundle = new Bundle();

        bundle.putString(TITLE_BUNDLE_KEY, title);
        bundle.putString(ERROR_MSG_BUNDLE_KEY, errorMsg);
        bundle.putBoolean(CALL_ON_CLOSED_LISTENER_BUNDLE_KEY, callOnClosedListener);

        edf.setArguments(bundle);

        return edf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String title = getArguments().getString(TITLE_BUNDLE_KEY);

        title = title == null ? getResources().getString(R.string.error_dialog_title) : title;

        getDialog().setTitle(title);

        setCancelable(false);

        View v = inflater.inflate(R.layout.error_dialog_fragment_layout, container, false);

        String errorMsg = getArguments().getString(ERROR_MSG_BUNDLE_KEY);

        errorMsg = errorMsg == null ? getResources().getString(R.string.unknown_error) : errorMsg;

        TextView errorTextView = (TextView) v.findViewById(R.id.errorMessageView);

        errorTextView.setText(errorMsg);

        Button btnOk = (Button) v.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                dismiss();
        }
    }
}
