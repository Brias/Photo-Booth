package de.braeuer.matthias.photobooth.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Calendar;

import de.braeuer.matthias.photobooth.CameraViewActivity;
import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.UploadImage;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;

/**
 * Created by Matze on 09.06.2016.
 */
public class ImageDialogFragment extends DialogFragment implements View.OnClickListener {

    private static String IMAGE_BUNDLE_KEY = "image";

    private static final String EDIT_ADDRESS_DIALOG_FRAGMENT = "EditAddressDialogFragment";

    private Bitmap bm;

    public static ImageDialogFragment newInstance(Bitmap bm) {
        ImageDialogFragment idf = new ImageDialogFragment();

        Bundle bundle = new Bundle();

        bundle.putParcelable(IMAGE_BUNDLE_KEY, bm);

        idf.setArguments(bundle);

        return idf;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        bm = getArguments().getParcelable(IMAGE_BUNDLE_KEY);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnEditEmailAddress.setOnClickListener(this);

    }

    public void editEmailAddress() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.remove(this);

        ft.addToBackStack(null);

        EditEmailAddressDialogFragment edf = EditEmailAddressDialogFragment.newInstance();

        edf.show(ft, EDIT_ADDRESS_DIALOG_FRAGMENT);
    }

    private void startUploadImage() {
        ProgressDialog pd = new ProgressDialog(getActivity());

        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DAY_OF_YEAR, 25);

        new UploadImage(pd, bm, rightNow.toString(), CameraViewActivity.SERVER).execute();
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
