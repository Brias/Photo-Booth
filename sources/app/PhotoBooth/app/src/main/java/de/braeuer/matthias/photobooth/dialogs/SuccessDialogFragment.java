package de.braeuer.matthias.photobooth.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;

/**
 * Created by Matze on 23.06.2016.
 */
public class SuccessDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String SUCCESS_DIALOG_FRAGMENT = "SuccessDialogFragment";

    private static final String TITLE_BUNDLE_KEY = "title_key";
    private static final String SUCCESS_MSG_BUNDLE_KEY = "success_message_key";
    private static final String CALL_ON_CLOSED_LISTENER_BUNDLE_KEY = "call_on_closed_listener_key";

    public static SuccessDialogFragment newInstance(String title, String errorMsg, boolean callOnClosedListener) {
        SuccessDialogFragment sdf = new SuccessDialogFragment();

        Bundle bundle = new Bundle();

        bundle.putString(TITLE_BUNDLE_KEY, title);
        bundle.putString(SUCCESS_MSG_BUNDLE_KEY, errorMsg);
        bundle.putBoolean(CALL_ON_CLOSED_LISTENER_BUNDLE_KEY, callOnClosedListener);

        sdf.setArguments(bundle);

        return sdf;
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

        View v = inflater.inflate(R.layout.success_dialog_fragment_layout, container, false);

        String successMsg = getArguments().getString(SUCCESS_MSG_BUNDLE_KEY);

        TextView successTextView = (TextView) v.findViewById(R.id.successMessageView);

        successTextView.setText(successMsg);

        Button btnOk = (Button) v.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(this);

        return v;
    }

    @Override
    public void dismiss() {
        Activity activity = getActivity();

        if (activity instanceof OnDialogFragmentClosedListener && getArguments().getBoolean
                (CALL_ON_CLOSED_LISTENER_BUNDLE_KEY)) {
            ((OnDialogFragmentClosedListener) activity).onDialogFragmentClosed();
        }

        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                cancel();
        }
    }
}
