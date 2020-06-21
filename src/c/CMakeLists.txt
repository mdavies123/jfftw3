cmake_minimum_required(VERSION 3.16)
project(jfftw)

set(CMAKE_C_STANDARD 11)

include_directories(.)
include_directories(/usr/local/include)
include_directories(/usr/local/java/latest/include)
include_directories(/usr/local/java/latest/include/linux)

link_libraries(fftw3_omp)
link_libraries(fftw3)

add_compile_options(-O3 -march=native)

add_library(jfftw SHARED
        CMakeLists.txt
        jfftw_Interface.c
        jfftw_Interface.h)
