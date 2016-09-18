package newgbacard.gbacard.com.gbacard.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Williamz on 17-Jul-16.
 */
public class ConvertImage {

    public byte[] convertFileToByteArray(String imageFilePath){

        byte[] byteArray = null;

        try {
            File imageFile = new File(imageFilePath);

            FileInputStream fis = new FileInputStream(imageFile);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                    //Writes to this byte array output stream
                    bos.write(buf, 0, readNum);
                    //System.out.println("read " + readNum + " bytes,");
                }
            } catch (IOException ex) {
                Logger.getLogger(ConvertImage.class.getName()).log(Level.SEVERE, null, ex);
            }

            byteArray = bos.toByteArray();

            return byteArray;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public byte[] convertBitmapToByteArray(String imageFilePath) {

        byte[] byteArray = null;

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, blob);
            byteArray = blob.toByteArray();

            return byteArray;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public Bitmap convertByteArrayToBitmap(byte[] byteArray){

        Bitmap bitmap = null;

        try{
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        } catch (Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }

    public String BitmapToBase64(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public Bitmap convertFileToBitmap(String filePath){
        Bitmap bitmap = null;
        try{
            File image = new File(filePath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

        } catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(jo.getString(Constants.TAG_IMAGE_BYTE_ARRAY));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }


}

