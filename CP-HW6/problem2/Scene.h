//
// Created by triom on 2022-05-16.
//

#ifndef PROBLEM2_SCENE_H
#define PROBLEM2_SCENE_H


#include "Layer.h"
#include <string>
#include <cstdint>
#include <vector>

class Scene {
public:
    Scene(int Width, int Height);
    ~Scene();
    int addLayerfromFile(std::string filename, std::string layername);
    int addLayerfromLayer(std::string layername, std::string baselayername);
    int removeLayer(std::string layername);
    Layer* selectLayer(std::string layername);
    int saveScene(std::string filename);
    int changeCanvasSize(int Width, int Height);
    int changeLayerOrder(std::string layername, int order);
    std::vector<Layer *> layers;
private:
    int Width, Height;
};


#endif //PROBLEM2_SCENE_H
