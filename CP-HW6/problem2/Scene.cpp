//
// Created by triom on 2022-05-16.
//
#define STB_IMAGE_IMPLEMENTATION
#define STB_IMAGE_WRITE_IMPLEMENTATION

#include <cmath>
#include <iostream>
#include "Scene.h"
#include "stb_image.h"
#include "stb_image_write.h"
#include "utils.h"
#include <algorithm>

Scene::Scene(int Width, int Height) {
    //Feel free to add more initailzition to this constructor
    this->Width = Width;
    this->Height = Height;
    this->layers = std::vector<Layer *>();
}

Scene::~Scene() {
    //Feel free to modifiy this destructor as you wish
    //여기 종속된 레이어들을 free 처리해줘야하나?
    std::vector<Layer *>().swap(layers);
}

int Scene::addLayerfromFile(std::string filename, std::string layername) {
    //TODO: Problem 2.1
    int comp = 4;
    int width, height;
    uint8_t *data = stbi_load(filename.c_str(), &width, &height, &comp, 4);
    /*printf("%u\n",data[100000]);
    printf("%d %d %d\n", _msize(data), width, height);*/
    if (data == NULL) return Util::FAIL;

    Layer *layer = new Layer(layername, width, height, data);
    layers.push_back(layer);
    return Util::SUCCESS;
}

int Scene::addLayerfromLayer(std::string layername, std::string baselayername) {
    Layer *layer = selectLayer(baselayername);

    if (layer == NULL) return Util::FAIL;
    Layer *newLayer = new Layer(layername, layer);
    layers.push_back(newLayer);

    return Util::SUCCESS;
}

int Scene::removeLayer(std::string layername) {
    Layer *layer = selectLayer(layername);
    if (layer == NULL) return Util::FAIL;
    layers.erase(remove(layers.begin(), layers.end(), Layer(layername)), layers.end());
    return Util::SUCCESS;
    //TODO: Problem 2.1
}

Layer *Scene::selectLayer(std::string layername) {
    //TODO: Problem 2.1
    auto found = std::find(layers.begin(), layers.end(), Layer(layername));

    if (found == layers.end()) return NULL;
    else return *found;

}

int Scene::changeLayerOrder(std::string layername, int order) {
    //TODO: Problem 2.1
    auto targetLayer = selectLayer(layername);
    if (targetLayer == NULL) return Util::FAIL;
    if (order > layers.size() - 1 || order < -1) return Util::FAIL;
    if (order == -1) order = layers.size() - 1;
    int index = std::distance(layers.begin(), std::find(layers.begin(), layers.end(), Layer(layername)));
    if (index > order) {
        for (int i = index; i < order; i++) {
            layers[i] = layers[i + 1];
        }
        layers[order] = targetLayer;
    } else {
        for (int i = index; i > order; i--) {
            layers[i] = layers[i - 1];
        }
        layers[order] = targetLayer;
    }
    return Util::SUCCESS;
}

int Scene::changeCanvasSize(int Width, int Height) {
    //TODO: Problem 2.1
    if (Width <= 0 || Height <= 0) return Util::FAIL;
    this->Width = Width;
    this->Height = Height;
    return Util::SUCCESS;
}

int Scene::saveScene(std::string filename) {
    //TODO: Problem 2.2

    // bias 고려해야됨
    // non-visible이면 패쓰
    //
    //layer에다가 바운더리랑 데이터 배열 주고, 이걸 처리하도록 하면 될 듯?
    //데이터 배열은 일단 255로 나눈 채로 알파 계산시키고, for문이 끝난 후에 다시 복구시킨다.
    //

    //invalid path가 대체 뭐가있음?

    uint8_t *newData = static_cast<uint8_t *>(calloc(Width * Height * 4, sizeof(uint8_t))); //0으로 처리됨.

    for (int i = layers.size() - 1; i >= 0; i--) {
        if (!layers[i]->visable) {
            continue;
        }
        layers[i]->makeScene(newData, Width, Height);
    }


    return stbi_write_png(filename.c_str(), Width, Height, 4, newData, 0);

}