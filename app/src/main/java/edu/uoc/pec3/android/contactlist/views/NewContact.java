package edu.uoc.pec3.android.contactlist.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.uoc.pec3.android.contactlist.R;
import edu.uoc.pec3.android.contactlist.model.Contact;
import edu.uoc.pec3.android.contactlist.model.GeoLocation;

/**
 * Created by Miquel Casals on 08/05/2016.
 */

public class NewContact extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int DATE_PICKER_REQUEST = 2;       // Pending implement the date picker.
    private static final int IMAGE_REQUEST = 3;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 0;

    private TextView name;
    private TextView birthday;
    private TextView phone;
    private TextView latLng;
    private EditText city;
    private EditText country;
    private TextView description;
    private ImageView image;
    private TextView email;

    private Contact contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_contact_main);

        // Initialize the contact object.
        contact = new Contact();

        // Get the references of all views
        name = (TextView) findViewById(R.id.newContactName);
        birthday = (TextView) findViewById(R.id.newContactBirthday);
        phone = (TextView) findViewById(R.id.newContactPhone);
        city = (EditText) findViewById(R.id.newContactCity);
        country = (EditText) findViewById(R.id.newContactCountry);
        latLng = (TextView) findViewById(R.id.newContactLatLng);
        description = (TextView) findViewById(R.id.newContactDescription);
        image = (ImageView) findViewById(R.id.newContactImage);
        email = (TextView) findViewById(R.id.newContactEmail);
    }


    /**
     * On edit location button clicked, launch the place picker.
     */
    public void getLocation(View view){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, R.string.location_permissions_denied, Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {

            // Creates the intent builder
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                // build the Intent and start the activity for result.
                Intent intent = builder.build(NewContact.this);
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                Log.d("PlacesAPI Demo", "GooglePlayServicesRepairableException thrown");
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.d("PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown");
            }
        }
    }


    /**
     * When user touch the ImageView the app launches the camera action to take the photo.
     */
    public void getPicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,IMAGE_REQUEST);
        }
    }

    /**
     * Get the data back of all the activities started for result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Geocoder geoCoder;  //Source: http://developer.android.com/intl/es/training/location/display-address.html

        // Chose the activity that finishes and set the next behaviour.
        // LOCATION
        if (requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                // Get the place of the place picker.
                Place place = PlacePicker.getPlace(this, data);
                // Get the latitude and longitude from the Place object and set the contact geoLocation.
                contact.setLocation(new GeoLocation(place.getLatLng()));

                // Set the text of the latitude and longitude text view.
                latLng.setText(contact.getLocation().toString());

                // Initializes the geoCoder object.
                geoCoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;

                /**
                 * Obtain one address from the geoCoder passing the latLng selected by the user. The max
                 * results is set to 1 because we only need the city and the country.
                 */
                try {
                    addresses = geoCoder.getFromLocation(
                            contact.getLocation().getLatitude(),
                            contact.getLocation().getLongitude(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Control that addresses never be empty.
                if (addresses != null && addresses.size() != 0){
                    Address ad = addresses.get(0);
                    // Set the contact fields with the city and the country obtained from geoCode.
                    if (ad.getLocality() != null) {
                        city.setText(ad.getLocality());
                    }
                    if (ad.getCountryName() != null) {
                        country.setText(ad.getCountryName());
                    } 
                } else {
                    // The geoCoder don't bring any address and show an advice to the user to compleed manually.
                    Toast.makeText(NewContact.this, R.string.new_contact_no_addresses,
                            Toast.LENGTH_LONG).show();
                }
            }
        // PICTURE
        } else if (requestCode == IMAGE_REQUEST) {
            if (resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                image.setImageBitmap((Bitmap) extras.get("data"));
                // TODO: Modification. Save the full picture in a file and store the path in contact.
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

    /**
     * Read the text of all views and copy to the contact object.
     * PENDING: Insert in the BBDD the contact.
     */
    private void saveContact() {

        // Date instance for the creation and update information.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar cal = Calendar.getInstance();

        // Sets all the contact fields.
        contact.setName(String.valueOf(name.getText()));
        contact.setBirthday(String.valueOf(birthday.getText()));
        contact.setPhone(String.valueOf(phone.getText()));
        contact.setEmail(String.valueOf(email.getText()));
        contact.setCity(String.valueOf(city.getText()));
        contact.setCountry(String.valueOf(country.getText()));
        contact.setDescription(String.valueOf(description.getText()));
        if (contact.getCreatedAt() == null){
            contact.setCreatedAt(sdf.format(cal.getTime()));
        }
        contact.setUpdatedAt(sdf.format(cal.getTime()));

        // TODO: Insert the contact to Firebase.

        Toast.makeText(NewContact.this, "New contact saved!!", Toast.LENGTH_SHORT).show();

        /**
         * When the contact is stored the activity can be finished. The ContactList class
         * should Override the onResume() method and modify some code in onCreate() for refresh the list
         * from the database information.
         */
        NewContact.this.finish();
    }


    /**
     * Overrides this method for advice to the user that the contact was not saved.
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(NewContact.this, "Insertion cancelled", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

}
