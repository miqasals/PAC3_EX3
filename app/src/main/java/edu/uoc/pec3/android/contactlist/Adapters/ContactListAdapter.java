package edu.uoc.pec3.android.contactlist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.uoc.pec3.android.contactlist.R;
import edu.uoc.pec3.android.contactlist.model.Contact;

public class ContactListAdapter extends ArrayAdapter<Contact>{

    private Context context;
    private ArrayList<Contact> contacts;

    public ContactListAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.contacts = (ArrayList<Contact>) objects;
    }

   
    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }


    /*
     * Is mandatory override the getView method for controls the actions to do when
     * in main class is called the method setAdapter.
     *
     * In the method creates a ViewHolder object. If don't still exist the view, it
     * creates the view, the viewHolder object is inicialized and obtain the references
     * of the view of the elements of the item list layout. Otherwise, if the view exists
     * it get back the reference for modify it.
     *
     * When the method has the control of the view elements it set the values and return
     * the view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null || convertView.getTag() == null) {
            // the view don't exist in memory or doesn't exist a viewHolder instance.
            viewHolder = new ViewHolder();
            // Inflate the view.
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
            // Get the reference of view elements.
            viewHolder.contactListImage = (ImageView) convertView.findViewById(R.id.contact_list_image);
            viewHolder.contactListName = (TextView) convertView.findViewById(R.id.contact_list_name);
            // Set the tag of the view with the viewHolder reference for use it in the "else" part.
            convertView.setTag(viewHolder);
        } else {
            // A instance of the view still remains in memory and can recover the reference to viewHolder.
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contact contact = contacts.get(position);

        // Setting the Picasso values.
        ImageView imageView = viewHolder.contactListImage;
        String url = contact.getImageUrl();
        // Setting the view.
        Picasso.with(getContext()).load(url).into(imageView);       //ImageView
        viewHolder.contactListName.setText(contact.getName());      //TextView

        return convertView;
    }



    /*
     * The ViewHolder class it's a recomendation of Android best practices for minimize the
     * .findViewById uses. One object of this class contains the reference of the view elements
     * and we can recover this object, if still exist in memory, and get the reference of the
     * view elements.
     */
    static class ViewHolder {
        protected ImageView contactListImage;
        protected TextView contactListName;
    }

}
