#include <stdlib.h>
#include "hu_grotesque_gecko_caff_parser_CAFF.h"
#include "caff_parser.h"

JNIEXPORT jlong JNICALL
Java_hu_grotesque_1gecko_caff_1parser_CAFF_parseCaff(JNIEnv *env, jobject thisObject, jobject byteBuffer) {
    unsigned char *buf = (unsigned char *) (*env)->GetDirectBufferAddress(env, byteBuffer);
    jlong capacity = (*env)->GetDirectBufferCapacity(env, byteBuffer);

    CAFF *caff;
    CAFF_RES result = caff_parse(buf, capacity, &caff);

    switch(result) {
        case CAFF_OK:
            return (jlong) caff;
        case CAFF_SIZE_ERROR:
            return (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/IllegalArgumentException"), "The size of the CAFF was too big");
        case CAFF_MEMORY:
            return (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/OutOfMemoryError"), "Out of memory");
        case CAFF_FORMAT_ERROR:
        default:
            return (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/IllegalArgumentException"), "Invalid CAFF format");
    }
}

JNIEXPORT void JNICALL
Java_hu_grotesque_1gecko_caff_1parser_CAFF_freeCaff(JNIEnv *env, jobject thisObject, jlong caff) {
    CAFF *caff_ptr = (CAFF *) caff;
    caff_free(caff_ptr);
}

JNIEXPORT jobject JNICALL
Java_hu_grotesque_1gecko_caff_1parser_CAFF_previewCaff(JNIEnv *env, jobject thisObject, jlong caff) {
    CAFF *caff_ptr = (CAFF *) caff;

    unsigned char *buffer;
    unsigned long long size;
    caff_preview(caff_ptr, &buffer, &size);

    if(buffer == NULL) {
        (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/IllegalArgumentException"), "Invalid CAFF format");
        return NULL;
    }

    return (*env)->NewDirectByteBuffer(env, buffer, size);
}
