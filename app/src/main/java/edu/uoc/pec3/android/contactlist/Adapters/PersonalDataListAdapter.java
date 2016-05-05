package edu.uoc.pec3.android.contactlist.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.uoc.pec3.android.contactlist.R;


/**
 * Created by Miquel Casals on 27/04/2016.
 *
 */
public class PersonalDataListAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> personalDataTxt;
    private ArrayList<String> personalDataTitle;




    public PersonalDataListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.personalDataTxt = (ArrayList<String>) objects;
        personalDataTitle = new ArrayList<>();
        personalDataTitle.add("Gender");
        personalDataTitle.add("Birth date");
        personalDataTitle.add("Email");
        personalDataTitle.add("Phone");
        personalDataTitle.add("City");
        personalDataTitle.add("Country");
        personalDataTitle.add("Last update");
        personalDataTitle.add("Creation date");
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_detail_data_item, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.item_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setText(position, viewHolder);
        return convertView;
    }

    private void setText(int position, ViewHolder viewHolder){
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd 'at' hh:mm:ss");
        Date date = null;
        String txt;
        try {
            date = sdfIn.parse(personalDataTxt.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            txt = sdfOut.format(date);
        } else {
            txt = "Not set";
        }
        switch (position) {
            case 1:         //birthday
                viewHolder.txt.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                viewHolder.setText(personalDataTitle.get(position), personalDataTxt.get(position));
                break;
            case 2:         //email
                viewHolder.txt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                viewHolder.setText(personalDataTitle.get(position), personalDataTxt.get(position));
                break;
            case 3:         //phone
                viewHolder.txt.setInputType(InputType.TYPE_CLASS_PHONE);
                viewHolder.setText(personalDataTitle.get(position), personalDataTxt.get(position));
                break;
            case 6:         //updateAt
                viewHolder.txt.setInputType(InputType.TYPE_CLASS_DATETIME);
                viewHolder.setText(personalDataTitle.get(position), txt);
                break;
            case 7:         //createdAt
                viewHolder.txt.setInputType(InputType.TYPE_CLASS_DATETIME);
                viewHolder.setText(personalDataTitle.get(position), txt);
                break;
            default:
                viewHolder.setText(personalDataTitle.get(position), personalDataTxt.get(position));
                break;
        }


    }


    static class ViewHolder {
        protected TextView title;
        protected TextView txt;

        protected void setText(String title, String txt){
            this.title.setText(title);
            this.txt.setText(txt);

        }
    }
}
