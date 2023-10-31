//
// Created by Dmytro Mandziuk on 31.10.2023.
//

#ifndef LAB6_FOXMULTIPLICATION_H
#define LAB6_FOXMULTIPLICATION_H

#include <math.h>

#include "Common.h"
#include "matrix.h"

static MPI_Comm ColComm;
static MPI_Comm RowComm;

int fox_grid[2];
int fox_grid_size;
static MPI_Comm fox_grid_Comm;

void init_grid_comms_fox() {
    int dim_size[2];
    int period[2];
    int sub_dimension[2];
    dim_size[0] = fox_grid_size;
    dim_size[1] = fox_grid_size;
    period[0] = 0;
    period[1] = 0;
    MPI_Cart_create(MPI_COMM_WORLD, 2, dim_size, period, 1, &fox_grid_Comm);
    MPI_Cart_coords(fox_grid_Comm, process_rank, 2, fox_grid);
    sub_dimension[0] = 0;
    sub_dimension[1] = 1;
    MPI_Cart_sub(fox_grid_Comm, sub_dimension, &RowComm);
    sub_dimension[0] = 1;
    sub_dimension[1] = 0;
    MPI_Cart_sub(fox_grid_Comm, sub_dimension, &ColComm);
}

void init_fox(double** A, double** B, double** C, double** A_block, double** B_block, double** C_block, double** A_sup_block, int* size, int* block_size) {
    *block_size = *size / fox_grid_size;
    *A_block = (double*)malloc((*block_size * *block_size) * sizeof(double));
    *B_block = (double*)malloc((*block_size * *block_size) * sizeof(double));
    *C_block = (double*)malloc((*block_size * *block_size) * sizeof(double));
    *A_sup_block = (double*)malloc((*block_size * *block_size) * sizeof(double));
    if (process_rank == 0) {
        *A = (double*)malloc((*size * *size) * sizeof(double));
        *B = (double*)malloc((*size * *size) * sizeof(double));
        *C = (double*)malloc((*size * *size) * sizeof(double));
        generateRandom(*A, *size, 2, 4);
        generateRandom(*B, *size, 2, 4);
    }
}

void scatter_matrices_fox(double* matrix, double* matrx_block, int size, int block_size) {
    double* row = (double*)malloc(sizeof(double) * (block_size * size));
    if (fox_grid[1] == 0) {
        MPI_Scatter(matrix, block_size * size, MPI_DOUBLE, row, block_size * size, MPI_DOUBLE, 0, ColComm);
    }
    for (int i = 0; i < block_size; i++) {
        MPI_Scatter(&row[i * size], block_size, MPI_DOUBLE, &(matrx_block[i * block_size]), block_size, MPI_DOUBLE, 0, RowComm);
    }
    free(row);
}

void scatter_fox(double* A, double* B, double* A_block, double* B_block, int size, int block_size) {
    scatter_matrices_fox(A, A_block, size, block_size);
    scatter_matrices_fox(B, B_block, size, block_size);
}

void collect_result_fox(double* C, double* C_block, int size, int block_size) {
    double* res_row = (double*)malloc(sizeof(double) * (size * block_size));
    for (int i = 0; i < block_size; i++) {
        MPI_Gather(&C_block[i * block_size], block_size, MPI_DOUBLE, &res_row[i * size], block_size, MPI_DOUBLE, 0, RowComm);
    }
    if (fox_grid[1] == 0) {
        MPI_Gather(res_row, block_size * size, MPI_DOUBLE, C, block_size * size, MPI_DOUBLE, 0, ColComm);
    }
    free(res_row);
}

void sendA(int i, double* A, double* A_sup_block, int block_size) {
    int Pivot = (fox_grid[0] + i) % fox_grid_size;
    if (fox_grid[1] == Pivot) {
        for (int i = 0; i < block_size * block_size; i++)
            A[i] = A_sup_block[i];
    }
    MPI_Bcast(A, block_size * block_size, MPI_DOUBLE, Pivot, RowComm);
}

void sendB(double* B, int block_size) {
    int next_process = fox_grid[0] + 1;
    if (fox_grid[0] == fox_grid_size - 1) next_process = 0;
    int prev_process = fox_grid[0] - 1;
    if (fox_grid[0] == 0) prev_process = fox_grid_size - 1;
    MPI_Status mpi_status;
    MPI_Sendrecv_replace(B, block_size * block_size, MPI_DOUBLE, next_process, 0, prev_process, 0, ColComm, &mpi_status);
}

void init_fox_computation(double* A, double* A_sup_block, double* B, double* C, int block_size) {
    for (int i = 0; i < fox_grid_size; i++) {
        sendA(i, A, A_sup_block, block_size);
        multiply_add(A, B, C, block_size);
        sendB(B, block_size);
    }
}

void deconstruct_fox(double* A, double* B, double* C, double* A_block, double* B_block, double* C_block, double* A_sup_block) {
    if (process_rank == 0) {
        free(A);
        free(B);
        free(C);
    }
    free(A_block);
    free(B_block);
    free(C_block);
    if (A_sup_block) {
        free(A_sup_block);
    }
}

void run_fox(int dim) {
    double* A, * B, * C, * A_block, * B_block, * C_block, * A_sup_block;
    int size;
    int block_size;
    double start_count, end_count, delta;
    fox_grid_size = sqrt((double)process_num);
    if (process_num != fox_grid_size * fox_grid_size) {
        if (process_rank == 0) {
            printf("\nNumber of processes must be a perfect square\n");
        }
        return;
    }
    size = dim;
    init_grid_comms_fox();
    init_fox(&A, &B, &C, &A_block, &B_block, &C_block, &A_sup_block, &size, &block_size);
    scatter_fox(A, B, A_block, B_block, size, block_size);
    start_count = MPI_Wtime();
    init_fox_computation(A_block, A_sup_block, B_block, C_block, block_size);
    end_count = MPI_Wtime();
    collect_result_fox(C, C_block, size, block_size);
    deconstruct_fox(A, B, C, A_block, B_block, C_block, A_sup_block);
    delta = end_count - start_count;
    if (process_rank == 0) {
        printf("%7.4f", delta);
    }
}


#endif //LAB6_FOXMULTIPLICATION_H
