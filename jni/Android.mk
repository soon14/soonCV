LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
#OPENCV_CAMERA_MODULES:=on
#OPENCV_INSTALL_MODULES:=off
#OPENCV_LIB_TYPE:=STATIC
include ${OPENCV_SDK}/sdk/native/jni/OpenCV.mk
 
LOCAL_SRC_FILES  := ImageProc.cpp DetectionBasedTracker_jni.cpp
LOCAL_MODULE     := image_proc 
LOCAL_LDLIBS    += -llog
include $(BUILD_SHARED_LIBRARY)