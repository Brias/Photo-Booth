package de.braeuer.matthias.photobooth.dialogs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import de.braeuer.matthias.photobooth.EmailAddressArrayAdapter;
import de.braeuer.matthias.photobooth.EmailAddressManager;
import de.braeuer.matthias.photobooth.FragmentHolder;
import de.braeuer.matthias.photobooth.R;
import de.braeuer.matthias.photobooth.listener.OnEmailAddressRemovedListener;

/**
 * Created by Matze on 09.06.2016.
 */
public class EditEmailAddressDialogFragment extends BaseDialogFragment implements View.OnClickListener, OnEmailAddressRemovedListener {

    public static final String EDIT_ADDRESS_DIALOG_FRAGMENT = "EditAddressDialogFragment";

    private EmailAddressArrayAdapter adapter;
    private Button btnAddEmail;
    private Button btnNext;
    private Button btnBack;
    private Button btnCancel;
    private EditText et;

    public static EditEmailAddressDialogFragment newInstance() {
        EditEmailAddressDialogFragment edf = new EditEmailAddressDialogFragment();

        Bundle bundle = new Bundle();

        edf.setArguments(bundle);

        return edf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        checkTitleStaus();
        setCancelable(false);

        View v = inflater.inflate(R.layout.edit_email_address_dialog_fragment_layout, container, false);

        adapter = new EmailAddressArrayAdapter(getActivity(), R.layout.email_list_view_item, EmailAddressManager.getEmailAddresses(), this);

        et = (EditText) v.findViewById(R.id.editEmailAddress);

        btnNext = (Button) v.findViewById(R.id.btnNext);
        btnBack = (Button) v.findViewById(R.id.btnBack);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);

        checkNextButtonStatus();

        btnAddEmail = (Button) v.findViewById(R.id.btnAddEmail);
        btnAddEmail.setEnabled(false);

        initButtonListener();
        initEditTextListener();
        initListView(v);

        setupUI(v);

        return v;
    }

    private void initEditTextListener() {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    btnAddEmail.setEnabled(true);
                } else {
                    btnAddEmail.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initButtonListener() {
        btnAddEmail.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initListView(View v) {
        final ListView lv = (ListView) v.findViewById(R.id.emailAddressList);

        lv.setAdapter(adapter);
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //From http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard visited 24.06.2016
    private void setupUI(View view) {
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(v);
                    return false;
                }

            });
        }

        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                dismiss();
                break;
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnNext:
                showUploadConfirmDialog();
                break;
            case R.id.btnAddEmail:
                addEmail();
                checkNextButtonStatus();
                checkTitleStaus();
                break;
        }
    }

    private void showUploadConfirmDialog(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.remove(this);

        ft.addToBackStack(EDIT_ADDRESS_DIALOG_FRAGMENT);
        FragmentHolder.dialogFragments.add(this);

        UploadConfirmDialogFragment udf = UploadConfirmDialogFragment.newInstance();

        udf.show(ft, UploadConfirmDialogFragment.UPLOAD_CONFIRM_DIALOG_FRAGMENT);
    }

    private void addEmail() {
        String emailAddress = et.getText().toString();

        if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            EmailAddressManager.addEmailAddress(emailAddress);
            adapter.notifyDataSetChanged();

            et.setText("");
        }
    }

    private void checkTitleStaus(){
        if(EmailAddressManager.getEmailAddresses().size() == 0){
          getDialog().setTitle(getResources().getString(R.string.email_edit_dialog_title));
        } else {
            getDialog().setTitle(getResources().getString(R.string.email_edit_remove_dialog_title));
        }
    }

    private void checkNextButtonStatus(){
        if(EmailAddressManager.getEmailAddresses().size() == 0){
            btnNext.setEnabled(false);
        }else{
            btnNext.setEnabled(true);
        }
    }

    @Override
    public void onEmailAddressRemoved() {
        checkTitleStaus();
        checkNextButtonStatus();
    }
}
