#include <stdio.h>

#include "mpi.h"
#include "NaiveMultiplication.h"

int main(int argc, char **argv)
{
    MPI_Init(&argc, &argv);

    calculateNaiveMultiplication(10);
    //NaiveMultiplication::calculate(argc, argv, 4);

    MPI_Finalize();

    return 0;
}
