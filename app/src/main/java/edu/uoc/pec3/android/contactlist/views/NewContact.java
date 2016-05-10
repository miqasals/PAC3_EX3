package edu.uoc.pec3.android.contactlist.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uoc.pec3.android.contactlist.R;
import edu.uoc.pec3.android.contactlist.model.Contact;
import edu.uoc.pec3.android.contactlist.model.GeoLocation;

public class NewContact extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int DATE_PICKER_REQUEST = 2;
    private static final int IMAGE_REQUEST = 3;

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

        contact = new Contact();

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


    // On edit location button clicked, launch the place picker.
    public void getLocation(View view){

        // Creates the intent builder
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            // build the Intent and start the activity for result.
            Intent intent = builder.build(NewContact.this);
            startActivityForResult( intent, PLACE_PICKER_REQUEST );
        } catch ( GooglePlayServicesRepairableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesRepairableException thrown" );
        } catch ( GooglePlayServicesNotAvailableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
        }
    }



    public void getPicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,IMAGE_REQUEST);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Geocoder geoCoder;  //http://developer.android.com/intl/es/training/location/display-address.html

        // Chose the activity finishes and set the next behaviour.
        if (requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                contact.setLocation(new GeoLocation(place.getLatLng()));
                String latLngTxt = contact.getLocation().toString();

                geoCoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;

                try {
                    addresses = geoCoder.getFromLocation(
                            contact.getLocation().getLatitude(),
                            contact.getLocation().getLongitude(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                latLng.setText(latLngTxt);

                if (addresses != null && addresses.size() != 0){
                    Address ad = addresses.get(0);
                    if (ad.getLocality() != null) {
                        city.setText(ad.getLocality());
                    }
                    if (ad.getCountryName() != null) {
                        country.setText(ad.getCountryName());
                    } 
                } else {
                    Toast.makeText(NewContact.this, "Country and city data not get. Please write it or try again please.",
                            Toast.LENGTH_LONG).show();
                }
            }
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

    private void saveContact() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar cal = Calendar.getInstance();

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

        NewContact.this.finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(NewContact.this, "Insertion cancelled", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

}
