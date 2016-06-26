package de.braeuer.matthias.photobooth.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import de.braeuer.matthias.photobooth.AccessStorage;
import de.braeuer.matthias.photobooth.CameraViewActivity;
import de.braeuer.matthias.photobooth.Connection;
import de.braeuer.matthias.photobooth.DBHelper;
import de.braeuer.matthias.photobooth.EmailAddressManager;
import de.braeuer.matthias.photobooth.FragmentHolder;
import de.braeuer.matthias.photobooth.Image;
import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.UploadImage;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;
import de.braeuer.matthias.photobooth.listener.OnHttpRequestDoneListener;
import de.braeuer.matthias.photobooth.listener.OnSavedInternalListener;

/**
 * Created by Matze on 09.06.2016.
 */
public class ImageDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String IMAGE_DIALOG_FRAGMENT = "ImageDialogFragment";

    private static final String THUMB_BUNDLE_KEY = "thumb_bundle_key";

    public static ImageDialogFragment newInstance(Bitmap thumb) {
        ImageDialogFragment idf = new ImageDialogFragment();

        Bundle bundle = new Bundle();

        bundle.putParcelable(THUMB_BUNDLE_KEY, thumb);

        idf.setArguments(bundle);

        return idf;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHolder.dialogFragments.remove(this);

        getDialog().setTitle(getResources().getString(R.string.image_preview_dialog_title));
        setCancelable(false);

        View v = inflater.inflate(R.layout.image_dialog_fragment_layout, container, false);

        initImagePreview(v);
        setOnClickListener(v);

        return v;
    }

    private void initImagePreview(View v) {
        ImageView iv = (ImageView) v.findViewById(R.id.imagePreview);

        iv.setImageBitmap((Bitmap) getArguments().getParcelable(THUMB_BUNDLE_KEY));
    }

    private void setOnClickListener(View v) {
        Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
        Button btnNext = (Button) v.findViewById(R.id.btnNext);

        btnCancel.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    public void editEmailAddress() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.remove(this);

        ft.addToBackStack(IMAGE_DIALOG_FRAGMENT);
        FragmentHolder.dialogFragments.add(this);

        EditEmailAddressDialogFragment edf = EditEmailAddressDialogFragment.newInstance();

        edf.show(ft, EditEmailAddressDialogFragment.EDIT_ADDRESS_DIALOG_FRAGMENT);
    }

    @Override
    public void cancel(){
        Activity activity = getActivity();

        if (activity instanceof OnDialogFragmentClosedListener) {
            ((OnDialogFragmentClosedListener) getActivity()).onDialogFragmentClosed();
        }

        FragmentHolder.dialogFragments.clear();
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnNext:
                editEmailAddress();
                break;
        }
    }
}
