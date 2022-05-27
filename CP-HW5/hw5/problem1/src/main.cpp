#include "header.h"
#define STB_IMAGE_IMPLEMENTATION
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "stb_image.h"
#include "stb_image_write.h"

template <typename T>
void CheckArray(const T* a, const std::vector<T> b, int N) {
    if (!a || b.size() != N) {
        std::cout << "Failed" << std::endl;
        return;
    }
    for (std::size_t i = 0; i < N; ++i) {
        if (a[i] != b.at(i)) {
            std::cout << "Failed" << std::endl;
            return;
        }
    }
    std::cout << "Passed" << std::endl;
}

template <typename T>
void CheckValue(const T a, const T b) {
    if (a == b) {
        std::cout << "Passed" << std::endl;
    } else {
        std::cout << "Failed" << std::endl;
    }
}

int main() {
    // Test more with your own test cases

    // 1.1
    std::cout << "1-1" << std::endl;
    CheckValue(IsPalindrome(""), true);
    CheckValue(IsPalindrome("ab"), false);
    CheckValue(IsPalindrome("aabb"), false);
    CheckValue(IsPalindrome("12321a12321"), true);

    // 1.2
    std::cout << "1-2" << std::endl;
    CheckValue(HammingDistance(1,4), 2);
    CheckValue(HammingDistance(3,1), 1);
    CheckValue(HammingDistance(33,15), 4);

    // 1.3
    std::cout << "1-3" << std::endl;
    //int arr1[] ={1,3,5,0,0};
    //int arr2[] = {3,5};
    int t1_arr1[] ={5,3,1,0,0};
    int t1_arr2[] = {5,3};
    int t2_arr1[] ={3,2,1,0,0,0};
    int t2_arr2[] = {6,5, 4};
    int t3_arr1[] ={16,15,10,-3,0,0,0};
    int t3_arr2[] = {50,-5,-99};
    MergeArrays(t1_arr1, 3, t1_arr2, 2);
    CheckArray(t1_arr1, {5,5,3,3,1}, sizeof(t1_arr1)/sizeof(int));
    MergeArrays(t2_arr1, 3, t2_arr2, 3);
    CheckArray(t2_arr1, {6,5,4,3,2,1}, sizeof(t2_arr1)/sizeof(int));
    MergeArrays(t3_arr1, 4, t3_arr2, 3);
    CheckArray(t3_arr1, {50,16,15,10,-3,-5,-99}, sizeof(t3_arr1)/sizeof(int));

    // 1.4
    std::cout << "1-4" << std::endl;
    int* res = PascalTriangle(4);
    CheckArray(res, {1,3,3,1}, 4);
    res = PascalTriangle(5);
    CheckArray(res, {1,4,6,4,1}, 5);
    delete[] res;

    // 1.5
    std::cout << "1-5" << std::endl;
    int width, height, channels;
    uint8_t* input_image = stbi_load("test/input1.png", &width, &height, &channels, 0);
    if(input_image == NULL) {
        printf("Error in loading the image\n");
        exit(1);
    }
    unsigned char *output_image = (unsigned char*)malloc(width*height*1);
    Labeling(input_image, output_image, width, height);
    stbi_write_png("test/output.png", width, height, 1, output_image, width);
    stbi_image_free(input_image);
    stbi_image_free(output_image);

    return 0;
}
