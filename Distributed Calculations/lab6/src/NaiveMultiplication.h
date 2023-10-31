//
// Created by Dmytro Mandziuk on 29.10.2023.
//

#ifndef LAB6_NAIVE_MULTIPLICATION
#define LAB6_NAIVE_MULTIPLICATION

#include "Common.h"

void init_naive(double** A, double** B, double** C, double** A_block, double** B_block, double** C_block, unsigned int* size, unsigned int* tape_len) {
    *tape_len = *size / process_num;

    /*
    Розмір буферів відповідає розміру матриці, поділенному на кількість потоків. Наприклад, матриця має
    розмірність 10 * 10, потоків - 4, тоді кожен буфер міститиме (10/4) * 10 = 25 елементів.
    Кожному потоку виділяється по три буфера.
    */
    *A_block = (double*)malloc((*tape_len * *size) * sizeof(double));
    *B_block = (double*)malloc((*tape_len * *size) * sizeof(double));
    *C_block = (double*)malloc((*tape_len * *size) * sizeof(double));
    fill(*C_block, 0, *tape_len);

    if (process_rank == 0) {
        *A = (double*)malloc((*size * *size) * sizeof(double));
        *B = (double*)malloc((*size * *size) * sizeof(double));
        *C = (double*)malloc((*size * *size) * sizeof(double));
        generateRandom(*A, *size, 2, 10);
        generateRandom(*B, *size, 2, 10);
    }
}

void run_naive(unsigned int size) {
    if (size % process_num != 0) {
        if (process_rank == 0) {
            printf("Invalid number of processes for multiplication of matrices of these sizes\n");
        }
        return;
    }

    double *A, *B, *C, *A_buffer, *B_buffer, *C_buffer;
    unsigned int tape_len;
    double start_time, end_time;

    init_naive(&A, &B, &C, &A_buffer, &B_buffer, &C_buffer, &size, &tape_len);

    /*
    Scatter розподіляє частини задачі по всіх потоках з комунікатора (для 4 потоків кожному дістається четверта
    частина кожної матриці). 1-й параметр - звідки розподіляються дані, 2-й - кількість елементів відправлених на
    кожен потік, 3-й - тип даних, 4-й - приймаючий буфер, 5-й - його розмір, 6-й - кількість елементів отриманих на
    кожному потоці, 7-й - тип даних отримуваних елементів, 8-й - номер процеса відправника, що розподіляє дані,
    останній - комунікатор.
    */
    MPI_Scatter(A, tape_len * size, MPI_DOUBLE, A_buffer, tape_len * size, MPI_DOUBLE, 0, MPI_COMM_WORLD);
    MPI_Scatter(B, tape_len * size, MPI_DOUBLE, B_buffer, tape_len * size, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    if (process_rank == 0) {
        start_time = MPI_Wtime();
    }

    // Для кожного потоку визначаємо наступника і попередника для циклічного обміну даними
    int next_proc = (process_rank + 1) % process_num;
    int prev_proc = process_rank - 1;
    if (prev_proc < 0)
        prev_proc = process_num - 1;
    int prev_data_num = process_rank;

    // Стрічкове перемноження матриць
    for (int p = 0; p < process_num; p++) {
        for (int i = 0; i < tape_len; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < tape_len; k++) {
                    /*
                    Виконується множення стрічки матриці A на стрічку матриці B, що містяться в даному потоці, і
                    результат записується у відповідний елемент буфера результуючої матриці C.
                    */
                    C_buffer[i * size + j] += A_buffer[prev_data_num * tape_len +
                                                       i * size + k] * B_buffer[k * size + j];
                }
            }
        }

        MPI_Status Status;

        prev_data_num -= 1;
        if (prev_data_num < 0)
            prev_data_num = process_num - 1;

        /*
        Виконується циклічне пересилання стрічки з матриці B у сусідні процеси (напрямок пересилки - за зростанням
        рангів процесів).
        */
        MPI_Sendrecv_replace(B_buffer, tape_len * size, MPI_DOUBLE,
                             next_proc, 0, prev_proc, 0, MPI_COMM_WORLD, &Status);
    }

    /*
    Після завершення циклу в кожному потоці міститися стрічка C, відповідна одній зі стрічок добутку A і B.
    Пересилаємо ії назад головному потоку. Тобто операція обернена до Scatter.
    Параметри для функції Gather ті самі, що й для Scatter.
    */
    MPI_Gather(C_buffer, tape_len * size, MPI_DOUBLE, C, tape_len * size, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    if (process_rank == 0) {
        end_time = MPI_Wtime();
        printf("Tape Algorithm[%dx%d]: %7.4f\n", size, size, end_time - start_time);

//            double *checker = (double*)malloc((size * size) * sizeof(double));
//            multiply(A, B, checker, size);
//
//            if (areEqual(C, checker, size)) {
//                printf("Matrices are equal.\n");
//            } else {
//                printf("Matrices are not equal.\n");
//            }
    }

    deconstruct(A, B, C, A_buffer, B_buffer, C_buffer);
}

#endif //LAB6_NAIVE_MULTIPLICATION