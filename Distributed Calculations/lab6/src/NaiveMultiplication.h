//
// Created by Dmytro Mandziuk on 29.10.2023.
//

#ifndef LAB6_NAIVE_MULTIPLICATION
#define LAB6_NAIVE_MULTIPLICATION

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <mpi.h>

#include "matrix.h"

void calculateNaiveMultiplication(unsigned int matrix_size) {
    int proc_rank, proc_size, tape_len;
    double *matrixA, *matrixB, *matrixC;
    double *bufferA, *bufferB, *bufferC;

    MPI_Comm_rank(MPI_COMM_WORLD, &proc_rank); // Ранг (номер) даного потоку
    MPI_Comm_size(MPI_COMM_WORLD, &proc_size); // Кількість потокіа

    double start_time, end_time;

    if (proc_rank == 0) {
        matrixA = (double*)malloc(matrix_size * matrix_size * sizeof(double));
        matrixB = (double*)malloc(matrix_size * matrix_size * sizeof(double));
        matrixC = (double*)malloc(matrix_size * matrix_size * sizeof(double));
        generateRandom(matrixA, matrix_size, 2, 10);
        generateRandom(matrixB, matrix_size, 2, 10);
        fill(matrixC, matrix_size, 0);
        start_time = MPI_Wtime();
    }

    tape_len = matrix_size / proc_size;

    /*
    Розмір буферів відповідає розміру матриці, поділенному на кількість потоків. Наприклад, матриця має
    розмірність 10 * 10, потоків - 4, тоді кожен буфер міститиме (10/4) * 10 = 25 елементів.
    Кожному потоку виділяється по три буфера.
    */
    bufferA = (double*)malloc(tape_len * matrix_size * sizeof(double));
    bufferB = (double*)malloc(tape_len * matrix_size * sizeof(double));
    bufferC = (double*)malloc(tape_len * matrix_size * sizeof(double));

    /*
    Scatter розподіляє частини задачі по всіх потоках з комунікатора (для 4 потоків кожному дістається четверта
    частина кожної матриці). 1-й параметр - звідки розподіляються дані, 2-й - кількість елементів відправлених на
    кожен потік, 3-й - тип даних, 4-й - приймаючий буфер, 5-й - його розмір, 6-й - кількість елементів отриманих на
    кожному потоці,7-й - тип даних отримуваних елементів, 7-й - номер процеса відправника, що розподіляє дані,
    останній - комунікатор.
    */
    MPI_Scatter(matrixA, tape_len * matrix_size, MPI_DOUBLE, bufferA, tape_len * matrix_size, MPI_DOUBLE, 0, MPI_COMM_WORLD);
    MPI_Scatter(matrixB, tape_len * matrix_size, MPI_DOUBLE, bufferB, tape_len * matrix_size, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    // Для кожного потоку визначаємо наступника і попередника для циклічного обміну даними
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
                    результат записується у відповідний елемент буфера результуючої матриці C.
                    */
                    bufferC[i * matrix_size + j] += bufferA[prev_data_num * tape_len +
                                                            i * matrix_size + k] * bufferB[k * matrix_size + j];
                }
            }
        }

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

    /*
    Після завершення циклу в кожному потоці міститися стрічка C, відповідна одній зі стрічок добутку A і B.
    Пересилаємо ії назад головному потоку. Тобто операція обернена до Scatter.
    Параметри для функції Gather ті самі, що й для Scatter.
    */
    MPI_Gather(bufferC, tape_len * matrix_size, MPI_DOUBLE, matrixC, tape_len * matrix_size, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    // Так як MPI_Gather є блокуючою операцією, то бар'єра для синхронизації не потрібно
    //MPI_Barrier(MPI_COMM_WORLD);

    if (proc_rank == 0) {
        end_time = MPI_Wtime();
        printf("Tape Algorithm[%dx%d]: %7.4f\n", matrix_size, matrix_size, end_time-start_time);

//            double *checker = new double[matrix_size * matrix_size];
//            SquareMatrix::multiply(matrixA, matrixB, checker, matrix_size);
//
//            if (SquareMatrix::areEqual(matrixC, checker, matrix_size)) {
//                printf("Matrices are equal.\n");
//            } else {
//                printf("Matrices are not equal.\n");
//            }
    }

    free(bufferA);
    free(bufferB);
    free(bufferC);
    if (proc_rank == 0) {
        free(matrixA);
        free(matrixB);
        free(matrixC);
    }
}

#endif //LAB6_NAIVE_MULTIPLICATION