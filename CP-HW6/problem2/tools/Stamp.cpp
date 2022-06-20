//
// Created by triom on 2022-05-17.
//
#include <cmath>
#include "Stamp.h"
#include "utils.h"

Stamp::Stamp() {
    this->stamplayer = NULL;
}

Stamp::~Stamp() {
    if (stamplayer)
        delete stamplayer;
}

int Stamp::cropLayer(Layer *layer, int xmin, int ymin, int xmax, int ymax) {
    //TODO: Problem 2.4
    if (layer == NULL) return Util::FAIL;
    if (!(xmin >= 0 && ymin >= 0 && xmax < layer->getW() && ymax < layer->getH() && xmin <= xmax && ymin <= ymax)) return Util::FAIL;

    if (stamplayer != NULL) free(stamplayer);

    stamplayer = new Layer("stamp", layer);
    stamplayer->cropSelf(xmin, ymin, xmax, ymax);

    return Util::SUCCESS;
}

int Stamp::StampOnLayer(Layer *layer, int xbias, int ybias) {
    //TODO: Problem 2.4

    stamplayer->setBias(xbias, ybias);
    stamplayer->stamp(layer);

    return 0;
}
