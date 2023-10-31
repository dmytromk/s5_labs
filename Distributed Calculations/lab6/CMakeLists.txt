cmake_minimum_required(VERSION 3.26)
project(lab6)

set(CMAKE_CXX_STANDARD 17)

add_executable(lab6 src/main.c
        src/matrix.h
        src/NaiveMultiplication.h
        src/CannonMultiplication.h
        src/Common.h
        src/FoxMultiplication.h
)

find_package(MPI REQUIRED)
set(MPI_EXECUTABLE_SUFFIX ".mpich")
include_directories(SYSTEM ${MPI_INCLUDE_PATH})
target_link_libraries(lab6 ${MPI_C_LIBRARIES})
target_include_directories(lab6 PRIVATE ${MPI_C_INCLUDE_DIRS})