package newgbacard.gbacard.com.gbacard.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Williamz on 17-Jul-16.
 */
public class SMSUtil {

    private MessageUtil messageUtil;

    public void sendContactBySms(final Context context, final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Warning");
                builder.setMessage("This option works only when the receiver also has the gbacard application installed");
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendSMS(context, message);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void sendSMS(Context context, String message) {

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", "");
        smsIntent.putExtra("sms_body", message);

        try {
            context.startActivity(smsIntent);
            //context.finish();
            Log.i("Finished sending SMS", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMSUsingPhoneNumber(final Context context, final String phoneNumber) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Uri smsUri = Uri.parse("tel:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                intent.putExtra("address", phoneNumber);
                intent.setType("vnd.android-dir/mms-sms");
                context.startActivity(intent);
            }
        });
    }

    public void sendContactExchangeSMS(final Context context, final String senderPhoneNumber, final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent sentIntent = new Intent(Constants.SENT);
                    Intent deliveredIntent = new Intent(Constants.DELIVERED);

                    /*Create the pending that will be broadcast when an sms is successfully sent */
                    PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    /*Create the pending that will be broadcast when an sms is successfully delivered */
                    PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, deliveredIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    /*Register for SMS send action to be able to receive updates about the SMS condition*/
                    context.registerReceiver(new BroadcastReceiver() {

                        @Override
                        public void onReceive(Context arg0, Intent arg1) {
                            // TODO Auto-generated method stub
                            String result = "";
                            switch (getResultCode()) {
                                case Activity.RESULT_OK:
                                    result = "SMS Successfully Sent";
                                    break;
                                case android.telephony.SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                    result = "SMS failed";
                                    break;
                                case android.telephony.SmsManager.RESULT_ERROR_RADIO_OFF:
                                    result = "Radio off";
                                    break;
                                case android.telephony.SmsManager.RESULT_ERROR_NULL_PDU:
                                    result = "No PDU defined";
                                    break;
                                case android.telephony.SmsManager.RESULT_ERROR_NO_SERVICE:
                                    result = "No service";
                                    break;
                            }
                            MessageUtil.showShortToast(context, result);

                        }
                    }, new IntentFilter(Constants.SENT));

                    /*Register for delivery intent*/
                    context.registerReceiver(new BroadcastReceiver() {

                        @Override
                        public void onReceive(Context arg0, Intent arg1) {
                            // TODO Auto-generated method stub
                            MessageUtil.showShortToast(context, "Message has been delivered");
                        }
                    }, new IntentFilter(Constants.DELIVERED));

                    android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                    smsManager.sendTextMessage(senderPhoneNumber, null, message, sentPI, deliveredPI);
                    //showtoast("SMS sent");
                } catch (Exception e) {
                    MessageUtil.showShortToast(context, "SMS failed, please try again later");
                    e.printStackTrace();
                }
            }
        });
    }
}