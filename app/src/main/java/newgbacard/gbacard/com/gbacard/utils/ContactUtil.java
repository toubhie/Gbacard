package newgbacard.gbacard.com.gbacard.utils;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.models.Register;

/**
 * Created by Williamz on 14-Jul-16.
 */
public class ContactUtil {

    private Preferences pref;

    public void createContact(Context context, Register register) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Display name/Contact name
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, register.getFirstName() + " " + register.getLastName())
                .build());

        //Phone Number
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, register.getPhoneNumber())
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "1").build());

        //Email details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, register.getEmail())
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "2").build());


        //Postal Address

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POBOX, register.getAddress())

						/*.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, "street")

						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, "city")

						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, "region")

						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, "postcode")

						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, "country")*/

                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, "3")


                .build());


		/*//Organization details
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, "Devindia")
						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.Organization.TITLE, "Developer")
						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.Organization.TYPE, "0")

						.build());*/
        //IM details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Im.DATA, "ImName")
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Im.DATA5, "2")


                .build());
        try {
            ContentProviderResult[] res = context.getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void createContact(Context context, Contact contact) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Display name/Contact name
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getFirstName()+ " " + contact.getLastName())
                .build());

        //Phone Number
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhoneNumber())
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "1").build());

        //Email details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, contact.getEmail())
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "2").build());


        //Postal Address

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POBOX, contact.getAddress())

						/*.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, "street")

						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, "city")

						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, "region")

						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, "postcode")

						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, "country")*/

                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, "3")


                .build());


		/*//Organization details
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, "Devindia")
						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.Organization.TITLE, "Developer")
						.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE )
						.withValue(ContactsContract.CommonDataKinds.Organization.TYPE, "0")

						.build());*/
        //IM details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Im.DATA, "ImName")
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Im.DATA5, "2")


                .build());
        try {
            ContentProviderResult[] res = context.getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getLookupKey(Context context, String searchName){
        boolean foundToken = false;
        // IDENTIFY Contact based on name and token
        String szLookupKey = "";
        Uri lkup = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, searchName);
        ContentResolver cr = context.getContentResolver();
        Cursor idCursor = context.getContentResolver().query(lkup, null, null, null, null);
        // get all the names
        while (idCursor.moveToNext()) {
            // get current row/contact ID = ID's are unreliable, so we will go for the lookup key
            String szId = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
            String szName = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            szLookupKey = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));

            // for this contact ID, search the custom field
            Log.d(Constants.TAG_GBACARD, "Searching token:" + szId + " Name:" + szName + " LookupKey:" + szLookupKey);
            //Log.d(LOG_TAG, "search: "+lid + " key: "+key + " name: "+name);
            String tokenWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] tokenWhereParams = new String[]{szId, Constants.TAG_MIMETYPE_RADUTOKEN};
            Cursor tokenCur = cr.query(ContactsContract.Data.CONTENT_URI, null, tokenWhere, tokenWhereParams, null);

            while (tokenCur.moveToNext()) {
                String token = tokenCur.getString(tokenCur.getColumnIndex(ContactsContract.Data.DATA1));
                if (Constants.TAG_SZ_TOKEN.compareTo(token) == 0) {
                    Log.e(Constants.TAG_GBACARD, "Target Token found: " + token);
                    tokenCur.close();
                    foundToken = true;
                    break;
                } else 	Log.d(Constants.TAG_GBACARD, "Another Token found: " + token);
            }
            tokenCur.close();
            if (foundToken) break;
        }
        idCursor.close();
        if (foundToken) {
            Log.d(Constants.TAG_GBACARD, "Using szLookupKey:" + szLookupKey);
        }
        return szLookupKey;
    }

    public void getVCardString(Context context, String searchName) {

        Cursor cursor;
        try {
            cursor = context.getContentResolver().
                    query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, null, null, null);
            Log.i("TAG two", "cursor" + cursor);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Log.i("Number of contacts", "cursorCount" + cursor.getCount());

                getContactUsingLookUpKey(context, cursor, searchName);
                cursor.moveToNext();
            } else {
                Log.i("TAG", "No Contacts in Your Phone");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getContactUsingLookUpKey(Context context, Cursor cursor, String searchName) {
        String lookupKey =  getLookupKey(context, searchName);
        Log.i(Constants.TAG_GBACARD, "lookupKey: " + lookupKey);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);

        File file;
        String vFile = "";
        String storagePath;
        ArrayList<String> vCard = new ArrayList<String>();
        pref = new Preferences(context);
        try {
            AssetFileDescriptor fd = context.getContentResolver().openAssetFileDescriptor(uri, "r");

            FileInputStream fis = fd.createInputStream();
            byte[] buf = new byte[(int) fd.getDeclaredLength()];
            fis.read(buf);

            String vcardstring = new String(buf);
            vCard.add(vcardstring);
            storagePath = Constants.CONTACTS_PATH + vFile;
            Log.i(Constants.TAG_GBACARD, "Storage Path: " + storagePath);

            pref.saveStoragePath(storagePath);
            Log.i(Constants.TAG_GBACARD, "Storage Path saved ");

            file = new File(storagePath, vFile);
            file.createNewFile();

            FileOutputStream mFileOutputStream = new FileOutputStream(file, true);
            mFileOutputStream.write(vcardstring.toString().getBytes());
            mFileOutputStream.close();

        } catch (Exception e1)  {
            e1.printStackTrace();
        }
    }

   /* public void fetchValues(Context context, ) {
        // Set up the projection
        String[] projection = {
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Contacts.Data.DATA1,
                ContactsContract.Contacts.Data.MIMETYPE};

        // Query ContactsContract.Data
        cursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI, projection,
                ContactsContract.Data.DISPLAY_NAME + " = ?",
                new String[]{name},
                null);

        if (cursor.moveToFirst()) {
            // Get the indexes of the MIME type and data
            mimeIdx = cursor.getColumnIndex(
                    ContactsContract.Contacts.Data.MIMETYPE);
            dataIdx = cursor.getColumnIndex(
                    ContactsContract.Contacts.Data.DATA1);

            // Match the data to the MIME type, store in variables
            do {
                mime = cursor.getString(mimeIdx);
                if (ContactsContract.CommonDataKinds.Email
                        .CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
                    email = cursor.getString(dataIdx);
                    Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
                }
                if (ContactsContract.CommonDataKinds.Phone
                        .CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
                    phone = cursor.getString(dataIdx);
                    phone = PhoneNumberUtils.formatNumber(phone);
                    //Toast.makeText(getApplicationContext(), phone, Toast.LENGTH_SHORT).show();
                }


            } while (cursor.moveToNext());

        }
    }*/
}
