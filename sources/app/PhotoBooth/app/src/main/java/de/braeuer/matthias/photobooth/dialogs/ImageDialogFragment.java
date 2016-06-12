package de.braeuer.matthias.photobooth.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import de.braeuer.matthias.photobooth.R;

/**
 * Created by Matze on 09.06.2016.
 */
public class ImageDialogFragment extends DialogFragment implements View.OnClickListener {

    private static String IMAGE_BUNDLE_KEY = "image";
    private static String EDIT_ADDRESS_DIALOG_FRAGMENT = "EditAddressDialogFragment";

    private Bitmap bm;

    private ImageView iv;

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
        iv = (ImageView) v.findViewById(R.id.imagePreview);

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

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
         //   ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    public void editEmailAddress() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.remove(this);

        ft.addToBackStack(null);

        EditEmailAddressDialogFragment edf = EditEmailAddressDialogFragment.newInstance();

        edf.show(ft, EDIT_ADDRESS_DIALOG_FRAGMENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.btnOk:

                break;
            case R.id.btnEditEmailAddress:
                editEmailAddress();
                break;
        }
    }
}
