//
// Created by Dmytro Mandziuk on 30.10.2023.
//

#ifndef LAB6_CANNONMULTIPLICATION_H
#define LAB6_CANNONMULTIPLICATION_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "Common.h"
#include "matrix.h"

static int slice_size;
static MPI_Comm col_Comm;
static MPI_Comm row_Comm;

int grid[2];
int grid_size;
static MPI_Comm grid_Comm;

void shift_left(double* A, int size, int block_size) {
    MPI_Status status;

    int next_p = grid[1] + 1;
    if (grid[1] == grid_size - 1) next_p = 0;

    int prev_p = grid[1] - 1;
    if (grid[1] == 0) prev_p = grid_size - 1;

    MPI_Sendrecv_replace(A, block_size * block_size, MPI_DOUBLE, next_p, 0, prev_p, 0, row_Comm, &status);
}

void shift_right(double* B, int size, int block_size) {
    MPI_Status Status;

    int NextProc = grid[0] + 1;
    if (grid[0] == grid_size - 1) NextProc = 0;

    int PrevProc = grid[0] - 1;
    if (grid[0] == 0) PrevProc = grid_size - 1;

    MPI_Sendrecv_replace(B, block_size * block_size, MPI_DOUBLE, NextProc, 0, PrevProc, 0, col_Comm, &Status);
}

void collect_result_cannon(double* C, double* C_block, int size, int block_size) {
    double* res_row = (double*)malloc(size * block_size * sizeof(double));

    for (int i = 0; i < block_size; i++) {
        MPI_Gather(&C_block[i * block_size], block_size, MPI_DOUBLE, &res_row[i * size], block_size, MPI_DOUBLE, 0, row_Comm);
    }

    if (grid[1] == 0) {
        MPI_Gather(res_row, block_size * size, MPI_DOUBLE, C, block_size * size, MPI_DOUBLE, 0, col_Comm);
    }

    free(res_row);
}

void init_computation(double* A, double* B, double* C, int size, int block_size) {
    for (int i = 0; i < grid_size; ++i) {
        multiply(A, B, C, block_size);
        shift_left(A, size, block_size);
        shift_right(B, size, block_size);
    }
}

void scatter_block(double* matr, double* block, int row, int col, int size, int block_size) {
    int start_pos = col * block_size * size + row * block_size;
    int cur_pos = start_pos;
    for (int i = 0; i < block_size; ++i, cur_pos += size) {
        MPI_Scatter(&matr[cur_pos], block_size, MPI_DOUBLE, &(block[i * block_size]), block_size, MPI_DOUBLE, 0, grid_Comm);
    }
}

void scatter(double* A, double* A_block, double* B, double* B_block, int size, int block_size) {
    int N = grid[0];
    int M = grid[1];
    scatter_block(A, A_block, N, (N + M) % grid_size, size, block_size);
    scatter_block(B, B_block, (N + M) % grid_size, M, size, block_size);
}

void init_grid_comms_cannon() {
    int d_size[2], period[2], sub_dim[2];
    d_size[0] = grid_size;
    d_size[1] = grid_size;
    period[0] = 0;
    period[1] = 0;

    MPI_Cart_create(MPI_COMM_WORLD, 2, d_size, period, 1, &grid_Comm);
    MPI_Cart_coords(grid_Comm, process_rank, 2, grid);

    sub_dim[0] = 0;
    sub_dim[1] = 1;
    MPI_Cart_sub(grid_Comm, sub_dim, &row_Comm);

    sub_dim[0] = 1;
    sub_dim[1] = 0;
    MPI_Cart_sub(grid_Comm, sub_dim, &col_Comm);
}

void init_cannon(double** A, double** B, double** C, double** A_block, double** B_block, double** C_block, int* size, int* block_size) {
    *block_size = *size / grid_size;
    *A_block = (double*)malloc((*block_size * *block_size) * sizeof(double));
    *B_block = (double*)malloc((*block_size * *block_size) * sizeof(double));
    *C_block = (double*)malloc((*block_size * *block_size) * sizeof(double));
    fill(*C_block, 0, *block_size);
    if (process_rank == 0) {
        *A = (double*)malloc((*size * *size) * sizeof(double));
        *B = (double*)malloc((*size * *size) * sizeof(double));
        *C = (double*)malloc((*size * *size) * sizeof(double));
        generateRandom(*A, *size, 2, 10);
        generateRandom(*B, *size, 2, 10);
    }
}

void run_cannon(int argc, char* argv[], int size) {
    double *A, *B, *C, *A_block, *B_block, *C_block;
    int block_size;
    double start_time, end_time;

    grid_size = sqrt((double)process_num);
    if (process_num != grid_size * grid_size) {
        if (process_rank == 0) {
            printf("\n Invalid number of processes for algorithm execution");
        }
        return;
    }
    init_grid_comms_cannon();
    init_cannon(&A, &B, &C, &A_block, &B_block, &C_block, &size, &block_size);
    scatter(A, A_block, B, B_block, size, block_size);

    if (process_rank == 0) {
        start_time = MPI_Wtime();
    }

    init_computation(A_block, B_block, C_block, size, block_size);

    if (process_rank == 0) {
        end_time = MPI_Wtime();
    }

    collect_result_cannon(C, C_block, size, block_size);
    deconstruct(A, B, C, A_block, B_block, C_block);
    if (process_rank == 0)
        printf("Cannon Test results (size %dx%d): %f\n", size, size, end_time - start_time);
}


#endif //LAB6_CANNONMULTIPLICATION_H
