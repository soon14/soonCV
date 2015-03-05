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
     
    //OpenCV类库加载并初始化成功后的回调函数，在此我们不进行任何操作  
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
        //将lena图像加载程序中并进行显示  
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
         /* 开启Pictures画面Type设定为image */  
         intent.setType("image/*");  
         /* 使用Intent.ACTION_GET_CONTENT这个Action */  
         intent.setAction(Intent.ACTION_GET_CONTENT);   
         /* 取得相片后返回本画面 */  
         startActivityForResult(intent, 1);
    }  
      
    @Override  
    public void onResume(){  
        super.onResume();  
        //通过OpenCV引擎服务加载并初始化OpenCV类库，所谓OpenCV引擎服务即是  
        //OpenCV_2.4.3.2_Manager_2.4_*.apk程序包，存在于OpenCV安装包的apk目录中  
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
                /* 将Bitmap设定到ImageView */  
                imageView.setImageBitmap(bitmap);  
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }  
        }  
		super.onActivityResult(requestCode, resultCode, data);
 

	}
    public static Bitmap decodeSampledBitmapFromResource(Resources res,  
            int resId, int reqWidth, int reqHeight) {  
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeResource(res, resId, options);  
        // 调用上面定义的方法计算inSampleSize值  
        options.inSampleSize = calculateInSampleSize(options, reqWidth,  
                reqHeight);  
        // 使用获取到的inSampleSize值再次解析图片  
        options.inJustDecodeBounds = false;  
        return BitmapFactory.decodeResource(res, resId, options);  
    }  
  
    public static int calculateInSampleSize(BitmapFactory.Options options,  
            int reqWidth, int reqHeight) {  
        // 源图片的高度和宽度  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
        if (height > reqHeight || width > reqWidth) {  
            // 计算出实际宽高和目标宽高的比率  
            final int heightRatio = Math.round((float) height  
                    / (float) reqHeight);  
            final int widthRatio = Math.round((float) width / (float) reqWidth);  
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高  
            // 一定都会大于等于目标的宽和高。  
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
        }  
        return inSampleSize;  
    }  
}  