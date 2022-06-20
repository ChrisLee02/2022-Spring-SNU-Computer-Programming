//
// Created by triom on 2022-05-16.
//

#ifndef PROBLEM2_LAYERMANIPULATOR_H
#define PROBLEM2_LAYERMANIPULATOR_H

#include <Layer.h>


int ChannelMask(Layer* mask, Layer* target, int channel);
int BoxBlur(Layer* layer, int kernelSize, int rep);
int ColorMatcher(Layer* targetlayer, Layer* sourcelayer);
int LevelChanger(Layer *layer, int channel, int low, int mid, int high);
int ChannelScaling(Layer *layer, int channel, float scaleRatio);

#endif //PROBLEM2_LAYERMANIPULATOR_H
