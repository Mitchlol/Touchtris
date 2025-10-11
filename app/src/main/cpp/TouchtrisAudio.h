//
// Created by moesk on 10/1/2025.
//

#ifndef TOUCHTRIS_NATIVE_LIB_H
#define TOUCHTRIS_NATIVE_LIB_H

#include "oboe/Oboe.h"
#include "TouchtrisBuffer.h"

class TouchtrisAudio {
public:
    oboe::Result open();
    oboe::Result start();
    oboe::Result stop();
    oboe::Result close();


    void buttonSound();
    void rotateSound();
    void moveSound();
    void dropSound();
    void lineSound();
    void quadSound();
    void holdSound();
    void deathSound();

private:
    class MyDataCallback : public oboe::AudioStreamDataCallback {
    public:
        MyDataCallback(TouchtrisAudio *parent) : mParent(parent) {}
        oboe::DataCallbackResult onAudioReady(
                oboe::AudioStream *audioStream,
                void *audioData,
                int32_t numFrames) override;
    private:
        TouchtrisAudio *mParent;
    };

    class MyErrorCallback : public oboe::AudioStreamErrorCallback {
    public:
        MyErrorCallback(TouchtrisAudio *parent) : mParent(parent) {}
        virtual ~MyErrorCallback() {}
        void onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) override;

    private:
        TouchtrisAudio *mParent;
    };

    std::shared_ptr<oboe::AudioStream> mStream;
    std::shared_ptr<MyDataCallback> mDataCallback;
    std::shared_ptr<MyErrorCallback> mErrorCallback;

    static constexpr int kChannelCount = 2;
    static constexpr int kBytesPerFrame = sizeof(float);
    int sampleRate = 48000;
    std::unique_ptr<TouchtrisBuffer> buffer;

    // Base reference note (C4 in Hz)
    static constexpr float C4_BASE = 261.63f;

    // Notes in the middle octave, calculated with equal temperament
    const float C4   = C4_BASE * std::pow(2.0f, 0.0f  / 12.0f);
    const float Cs4  = C4_BASE * std::pow(2.0f, 1.0f  / 12.0f);
    const float D4   = C4_BASE * std::pow(2.0f, 2.0f  / 12.0f);
    const float Ds4  = C4_BASE * std::pow(2.0f, 3.0f  / 12.0f);
    const float E4   = C4_BASE * std::pow(2.0f, 4.0f  / 12.0f);
    const float F4   = C4_BASE * std::pow(2.0f, 5.0f  / 12.0f);
    const float Fs4  = C4_BASE * std::pow(2.0f, 6.0f  / 12.0f);
    const float G4   = C4_BASE * std::pow(2.0f, 7.0f  / 12.0f);
    const float Gs4  = C4_BASE * std::pow(2.0f, 8.0f  / 12.0f);
    const float A4   = C4_BASE * std::pow(2.0f, 9.0f  / 12.0f);
    const float As4  = C4_BASE * std::pow(2.0f, 10.0f / 12.0f);
    const float B4   = C4_BASE * std::pow(2.0f, 11.0f / 12.0f);

    void createAndPlaySound(int lenMillis, const std::vector<std::vector<float>>& chords, float fadeStart);
};

#endif //TOUCHTRIS_NATIVE_LIB_H
