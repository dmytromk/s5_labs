#include <stdio.h>

#include "Common.h"
#include "NaiveMultiplication.h"
#include "CannonMultiplication.h"

int main(int argc, char **argv)
{
    srand(time(0));
    setvbuf(stdout, 0, _IONBF, 0);

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &process_num);
    MPI_Comm_rank(MPI_COMM_WORLD, &process_rank);

    int size = 2;

    run_naive(size);
    run_cannon(argc, argv, size);

    MPI_Finalize();

    return 0;
}
