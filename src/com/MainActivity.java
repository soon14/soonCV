package com;

import java.io.FileNotFoundException;

import org.opencv.android.BaseLoaderCallback;  
import org.opencv.android.LoaderCallbackInterface;  
import org.opencv.android.OpenCVLoader;  

import com.example.grapprocess2.R; 
import com.show.Show;

import android.net.Uri;
import android.os.Bundle;  
import android.app.Activity;  
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;   
import android.util.Log;
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.ImageView;   
  
public class MainActivity extends Activity implements OnClickListener{  
  
    private Button btnProc;  
    private Button btnOpengl;
    private Button btnFaceDetect;
    private ImageView imageView;  
    private Uri imgUri;   
     
    //OpenCV�����ز���ʼ���ɹ���Ļص��������ڴ����ǲ������κβ���  
     private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {  
        @Override  
        public void onManagerConnected(int status) {  
            switch (status) {  
                case LoaderCallbackInterface.SUCCESS:{  
                    System.loadLibrary("image_proc");  
                } break;  
                default:{  
                    super.onManagerConnected(status);  
                } break;  
            }  
        }  
    };  
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        btnProc = (Button) findViewById(R.id.btn_gray_process);  
        btnOpengl=(Button)findViewById(R.id.btn_opengl_process);
        btnFaceDetect=(Button)findViewById(R.id.btn_facedetect);
        imageView = (ImageView) findViewById(R.id.image_view);  
        //��lenaͼ����س����в�������ʾ  
       //  bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mypic);  
       // imageView.setImageBitmap(bmp);  
        btnProc.setOnClickListener(this);  
        btnOpengl.setOnClickListener(listener);
        btnFaceDetect.setOnClickListener(listenerFace);
    }  
  
    @Override  
    public void onClick(View v) {  
           
      /*  int w = bmp.getWidth();  
        int h = bmp.getHeight();  
        int[] pixels = new int[w*h];       
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);  
        int[] resultInt = ImageProc.grayProc(pixels, w, h);  
        Bitmap resultImg = Bitmap.createBitmap(w, h, Config.ARGB_8888);  
        resultImg.setPixels(resultInt, 0, w, 0, 0, w, h);  
        imageView.setImageBitmap(resultImg);  */    
    	 Intent intent = new Intent();  
         /* ����Pictures����Type�趨Ϊimage */  
         intent.setType("image/*");  
         /* ʹ��Intent.ACTION_GET_CONTENT���Action */  
         intent.setAction(Intent.ACTION_GET_CONTENT);   
         /* ȡ����Ƭ�󷵻ر����� */  
         startActivityForResult(intent, 1);
    }  
      
    @Override  
    public void onResume(){  
        super.onResume();  
        //ͨ��OpenCV���������ز���ʼ��OpenCV��⣬��νOpenCV���������  
        //OpenCV_2.4.3.2_Manager_2.4_*.apk�������������OpenCV��װ����apkĿ¼��  
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);  
    }  
    private OnClickListener listener = new OnClickListener()

    {

		@Override

		public void onClick(View v) {

				//int w = bmp.getWidth();  
				//int h = bmp.getHeight();  
	        	//int[] pixels = new int[w*h];       
	        	//bmp.getPixels(pixels, 0, w, 0, 0, w, h);  
	        	//float[] resultInt = ImageProc.getDataProc(pixels, w, h);
		        Intent intent = new Intent(); 
				intent.setClass(MainActivity.this, Show.class);
				// getImageData(grayMat);
				//intent.putExtra("imgData",resultInt);
				//intent.putExtra("width",w);
				//intent.putExtra("height",h);
				intent.setData(imgUri);
				startActivity(intent); 
				//startActivityForResult(intent, REQUEST_CODE);
		}

    };
    private OnClickListener listenerFace = new OnClickListener()

    {

		@Override

		public void onClick(View v) {
				//if(imgUri!=null)
				{
			        Intent intent = new Intent(); 
					intent.setClass(MainActivity.this, FaceDetectActivity.class); 
					//intent.setData(imgUri);
				    //intent.putExtra("bitmap", bmp);
					startActivity(intent); 
				}
		}

    };
    @Override

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		 if (resultCode == RESULT_OK) {  
            imgUri = data.getData();  
            Log.e("uri", imgUri.toString());  
            ContentResolver cr = this.getContentResolver();  
            try {  
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(imgUri));  
                //ImageView imageView = (ImageView) findViewById(R.id.iv01);  
                /* ��Bitmap�趨��ImageView */  
                imageView.setImageBitmap(bitmap);  
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }  
        }  
		super.onActivityResult(requestCode, resultCode, data);
 

	}
    public static Bitmap decodeSampledBitmapFromResource(Resources res,  
            int resId, int reqWidth, int reqHeight) {  
        // ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С  
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeResource(res, resId, options);  
        // �������涨��ķ�������inSampleSizeֵ  
        options.inSampleSize = calculateInSampleSize(options, reqWidth,  
                reqHeight);  
        // ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ  
        options.inJustDecodeBounds = false;  
        return BitmapFactory.decodeResource(res, resId, options);  
    }  
  
    public static int calculateInSampleSize(BitmapFactory.Options options,  
            int reqWidth, int reqHeight) {  
        // ԴͼƬ�ĸ߶ȺͿ��  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
        if (height > reqHeight || width > reqWidth) {  
            // �����ʵ�ʿ�ߺ�Ŀ���ߵı���  
            final int heightRatio = Math.round((float) height  
                    / (float) reqHeight);  
            final int widthRatio = Math.round((float) width / (float) reqWidth);  
            // ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�  
            // һ��������ڵ���Ŀ��Ŀ�͸ߡ�  
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
        }  
        return inSampleSize;  
    }  
}  