package newgbacard.gbacard.com.gbacard.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.models.Contact;

/**
 * Created by Williamz on 18-Jul-16.
 */
public class ContactUserAdapter extends BaseAdapter {
    public List<Contact> _data;
    private ArrayList<Contact> contactList;
    Context context;
    ViewHolder viewHolder;

    public ContactUserAdapter(List<Contact> selectUsers, Context con) {
        _data = selectUsers;
        context = con;
        this.contactList = new ArrayList<Contact>();
        this.contactList.addAll(_data);
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //view = li.inflate(R.layout.contact_list_item, null);
            view = li.inflate(R.layout.contact_list_item, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        viewHolder = new ViewHolder();

        viewHolder.name = (TextView) view.findViewById(R.id.text_name);

        final Contact contact = (Contact) _data.get(i);

        String fullName = contact.getFirstName()+ " " + contact.getLastName();
        viewHolder.name.setText(fullName);
/*
        // Set image if exists
        try {

            if (data.getThumb() != null) {
                v.imageView.setImageBitmap(data.getThumb());
            } else {
                v.imageView.setImageResource(R.drawable.image);
            }
            // Seting round image
            Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.image); // Load default image
            roundedImage = new RoundImage(bm);
            v.imageView.setImageDrawable(roundedImage);
        } catch (OutOfMemoryError e) {
            // Add default picture
            v.imageView.setImageDrawable(this.context.getDrawable(R.drawable.image));
            e.printStackTrace();
        }*/

        //Log.e("Image Thumb", "--------------" + data.getThumb());

        /*// Set check box listener android
        v.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    data.setCheckedBox(true);
                  } else {
                    data.setCheckedBox(false);
                }
            }
        });*/

        view.setTag(contact);
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(contactList);
        } else {
            for (Contact contact : contactList) {
                String fullName = contact.getFirstName() + " " + contact.getLastName();

                if (fullName.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    _data.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }
    static class ViewHolder {
        TextView name;
    }
}
