//
// Created by triom on 2022-05-16.
//
#include <cmath>
#include "LayerManipulator.h"
#include "utils.h"
#include <iostream>

int BoxBlur(Layer *layer, int kernelSize, int rep) {
    //TODO: Problem 2.3
    if (layer == NULL || kernelSize < 1 || rep < 0) return Util::FAIL;

    for (int i = 0; i < rep; i++) {
        layer->boxBlur(kernelSize);
    }


    return Util::SUCCESS;
}

int LevelChanger(Layer *layer, int channel, int low, int mid, int high) {
    //TODO: Problem 2.3
    if (layer == NULL) return Util::FAIL;
    if (channel < 0 || channel > 3) return Util::FAIL;
    if (!(0 <= low && low <= mid && mid <= high && high <= 255 && low < high)) return Util::FAIL;

    layer->levelChange(low, mid, high, channel);

    return Util::SUCCESS;
}

int ChannelScaling(Layer *layer, int channel, float scaleRatio) {
    //TODO: Problem 2.3
    if (layer == NULL) return Util::FAIL;
    if (channel < 0 || channel > 3) return Util::FAIL;
    if (scaleRatio < 0) return Util::FAIL;

    layer->channelScale(channel, scaleRatio);


    return Util::SUCCESS;
}

int ChannelMask(Layer *mask, Layer *target, int channel) {
    //TODO: Problem 2.4
    if (mask == NULL || target == NULL) return Util::FAIL;
    if (channel < 0 || channel > 3) return Util::FAIL;
    if (mask->getH() != target->getH() || mask->getW() != target->getW()) return Util::FAIL;
    uint8_t *maskData = mask->getRGBA();
    target->channelMask(maskData, channel);
    return Util::SUCCESS;
}

int ColorMatcher(Layer *targetlayer, Layer *sourcelayer) {
    //TODO: Problem 2.4
    if (targetlayer == NULL || sourcelayer == NULL) return Util::FAIL;
    for (int i = 0; i < 3; ++i) {
        int min, average, max;
        sourcelayer->getSourceValue(&min, &average, &max, i);
        int mid = 256-average;
        if(mid > 255) mid = 255;
        if(mid > max) mid = max;
        if(mid < min) mid = min;
        targetlayer->levelChange(min, mid, max, i);
    }

    return 0;
}