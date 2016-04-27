package edu.uoc.pec3.android.contactlist.views;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.uoc.pec3.android.contactlist.R;
import edu.uoc.pec3.android.contactlist.manager.FirebaseContactManager;
import edu.uoc.pec3.android.contactlist.model.Contact;

/**
 * Created by Miquel Casals on 18/04/2016.
 *
 * Based on information and samples of the Android Developers web.
 * http://developer.android.com/intl/es/training/animation/screen-slide.html#viewpager
 */


public class ContactDetail extends FragmentActivity {

    private Contact contact;
    /*
     * Number of pages. Personal data, description and Location.
     */
    private static final int NUM_PAGES = 3;

    /*
     * The pager widget, which handles animation and allows swiping horizontally
     * to acces and next wizard steps.
     */
    private ViewPager mPager;

    /*
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);

        // Recive the contactId from previous activity and get the contact full data.
        Intent intent = getIntent();
        String contactId = intent.getStringExtra("contactId");
        contact = FirebaseContactManager.getInstance().getContactByObjectId(contactId);

        // Get the reference of the static view elements.
        ImageView contactImage = (ImageView) findViewById(R.id.contact_detail_image);
        TextView contactName = (TextView) findViewById(R.id.contact_detail_name);

        // Set up the ViewPager.
        mPager = (ViewPager) findViewById(R.id.contact_pager);
        // Create the adapter that returns a fragment.
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        // Attach the adapter to the viewPager.
        mPager.setAdapter(mPagerAdapter);

        // Setting the value of the static view elements
        String url = contact.getImageUrl();
        Picasso.with(this).load(url).into(contactImage);
        contactName.setText(contact.getName());
    }


    // Sets the back button for return to contacts list from any of the pages of ViewPager.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * The adapter extends FragmentStatePagerAdapter that returns a fragment representing
     * a view of the collection of pages.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * When the user changes the page of ViewPager this method is called.
         * @param position of the viewPager.
         * @return Fragment to show at this position.
         */
        @Override
        public Fragment getItem(int position) {
            return ContactDetailPageFragment.create(contact ,position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "PERSONAL DATA";
                case 1:
                    return "DESCRIPTION";
                case 2:
                    return "LOCATION";
                default:
                    return "No data";
            }
        }

    }
}
