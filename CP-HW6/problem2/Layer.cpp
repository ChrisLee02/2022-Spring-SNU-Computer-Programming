//
// Created by triom on 2022-05-16.
//
#define STB_IMAGE_RESIZE_IMPLEMENTATION

#include "stb_image_resize.h"
#include "Layer.h"
#include "utils.h"
#include <iostream>

Layer::Layer(std::string layername, int Width, int Height, uint8_t *data) {
    this->layername = layername;
    this->Width = Width;
    this->Height = Height;
    this->RGBA = data;
    this->visable = true;
    this->x_bias = 0;
    this->y_bias = 0;
}

Layer::Layer(std::string layername, const Layer *layer) {
    this->layername = layername;
    this->Width = layer->Width;
    this->Height = layer->Height;




    RGBA = static_cast<uint8_t *>(malloc(sizeof(uint8_t) * Width * Height * 4));
    memcpy(RGBA, layer->RGBA, sizeof(uint8_t) * Width * Height * 4);
    //printf("%u %u\n", RGBA[100000], layer->RGBA[100000]);
    this->visable = layer->visable;
    this->x_bias = 0;
    this->y_bias = 0;
    //TODO: Problem 2.1
}

Layer::Layer(Layer *pLayer) : layername(pLayer->layername) {
    this->layername = pLayer->layername;
    this->Width = pLayer->Width;
    this->Height = pLayer->Height;
    RGBA = static_cast<uint8_t *>(malloc(sizeof(uint8_t) * Width * Height * 4));
    memcpy(RGBA, pLayer->RGBA, sizeof(uint8_t) * Width * Height * 4);
    this->visable = pLayer->visable;
    this->x_bias = 0;
    this->y_bias = 0;
}

Layer::Layer(std::string layername) : layername(layername) {
    RGBA = static_cast<uint8_t *>(malloc(sizeof(uint8_t)));
}

Layer::~Layer() {
    //TODO: Problem 2.1
    free(RGBA);
}

int Layer::resizeLayer(int newWidth, int newHeight) {
    if (newWidth <= 0 || newHeight <= 0) return Util::FAIL;
    // 포인터 변수 새로 만들고, 기존 RGBA랑 같이 인자로 전달한다.
    // 기존 RGBA를 free하고, RGBA가 새 포인터를 가리키도록 설정한다.
    uint8_t *newRGBA = static_cast<uint8_t *>(malloc(sizeof(uint8_t) * newWidth * newHeight * 4));
    stbir_resize_uint8(RGBA, Width, Height, 0, newRGBA, newWidth, newHeight, 0, 4);
    free(RGBA);
    RGBA = newRGBA;
    Width = newWidth;
    Height = newHeight;
    return Util::SUCCESS;
    // TODO: Problem 2.1
}

uint8_t Layer::getdata(int x, int y, int channel, int *status) {
    if (x < 0 || x >= Width) {
        *status = Util::FAIL;
        return 0;
    }
    if (y < 0 || y >= Height) {
        *status = Util::FAIL;
        return 0;
    }
    if (channel < 0 || channel >= 4) {
        *status = Util::FAIL;
        return 0;
    }

    *status = Util::SUCCESS;
    int index = getIndex(x, y, channel, Width);
    return RGBA[index];
    // TODO: Problem 2.1




}

void Layer::setdata(int x, int y, int channel, uint8_t data, int *status) {
    // TODO: Problem 2.1
    if (x < 0 || x >= Width) {
        *status = Util::FAIL;
        return;
    }
    if (y < 0 || y >= Height) {
        *status = Util::FAIL;
        return;
    }
    if (channel < 0 || channel >= 4) {
        *status = Util::FAIL;
        return;
    }

    *status = Util::SUCCESS;
    int index = getIndex(x, y, channel, Width);
    RGBA[index] = data;
}

std::string Layer::getName() {
    return layername;
}

int Layer::getW() {
    return Width;
}

int Layer::getH() {
    return Height;
}

int Layer::getXbias() {
    return x_bias;
}

int Layer::getYbias() {
    return y_bias;
}

int Layer::setBias(int x_bias, int y_bias) {
    this->x_bias = x_bias;
    this->y_bias = y_bias;
    return 0;
}

bool Layer::operator==(Layer &oLayer) {
    return this->layername == oLayer.layername;
}

bool operator==(const Layer &lhs, const Layer &rhs) {
    return lhs.layername == rhs.layername;
}

void Layer::makeScene(uint8_t *newData, int XBoundary, int YBoundary) {
    //255로 나누는 과정 들어가야됨. <- 들어감
    //절편 음수인 경우 안다뤘다 아직

    for (int y = 0; y + y_bias < YBoundary && y < Height; y++) {
        if (y + y_bias < 0) continue;
        for (int x = 0; x + x_bias < XBoundary && x < Width; x++) {
            if (x + x_bias < 0) continue;
            int alphaIndex = getIndex(x, y, 3, Width);
            int newAlphaIndex = getIndex(x + x_bias, y + y_bias, 3, XBoundary);;

            int RGBIndex[3];
            int newRGBIndex[3];
            double alphaZero, alphaA, alphaB;
            for (int i = 0; i < 3; i++) {
                RGBIndex[i] = getIndex(x, y, i, Width);
                newRGBIndex[i] = getIndex(x + x_bias, y + y_bias, i, XBoundary);
            }
            alphaA = (double) newData[newAlphaIndex] / 255;
            alphaB = (double) RGBA[alphaIndex] / 255;
            alphaZero = (alphaA + (1 - alphaA) * alphaB);
            newData[newAlphaIndex] = (uint8_t) (alphaZero * 255);
            if(alphaZero==0) alphaZero = 1;
            for (int i = 0; i < 3; i++) {
                //여기서 소숫점 버림 오차 생기는건 아닌지 체크해보기

                newData[newRGBIndex[i]] = (uint8_t) (
                        (newData[newRGBIndex[i]] * alphaA + RGBA[RGBIndex[i]] * (1 - alphaA) * alphaB) / alphaZero);
            }


        }
    }
}

void Layer::boxBlur(int kernelSize) {
    uint8_t *blurred = static_cast<uint8_t *>(malloc(sizeof(uint8_t) * Width * Height * 4));
    int radius = (kernelSize - 1) / 2;
    for (int y = 0; y < Height; y++) {
        for (int x = 0; x < Width; x++) {
            //printf("%d %u\n", getIndex(x, y, 2, Width), RGBA[getIndex(x, y, 2, Width)]);
            int RGBAIndex[4];
            for (int i = 0; i < 4; i++) {
                RGBAIndex[i] = getIndex(x, y, i, Width);
            }

            for (int i = 0; i < 4; i++) {
                int count = 0;
                int sum = 0;

                for (int kernelY = y - radius; kernelY <= y + radius; kernelY++) {
                    if (kernelY < 0 || kernelY >= Height) continue;
                    for (int kernelX = x - radius; kernelX <= x + radius; kernelX++) {
                        if (kernelX < 0 || kernelX >= Width) continue;
                        //std::cout << kernelX << " " << kernelY <<std::endl;
                        //printf("%u\n",RGBA[getIndex(kernelX, kernelY, i, Width)]);
                        sum = sum + RGBA[getIndex(kernelX, kernelY, i, Width)];
                        count++;
                    }
                }
                //printf("%u\n", sum);
                if (count == 0) blurred[RGBAIndex[i]] = 0;
                else blurred[RGBAIndex[i]] = sum / count;
            }
        }
    }
    /*for(int i = 0; i< sizeof(uint8_t) * Width * Height * 4;); i++) {
        if(blurred[i]==0) continue;
        printf("%u\n",blurred[i]);
    }*/
    free(RGBA);
    RGBA = blurred;
}

void Layer::levelChange(int low, int mid, int high, int channel) {
    for (int y = 0; y < Height; y++) {
        for (int x = 0; x < Width; x++) {
            int index = getIndex(x, y, channel, Width);
            uint8_t value = RGBA[index];
            if (value < low) {
                RGBA[index] = 0;
            } else if (value < mid) {
                RGBA[index] = (uint8_t) (((float) 128 / (float) (mid - low)) *
                                         (float) ((float) RGBA[index] - (float) low));
            } else if (value == mid) {
                RGBA[index] = 128;
            } else if (value <= high) {
                RGBA[index] = (uint8_t) (128 + ((float) 127 / (float) (high - mid)) *
                                               (float) ((float) RGBA[index] - (float) mid));
            } else {
                RGBA[index] = 255;
            }
        }
    }
}

void Layer::channelScale(int channel, float scaleRatio) {
    for (int y = 0; y < Height; y++) {
        for (int x = 0; x < Width; x++) {
            int index = getIndex(x, y, channel, Width);
            float result = (float) RGBA[index] * scaleRatio;
            if(result > 255) result = 255;
            RGBA[index] = (uint8_t) std::round(result);
        }
    }
}


int Layer::getIndex(int x, int y, int channel, int Width) {
    return (y * Width + x) * 4 + channel;
}

uint8_t *Layer::getRGBA() {
    return RGBA;
}

void Layer::channelMask(uint8_t *maskData, int channel) {
    for (int y = 0; y < Height; y++) {
        for (int x = 0; x < Width; x++) {
            int maskIndex = getIndex(x, y, channel, Width);
            int targetIndex = getIndex(x, y, 3, Width);
            RGBA[targetIndex] = 255 - maskData[maskIndex];
        }
    }
}

void Layer::getSourceValue(int *min, int *average, int *max, int channel) {
    *min = RGBA[getIndex(0,0,channel,Width)];
    *max = RGBA[getIndex(0,0,channel,Width)];
    int sum = 0;
    for (int y = 0; y < Height; y++) {
        for (int x = 0; x < Width; x++) {
            if(*min > RGBA[getIndex(x,y,channel,Width)]) {
                *min = RGBA[getIndex(x,y,channel,Width)];
            }
            if(*max < RGBA[getIndex(x,y,channel,Width)]) {
                *max = RGBA[getIndex(x,y,channel,Width)];
            }
            sum += RGBA[getIndex(x,y,channel,Width)];
        }
    }
    *average = sum / (Width * Height);
}

void Layer::cropSelf(int xmin, int ymin, int xmax, int ymax) {
    int newWidth = (xmax - xmin + 1);
    int newHeight = (ymax - ymin + 1);
    uint8_t *newData = static_cast<uint8_t *>(malloc(newWidth * newHeight * 4));
    for(int y = ymin; y<=ymax; y++) {
        for (int x = xmin; x <= xmax ; x++) {
            for(int i = 0; i<4; i++) {
                newData[getIndex(x-xmin, y-ymin, i, newWidth)] = RGBA[getIndex(x,y,i,Width)];
            }
        }
    }

    free(RGBA);
    RGBA = newData;
    Width = newWidth;
    Height = newHeight;

}

void Layer::stamp(Layer *targetLayer) {
    int YBoundary = targetLayer->getH();
    int XBoundary = targetLayer->getW();
    //A over B의 상황 - A가 위에 있는 놈임.
    uint8_t *a_RGBA = this->RGBA;
    uint8_t *b_RGBA = targetLayer->RGBA;
    for (int y = 0; y + y_bias < YBoundary && y < Height; y++) {
        if (y + y_bias < 0) continue;
        for (int x = 0; x + x_bias < XBoundary && x < Width; x++) {
            if (x + x_bias < 0) continue;
            int a_AlphaIndex = getIndex(x, y, 3, Width);
            int b_AlphaIndex = getIndex(x + x_bias, y + y_bias, 3, XBoundary);;

            int a_RGBIndex[3];
            int b_RGBIndex[3];
            double alphaZero, alphaA, alphaB;
            for (int i = 0; i < 3; i++) {
                a_RGBIndex[i] = getIndex(x, y, i, Width);
                b_RGBIndex[i] = getIndex(x + x_bias, y + y_bias, i, XBoundary);
            }

            alphaA = (double) a_RGBA[a_AlphaIndex] / 255;
            alphaB = (double) b_RGBA[b_AlphaIndex] / 255;
            alphaZero = (alphaA + (1 - alphaA) * alphaB);
            b_RGBA[b_AlphaIndex] = (uint8_t) (alphaZero * 255);
            if(alphaZero==0) alphaZero = 1;
            for (int i = 0; i < 3; i++) {
                //여기서 소숫점 버림 오차 생기는건 아닌지 체크해보기
                b_RGBA[b_RGBIndex[i]] = (uint8_t) (
                        (a_RGBA[a_RGBIndex[i]] * alphaA + b_RGBA[b_RGBIndex[i]] * (1 - alphaA) * alphaB) / alphaZero);
            }

        }
    }
}





