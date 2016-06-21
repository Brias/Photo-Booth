package de.braeuer.matthias.photobooth.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.braeuer.matthias.photobooth.R;

/**
 * Created by Matze on 20.06.2016.
 */
public class InfoDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String INFO_DIALOG_FRAGMENT = "ImageDialogFragment";

    public static InfoDialogFragment newInstance(){
        InfoDialogFragment idf = new InfoDialogFragment();

        Bundle bundle = new Bundle();

        idf.setArguments(bundle);

        return idf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setCancelable(false);

        getDialog().setTitle(getResources().getString(R.string.info_dialog_fragment_title));

        View v = inflater.inflate(R.layout.info_dialog_fragment_layout, container, false);

        v.findViewById(R.id.btnOk).setOnClickListener(this);

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
