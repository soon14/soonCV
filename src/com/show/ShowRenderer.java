package com.show;

import java.nio.ByteBuffer;  
import java.nio.ByteOrder;  
import java.nio.FloatBuffer;  
import java.nio.ShortBuffer;  
  



import javax.microedition.khronos.egl.EGLConfig;  
import javax.microedition.khronos.opengles.GL10;  
  



import com.example.grapprocess2.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;  
import android.opengl.GLU;
  
public class ShowRenderer implements GLSurfaceView.Renderer {  
    //private static final String LOG_TAG = VortexRenderer.class.getSimpleName();  
      
    private float _red = 0f;  
    private float _green = 0f;  
    private float _blue = 0f;  
  
    // a raw buffer to hold indices allowing a reuse of points.  
    private ShortBuffer _indexBuffer;  
    private ShortBuffer _indexBufferStatic;  
      
    // a raw buffer to hold the vertices  
    private FloatBuffer _vertexBuffer;  
    private FloatBuffer _vertexBufferStatic;  
      
    private short[] _indicesArray = {0, 1, 2};  
    private int _nrOfVertices = 3;  
  
    private float _angle;  
    private float _dep;
    private float[] imgData;
    private int width=0;
    private int height=0;
  
    public ShowRenderer(float[] imgData,int width,int height){
    	 
    	this.imgData=imgData;
    	this.width=width;
    	this.height=height;
    	this._nrOfVertices=width*height;
    	//_indicesArray=new short[this._nrOfVertices*3];
    	//for(int i=0;i<this._nrOfVertices;i++)
    	{
    		//if(imgData[i*3+2]!=1.0f)
    		{
    			//_indicesArray[i*3+0]=(short) (i*3+0);
    			//_indicesArray[i*3+1]=(short) (i*3+1);
    			//_indicesArray[i*3+2]=(short) (i*3+2);
    		}
    	}
    }
    @Override  
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {  
        // preparation  
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
        initImageData();
       // initTriangle();  
       // initStaticTriangle();  
    }  
  
    @Override  
    public void onSurfaceChanged(GL10 gl, int w, int h) {  
        gl.glViewport(0, 0,w,  h);  
        //设置投影变换   
        gl.glMatrixMode(GL10.GL_PROJECTION);      
        gl.glLoadIdentity();                      
        //Calculate The Aspect Ratio Of The Window   
       // GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);  
        gl.glMatrixMode(GL10.GL_MODELVIEW);     //设定模型视图矩阵   
        gl.glLoadIdentity();  
    }  
      
    public void setAngle(float angle,float dep) {  
        _angle = angle;  
        _dep=dep;
    }  
  
    @Override  
    public void onDrawFrame(GL10 gl) {  
    	 gl.glClearColor(_red, _green, _blue, 1.0f);  
        // clear the color buffer to show the ClearColor we called above...  
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);  
        // reset the matrix - good to fix the rotation to a static angle  
        gl.glLoadIdentity();  
        
      //  gl.glTranslatef(0.0f, 0.0f, -5.0f);  
        //gl.glViewport(0, 0, 7, 0, 0, 0, 0, 1, 0);
       // GLU.gluLookAt(gl, 0, 0, 4, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        gl.glRotatef(_angle/3, 0f, 1f, 0f);  
        gl.glRotatef(_dep/5, 1f,0f,  0f); 
       
        gl.glPointSize(2.0f);
       // gl.glglBegin(GL_POINTS);//GL_POINTS
      //  gl.glTranslatef(0, 0, -4);  
        gl.glColor4f(0f, 0.5f, 0f, 0.5f);  
        //gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer); 
       // gl.glDrawElements(GL10.GL_POINTS, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
      
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
        
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);  
        gl.glDrawArrays(GL10.GL_POINTS, 0, _nrOfVertices);  
          
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); 
        //gl.glFlush();
        // draw the static triangle  
       // gl.glColor4f(0.5f, 0f, 0f, 0.5f);  
       // gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);  
        //第三个参数表示在数组元素中，两个有意义的数据单元之间相隔的字节数。  
        //当值为0时，表示该数组数据是紧密排列的，没有间隔  
       // gl.glDrawElements(GL10.GL_TRIANGLES, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);  
        //gl.glDrawArrays(mode, first, count);  
        // set rotation for the non-static triangle  
       // gl.glRotatef(_angle, 0f, 1f, 0f);  
  
       // gl.glColor4f(0f, 0.5f, 0f, 0.5f);  
       // gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBufferStatic);  
        //gl.glDrawElements(GL10.GL_TRIANGLES, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBufferStatic);  
          
    }  
    private void initImageData(){
    	if(width==0||height==0||imgData.length==0) return;
    	/*// float has 4 bytes  
        ByteBuffer vbb = ByteBuffer.allocateDirect(width*height * 3 * 4);//对于每个顶点的位置，由x,y,z3个float分量组成，每个float占4个字节  
        //因此申请的总长度为_nrOfVertices * 3 * 4,必须要使用allocateDirect.  
        vbb.order(ByteOrder.nativeOrder());//必须要设置为原生字节序  
        _vertexBuffer = vbb.asFloatBuffer();  
        
     // short has 4 bytes  
        ByteBuffer ibb = ByteBuffer.allocateDirect(width*height * 2);  
        ibb.order(ByteOrder.nativeOrder());  
        _indexBuffer = ibb.asShortBuffer(); 
        
        _vertexBuffer.put(imgData); 
        _indexBuffer.put(_indicesArray);
        _vertexBuffer.position(0);  
        _indexBuffer.position(0); */
        // float has 4 bytes  
        ByteBuffer vbb = ByteBuffer.allocateDirect(_nrOfVertices * 3 * 4);//对于每个顶点的位置，由x,y,z3个float分量组成，每个float占4个字节  
        //因此申请的总长度为_nrOfVertices * 3 * 4,必须要使用allocateDirect.  
        vbb.order(ByteOrder.nativeOrder());//必须要设置为原生字节序  
        _vertexBuffer = vbb.asFloatBuffer();  
          
        // short has 2 bytes  
       // ByteBuffer ibb = ByteBuffer.allocateDirect(_nrOfVertices * 3*2);//同上  
       // ibb.order(ByteOrder.nativeOrder());  
       // _indexBuffer = ibb.asShortBuffer();  
          
         
        _vertexBuffer.put(imgData);  
          
       // _indexBuffer.put(_indicesArray);  
          
        _vertexBuffer.position(0);  
        //_indexBuffer.position(0);  
    }
    private void initTriangle() {  
        // float has 4 bytes  
        ByteBuffer vbb = ByteBuffer.allocateDirect(_nrOfVertices * 3 * 4);//对于每个顶点的位置，由x,y,z3个float分量组成，每个float占4个字节  
        //因此申请的总长度为_nrOfVertices * 3 * 4,必须要使用allocateDirect.  
        vbb.order(ByteOrder.nativeOrder());//必须要设置为原生字节序  
        _vertexBuffer = vbb.asFloatBuffer();  
          
        // short has 2 bytes  
        ByteBuffer ibb = ByteBuffer.allocateDirect(_nrOfVertices * 2);//同上  
        ibb.order(ByteOrder.nativeOrder());  
        _indexBuffer = ibb.asShortBuffer();  
          
        float[] coords = {  
            -0.5f, -0.5f, 0f, // (x1, y1, z1)  
            0.5f, -0.5f, 0f, // (x2, y2, z2)  
            0f, 0.5f, 0f // (x3, y3, z3)  
        };  
          
        _vertexBuffer.put(coords);  
          
        _indexBuffer.put(_indicesArray);  
          
        _vertexBuffer.position(0);  
        _indexBuffer.position(0);  
    }  
      
    private void initStaticTriangle() {  
        // float has 4 bytes  
        ByteBuffer vbb = ByteBuffer.allocateDirect(_nrOfVertices * 3 * 4);  
        vbb.order(ByteOrder.nativeOrder());  
        _vertexBufferStatic = vbb.asFloatBuffer();  
          
        // short has 4 bytes  
        ByteBuffer ibb = ByteBuffer.allocateDirect(_nrOfVertices * 2);  
        ibb.order(ByteOrder.nativeOrder());  
        _indexBufferStatic = ibb.asShortBuffer();  
          
        float[] coords = {  
            -0.4f, -0.4f, 0f, // (x1, y1, z1)  
            0.4f, -0.4f, 0f, // (x2, y2, z2)  
            0f, 0.4f, 0f // (x3, y3, z3)  
        };  
          
        _vertexBufferStatic.put(coords);  
          
        _indexBufferStatic.put(_indicesArray);  
          
        _vertexBufferStatic.position(0);  
        _indexBufferStatic.position(0);  
    }  
      
    public void setColor(float r, float g, float b) {  
        _red = r;  
        _green = g;  
        _blue = b;  
    }  
}  