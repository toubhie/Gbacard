package newgbacard.gbacard.com.gbacard.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.activities.ContactDetailActivity;
import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.utils.Constants;

/**
 * Created by Williamz on 15-Jul-16.
 */
public class ListeningToSMS extends BroadcastReceiver {

    private String name;
    private String phone;
    private String email;
    private String address;
    private String sender;

    private Contact userContact;

    private int notificationId = 100;

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (intent.getAction().equals(Constants.SMS_RECEIVED)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    // get sms objects
                    Object[] pdus = (Object[]) bundle.get("pdus");

                    /*if (pdus.length == 0) {
                        return;
                    }*/

                    // large message might be broken into many
                    SmsMessage[] fullMessage = new SmsMessage[pdus.length];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < pdus.length; i++) {
                        fullMessage[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sb.append(fullMessage[i].getMessageBody().toString());
                    }
                    sender = fullMessage[0].getOriginatingAddress().toString();
                    String message = sb.toString();

                    String splittedMessage[] = message.split("::");


                    // the arrray of the message will be ispon,name of user, lattitude, longitude. and other things in case
                    if (splittedMessage[0].equalsIgnoreCase(Constants.TAG_GBACARD_PREFIX)) {
                        // prevent any other broadcast receivers from receiving broadcast. Like cases when it is an ispon message, so others would not be able to
                        abortBroadcast();

                        Toast.makeText(context, "GbaCard message has entered", Toast.LENGTH_SHORT).show();

                        Log.i(Constants.TAG_GBACARD, "GbaCard message entered");

                        try {
                            String contactMessage[] = splittedMessage[1].split(",");

                            String nameArray[] = contactMessage[0].split(":");
                            String phoneArray[] = contactMessage[1].split(":");
                            String emailArray[] = contactMessage[2].split(":");
                            String addressArray[] = contactMessage[3].split(":");

                            if (nameArray[0].equalsIgnoreCase("Name")) {
                                name = nameArray[1];
                            } else if (phoneArray[0].equalsIgnoreCase("Phone")) {
                                phone = phoneArray[1];
                            } else if (emailArray[0].equalsIgnoreCase("Email")) {
                                email = emailArray[1];
                            } else if (addressArray[0].equalsIgnoreCase("Address")) {
                                address = addressArray[1];
                            }

                            userContact = new Contact();

                            userContact.setFirstName(name);
                            userContact.setLastName("");
                            userContact.setPhoneNumber(phone);
                            userContact.setEmail(email);
                            userContact.setAddress(address);

                            displayNotificationInActivity(context, userContact);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, name + phone + email + " Contact not saved!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayNotificationInActivity(Context context, Contact userContact) {

        try {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Calling the default notification service
        NotificationCompat.Builder myBuilder = new NotificationCompat.Builder(context);
        myBuilder.setTicker("You have received a new Contact"); //this is the first text that is shown to users b4 notification bar is drawn down
        myBuilder.setSmallIcon(R.drawable.logo3);
        myBuilder.setContentTitle("New Contact");
        //myBuilder.setContentText(salutation + " "+ name+ "'s contact has been added!");
        myBuilder.setAutoCancel(true);
        //myBuilder.addAction(R.drawable.ic_launcher, "", pIntent);//it automatically cancels when a user clicks on it
        //myBuilder.setDefaults(Notification.DEFAULT_SOUND);


        Intent resultIntent = new Intent(context, ContactDetailActivity.class);
        resultIntent.putExtra(Constants.TAG_RECEIVE_AND_SEND, Constants.TAG_RECEIVE_AND_SEND);
        resultIntent.putExtra(Constants.TAG_CONTACT_DETAIL, userContact);
        resultIntent.putExtra(Constants.TAG_SENDER_PHONE_NUMBER, sender);

        /* resultIntent.putExtra(Constants.TAG_NAME, name);
        resultIntent.putExtra(Constants.TAG_PHONE_NUMBER, phone);
        resultIntent.putExtra(Constants.TAG_EMAIL, email);*/


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        myBuilder.setContentIntent(pendingIntent);


        //and finally use the notificationManger to build the whole process
        //notification id allows you update the notification later
        notificationManager.notify(++notificationId, myBuilder.build());

    }
}
