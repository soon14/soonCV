package com.show;

import android.content.Context;  
import android.opengl.GLSurfaceView;  
import android.view.MotionEvent;  
  
public class ShowView extends GLSurfaceView {  
    // private static final String LOG_TAG = VortexView.class.getSimpleName();  
    private ShowRenderer _renderer;  
  
    public ShowView(Context context,float[] imgData,int width,int height) {  
        super(context);  
        _renderer = new ShowRenderer(imgData,width,height);  
        setRenderer(_renderer);  
    }  
  
    public boolean onTouchEvent(final MotionEvent event) {  
        queueEvent(new Runnable() {  
            public void run() {  
                //_renderer.setColor(event.getX() / getWidth(), event.getY()  / getHeight(), 1.0f);  
                _renderer.setAngle(event.getX(),event.getY());  
            }  
        });  
        return true;  
    }  
}  