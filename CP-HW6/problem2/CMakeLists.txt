cmake_minimum_required(VERSION 3.22)
project(problem2)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(problem2
        main.cpp
        stb_image.h
        stb_image_write.h
        stb_image_resize.h
        Scene.cpp Scene.h
        Layer.cpp Layer.h
        tools/LayerManipulator.cpp tools/LayerManipulator.h
        tools/Stamp.cpp tools/Stamp.h
        yourimage.cpp yourimage.h)

add_executable(compareImage
        compareImage.cpp
        stb_image.h
       )
