#include <stdio.h>

#include "mpi.h"
#include "NaiveMultiplication.h"
#include "CannonMultiplication.h"

int main(int argc, char **argv)
{
    MPI_Init(&argc, &argv);

    calculateNaiveMultiplication(10);
    (10);
    calculateCannonMultiplication(10);

    MPI_Finalize();

    return 0;
}
