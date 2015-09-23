#include "art.h"
#include "art_helper.h"

static void hook_yposed_method(JNIEnv* env, jobject thiz, jobject method_origin, jobject method_proxy, jboolean is_static) {
	jmethodID meth_ori = (*env)->FromReflectedMethod(env, method_origin);
	jmethodID meth_pro = (*env)->FromReflectedMethod(env, method_proxy);
	switchQuickToInterpret(meth_ori);
	switchInterpretToInterpret(meth_ori);
	switchInterpretToInterpret(meth_pro);
	doHook(meth_ori, meth_pro);
}

static JNINativeMethod gMethods[] = {
		{ "hookYposedMethod", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Z)V", (void*) hook_yposed_method },
		};


static int registerNativeMethods(JNIEnv* env, const char* className,
		JNINativeMethod* gMethods, int numMethods) {
	jclass clazz = (*env)->FindClass(env, className);
	if (clazz == NULL) {
		return JNI_FALSE;
	}
	if ((*env)->RegisterNatives(env, clazz, gMethods, numMethods) < 0) {
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

jint art_jni_onload(JavaVM* vm, void* reserved) {
	JNIEnv* env = NULL;
	jint result = -1;
	gJVM = vm;

	if ((*vm)->GetEnv(vm, (void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		return -1;
	}

	if (!registerNativeMethods(env, JNIHOOK_CLASS, gMethods,
			sizeof(gMethods) / sizeof(gMethods[0]))) {
		return -1;
	}
	result = JNI_VERSION_1_4;
	init_member(env);

	return result;
}
