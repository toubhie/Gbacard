package newgbacard.gbacard.com.gbacard.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.activities.ContactDetailActivity;
import newgbacard.gbacard.com.gbacard.activities.SettingsActivity;
import newgbacard.gbacard.com.gbacard.adapters.ContactUserAdapter;
import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.utils.Constants;

/**
 * Created by Williamz on 18-Jul-16.
 */
public class AddressBookFragment2 extends Fragment {

    // ArrayList
    ArrayList<Contact> selectUsers;
    List<Contact> temp;
    // Contact List
    ListView listView;
    // Cursor to load contacts list
    Cursor phones, email;

    private MenuItem searchMenuItem;

    // Pop up
    ContentResolver resolver;
    SearchView searchView;
    ContactUserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_addressbook, null);

        selectUsers = new ArrayList<Contact>();
        resolver = getActivity().getContentResolver();
        listView = (ListView) view.findViewById(R.id.list);

        try {

            phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            LoadContact loadContact = new LoadContact();
            loadContact.execute();

            setHasOptionsMenu(true);
        } catch (Exception e){
            e.printStackTrace();
        }

       /* search = (SearchView) view.findViewById(R.id.searchView);

        /*//*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                adapter.filter(newText);
                return false;
            }
        });*/

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_addressbook_fragment, menu);

        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(listener);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // newText is text entered by user to SearchView
            adapter.filter(newText);
            return false;
        }
    };


    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    Toast.makeText(getActivity(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }

                while (phones.moveToNext()) {
                    Bitmap bitmapThumbnail = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String displayName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String email = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    String address = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                    String imageThumbnail = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                    try {
                        if (imageThumbnail != null) {
                            bitmapThumbnail = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(imageThumbnail));
                        } else {
                           // Log.e("No Image Thumb", "--------------");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Contact contact = new Contact();
                    contact.setThumb(bitmapThumbnail);
                    contact.setFirstName(displayName);
                    contact.setLastName("");
                    contact.setPhoneNumber(phoneNumber);
                    contact.setEmail(email);
                    contact.setAddress(address);

                    selectUsers.add(contact);
                }
            }
            //phones.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ContactUserAdapter(selectUsers, getActivity());
            listView.setAdapter(adapter);

            // Select item on listclick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        Contact contact = selectUsers.get(i);
                        Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
                        intent.putExtra(Constants.TAG_CONTACT_DETAIL, contact);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //listView.setFastScrollEnabled(true);
        }
    }
}
