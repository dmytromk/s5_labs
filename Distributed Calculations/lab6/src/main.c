#include <stdio.h>

#include "NaiveMultiplication.h"
#include "CannonMultiplication.h"
#include "FoxMultiplication.h"

void run_tests(int size) {
    run_naive(size);
    run_cannon(size);
    runFoxMultiplicationTest(size);
}

int main(int argc, char **argv)
{
    srand(time(0));
    setvbuf(stdout, 0, _IONBF, 0);

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &process_num);
    MPI_Comm_rank(MPI_COMM_WORLD, &process_rank);

    run_tests(108);
    run_tests(216);
    run_tests(432);
    run_tests(864);
    run_tests(1728);

//    if (process_num == 1) {
//        run_tests(100);
//        run_tests(200);
//        run_tests(400);
//        run_tests(800);
//        run_tests(1600);
//    }
//    else if (process_num == 4) {
//        run_tests(144);
//        run_tests(288);
//        run_tests(576);
//        run_tests(1152);
//        run_tests(2304);
//    }
//    else if (process_num == 9) {
//        run_tests(162);
//        run_tests(324);
//        run_tests(648);
//        run_tests(1296);
//        run_tests(2592);
//    }

    MPI_Finalize();

    return 0;
}
