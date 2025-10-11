#include <stdlib.h>
#include <android/log.h>
#include "TouchtrisAudio.h"

static const char *TAG = "TouchtrisAudio";

using namespace oboe;

oboe::Result TouchtrisAudio::open() {
    // Use shared_ptr to prevent use of a deleted callback.
    mDataCallback = std::make_shared<MyDataCallback>(this);
    mErrorCallback = std::make_shared<MyErrorCallback>(this);

    AudioStreamBuilder builder;
    oboe::Result result = builder.setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::LowLatency)
            ->setUsage(oboe::Usage::Game)
            ->setFormat(oboe::AudioFormat::Float)
            ->setChannelCount(kChannelCount)
            ->setDataCallback(mDataCallback)
            ->setErrorCallback(mErrorCallback)
                    // Open using a shared_ptr.
            ->openStream(mStream);
    sampleRate = mStream->getSampleRate();
    buffer = std::make_unique<TouchtrisBuffer>(sampleRate * kChannelCount);
    return result;
}

oboe::Result TouchtrisAudio::start() {
    return mStream->requestStart();
}

oboe::Result TouchtrisAudio::stop() {
    return mStream->requestStop();
}

oboe::Result TouchtrisAudio::close() {
    return mStream->close();
}

void TouchtrisAudio::createAndPlaySound(int lenMillis, const std::vector<std::vector<float>>& chords, float fadeStart){
    // Calculate length
    if(lenMillis > 1000){
        lenMillis = 1000;
    }
    int numFrames = (int)((float)sampleRate * ((float)lenMillis/1000.0f));

    // Create output buffer
    std::vector<float> tempBuffer(numFrames * kChannelCount);

    // Calculate note length as equal percentage of total length
    int framesPerChord = numFrames/(int)chords.size();

    // Generate chords for intervals
    for(int chordIndex = 0; chordIndex < chords.size(); chordIndex++){
        if(!chords[chordIndex].empty()){
            int frameOffset = chordIndex * framesPerChord;
            for(int frameIndex = frameOffset; frameIndex < frameOffset + framesPerChord; frameIndex++){
                float frame = 0;
                for(float noteIndex : chords[chordIndex]){
                    int noteFrames = (int)((float)sampleRate/noteIndex);
                    frame += (float)((frameIndex % (noteFrames)) > noteFrames/2);
                }
                frame /= (float)chords[chordIndex].size(); // Average notes
                frame -= (float)(!chords[chordIndex].empty()) * 0.5f; // Center values around 0
                for (int channel = 0; channel < kChannelCount; channel++) {
                    tempBuffer[frameIndex * kChannelCount + channel] = frame;
                }
            }
        }
    }

    // Fade audio
    int fadeFrameStart = (int)((float)numFrames * fadeStart);
    for (int i = fadeFrameStart; i < numFrames; ++i) {
        for (int j = 0; j < kChannelCount; j++) {
            tempBuffer[i * kChannelCount + j] = tempBuffer[i * kChannelCount + j] * (1.0f - ((float)(i-fadeFrameStart)/(float)(numFrames-fadeFrameStart)));
        }
    }

    // Write sound to ring buffer
    int32_t samplesWritten = buffer->write(tempBuffer.data(), (int)tempBuffer.size());
}

void TouchtrisAudio::buttonSound() {
    createAndPlaySound(
            100,
            {
                    {C4, Ds4},
                    {Ds4, As4},
                    {G4,Ds4},
            },
            0.75f
    );
}

void TouchtrisAudio::rotateSound() {
    createAndPlaySound(
            167,
            {
                    {C4,Ds4,G4},
                    {},
                    {C4,Ds4},
                    {C4,Ds4},
            },
            0.75f
    );
}

void TouchtrisAudio::moveSound() {
    createAndPlaySound(
            100,
            {
                    {C4, Ds4},
                    {Ds4, As4},
                    {G4,Ds4},
            },
            0.75f
    );
}

void TouchtrisAudio::dropSound() {
    createAndPlaySound(
            125,
            {
                    {Ds4/8, G4/8, As4/4},
                    {C4/4, G4/8, As4/4},
                    {C4/4, Ds4/8, As4/4},
                    {C4/4, Ds4/8, G4/8},
            },
            0.75f
    );
}

void TouchtrisAudio::lineSound() {
    createAndPlaySound(
            400,
            {
                    {C4, E4, G4},
                    {C4, E4, G4},
                    {},
                    {C4, E4, G4},
                    {C4, E4, G4},
                    {},
                    {F4, A4, C4*1},
                    {F4, A4, C4*1},
                    {},
                    {G4, B4*1, D4*1},
                    {G4, B4*1, D4*1},
            },
            0.95f
    );
}

void TouchtrisAudio::quadSound() {
    createAndPlaySound(
            800,
            {
                    {C4*2, E4*2},
                    {E4*2, G4*2},
                    {},
                    {C4*2, E4*2},
                    {E4*2, G4*2},
                    {},
                    {E4*2, G4*2},
                    {G4*2, B4*4},
                    {},
                    {E4*2, G4*2},
                    {G4*2, B4*4},
                    {},
                    {G4*2, B4*4},
                    {B4*4, D4*4},
                    {},
                    {G4*2, B4*4},
                    {B4*4, D4*4},
            },
            0.3f
    );
}

void TouchtrisAudio::holdSound() {
    createAndPlaySound(
            400,
            {
                    {A4, C4, E4},
                    {C4, E4, G4},
                    {A4, C4, E4},
                    {E4, G4, B4},
            },
            0.75f
    );
}

void TouchtrisAudio::deathSound() {
    createAndPlaySound(
            2000,
            {
                    { C4 / 2.0f, Ds4 / 2.0f },
                    { C4 / 2.0f, Ds4 / 2.0f },
                    { C4 / 2.0f, Ds4 / 2.0f },
                    {},
                    { F4 / 2.0f, Gs4 / 2.0f },
                    { F4 / 2.0f, Gs4 / 2.0f },
                    { F4 / 2.0f, Gs4 / 2.0f },
                    {},
                    { G4 / 2.0f, B4 / 2.0f, F4 },
                    { G4 / 2.0f, B4 / 2.0f, F4 },
                    { G4 / 2.0f, B4 / 2.0f, F4 },
                    {},
                    { C4 / 2.0f, Ds4 / 2.0f },
                    { C4 / 2.0f, Ds4 / 2.0f },
                    { C4 / 2.0f, Ds4 / 2.0f },
                    {},
                    { C4 / 2.0f, Ds4 / 2.0f },
                    { C4 / 2.0f, Ds4 / 2.0f },
                    { C4 / 2.0f, Ds4 / 2.0f },
                    {},
                    { C4 / 2.0f, Ds4 / 2.0f },
                    { C4 / 2.0f, Ds4 / 2.0f },
                    { C4 / 2.0f, Ds4 / 2.0f },

            },
            0.75f
    );
}

/**
 * This callback method will be called from a high priority audio thread.
 * It should only do math and not do any blocking operations like
 * reading or writing files, memory allocation, or networking.
 * @param audioStream
 * @param audioData pointer to an array of samples to be filled
 * @param numFrames number of frames needed
 * @return
 */
DataCallbackResult TouchtrisAudio::MyDataCallback::onAudioReady(
        AudioStream *audioStream,
        void *audioData,
        int32_t numFrames) {

    float* out = static_cast<float*>(audioData);
    int32_t requestedBytes = numFrames * kChannelCount;
    for (int i = 0; i < requestedBytes; i++){
        out[i] = mParent->buffer->read();
    }

    return oboe::DataCallbackResult::Continue;
}

void TouchtrisAudio::MyErrorCallback::onErrorAfterClose(oboe::AudioStream *oboeStream,
                                                          oboe::Result error) {
    __android_log_print(ANDROID_LOG_INFO, TAG,
                        "%s() - error = %s",
                        __func__,
                        oboe::convertToText(error)
    );
    // Try to open and start a new stream after a disconnect.
    if (mParent->open() == Result::OK) {
        mParent->start();
    }
}