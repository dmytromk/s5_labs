//
// Created by Dmytro Mandziuk on 30.10.2023.
//

#pragma once

#include <time.h>
#include <mpi.h>
#include <string.h>
#include <math.h>

#include "matrix.h"

int thread_grid_coords[2]; // Декартові координати даного потоку
MPI_Comm col_comm;
MPI_Comm row_comm;

// Розподілення блоків між потоками
void matrixScatter(double *matrix, double *matrixBlock, unsigned int matrix_size, int block_size) {
    double *matrix_row = (double *)malloc(sizeof(double) * block_size * matrix_size);

    if (thread_grid_coords[1] == 0)
        MPI_Scatter(matrix, block_size * matrix_size, MPI_DOUBLE, matrix_row,
                    block_size * matrix_size, MPI_DOUBLE, 0, col_comm);

    for (int i = 0; i < block_size; i++) {
        double* sub_row = (double*)malloc(sizeof(double) * block_size);
        memcpy(sub_row, matrix_row + i * matrix_size, block_size * sizeof(int));

        double *sub_row_result = (double *)malloc(sizeof(double) * block_size);

        MPI_Scatter(sub_row, block_size, MPI_DOUBLE, sub_row_result, block_size, MPI_DOUBLE, 0, row_comm);
        memcpy(matrixBlock + i * block_size, sub_row_result, sizeof(double) * block_size);

        free(sub_row_result);
    }

    free(matrix_row);
}

void calculateCannonMultiplication(unsigned int matrix_size) {
    int proc_rank, proc_size, gridSize;

    double *matrixA, *matrixB, *matrixC;
    double *blockMatrixA, *blockMatrixB, *blockMatrixC;

    double start_time, end_time;

    MPI_Comm_rank(MPI_COMM_WORLD, &proc_rank); // Ранг (номер) даного потоку
    MPI_Comm_size(MPI_COMM_WORLD, &proc_size); // Кількість потокіа
    gridSize = (int) sqrt(proc_size); // Загальна кількість кроків

    if (proc_size != gridSize * gridSize) {
        if (proc_rank == 0)
            printf("4) %d x %d, 0 ms (procNum != %d x %d)\n", matrix_size, matrix_size, gridSize, gridSize);
        return;
    }

    if (proc_rank == 0) {
        matrixA = (double*)malloc(matrix_size * matrix_size * sizeof(double));
        matrixB = (double*)malloc(matrix_size * matrix_size * sizeof(double));
        matrixC = (double*)malloc(matrix_size * matrix_size * sizeof(double));
        generateRandom(matrixA, matrix_size, 2, 10);
        generateRandom(matrixB, matrix_size, 2, 10);
        fill(matrixC, matrix_size, 0);
        start_time = MPI_Wtime();
    }


    // Комунікатор для декартової решітки потоків
    MPI_Comm grid_comm;

    // Розмір блоку
    int blockSize = matrix_size / gridSize;

    // Виділення кожному з потоків місця для зберігання блоків з кожної матриці
    blockMatrixA = (double*)malloc(blockSize * blockSize * sizeof(double));
    blockMatrixB = (double*)malloc(blockSize * blockSize * sizeof(double));
    blockMatrixC = (double*)malloc(blockSize * blockSize * sizeof(double));

    // Потреба у фіксації виміру решітки потоків
    int *subdims = (int*)malloc(2 * sizeof(int));

    int *dim_dize = (int*)malloc(2 * sizeof(int));
    int *periodic = (int*)malloc(2 * sizeof(int));

    int *grid_coords = (int*)malloc(2 * sizeof(int));

    dim_dize[0] = gridSize;
    dim_dize[1] = gridSize;

    periodic[0] = 0;
    periodic[1] = 0;

    /*
    Створення комунікатора COMM_CART з декартовою топологією з процесів комунікатора COMM_WORLD. Перший параметр -
    вхідний комунікатор, другий параметр задає розмірність одержуваної декартовой решітки, третій - цілочисельний масив
    ndims розміру, що вказує на к-ть процесів в кожному вимірі, четвертий - це логічний масив, що визначає, чи є решітка
    періодичною (значення false) уздовж кожного виміру. reorder - логічний параметр, що визначає, що при значенні
    true системі дозволено змінювати порядок нумерації потоків для оптимізації розподілу потоків по фізичним
    процесорам використовуваного паралельного комп'ютера. Останній параметр - комунікатор з новою декартовою топологією.
    */
    MPI_Cart_create(MPI_COMM_WORLD, 2, dim_dize, periodic, 1, &grid_comm);

    // Визначення декартових координат для кожного потоку
    MPI_Cart_coords(grid_comm, proc_rank, 2, grid_coords);

    // Створення комунікаторів для кожного з рядків решітки потоків
    subdims[0] = 0;
    subdims[1] = 1;
    MPI_Cart_sub(grid_comm, subdims, &row_comm);

    // Створення комунікаторів для кожного зі стовпчиків решітки потоків
    subdims[0] = 1;
    subdims[1] = 0;
    MPI_Cart_sub(grid_comm, subdims, &col_comm);

    // Розподілення задач для потоків декартової решітки
    matrixScatter(matrixA, blockMatrixA, matrix_size, blockSize);
    matrixScatter(matrixB, blockMatrixB, matrix_size, blockSize);

    /*
    Для кожного рядка декартової решітки потоків (крім першого рядка) виконується циклічний зсув блоків матриці A
    на (i - 1) позицій вліво (тобто в напрямку зменшення номерів стовпців).
    */
    if (grid_coords[0] != 0) {
        int nextProc = grid_coords[1] - grid_coords[0];
        if (nextProc < 0)
            nextProc += gridSize;

        MPI_Status status;

        MPI_Sendrecv_replace(blockMatrixA, blockSize * blockSize, MPI_DOUBLE,
                             nextProc, 0, MPI_ANY_SOURCE, 0, row_comm, &status);
    }

    /*
    Для кожного стовпця j декартової решітки потоків (крім першого стовпця) виконується циклічний зсув блоків
    матриці B на (j - 1) позицій вгору (тобто в напрямку зменшення номерів рядків).
    */
    if (grid_coords[1] != 0) {
        int nextProc = grid_coords[0] - grid_coords[1];
        if (nextProc < 0)
            nextProc += gridSize;

        MPI_Status status;

        MPI_Sendrecv_replace(blockMatrixB, blockSize * blockSize, MPI_DOUBLE,
                             nextProc, 1, MPI_ANY_SOURCE, 1, col_comm, &status);
    }

    // Встановлення бар'єру
    MPI_Barrier(MPI_COMM_WORLD);

    // Блоки матриць A і B, які містяться в процесі (i, j) перемножуються, і результат додається до матриці Сij
    for (int i = 0; i < blockSize; i++)
        for (int j = 0; j < blockSize; j++)
            for (int k = 0; k < blockSize; k++)
                blockMatrixC[i * blockSize + j] += blockMatrixA[i * blockSize + k] * blockMatrixB[k * blockSize + j];


    /*
    Для кожного рядка виконується циклічне пересилання блоків матриці A, які містяться в кожному потоці цього рядка,
    в напрямку зменшення номерів стовпців.
    */
    for (int iter = 0; iter < gridSize - 1; iter++) {
        int nextProc = grid_coords[1] - 1;
        if (nextProc < 0)
            nextProc += gridSize;

        MPI_Status status;

        MPI_Sendrecv_replace(blockMatrixA, blockSize, MPI_DOUBLE,
                             nextProc, 0, MPI_ANY_SOURCE, 0, row_comm, &status);

        nextProc = grid_coords[0] - 1;
        if (nextProc < 0)
            nextProc += gridSize;

        /*
        Для кожного стовпця виконується циклічне пересилання блоків матриці B, які містяться в кожному потоці цього
        стовпця, в напрямку зменшення номерів рядків.
        */
        MPI_Sendrecv_replace(blockMatrixB, blockSize, MPI_DOUBLE,
                             nextProc, 1, MPI_ANY_SOURCE, 1, col_comm, &status);

        for (int i = 0; i < blockSize; i++)
            for (int j = 0; j < blockSize; j++)
                for (int k = 0; k < blockSize; k++)
                    blockMatrixC[i * blockSize + j] += blockMatrixA[i * blockSize + k] * blockMatrixB[k * blockSize + j];
    }

    // Результат
    double *resultRow = (double *)malloc(matrix_size * blockSize * sizeof(double));

    for (int i = 0; i < blockSize; i++) {
        MPI_Gather(&blockMatrixA[i * blockSize], blockSize, MPI_DOUBLE,
                   &resultRow[i * matrix_size], blockSize, MPI_DOUBLE, 0, row_comm);

//        double *subRow = (double *)malloc(blockSize * sizeof(double));
//        memcpy(subRow, blockMatrixC + i * blockSize, blockSize * sizeof(int));
//
//        double *subRowRes = (double *)malloc(gridSize * blockSize * sizeof(double));
//
//        MPI_Gather(subRow, blockSize, MPI_DOUBLE,
//                   subRowRes, blockSize, MPI_DOUBLE, 0, row_comm);
//        memcpy(&resultRow[i * matrix_size], subRowRes, gridSize * blockSize * sizeof(int));
//
//        free(subRow);
//        free(subRowRes);
    }

    if (grid_coords[1] == 0)
        MPI_Gather(resultRow, blockSize * matrix_size, MPI_DOUBLE,
                   matrixC, blockSize * matrix_size, MPI_DOUBLE, 0, col_comm);

    if (proc_rank == 0) {
        end_time = MPI_Wtime();
        printf("Cannon Algorithm[%dx%d]: %7.4f\n", matrix_size, matrix_size, end_time-start_time);

        double *checker = (double *)malloc(matrix_size * matrix_size * sizeof(int));
        multiply(matrixA, matrixB, checker, matrix_size);

        if (areEqual(matrixC, checker, matrix_size)) {
            printf("Matrices are equal.\n");
        } else {
            printf("Matrices are not equal.\n");
        }
    }
}