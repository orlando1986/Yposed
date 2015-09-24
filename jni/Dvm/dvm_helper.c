#include "dvm_helper.h"

static int gInsns = 32;
static int gRegistersSize = 10;
static int gOutsSize = 12;
static int gInsSize = 14;
static int gMethodSize = 60;

void dvm_doHook(void* origin, void* proxy) {
	int* ori_code_item = (int*) (origin + gInsns);
	int* pro_code_item = (int*) (proxy + gInsns);
	int code_temp = *ori_code_item;
	*ori_code_item = *pro_code_item;

	int* ori_register = (int*) (origin + gRegistersSize);
	int* pro_register = (int*) (proxy + gRegistersSize);
	int register_temp = *ori_register;
	*ori_register = *pro_register;

	int* ori_out = (int*) (origin + gOutsSize);
	int* pro_out = (int*) (proxy + gOutsSize);
	int out_temp = *ori_out;
	*ori_out = *pro_out;

	int* ori_size = (int*) (origin + gInsSize);
	int* pro_size = (int*) (proxy + gInsSize);
	int size_temp = *ori_size;
	*ori_size = *pro_size;

	memcpy(proxy, origin, gMethodSize);
	*pro_code_item = code_temp;
	*pro_register = register_temp;
	*pro_out = out_temp;
	*pro_size = size_temp;
}
