#include "art_helper.h"

static void (*art_quick_to_interpreter_bridge)(void*);
static void (*artInterpreterToInterpreterBridge)(void*);

void init_member(JNIEnv* env) {
	void* handle = dlopen("libart.so", RTLD_LAZY | RTLD_GLOBAL);
	art_quick_to_interpreter_bridge = (void (*)(void*)) dlsym(handle, "art_quick_to_interpreter_bridge");
	artInterpreterToInterpreterBridge = (void (*)(void*)) dlsym(handle, "artInterpreterToInterpreterBridge");
}

void switchQuickToInterpret(void* artmeth) {
	int* bridge = (int*) (artmeth + METHOD_QUICK_CODE_OFFSET_32);
	*bridge = (int)(art_quick_to_interpreter_bridge);
}

void switchInterpretToInterpret(void* artmeth) {
	int* bridge = (int*) (artmeth + METHOD_INTERPRET_OFFSET_32);
	*bridge = (int)(artInterpreterToInterpreterBridge);
}

void doHook(void* origin, void* proxy) {
	int* ori_code_item = (int*) (origin + METHOD_DEX_OFFSET_32);
	int* pro_code_item = (int*) (proxy + METHOD_DEX_OFFSET_32);
	int temp = *ori_code_item;
	*ori_code_item = *pro_code_item;
	memcpy(proxy, origin, METHOD_SIZE_32);
	*pro_code_item = temp;
}
