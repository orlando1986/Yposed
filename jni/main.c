#include <jni.h>
#include <stdio.h>
#include "Art/art.h"
#include "Dvm/dvm.h"

void Java_com_catfish_yposed_HookManager_initVM(JNIEnv* env, jobject thiz, jint version) {
	if ((int) version < 0) {
		dvm_jni_onload(env);
	} else {
		art_jni_onload(env, (int)version);
	}
}
