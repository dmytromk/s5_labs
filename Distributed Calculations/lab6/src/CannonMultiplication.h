//
// Created by Dmytro Mandziuk on 30.10.2023.
//

#pragma once

#include <time.h>
#include <mpi.h>
#include <string.h>

#include "matrix.h"

int gridCoords[2];
MPI_Comm col_comm;
MPI_Comm col_row;

// Розподілення блоків між потоками
void matrixScatter(double *matrix, double *matrixBlock, unsigned int matrix_size, int block_size) {
    int *matrix_row = (double *)malloc(sizeof(double) * block_size * matrix_size);

    if (gridCoords[1] == 0)
        MPI_Scatter(matrix, block_size * matrix_size, MPI_DOUBLE, matrix_row,
                    block_size * matrix_size, MPI_DOUBLE, 0, col_comm);

    for (int i = 0; i < block_size; i++) {
        double* sub_row = (double*)malloc(sizeof(double) * block_size);
        double *sub_row_result = (double *)malloc(sizeof(double) * block_size);

        memcpy(sub_row, matrix_row + i * matrix_size, block_size * sizeof(int));

        MPI_Scatter(sub_row, block_size, MPI_DOUBLE, sub_row_result, block_size, MPI_DOUBLE, 0, col_row);
        memcpy(matrixBlock + i * block_size, sub_row_result, sizeof(double) * block_size);

        free(sub_row_result);
    }

    free(matrix_row);
}

void calculate(unsigned int matrix_size) {

}