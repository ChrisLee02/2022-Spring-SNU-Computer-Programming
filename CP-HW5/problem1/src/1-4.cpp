#include "header.h"

int* PascalTriangle(int N) {
    // TODO: problem 1.4
    int **pascalTriangle = new int*[N];
    for(int i = 0; i<N; i++) {
        pascalTriangle[i] = new int[i+1];
    }
    pascalTriangle[0][0] = 1;
    for(int i = 1; i<N; i++) {
        pascalTriangle[i][0] = 1;
        pascalTriangle[i][i] = 1;
        for(int j = 1; j<i; j++) {
            pascalTriangle[i][j] = pascalTriangle[i-1][j-1] + pascalTriangle[i-1][j];
        }
    }

    return pascalTriangle[N-1];
}

