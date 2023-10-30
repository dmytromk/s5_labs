#include <stdio.h>

#include "mpi.h"
#include "NaiveMultiplication.h"
#include "CannonMultiplication.h"

int main(int argc, char **argv)
{
    MPI_Init(&argc, &argv);

    int size = 4;

    //calculateNaiveMultiplication(size);
    calculateCannonMultiplication(size);

    MPI_Finalize();

    return 0;
}
