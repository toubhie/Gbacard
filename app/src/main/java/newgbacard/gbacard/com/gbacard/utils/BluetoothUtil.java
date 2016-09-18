package newgbacard.gbacard.com.gbacard.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Williamz on 17-Jul-16.
 */
public class BluetoothUtil {

    public void sendContactByBluetooth(final Context context, final String storagePath){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(context, storagePath, Toast.LENGTH_SHORT).show();

                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        MessageUtil.showAlert(context, "Error!", "Your Device does not support the Bluetooth Feature.");
                    }
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        ((Activity) context).startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
                    }

                    // bring up Android chooser
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(storagePath)));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
