package edu.uoc.pec3.android.contactlist.views;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.uoc.pec3.android.contactlist.adapters.ContactListAdapter;
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
        final ArrayList<Contact> contacts = new ArrayList<>(FirebaseContactManager.getInstance().getAllContacts());


        // Init adapter and pupulates the view.
        ContactListAdapter contactListAdapter = new ContactListAdapter(this, R.id.contact_list, contacts);
        mListView.setAdapter(contactListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ContactDetail.class);
                intent.putExtra("contactId", contacts.get(position).getObjectId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to close the application?")
                .setTitle("Exit application")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent a = new Intent(Intent.ACTION_MAIN);
                                a.addCategory(Intent.CATEGORY_HOME);
                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                            }
                        })
                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    /**
     * TODO: Implement actionBar and add the "new user" icon
     * TODO: Implement the new user function
     * TODO: Create the user data request layout.
     * This layout must:
     *  - Check the format of the data introduced on EditText.
     *  - Contain a button for get a picture from the camera
     *  - Conatin a button for get the location ¿¿¿???? (a new map activity with an acceptance button?)
     * TODO: Indicate the source of the onBackPressed() method code.
     */

}
