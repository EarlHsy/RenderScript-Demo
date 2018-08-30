#pragma version(1)
#pragma rs java_package_name(com.hsy.testrenderscript)
#include "rs_debug.rsh"


int __attribute__ ((kernel)) invert(long in){
    long result = 1;
        for (int j = 0; j < in; j++) {
       //     result += ( long)sqrt((float)in);

            for (int k = 1; k < 3; k++) {
                result = ( long)pow((float)result, (float)k);
            }
        }
        rsDebug("test rsDebug", result);
    return result;
}


