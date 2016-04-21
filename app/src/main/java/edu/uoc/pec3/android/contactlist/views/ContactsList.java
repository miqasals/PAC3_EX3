package edu.uoc.pec3.android.contactlist.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import edu.uoc.pec3.android.contactlist.Adapters.ContactListAdapter;
import edu.uoc.pec3.android.contactlist.R;
import edu.uoc.pec3.android.contactlist.manager.FirebaseContactManager;
import edu.uoc.pec3.android.contactlist.model.Contact;


/*
 * Created by Miquel Casals on 18/04/2016.
 *
 * This class controls the contact list. It populates the list, manage the adapter,
 * inflates the view and manage the behaviour of the element cliks.
 */
public class ContactsList extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        // Get the ListView.
        ListView mListView = (ListView) findViewById(R.id.contact_list);

        // Define the array values from FireBaseContactManager.
        ArrayList<Contact> contacts = new ArrayList<>(FirebaseContactManager.getInstance().getAllContacts());

        Log.i("///////////////////////", contacts.toString());

        // Init adapter and pupulates the view.
        ContactListAdapter contactListAdapter = new ContactListAdapter(this, contacts);
        mListView.setAdapter(contactListAdapter);
    }


}
