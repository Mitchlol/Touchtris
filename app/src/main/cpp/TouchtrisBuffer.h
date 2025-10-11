#ifndef TOUCHTRIS_TOUCHTRISBUFFER_H
#define TOUCHTRIS_TOUCHTRISBUFFER_H

#include <cstdint>

class TouchtrisBuffer {
private:
    int32_t bufferSize;
    float* buffer;
    int32_t readIndex;

public:
    explicit TouchtrisBuffer(int32_t size) {
        bufferSize = size;
        buffer = new float[bufferSize]();
        readIndex = 0;
    }
    int32_t get_buffer_size() const;

    int32_t write(const float* data, int32_t numFrames);
    
    float read();

};

#endif //TOUCHTRIS_TOUCHTRISBUFFER_H
