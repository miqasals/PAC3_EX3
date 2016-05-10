package edu.uoc.pec3.android.contactlist.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
 * This class controls the view of the personal data information. It fill all textviews adapting the
 * information to the corresponding type. In the phone element make visible the call button.
 */
public class PersonalDataListAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> personalDataTxt;      // List of the contact data
    private ArrayList<String> personalDataTitle;    // List of the permanent titles


    /**
     * Constructor. Initialize the global fields and fill the list of titles.
     */
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

        // Create the viewHolder element.
        ViewHolder viewHolder;

        /**
         * If the view isn't in memory the viewHolder is created and get the references from the view elements and
         * save the reference of the viewHolder for next use if it's necessary. However, if exist the view of the
         * element, the tag contain the reference to the viewHolder element and it's not necessary create it again
         * saving resources and improve the performance.
         */
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_detail_data_item, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.item_txt);
            viewHolder.btn = (ImageButton) convertView.findViewById(R.id.callButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // With the viewHolder reference setText is called for update the view elements.
        setText(position, viewHolder);
        return convertView;
    }

    private void setText(int position, ViewHolder viewHolder){

        // Read the dates of the contact in date format.
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd 'at' hh:mm:ss");
        Date date;
        String txt;
        try {
            date = sdfIn.parse(personalDataTxt.get(position));
            txt = sdfOut.format(date);
        } catch (ParseException e) {
            txt = context.getString(R.string.personal_data_date_format_error);
        }

        /**
         * Before seting the layout values must be modified the list element type for prevent to
         * display text format errors.
         */
        switch (position) {
            case 1:         //birthday: only date.
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
                viewHolder.btn.setVisibility(View.VISIBLE);
                break;
            case 6:         //updateAt: full date and time.
                viewHolder.txt.setInputType(InputType.TYPE_CLASS_DATETIME);
                viewHolder.setText(personalDataTitle.get(position), txt);
                break;
            case 7:         //createdAt: full date and time
                viewHolder.txt.setInputType(InputType.TYPE_CLASS_DATETIME);
                viewHolder.setText(personalDataTitle.get(position), txt);
                break;
            default:
                viewHolder.setText(personalDataTitle.get(position), personalDataTxt.get(position));
                break;
        }


    }


    /**
     * The ViewHolder class creates a unique instance of the view references avoiding call the method
     * findViewById(...) in multiple ocasions.
     */
    static class ViewHolder {
        protected TextView title;
        protected TextView txt;
        protected ImageButton btn;

        protected void setText(String title, String txt){
            this.title.setText(title);
            this.txt.setText(txt);

        }
    }
}
