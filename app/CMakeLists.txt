cmake_minimum_required(VERSION 3.22.1)

project(Touchtris)

## Specifies a path to native header files.
#include_directories(src/main/cpp)

set (APP_SOURCES
        src/main/cpp/TouchtrisAudio.cpp
        src/main/cpp/TouchtrisBuffer.cpp
        src/main/cpp/TouchtrisJNI.cpp
)

# Build our own native library
add_library (native-lib SHARED ${APP_SOURCES} )

# Find the Oboe package
find_package (oboe REQUIRED CONFIG)

# Specify the libraries which our native library is dependent on, including Oboe
target_link_libraries(native-lib log oboe::oboe)

