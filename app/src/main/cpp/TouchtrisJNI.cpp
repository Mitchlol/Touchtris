#include <jni.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

static const char *TAG = "TouchtrisJNI";

#include <android/log.h>

#include "TouchtrisAudio.h"

// JNI functions are "C" calling convention
#ifdef __cplusplus
extern "C" {
#endif

using namespace oboe;

// Use a static object so we don't have to worry about it getting deleted at the wrong time.
static TouchtrisAudio sPlayer;


JNIEXPORT jint JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_startAudioStreamNative(
        JNIEnv * /* env */, jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    Result result = sPlayer.open();
    if (result == Result::OK) {
        result = sPlayer.start();
    }
    return (jint) result;
}

JNIEXPORT jint JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_stopAudioStreamNative(
        JNIEnv * /* env */, jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    // We need to close() even if the stop() fails because we need to delete the resources.
    Result result1 = sPlayer.stop();
    Result result2 = sPlayer.close();
    // Return first failure code.
    return (jint) ((result1 != Result::OK) ? result1 : result2);
}

JNIEXPORT void JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_buttonSoundNative(JNIEnv * /* env */,
                                                                                jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    sPlayer.buttonSound();
}

JNIEXPORT void JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_rotateSoundNative(JNIEnv * /* env */,
                                                                                jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    sPlayer.rotateSound();
}

JNIEXPORT void JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_moveSoundNative(JNIEnv * /* env */,
                                                                              jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    sPlayer.moveSound();
}

JNIEXPORT void JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_dropSoundNative(JNIEnv * /* env */,
                                                                              jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    sPlayer.dropSound();
}

JNIEXPORT void JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_lineSoundNative(JNIEnv * /* env */,
                                                                              jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    sPlayer.lineSound();
}

JNIEXPORT void JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_quadSoundNative(JNIEnv * /* env */,
                                                                              jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    sPlayer.quadSound();
}

JNIEXPORT void JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_holdSoundNative(JNIEnv * /* env */,
                                                                              jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    sPlayer.holdSound();
}

JNIEXPORT void JNICALL
Java_com_mitchelllustig_touchtris_audio_NativeAudioBridgeImpl_deathSoundNative(JNIEnv * /* env */,
                                                                               jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    sPlayer.deathSound();
}

#ifdef __cplusplus
}
#endif