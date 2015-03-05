package com.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtil {      
	  public static IntBuffer mIntBuffer;
      public static FloatBuffer mFloatBuffer;
      public static ByteBuffer mByteBuffer;  //�����Ա������ǰ2����������ľֲ�����������ͬ����ʱҲû�뵽���õ����֡�
      
       /**   
   * ��float����ת���洢���ֽڻ�������   
   * @param arr   
   * @return   
   */ 
      public static IntBuffer intBuffer(int []a)
      {
              //���仺��ռ䣬һ��intռ4���ֽ�
              ByteBuffer mByteBuffer = ByteBuffer.allocateDirect(a.length*4);
              //�����ֽ�˳�� ����ByteOrder.nativeOrder()�ǻ�ȡ�����ֽ�˳��    
              mByteBuffer.order(ByteOrder.nativeOrder());
              //ת��Ϊint��
              mIntBuffer = mByteBuffer.asIntBuffer();
              //������� 
              mIntBuffer.put(a);
              //�����������ʼλ�� 
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
              mByteBuffer = mbb;  //ע�⣬���ﲻ��Ҫ����ת���ģ���Ϊmbb��������ByteBuffer����
              mByteBuffer.put(c);
              mByteBuffer.position(0);
              return mByteBuffer;        
      }    
}
