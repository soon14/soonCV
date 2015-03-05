package com.show;

import java.io.FileNotFoundException;

import com.ImageProc;
import com.example.grapprocess2.R;

import android.app.Activity;  
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;  
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
  
public class Show extends Activity {  
    //private static final String LOG_TAG = Vortex.class.getSimpleName();  
    private ShowView _vortexView;   
    private Bitmap bmp; 
    public static final int RESULT_CODE=1;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        Intent intent = this.getIntent();
        Uri imgUri = intent.getData(); 
        ContentResolver cr = this.getContentResolver();  
        try {  
        	bmp = BitmapFactory.decodeStream(cr.openInputStream(imgUri));   
        } catch (FileNotFoundException e) {  
            Log.e("Exception", e.getMessage(),e);  
        }   
         if(bmp!=null){
	        int w = bmp.getWidth();  
			int h = bmp.getHeight();  
	    	int[] pixels = new int[w*h];       
	    	bmp.getPixels(pixels, 0, w, 0, 0, w, h);  
	    	float[] imgData = ImageProc.getDataProc(pixels, w, h);
	        //Intent intent = this.getIntent();
	       // float[]imgData = intent.getFloatArrayExtra("imgData");   
	        //int width=intent.getIntExtra("width",0); 
	       // int height=intent.getIntExtra("height",0); 
	    	 requestWindowFeature(Window.FEATURE_NO_TITLE);
	         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
	         _vortexView = new ShowView(this,imgData,w,h);  
        	 setContentView(_vortexView);  
         }
    }  
}
