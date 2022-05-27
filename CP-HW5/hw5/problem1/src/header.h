#include <iostream>
#include <iterator>
#include <vector>
#include <stdint.h>

struct Pixel {
    int x, y;
};

bool IsPalindrome(std::string s);
int HammingDistance(int x, int y);
void MergeArrays(int* arr1, int len1, int* arr2, int len2);
int* PascalTriangle(int N);
void Labeling(uint8_t* input_image, uint8_t* output_image, int width, int height);
