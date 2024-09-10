package com.android.kotemanagement.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ConvertImage {
    public static String convertToString(Bitmap bitmap, Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        boolean resultCompress = bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        if(resultCompress) {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if(byteArray.length > 1000000) {
                Toast.makeText(context, "Image Size should be less than 1MB." ,Toast.LENGTH_SHORT).show();
                return null;
            } else {
                return Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
        }
        return null;
    }

    public static Bitmap convertToBitmap(String imageAsString) {
        byte[] byteArray = Base64.decode(imageAsString, Base64.DEFAULT);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmapImage;
    }

}
