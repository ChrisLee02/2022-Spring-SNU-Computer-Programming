cmake_minimum_required(VERSION 3.21)
project(problem1)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(problem1
        src/1-1.cpp
        src/1-2.cpp
        src/1-3.cpp
        src/1-4.cpp
        src/1-5.cpp
        src/main.cpp
        src/stb_image.h
        src/stb_image_write.h
        src/header.h
        )
