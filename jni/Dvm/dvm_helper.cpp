#include "dvm_helper.h"
#include "dvm.h"

static void* (*dvmGetMethodFromReflectObj)(void*);
static void* (*dvmInvokeMethod)(void*, void*, void*, void*, void*, bool);

extern "C" {
static void realInvokeOriginalMethodNative(const u4* args, JValue* pResult, const void* method, void* self) {
	void* meth = dvmGetMethodFromReflectObj((void*) args[0]);
	void* thisObject = (void*) args[1];
	void* argList = (void*) args[2];
	void* params = (void*) args[3];
	void* returnType = (void*) args[4];

	pResult->l = dvmInvokeMethod(thisObject, meth, argList, params, returnType, true);
}

void init_dvm(JNIEnv* env) {
	void* handle = dlopen("libdvm.so", RTLD_LAZY | RTLD_GLOBAL);
	void (*dvmSetNativeFunc)(void*, void (*)(const u4*, JValue*, const void*, void*), const u2*) =
			(void (*)(void*, void (*)(const u4*, JValue*, const void*, void*), const u2*)) dlsym(handle, "_Z16dvmSetNativeFuncP6MethodPFvPKjP6JValuePKS_P6ThreadEPKt");
	dvmGetMethodFromReflectObj = (void* (*)(void*)) dlsym(handle, "_Z26dvmGetMethodFromReflectObjP6Object");
	dvmInvokeMethod = (void* (*)(void*, void*, void*, void*, void*, bool)) dlsym(handle, "_Z15dvmInvokeMethodP6ObjectPK6MethodP11ArrayObjectS5_P11ClassObjectb");

	jclass clazz = env->FindClass(JNIHOOK_CLASS);
	void* invokeOriginalMethodNative =
			(void*) env->GetStaticMethodID(clazz,
					"invokeDvmMethod",
					"(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;[Ljava/lang/Class;Ljava/lang/Class;)Ljava/lang/Object;");

	dvmSetNativeFunc(invokeOriginalMethodNative, realInvokeOriginalMethodNative, NULL);
}
}
