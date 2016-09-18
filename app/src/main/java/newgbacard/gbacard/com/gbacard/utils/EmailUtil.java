package newgbacard.gbacard.com.gbacard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Williamz on 17-Jul-16.
 */
public class EmailUtil {

    public void sendContactByEmail(final Context context, final String storagePath, final String subject) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    Toast.makeText(context, storagePath, Toast.LENGTH_SHORT).show();

                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    // Add attributes to the intent
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject + "'s Contact");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my contact.");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(storagePath)));
                    sendIntent.setType("vnd.android.cursor.dir/email");

                    context.startActivity(Intent.createChooser(sendIntent, "Email:"));
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendPlainEmail(final Context context, final String emailAddress){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    if (emailAddress.length() != 0){

                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                        context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
                    }else{
                        Toast.makeText(context, "No email available for this contact", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                            e.printStackTrace();}
            }
        });
    }
}
