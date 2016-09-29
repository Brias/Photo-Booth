package de.braeuer.matthias.photobooth;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.braeuer.matthias.photobooth.listener.OnEmailAddressRemovedListener;

/**
 * This class holds email addresses which should be shown in a list view
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: EmailAddressArrayAdapter.java,v 1.0 2016/09/29 16:56:00 Exp $
 */
public class EmailAddressArrayAdapter extends ArrayAdapter<String> {

    public static class EmailAddressHolder {
        private TextView tv;
        private Button button;

        public TextView getTextView() {
            return tv;
        }

        public void setTextView(TextView textView) {
            tv = textView;
        }

        public Button getButton() {
            return button;
        }

        public void setButton(Button button) {
            this.button = button;
        }
    }

    private Context context;
    private int resource;
    private ArrayList<String> data;
    private OnEmailAddressRemovedListener listener;

    public EmailAddressArrayAdapter(Context context, int resource, ArrayList<String> data,
                                    OnEmailAddressRemovedListener listener) {
        super(context, resource, data);

        this.context = context;
        this.resource = resource;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final EmailAddressHolder holder;

        if (row == null) {
            holder = new EmailAddressHolder();

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row = inflater.inflate(resource, parent, false);

            TextView tv = (TextView) row.findViewById(R.id.emailAddressView);
            Button btn = (Button) row.findViewById(R.id.removeItem);

            holder.setTextView(tv);
            holder.setButton(btn);

            row.setTag(holder);
        } else {
            holder = (EmailAddressHolder) row.getTag();
        }

        holder.getTextView().setText(data.get(position));


        final int index = position;

        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(index);
                notifyDataSetChanged();
                listener.onEmailAddressRemoved();
            }
        });

        return row;
    }
}
