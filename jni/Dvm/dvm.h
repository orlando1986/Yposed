#include <jni.h>
#include <dlfcn.h>
#include "log.h"
#include <stdio.h>
#include <stdlib.h>

#define JNIHOOK_CLASS "com/catfish/yposed/HookManager"

void dvm_jni_onload(JNIEnv* env);
