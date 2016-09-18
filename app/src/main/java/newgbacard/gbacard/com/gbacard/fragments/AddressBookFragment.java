package newgbacard.gbacard.com.gbacard.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.activities.ContactDetailActivity;

/**
 * Created by Williamz on 13-Jul-16.
 */
public class AddressBookFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter mAdapter;
    private TextView mTextView;
    private ListView listView;

    // columns requested from the database
    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID, // _ID is always required
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
    };


    // this goes in the CursorLoader parameter list, it filters
    // out only those contacts who have a phone number
    private static final String SELECTION =
            ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";

    // and name should be displayed in the text1 textview in item layout
    private static final String[] FROM = { ContactsContract.Contacts.DISPLAY_NAME_PRIMARY };
    private static final int[] TO = { android.R.id.text1 };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create adapter once
        Context context = getActivity();
        int layout = android.R.layout.simple_list_item_1;
        Cursor c = null; // there is no cursor yet
        int flags = 0; // no auto-requery! Loader requeries.
        mAdapter = new SimpleCursorAdapter(context, layout, c, FROM, TO, flags);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view =  inflater.inflate(R.layout.fragment_addressbook,
                container, false);
        listView = (ListView) view.findViewById(R.id.list);

        return view;
    }

    public void setUpViews(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // each time we are started use our listadapter
        setListAdapter(mAdapter);
        // and tell loader manager to start loading
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // load from the "Contacts table"
        Uri contentUri = ContactsContract.Contacts.CONTENT_URI;

        // no sub-selection, no sort order, simply every row
        // projection says we want just the _id and the name column
        return new CursorLoader(getActivity(),
                contentUri,
                PROJECTION,
                SELECTION,
                null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Once cursor is loaded, give it to adapter
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // on reset take any old cursor away
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        mTextView = (TextView)v.findViewById(android.R.id.text1);
        //Toast.makeText(getActivity(), mTextView.getText()+"", Toast.LENGTH_SHORT).show();

        Intent in = new Intent(getActivity().getApplicationContext(),
                ContactDetailActivity.class);
        in.putExtra("Name", mTextView.getText().toString());
        startActivity(in);
    }

}
