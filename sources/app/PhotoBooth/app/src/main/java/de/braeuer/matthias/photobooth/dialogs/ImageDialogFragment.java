package de.braeuer.matthias.photobooth.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
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
import de.braeuer.matthias.photobooth.Image;
import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.UploadImage;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;
import de.braeuer.matthias.photobooth.listener.OnHttpRequestDoneListener;
import de.braeuer.matthias.photobooth.listener.OnSavedInternalListener;

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
        Button btnUpload = (Button) v.findViewById(R.id.btnUpload);
        Button btnEditEmailAddress = (Button) v.findViewById(R.id.btnEditEmailAddress);
        Button btnInfo = (Button) v.findViewById(R.id.btnInfo);

        if (EmailAddressManager.getEmailAddresses().size() == 0) {
            btnUpload.setEnabled(false);
        }

        btnCancel.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnEditEmailAddress.setOnClickListener(this);
        btnInfo.setOnClickListener(this);

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

        Image image = new Image();

        image.setBitmap(bm);
        image.setEmail(EmailAddressManager.addressesToString());

        if(Connection.isWifiConnection(getActivity())) {
            new UploadImage(((OnHttpRequestDoneListener) getActivity()), up, image, CameraViewActivity.SERVER).execute();

            dismiss();
        } else {
            boolean savedInternal = saveImageInternally(image);

            Activity activity = getActivity();

            if(savedInternal){
                if (activity instanceof OnSavedInternalListener) {
                    ((OnSavedInternalListener) getActivity()).onSavedInternalSuccess();

                    dismiss();
                }
            } else {
                if (activity instanceof OnSavedInternalListener) {
                    ((OnSavedInternalListener) getActivity()).onSavedInternalError();
                }
            }
        }
    }

    private void showInfo() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.remove(this);

        ft.addToBackStack(null);

        InfoDialogFragment idf = InfoDialogFragment.newInstance();

        idf.show(ft, InfoDialogFragment.INFO_DIALOG_FRAGMENT);
    }

    private void cancel() {
        Activity activity = getActivity();

        if (activity instanceof OnDialogFragmentClosedListener) {
            ((OnDialogFragmentClosedListener) getActivity()).onDialogFragmentClosed();
        }

        dismiss();
    }

    private boolean saveImageInternally(Image image){
        DBHelper db = new DBHelper(getActivity());

        String name = AccessStorage.saveImageToInternalStorage(getActivity(), image.getBitmap());

        if(name != null){
            image.setName(name);

            boolean inserted = db.insertImage(image.getName(), image.getEmail());

            if(inserted){
                return true;
            } else {
                AccessStorage.deleteImageFromInternalStorage(getActivity(), image.getName());
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnUpload:
                startUploadImage();
                break;
            case R.id.btnEditEmailAddress:
                editEmailAddress();
                break;
            case R.id.btnInfo:
                showInfo();
        }
    }
}
