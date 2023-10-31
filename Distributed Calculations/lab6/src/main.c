#include <stdio.h>

#include "Common.h"
#include "NaiveMultiplication.h"
#include "CannonMultiplication.h"

int main(int argc, char **argv)
{
    MPI_Init(&argc, &argv);

    MPI_Comm_size(MPI_COMM_WORLD, &process_num);
    MPI_Comm_rank(MPI_COMM_WORLD, &process_rank);

    int size = 1024;

    run_naive(size);
    run_cannon(argc, argv, size);

    MPI_Finalize();

    return 0;
}
