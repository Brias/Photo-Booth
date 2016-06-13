package de.braeuer.matthias.photobooth.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import de.braeuer.matthias.photobooth.CameraViewActivity;
import de.braeuer.matthias.photobooth.EmailAddressManager;
import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.UploadImage;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;
import de.braeuer.matthias.photobooth.listener.OnHttpRequestDoneListener;

/**
 * Created by Matze on 09.06.2016.
 */
public class ImageDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String IMAGE_DIALOG_FRAGMENT = "ImageDialogFragment";

    private static String IMAGE_BUNDLE_KEY = "image";

    private Bitmap bm;

    public static ImageDialogFragment newInstance(Bitmap bm) {
        ImageDialogFragment idf = new ImageDialogFragment();

        Bundle bundle = new Bundle();

        bundle.putParcelable(IMAGE_BUNDLE_KEY, bm);

        idf.setArguments(bundle);

        return idf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bm = getArguments().getParcelable(IMAGE_BUNDLE_KEY);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getString(R.string.image_preview_dialog_title));
        setCancelable(false);

        View v = inflater.inflate(R.layout.image_dialog_fragment_layout, container, false);

        initImagePreview(v);
        setOnClickListener(v);

        return v;
    }

    private void initImagePreview(View v) {
        ImageView iv = (ImageView) v.findViewById(R.id.imagePreview);

        iv.setImageBitmap(bm);
    }

    private void setOnClickListener(View v) {
        Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
        Button btnOk = (Button) v.findViewById(R.id.btnOk);
        Button btnEditEmailAddress = (Button) v.findViewById(R.id.btnEditEmailAddress);

        if (EmailAddressManager.getEmailAddresses().size() == 0) {
            btnOk.setEnabled(false);
        }

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnEditEmailAddress.setOnClickListener(this);

    }

    public void editEmailAddress() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.remove(this);

        ft.addToBackStack(null);

        EditEmailAddressDialogFragment edf = EditEmailAddressDialogFragment.newInstance();

        edf.show(ft, EditEmailAddressDialogFragment.EDIT_ADDRESS_DIALOG_FRAGMENT);
    }

    private void startUploadImage() {
        UploadDialog up = new UploadDialog(getActivity());

        new UploadImage(((OnHttpRequestDoneListener) getActivity()), up, bm, CameraViewActivity.SERVER).execute();

        dismiss();
    }

    private void cancel() {
        Activity activity = getActivity();

        if (activity instanceof OnDialogFragmentClosedListener) {
            ((OnDialogFragmentClosedListener) getActivity()).onDialogFragmentClosed();
        }

        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnOk:
                startUploadImage();
                break;
            case R.id.btnEditEmailAddress:
                editEmailAddress();
                break;
        }
    }
}
