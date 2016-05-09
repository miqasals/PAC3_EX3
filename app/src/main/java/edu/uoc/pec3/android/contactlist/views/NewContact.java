package edu.uoc.pec3.android.contactlist.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import edu.uoc.pec3.android.contactlist.R;
import edu.uoc.pec3.android.contactlist.model.Contact;
import edu.uoc.pec3.android.contactlist.model.GeoLocation;

public class NewContact extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    Contact contact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_contact_main);


    }


    public void setLocation (View view){
        //On edit location button clicked, launch the place picker.

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            builder.setLatLngBounds(new LatLngBounds(
                    new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090)));
            Intent intent = builder.build(NewContact.this);
            startActivityForResult( intent, 2 );
        } catch ( GooglePlayServicesRepairableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesRepairableException thrown" );
        } catch ( GooglePlayServicesNotAvailableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                //contact.setCity(place.);
                contact.setLocation(new GeoLocation(place.getLatLng()));

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.saveUser:
                // If the "save" button is pressed call the saveContact method.
                saveContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveContact() {
        // TODO: Insert the contact to Firebase.
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(NewContact.this, "Insertion cancelled", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
