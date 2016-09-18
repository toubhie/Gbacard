package newgbacard.gbacard.com.gbacard.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import newgbacard.gbacard.com.gbacard.scannerUtils.Contents;
import newgbacard.gbacard.com.gbacard.scannerUtils.QRCodeEncoder;

/**
 * Created by Williamz on 17-Jul-16.
 */
public class BarcodeUtil {

    private QRCodeEncoder qrCodeEncoder;

    public void showBarcode(final Context context, final Bundle bundle, final ImageView barcodeImageView){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Find screen size
                    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3/4;

                    //Encode with a QR Code image
                    qrCodeEncoder = new QRCodeEncoder(null,
                            bundle,
                            Contents.Type.CONTACT,
                            BarcodeFormat.QR_CODE.toString(),
                            smallerDimension);
                    try {
                        Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                        barcodeImageView.setImageBitmap(bitmap);
                        barcodeImageView.setVisibility(View.VISIBLE);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
