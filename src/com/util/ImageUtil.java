package com.util;

import java.io.FileNotFoundException;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class ImageUtil {
	public static Bitmap loadImage(ContentResolver cr,Uri imgUri){
        try {
            //解码图片大小
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither=false;                     
            options.inPurgeable=true;                 
            options.inInputShareable=true;
            options.inJustDecodeBounds = true;
           
            BitmapFactory.decodeStream(cr.openInputStream(imgUri),null,options);

            //我们想要的新的图片大小
            final int REQUIRED_SIZE=250;
            int scale=1;
            while(options.outWidth/scale/2>=REQUIRED_SIZE && options.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //用inSampleSize解码
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(cr.openInputStream(imgUri), null, o2);
        } catch (FileNotFoundException e) {
        	 Log.e("Exception", e.getMessage(),e);  
        }
        return null;
    }
}
