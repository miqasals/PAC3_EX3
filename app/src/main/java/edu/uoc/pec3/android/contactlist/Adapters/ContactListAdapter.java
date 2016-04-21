package edu.uoc.pec3.android.contactlist.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.uoc.pec3.android.contactlist.R;
import edu.uoc.pec3.android.contactlist.model.Contact;

/**
 * Created by Miquel Casals on 18/04/2016.
 */
public class ContactListAdapter extends ArrayAdapter<Contact>{

    private Context context;
    private ArrayList<Contact> contacts;

    private static String TAG = "--ContactListAdapter---";

    public ContactListAdapter(Context context, ArrayList<Contact> list) {
        super(context, R.layout.contact_list);
        this.context = context;
        this.contacts = list;
        Log.i("TAG", "----------------CONSTRUCTOR DE ContactListAdapter");

    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        Log.i("TAG", "----------------PUNT 1");
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            Log.i("TAG", "----------------PUNT 2: convertView == null");

            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);

            viewHolder.contactListImage = (ImageView) convertView.findViewById(R.id.contact_list_image);
            viewHolder.contactListName = (TextView) convertView.findViewById(R.id.contact_list_name);

            convertView.setTag(viewHolder);
        } else {
            Log.i("TAG", "----------------PUNT 3: convertView != null");

            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contact contact = contacts.get(position);
        viewHolder.contactListName.setText(contact.getName());
        if (viewHolder.contactListImage != null) {
            new ImageDownloadTask(viewHolder.contactListImage)
                    .execute(contacts.get(position).getImageUrl());
        }
        return convertView;
    }





    // ASYNKTASK download of images from URL.////////////////////////////////////

    class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloadTask (ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.i("adsfadfad","----------------------");
                }
            }
        }

        private Bitmap downloadBitmap(String url){

            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                //if (statusCode != -1) {
                //    return null;
                //}
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

    }

    //////////////////////////////////////////////////////////////////////////////////


    // TODO: onItemClick managment.
    // http://developer.android.com/intl/es/guide/topics/ui/declaring-layout.html#AdapterViews



    /*
     * The ViewHolder class it's a recomendation of Android best practices for prevent usage of
     * .findViewById more than once.
     */
    static class ViewHolder {
        protected ImageView contactListImage;
        protected TextView contactListName;
    }

}
