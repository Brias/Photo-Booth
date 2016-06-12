package de.braeuer.matthias.photobooth.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import de.braeuer.matthias.photobooth.EmailAddressArrayAdapter;
import de.braeuer.matthias.photobooth.EmailAddressManager;
import de.braeuer.matthias.photobooth.R;

/**
 * Created by Matze on 09.06.2016.
 */
public class EditEmailAddressDialogFragment extends DialogFragment implements View.OnClickListener {

    private EmailAddressArrayAdapter adapter;
    private Button btnAddEmail;
    private Button btnOk;
    private EditText et;

    public static EditEmailAddressDialogFragment newInstance() {
        EditEmailAddressDialogFragment edf = new EditEmailAddressDialogFragment();

        Bundle bundle = new Bundle();

        edf.setArguments(bundle);

        return edf;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View v = inflater.inflate(R.layout.edit_email_address_dialog_fragment_layout, container, false);

        adapter = new EmailAddressArrayAdapter(getActivity(), R.layout.email_list_view_item, EmailAddressManager.getEmailAddressesCopy());

        et = (EditText) v.findViewById(R.id.editEmailAddress);

        btnOk = (Button) v.findViewById(R.id.btnOk);

        btnAddEmail = (Button) v.findViewById(R.id.btnAddEmail);
        btnAddEmail.setEnabled(false);

        initButtonListener();
        initEditTextListener();
        initListView(v);

        return v;
    }

    private void initEditTextListener(){
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

    private void initButtonListener(){
        btnAddEmail.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    private void initListView(View v) {
        final ListView lv = (ListView) v.findViewById(R.id.emailAddressList);

        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                dismiss();
                break;
            case R.id.btnAddEmail:
                addEmail();
                break;
        }
    }

    private void addEmail(){
        String emailAddress = et.getText().toString();

        if(Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            EmailAddressManager.addEmailAddress(emailAddress);
            adapter.notifyDataSetChanged();
        }
    }
}
