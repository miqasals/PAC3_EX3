package edu.uoc.pec3.android.contactlist.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.uoc.pec3.android.contactlist.R;
import edu.uoc.pec3.android.contactlist.manager.FirebaseContactManager;
import edu.uoc.pec3.android.contactlist.model.Contact;

/**
 * Created by mgarcia on 24/03/2016.
 *
 * Miquel Casals: By implementing the ValueEventListener the class inherits the methods onDataChange
 * and onCancelled.
 */
public class SplashActivity extends AppCompatActivity implements ValueEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Get a reference to the URL data and
        FirebaseContactManager.getInstance().getContactFromServer(this);
    }



    // ValueEventListener child method
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        /*
         * For every contact snapshot of the database snapshot, add the contact object to the
         * hashmap of the FirebaseContactManager class.
         */
        for (DataSnapshot contact: dataSnapshot.getChildren()) {
            FirebaseContactManager.getInstance().addContactHashMap(contact.getValue(Contact.class));
        }
        startMainActivity();
    }

    /*
     * ValueEventListener child method. Called when the client doesn't have permission to read from
     * the source.
     */
    @Override
    public void onCancelled(FirebaseError firebaseError) {
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, ContactsList.class);
        startActivity(intent);
    }
}
