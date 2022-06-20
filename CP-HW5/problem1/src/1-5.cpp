#include "header.h"
#include <utility>
#include <stack>


uint8_t GetPixel(uint8_t *image, int i, int j, int width) {
    return image[i * width + j];
}

void SetPixel(uint8_t *image, int i, int j, int width, int value) {
    image[i * width + j] = value;
}

void UpdateAndFind(int **image, int x, int y, int width, int height, int cnt) {

    //예외처리
    std::stack<std::pair<int, int>> stack;
    stack.push(std::pair<int,int>(x,y));
    while(!stack.empty()) {
        int i,j;
        std::pair<int, int> pair = stack.top();
        image[pair.first][pair.second] = cnt;
        for (i = pair.first - 1; i <= pair.first + 1; i++) {
            for (j = pair.second - 1; j <= pair.second + 1; j++) {
                if (i == pair.first && j == pair.second) continue;
                if (i < 0 || i >= height || j < 0 || j >= width) continue;
                if (image[i][j] != -1) continue;
                stack.push(std::pair<int,int>(i,j));
            }
        }
        if(pair == stack.top()) {
            stack.pop();
        }

    }
    /*image[x][y] = cnt;
    for (int i = x - 1; i <= x + 1; i++) {
        for (int j = y - 1; j <= y + 1; j++) {
            if (i == x && j == y) continue;
            else UpdateAndFind(image, i, j, width, height, cnt);
        }
    }*/
}

void Labeling(uint8_t *input_image, uint8_t *output_image, int width, int height) {
    // TODO: problem 1.5
    //image[i][j] => input_image[i * width + j] 에 해당
    int cnt = -1;
    int **image = new int *[height];


    for (int i = 0; i < height; i++) {
        image[i] = new int[width];
        for (int j = 0; j < width; j++) {
            uint8_t inputValue = GetPixel(input_image, i, j, width);
            if (inputValue == 0) {
                image[i][j] = -1;
            } else {
                image[i][j] = 255;
            }
        }
    } // image에서 0인 부분은 -1로 처리

    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            int value = image[i][j];
            if (value == -1) {
                cnt++;
                UpdateAndFind(image, i, j, width, height, cnt);
            }
        }
    }

    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            if (image[i][j] == 255) {
                SetPixel(output_image, i, j, width, 255);
            } else {
                SetPixel(output_image, i, j, width, image[i][j] * 50);
            }

        }
    }
    /*for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            std::cout << image[i][j] << " ";
        }
        std::cout << std::endl;
    }*/


}