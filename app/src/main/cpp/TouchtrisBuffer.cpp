#include "TouchtrisBuffer.h"

int32_t TouchtrisBuffer::get_buffer_size() const {
    return bufferSize;
}

int32_t TouchtrisBuffer::write(const float* data, int32_t numFrames) {
    for(int i = 0; i < numFrames; i++){
        buffer[(readIndex + i) % bufferSize] = data[i];
    }
    return numFrames;
}

float TouchtrisBuffer::read() {
    float value = buffer[readIndex];
    buffer[readIndex] = 0.0f;
    readIndex = (readIndex + 1) % bufferSize;
    return value;
}
