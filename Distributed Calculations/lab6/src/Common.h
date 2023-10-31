//
// Created by Dmytro Mandziuk on 31.10.2023.
//

#ifndef LAB6_COMMON_H
#define LAB6_COMMON_H

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "mpi.h"
#include "matrix.h"

static int process_num = 0;
static int process_rank = 0;

void deconstruct(double* A, double* B, double* C, double* A_block, double* B_block, double* C_block) {
    if (process_rank == 0) {
        free(A);
        free(B);
        free(C);
    }
    free(A_block);
    free(B_block);
    free(C_block);
}

#endif //LAB6_COMMON_H
