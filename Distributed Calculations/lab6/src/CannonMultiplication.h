//
// Created by Dmytro Mandziuk on 30.10.2023.
//

#pragma once

#include <iostream>
#include <time.h>
#include <mpi.h>

#include "matrix.h"

namespace CannonMultiplication {
    MPI::Comm
    MPI_Comm ColComm;

    // Комунікатор для ствпчиків потоків
    private static Cartcomm RowComm;

    void calculate(unsigned int matrix_size) {

    }
}