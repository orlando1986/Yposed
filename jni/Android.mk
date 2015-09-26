LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := \
    main.c \
    Art/art.c\
    Art/art_helper.c\
    Dvm/dvm.c\
    Dvm/dvm_helper.cpp\

LOCAL_MODULE := libhook
LOCAL_MODULE_TAGS := optional

LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog

include $(BUILD_SHARED_LIBRARY)
