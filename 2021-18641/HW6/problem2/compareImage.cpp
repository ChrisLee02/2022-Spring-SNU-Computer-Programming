//
// Created by triom on 2022-05-30.
//
#define STB_IMAGE_IMPLEMENTATION

#include <cmath>
#include <iostream>
#include <string>
#include "stb_image.h"

int main(int argv, char** argc){
    if(argv != 3) return 1;
    std::string image1 = argc[1];
    std::string image2 = argc[2];
    int w1, h1, c1, w2, h2, c2;
    uint8_t* input_image1 = stbi_load(image1.data(), &w1, &h1, &c1, 0);
    uint8_t* input_image2 = stbi_load(image2.data(), &w2, &h2, &c2, 0);

    double diff[4] = {0.0};
    if(input_image1 == nullptr || input_image2== nullptr){
        std::cout << "Wrong Filepath" << std::endl;
        return 0;
    }
    else if(w1 == w2 && h1 == h2 && c1 == c2){
        for(int i = 0; i<w1*h1*c1; i+=4){
            diff[0] += std::pow((double) input_image1[i] - (double) input_image2[i], 2.0);
            diff[1] += std::pow((double) input_image1[i+1] - (double) input_image2[i+1], 2.0);
            diff[2] += std::pow((double) input_image1[i+2] - (double) input_image2[i+2], 2.0);
            diff[3] += std::pow((double) input_image1[i+3] - (double) input_image2[i+3], 2.0);
        }
        double size = w1 * h1;
        bool allpass = true;
        for(int k=0;k<4;k++){
            diff[k] /= size;
            diff[k] = std::sqrt(diff[k]);
            bool pass = diff[k] < 2;
            allpass = allpass && pass;
            std::cout << "Channel "<< k;
            std::cout << " RMSE " << diff[k] << std::endl;
        }
        std::cout << "Result ";
        if(allpass) std::cout << "GOOD" << std::endl;
        else std::cout << "BAD" << std::endl;

    }
    else{
        std::cout << "Different Image Size" << std::endl;
    }

    stbi_image_free(input_image1);
    stbi_image_free(input_image2);
    return 0;
}