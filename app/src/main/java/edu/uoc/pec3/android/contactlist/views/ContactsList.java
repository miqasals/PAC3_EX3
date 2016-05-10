package edu.uoc.pec3.android.contactlist.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.uoc.pec3.android.contactlist.R;
import edu.uoc.pec3.android.contactlist.adapters.ContactListAdapter;
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

        // Initiate the listener for manage the click on any element of the list.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ContactDetail.class);
                // The contactId is passed to the next Activity with the intent.
                intent.putExtra("contactId", contacts.get(position).getObjectId());
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.addUser:
                // If the "new user" button is pressed it starts the new contact Activity.
                Intent i = new Intent(this, NewContact.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * The back button behaviour is modified enabling the option to exit the application. Before exit
     * it require user confirmation.
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to close the application?")
                .setTitle("Exit application")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /**
                                 * Finish the application. Source of the sample:.
                                 * http://stackoverflow.com/questions/3141996/android-how-to-override-the-back-button-so-it-doesnt-finish-my-activity
                                 */
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


}
