#include <iostream>
#include "mpi.h"

#include <stdio.h>
#include "NaiveMultiplication.h"

int main(int argc, char **argv)
{
    MPI_Init(&argc, &argv);

    NaiveMultiplication::calculate(1400);
    //NaiveMultiplication::calculate(argc, argv, 4);

    MPI_Finalize();

    return 0;
}
