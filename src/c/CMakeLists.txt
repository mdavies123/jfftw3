cmake_minimum_required(VERSION 3.16)
project(jfftw)

set(CMAKE_C_STANDARD 11)

include_directories(.)
include_directories(/usr/local/include)
include_directories(/usr/local/java/latest/include)
include_directories(/usr/local/java/latest/include/linux)

link_libraries(fftw3)

add_compile_options(-O3 -march=native)

add_library(jfftw SHARED
        CMakeLists.txt
        common.h
        jfftw_Execution.c
        jfftw_Execution.h
        jfftw_Interface.c
        jfftw_Interface.h
        jfftw_Plan.c
        jfftw_Plan.h
        jfftw_Wisdom.c
        jfftw_Wisdom.h)
