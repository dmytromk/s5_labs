//
// Created by Dmytro Mandziuk on 29.10.2023.
//

#pragma once

#include <iostream>
#include <time.h>
#include <mpi.h>

#include "matrix.h"

//static int slice_proc_size;
//static int process_num = 0;
//static int process_proc_rank = 0;
//static MPI_Comm col_comm;
//static MPI_Comm row_comm;

namespace NaiveMultiplication {
    void calculate(int matrix_size) {
        int proc_rank, proc_size, tape_len;
        double *matrixA, *matrixB, *matrixC;
        double *bufferA, *bufferB, *bufferC;

        MPI_Comm_rank(MPI_COMM_WORLD, &proc_rank); // Id of the current process
        MPI_Comm_size(MPI_COMM_WORLD, &proc_size); // Amount of processes

        double start_time, end_time;

        if (proc_rank == 0) {
            matrixA = new double[matrix_size * matrix_size];
            matrixB = new double[matrix_size * matrix_size];
            matrixC = new double[matrix_size * matrix_size];
            SquareMatrix::generateRandom(matrixA, matrix_size, 2, 10);
            SquareMatrix::generateRandom(matrixB, matrix_size, 2, 10);
            SquareMatrix::fill(matrixC, matrix_size, 0);
            start_time = MPI_Wtime();
        }

        tape_len = matrix_size / proc_size;

        bufferA = new double[tape_len * matrix_size];
        bufferB = new double[tape_len * matrix_size];
        bufferC = new double[tape_len * matrix_size];

//        if (proc_rank == 0) {
//            SquareMatrix::transpose(bufferB, matrix_size);
//        }
        MPI_Scatter(matrixA, tape_len * matrix_size, MPI_DOUBLE, bufferA, tape_len * matrix_size, MPI_DOUBLE, 0, MPI_COMM_WORLD);
        MPI_Scatter(matrixB, tape_len * matrix_size, MPI_DOUBLE, bufferB, tape_len * matrix_size, MPI_DOUBLE, 0, MPI_COMM_WORLD);

        // Для кожного потоку визначається його наступник та попередник для циклічного обміну даними
        int next_proc = (proc_rank + 1) % proc_size;
        int prev_proc = proc_rank - 1;
        if (prev_proc < 0)
            prev_proc = proc_size - 1;
        int prev_data_num = proc_rank;

        printf("Test size %d %d \n", proc_rank, proc_size);

        // Стрічкове перемноження матриць
        for (int p = 0; p < proc_size; p++) {
            for (int i = 0; i < tape_len; i++) {
                for (int j = 0; j < matrix_size; j++) {
                    for (int k = 0; k < tape_len; k++) {
                        /*
                        Виконується множення стрічки матриці A на стрічку матриці B, що містяться в даному потоці, і
                        результат записується у відповідний елемент стрічки с результуючої матриці.
                        */
                        bufferC[i * matrix_size + j] += bufferA[prev_data_num * tape_len +
                                                                i * matrix_size + k] * bufferB[k * matrix_size + j];
                    }
                }
            }

            printf("Test");

            MPI_Status Status;

            prev_data_num -= 1;
            if (prev_data_num < 0)
                prev_data_num = proc_size - 1;

            /*
            Виконується циклічне пересилання стрічки з матриці B у сусідні процеси (напрямок пересилки - за зростанням
            рангів процесів).
            */
            MPI_Sendrecv_replace(bufferB, tape_len * matrix_size, MPI_DOUBLE, next_proc, 0, prev_proc, 0, MPI_COMM_WORLD, &Status);
        }

        MPI_Gather(bufferC, tape_len * matrix_size, MPI_DOUBLE, matrixC, tape_len * matrix_size, MPI_DOUBLE, 0, MPI_COMM_WORLD);

        if (proc_rank == 0) {
            end_time = MPI_Wtime();
            printf("Tape Algorithm[%dx%d]: %7.4f\n", matrix_size, matrix_size, end_time-start_time);
            SquareMatrix::print(matrixA, matrix_size);
            SquareMatrix::print(matrixB, matrix_size);
            SquareMatrix::print(matrixC, matrix_size);
        }

        delete[] bufferA;
        delete[] bufferB;
        delete[] bufferC;
        if (proc_rank == 0) {
            delete[] matrixA;
            delete[] matrixB;
            delete[] matrixC;
        }
    }
}
