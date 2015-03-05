package com.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtil {      
	  public static IntBuffer mIntBuffer;
      public static FloatBuffer mFloatBuffer;
      public static ByteBuffer mByteBuffer;  //这个成员变量跟前2个函数里面的局部变量名字相同，当时也没想到更好的名字。
      
       /**   
   * 将float数组转换存储在字节缓冲数组   
   * @param arr   
   * @return   
   */ 
      public static IntBuffer intBuffer(int []a)
      {
              //分配缓冲空间，一个int占4个字节
              ByteBuffer mByteBuffer = ByteBuffer.allocateDirect(a.length*4);
              //设置字节顺序， 其中ByteOrder.nativeOrder()是获取本机字节顺序    
              mByteBuffer.order(ByteOrder.nativeOrder());
              //转换为int型
              mIntBuffer = mByteBuffer.asIntBuffer();
              //添加数据 
              mIntBuffer.put(a);
              //设置数组的起始位置 
              mIntBuffer.position(0);
              return mIntBuffer;
      }
      
      public static FloatBuffer floatBuffer(float []b)
      {
              ByteBuffer mByteBuffer = ByteBuffer.allocateDirect(b.length*4);
              mByteBuffer.order(ByteOrder.nativeOrder());
              mFloatBuffer = mByteBuffer.asFloatBuffer();
              mFloatBuffer.put(b);
              mFloatBuffer.position(0);
              return mFloatBuffer;
      }
      
      public static ByteBuffer byteBuffer(byte []c)
      {
              ByteBuffer mbb = ByteBuffer.allocateDirect(c.length);
              mbb.order(ByteOrder.nativeOrder());
              mByteBuffer = mbb;  //注意，这里不需要进行转化的，因为mbb本来就是ByteBuffer类型
              mByteBuffer.put(c);
              mByteBuffer.position(0);
              return mByteBuffer;        
      }    
}
