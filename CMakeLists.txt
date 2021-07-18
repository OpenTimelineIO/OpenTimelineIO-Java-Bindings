cmake_minimum_required(VERSION 3.10 FATAL_ERROR)

project(jotio VERSION 0.14.0 LANGUAGES CXX)

set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_CXX_STANDARD 11)

include(GNUInstallDirs)
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/${CMAKE_INSTALL_LIBDIR})
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib/$<CONFIG>/${CMAKE_SYSTEM_NAME})
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin/$<CONFIG>/${CMAKE_SYSTEM_NAME})

# jvm
if(NOT DEFINED ANDROID_ABI)
else()
    set(JAVA_INCLUDE_PATH2 NotNeeded)
endif()
# https://stackoverflow.com/questions/51047978/cmake-could-not-find-jni
set(JAVA_AWT_LIBRARY NotNeeded)
set(JAVA_JVM_LIBRARY NotNeeded)
set(JAVA_AWT_INCLUDE_PATH NotNeeded)
find_package(JNI REQUIRED)

include_directories(${JNI_INCLUDE_DIRS})

# force off-tree build
if(${CMAKE_SOURCE_DIR} STREQUAL ${CMAKE_BINARY_DIR})
  message(FATAL_ERROR "CMake generation is not allowed within the source directory!
  Remove the CMakeCache.txt file and try again from another folder, e.g.:
    mkdir build && cd build
    cmake ..
  ")
endif()

# default to Release build
if(NOT CMAKE_BUILD_TYPE)
  set(CMAKE_BUILD_TYPE Release CACHE STRING
      "Choose the type of build, options are: None Debug Release RelWithDebInfo MinSizeRel."
      FORCE)
endif()

set(OTIO_PYTHON_INSTALL OFF CACHE BOOL "")
set(OTIO_CXX_INSTALL OFF CACHE BOOL "")
set(OTIO_DEPENDENCIES_INSTALL OFF CACHE BOOL "")
set(OTIO_INSTALL_COMMANDLINE_TOOLS OFF CACHE BOOL "")
set(OTIO_SHARED_LIBS ON)

set(CMAKE_POLICY_DEFAULT_CMP0077 NEW)

if(APPLE)
    set(CMAKE_OSX_ARCHITECTURES arm64;x86_64)
endif()

include_directories(src/main/include)
add_subdirectory(deps/OpenTimelineIO build/natives)
add_subdirectory(src/main/cpp)