#include "asm_support.h"
#include <jni.h>
#include <dlfcn.h>
#include <stdlib.h>

void init_member(JNIEnv* env);
void switchQuickToInterpret(void* artmeth);
void switchInterpretToInterpret(void* artmeth);
void doHook(void* origin, void* proxy);
