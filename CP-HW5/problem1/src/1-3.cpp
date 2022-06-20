#include "header.h"

void MergeArrays(int *arr1, int len1, int *arr2, int len2) {
    // TODO: problem 1.3
    int i = 0, j = 0, k=0;
    int *merged = new int[len1+len2];
    while (i < len1 && j < len2) {
        if (arr1[i] > arr2[j]) {
            merged[k++] = arr1[i++];
        } else {
            merged[k++] = arr2[j++];
        }
    }
    while (i < len1) {
        merged[k++] = arr1[i++];
    }
    while (j < len2) {
        merged[k++] = arr2[j++];
    }

    for(int i = 0; i<len1+len2; i++) {
        arr1[i] = merged[i];
    }
}

