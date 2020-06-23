cmake_minimum_required(VERSION 3.16)
project(jfftw)

set(CMAKE_C_STANDARD 11)

include_directories(.)
include_directories(/usr/local/include)
include_directories(/usr/local/java/latest/include)
include_directories(/usr/local/java/latest/include/darwin)

link_directories(/usr/local/lib)

find_package(OpenMP REQUIRED)

link_libraries(fftw3_omp)
link_libraries(fftw3)
link_libraries(omp)

add_compile_options(-Ofast -march=native -fopenmp)

add_library(jfftw SHARED
        CMakeLists.txt
        jfftw_Interface.c
        jfftw_Interface.h)
