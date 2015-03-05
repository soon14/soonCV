#include <ImageProc.h>
#include <opencv2/core/core.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/calib3d/calib3d.hpp>
#include <opencv2/imgproc/imgproc_c.h>
#include <string>
#include <vector>
#include <android/log.h>
#define LOG_TAG "show infomation"
#define LOGW(a )  __android_log_write(ANDROID_LOG_WARN,LOG_TAG,a)
using namespace cv;
using namespace std;

JNIEXPORT jintArray JNICALL Java_com_ImageProc_grayProc(JNIEnv* env, jclass obj,
		jintArray buf, jint w, jint h) {
	jint *cbuf;
	cbuf = env->GetIntArrayElements(buf, false);
	if (cbuf == NULL) {
		return 0;
	}

	Mat imgData(h, w, CV_8UC4, (unsigned char*) cbuf);

	uchar* ptr = imgData.ptr(0);
	for (int i = 0; i < w * h; i++) {
		//计算公式：Y(亮度) = 0.299*R + 0.587*G + 0.114*B
		//对于一个int四字节，其彩色值存储方式为：BGRA
		int grayScale = (int) (ptr[4 * i + 2] * 0.299 + ptr[4 * i + 1] * 0.587
				+ ptr[4 * i + 0] * 0.114);
		ptr[4 * i + 1] = grayScale;
		ptr[4 * i + 2] = grayScale;
		ptr[4 * i + 0] = grayScale;
	}

	int size = w * h;
	jintArray result = env->NewIntArray(size);
	env->SetIntArrayRegion(result, 0, size, cbuf);
	env->ReleaseIntArrayElements(buf, cbuf, 0);
	return result;
}
uchar* img2Gray(Mat imgData,int w,int h, IplImage* mask){

	uchar* ptr = imgData.ptr(0);
	uchar* gray=new uchar[w*h*3];
	uchar* ptrMask = (unsigned char *)mask->imageData;
	cvSetZero(mask);
	for (int i = 0; i < w * h; i++) {
		//计算公式：Y(亮度) = 0.299*R + 0.587*G + 0.114*B
		//对于一个int四字节，其彩色值存储方式为：BGRA
		int grayScale = (int) (ptr[4 * i + 2] * 0.299 + ptr[4 * i + 1] * 0.587+ ptr[4 * i + 0] * 0.114);
		ptrMask[3*i+0]=ptr[4 * i + 0];
		ptrMask[3*i+1]=ptr[4 * i + 1];
		ptrMask[3*i+2]=ptr[4 * i + 2];
		ptr[4 * i + 1] = (int)grayScale;
		ptr[4 * i + 2] = (int)grayScale;
		ptr[4 * i + 0] = (int)grayScale;
		gray[i*3+0]=grayScale;
		gray[i*3+1]=grayScale;
		gray[i*3+2]=grayScale;
	}
	return gray;
}
void cvSkinSegment(IplImage* img, IplImage* mask){
    CvSize imageSize = cvSize(img->width, img->height);
    IplImage *imgY = cvCreateImage(imageSize, IPL_DEPTH_8U, 1);
    IplImage *imgCr = cvCreateImage(imageSize, IPL_DEPTH_8U, 1);
    IplImage *imgCb = cvCreateImage(imageSize, IPL_DEPTH_8U, 1);


    IplImage *imgYCrCb = cvCreateImage(imageSize, img->depth, img->nChannels);//
    cvCvtColor(img,imgYCrCb,CV_BGR2YCrCb);
    cvSplit(imgYCrCb, imgY, imgCr, imgCb, 0);
    int y, cr, cb, l, x1, y1, value;
    unsigned char *pY, *pCr, *pCb, *pMask;

    pY = (unsigned char *)imgY->imageData;
    pCr = (unsigned char *)imgCr->imageData;
    pCb = (unsigned char *)imgCb->imageData;
    pMask = (unsigned char *)mask->imageData;
    cvSetZero(mask);
    l = img->height * img->width;
    for (int i = 0; i < l; i++){
        y  = *pY;
        cr = *pCr;
        cb = *pCb;
        cb -= 109;
        cr -= 152
            ;
        x1 = (819*cr-614*cb)/32 + 51;
        y1 = (819*cr+614*cb)/32 + 77;
        x1 = x1*41/1024;
        y1 = y1*73/1024;
        value = x1*x1+y1*y1;
        if(y<100)    (*pMask)=(value<700) ? 255:0;
        else        (*pMask)=(value<850)? 255:0;
        pY++;
        pCr++;
        pCb++;
        pMask++;
    }
    cvReleaseImage(&imgY);
    cvReleaseImage(&imgCr);
    cvReleaseImage(&imgCb);
    cvReleaseImage(&imgYCrCb);
}

JNIEXPORT jfloatArray JNICALL Java_com_ImageProc_getDataProc(JNIEnv* env,
		jclass obj, jintArray buf, jint w, jint h) {
	jint *cbuf;
	cbuf = env->GetIntArrayElements(buf, 0);
	if (cbuf == NULL) {
		LOGW( "Import bufArr  is empty!" );
		return 0;
	}
	Mat imgData(h, w, CV_8UC4, (unsigned char*) cbuf);

	//soon 20140923 add
	IplImage* pSkin =cvCreateImage(cvSize(w,h),8,1);
	IplImage *src =cvCreateImage(cvSize(w, h), 8, 3);
	IplImage *srcMask =cvCreateImage(cvSize(w, h), 8, 3);
	src->imageData=(char*)img2Gray(imgData,w,h,srcMask);
	 cvSkinSegment(srcMask,pSkin);
	// cvSkinSegment(src,pSkin);
		// cvErode(pSkin, pSkin, NULL, 1);
	 cvDilate(pSkin, pSkin, NULL, 1);
	// cvSmooth(pSkin, pSkin, CV_GAUSSIAN, 21, 0, 0);
	 cvThreshold(pSkin, pSkin,130, 255, CV_THRESH_BINARY);
   //end add
	uchar* ptr = imgData.ptr(0);
	float imageCenterX = (float) (w * .5);
	float imageCenterY = (float) (h * .5);
	float x, y, z;
	float scalar = 255;
	float* retbuf=new float[w * h*3];

	// char mess[512];
	//sprintf( mess, "widthStep:%d\r\n",  pSkin->widthStep);
	//LOGW( mess );

	for (int i = 0; i < h; i++) {

		 uchar * ptrSkin = (uchar *)(pSkin->imageData + i*pSkin->widthStep);

		 for(int j=0;j<w;j++){

			float grayScale = ptr[(i*w+j)*4];
			x= 2*(j - imageCenterX) / (float)w;
			y = 2*( i - imageCenterY) / (float)h;

			int value=(int)ptrSkin[j];

			if(value==255){
				z = (grayScale)  / scalar;
			}else{
				z = grayScale/ scalar;
			}
			retbuf[3*(i*w+j)+0] =x;
			retbuf[3*(i*w+j)+1] =y;
			retbuf[3*(i*w+j)+2] =z;
		}
	}

    cvReleaseImage(&pSkin);
    cvReleaseImage(&src);
    cvReleaseImage(&srcMask);
	int size = w * h*3;
	jfloatArray result = env->NewFloatArray(size);
	env->SetFloatArrayRegion(result, 0, size, retbuf);
	env->ReleaseIntArrayElements(buf, cbuf, 0);
	return result;
}
