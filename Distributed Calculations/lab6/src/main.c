#include <stdio.h>

#include "NaiveMultiplication.h"
#include "CannonMultiplication.h"
#include "FoxMultiplication.h"

/*
            TESTING RESULTS
                1 Process
    Processes    Tape     Cannon     Fox
    108         0.0051    0.0050    0.0046
    216         0.0355    0.0342    0.0340
    432         0.2820    0.2805    0.2813
    864         2.2641    2.2401    2.2332
    1728       18.6767   18.7514   18.3316


			4 Processes
    Sizes   Tape         Cannon       Fox
    108     0.0018108    0.0015108    0.0014108
    216     0.0102216    0.0096216    0.0091216
    432     0.0740432    0.0733432    0.0718432
    864     0.5807864    0.5804864    0.5803864
    1728    4.80351728   4.89821728   4.80221728

			9 Processes
    Sizes     Tape           Cannon         Fox
    108       0.0030108      0.0028108      0.0008108
    216       0.0093216      0.0089216      0.0083216
    432       0.0552432      0.0479432      0.0440432
    864       0.3259864      0.3411864      0.3282864
    1728      2.50221728     2.55771728     2.60901728
*/

void run_tests(int size) {
    if (process_rank == 0) {
        printf("%d\t", size);
    }
    run_naive(size);

    if (process_rank == 0) {
        printf("%d\t", size);
    }

    run_cannon(size);

    if (process_rank == 0) {
        printf("%d\t", size);
    }

    runFoxMultiplicationTest(size);

    if (process_rank == 0) {
        printf("%d\n", size);
    }
}

int main(int argc, char **argv)
{
    srand(time(0));
    setvbuf(stdout, 0, _IONBF, 0);

    if (process_rank == 0) {
        printf("%d processes\n", process_num);
        printf("sizes\tTape\tCannon\tFox\n");
    }

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
