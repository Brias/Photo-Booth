package de.braeuer.matthias.photobooth;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Matze on 11.06.2016.
 */
public class EmailAddressArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;

    private ArrayList<String> data;

    public EmailAddressArrayAdapter(Context context, int resource, ArrayList<String> data) {
        super(context, resource, data);

        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        final EmailAddressHolder holder;

        if(row == null){
            holder = new EmailAddressHolder();

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row = inflater.inflate(resource, parent, false);

            TextView tv = (TextView) row.findViewById(R.id.emailAddressView);
            Button btn = (Button) row.findViewById(R.id.removeItem);

            holder.setTextView(tv);
            holder.setButton(btn);

            row.setTag(holder);
        }else{
            holder = (EmailAddressHolder) row.getTag();
        }

        holder.getTextView().setText(data.get(position));


        final int index = position;

        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ONCLICK", "" + index);
                data.remove(index);
                notifyDataSetChanged();
            }
        });

        return row;
    }

    public static class EmailAddressHolder {
        private TextView tv;
        private Button button;

        public TextView getTextView(){
            return tv;
        }

        public void setTextView(TextView textView){
            tv = textView;
        }

        public Button getButton(){
            return button;
        }

        public void setButton(Button button){
            this.button = button;
        }
    }
}
