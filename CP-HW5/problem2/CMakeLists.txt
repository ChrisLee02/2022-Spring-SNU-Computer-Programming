cmake_minimum_required(VERSION 3.21)
project(problem2)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(problem2
        src/Delta.cpp
        src/Delta.h
        src/main.cpp
        src/RunLength.cpp
        src/RunLength.h
        src/TestHelper.cpp
        src/TestHelper.h
        )
