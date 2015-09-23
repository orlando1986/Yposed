#include <jni.h>
#include <dlfcn.h>
#include "log.h"
#include "asm_support.h"
#include <stdio.h>
#include <stdlib.h>

#define JNIHOOK_CLASS "com/catfish/yposed/HookManager"

JavaVM* gJVM;
jint art_jni_onload(JavaVM* vm, void* reserved);
