package de.braeuer.matthias.photobooth.dialogs;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import de.braeuer.matthias.photobooth.AccessStorage;
import de.braeuer.matthias.photobooth.BitmapHolder;
import de.braeuer.matthias.photobooth.CameraViewActivity;
import de.braeuer.matthias.photobooth.Connection;
import de.braeuer.matthias.photobooth.DBHelper;
import de.braeuer.matthias.photobooth.EmailAddressManager;
import de.braeuer.matthias.photobooth.Image;
import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.UploadImage;
import de.braeuer.matthias.photobooth.listener.OnHttpRequestDoneListener;
import de.braeuer.matthias.photobooth.listener.OnSavedInternalListener;

/**
 * This class initializes the upload of an image
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: UploadConfirmDialogFragment.java,v 1.0 2016/09/29 16:55:00 Exp $
 */
public class UploadConfirmDialogFragment extends BaseDialogFragment implements View.OnClickListener,
        OnHttpRequestDoneListener, OnSavedInternalListener {
    public static final String UPLOAD_CONFIRM_DIALOG_FRAGMENT = "UploadConfirmDialogFragment";

    private ArrayAdapter<String> adapter;

    public static UploadConfirmDialogFragment newInstance() {
        UploadConfirmDialogFragment udf = new UploadConfirmDialogFragment();

        Bundle bundle = new Bundle();

        udf.setArguments(bundle);

        return udf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getString(R.string.confirm_upload_title));

        setCancelable(false);

        View v = inflater.inflate(R.layout.confirm_upload_dialog_fragment_layout, container, false);

        initButtonListener(v);
        initListView(v);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpload:
                startUploadImage();
                break;
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnBack:
                dismiss();
        }
    }

    @Override
    public void onHttpRequestError(String errorMsg) {
        errorMsg = errorMsg != null ? errorMsg : getResources().getString(R.string.http_request_error);

        showErrorDialog(getResources().getString(R.string.http_request_error_title), errorMsg, false);
    }

    @Override
    public void onHttpRequestSuccess() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.remove(this);

        KeepEmailAddressesDialogFragment kdf = KeepEmailAddressesDialogFragment.newInstance();

        kdf.show(ft, KeepEmailAddressesDialogFragment.KEEP_EMAIL_ADDRESSES_DIALOG_FRAGMENT);
    }

    @Override
    public void onSavedInternalSuccess() {
        cancel();

        showSuccessDialog(getResources().getString(R.string.saved_image_internally_success_title), getResources()
                .getString(R.string.saved_image_internally_success), true);
    }

    @Override
    public void onSavedInternalError() {
        cancel();

        showErrorDialog(getResources().getString(R.string.saved_image_internally_error_title), getResources()
                .getString(R.string.saved_image_internally_error), false);
    }

    private void initButtonListener(View v) {
        Button btnUpload = (Button) v.findViewById(R.id.btnUpload);
        Button btnBack = (Button) v.findViewById(R.id.btnBack);
        Button btnCancel = (Button) v.findViewById(R.id.btnCancel);

        btnUpload.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initListView(View v) {
        ListView lv = (ListView) v.findViewById(R.id.emailAddressList);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, EmailAddressManager
                .getEmailAddresses());

        lv.setAdapter(adapter);
    }

    private void startUploadImage() {
        UploadDialog up = new UploadDialog(getActivity());

        Image image = new Image();

        image.setEmail(EmailAddressManager.addressesToString());

        if (Connection.isWifiConnection(getActivity())) {
            new UploadImage(this, up, image, CameraViewActivity.SERVER).execute();
        } else {
            boolean savedInternal = saveImageInternally(image);

            if (savedInternal) {
                onSavedInternalSuccess();
            } else {
                onSavedInternalError();
            }
        }
    }

    private boolean saveImageInternally(Image image) {
        DBHelper db = new DBHelper(getActivity());

        String name = AccessStorage.saveImageToInternalStorage(getActivity(), BitmapHolder.bm);

        if (name != null) {
            image.setName(name);

            boolean inserted = db.insertImage(image.getName(), image.getEmail());

            if (inserted) {
                return true;
            } else {
                AccessStorage.deleteImageFromInternalStorage(getActivity(), image.getName());
            }
        }

        return false;
    }

    private void showErrorDialog(String title, String errorMsg, boolean callOnClosedListener) {
        ErrorDialogFragment edf = ErrorDialogFragment.newInstance(title, errorMsg, callOnClosedListener);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        edf.show(ft, ErrorDialogFragment.ERROR_DIALOG_FRAGMENT);
    }

    private void showSuccessDialog(String title, String successMsg, boolean callOnClosedListener) {
        SuccessDialogFragment sdf = SuccessDialogFragment.newInstance(title, successMsg, callOnClosedListener);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        sdf.show(ft, SuccessDialogFragment.SUCCESS_DIALOG_FRAGMENT);
    }
}
