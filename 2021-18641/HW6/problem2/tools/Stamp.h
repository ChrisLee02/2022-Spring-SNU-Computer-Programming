//
// Created by triom on 2022-05-17.
//

#ifndef PROBLEM2_STAMP_H
#define PROBLEM2_STAMP_H

#include "Layer.h"


class Stamp {
public:
    Stamp();
    ~Stamp();
    int cropLayer(Layer *layer, int xmin, int ymin, int xmax, int ymax);
    int StampOnLayer(Layer* layer, int xbias, int ybias);
private:
    Layer* stamplayer;
};


#endif //PROBLEM2_STAMP_H
