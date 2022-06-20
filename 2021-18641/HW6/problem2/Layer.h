//
// Created by triom on 2022-05-16.
//

#ifndef PROBLEM2_LAYER_H
#define PROBLEM2_LAYER_H

#include <cstdint>
#include <string>

class Scene;
class Layer {
public:

    Layer(std::string layername, int Width, int Height, uint8_t* data);
    Layer(std::string layername, const Layer* layer);
    Layer(std::string layername);

    Layer(Layer *pLayer);

    ~Layer();
    std::string getName();

    int getW();
    int getH();
    int resizeLayer( int newWidth,  int newHeight);
    bool operator==(Layer &oLayer);
    friend bool operator==(const Layer &lhs, const Layer &rhs);
    int getXbias();
    int getYbias();
    int setBias(int x_bias, int y_bias);

    uint8_t getdata(int x, int y, int channel, int* status);
    void setdata(int x, int y, int channel, uint8_t data, int* status);

    bool visable;

    void makeScene(uint8_t *string, int i, int i1);

    void boxBlur(int kernelSize);

    void levelChange(int low, int mid, int high, int channel);

    void channelScale(int channel, float scaleRatio);

    uint8_t *getRGBA();

    void channelMask(uint8_t *maskData, int channel);

    void getSourceValue(int *min, int *average, int *max, int channel);

    void cropSelf(int xmin, int ymin, int xmax, int ymax);

    void stamp(Layer *targetLayer);

private:
    int Width, Height, x_bias, y_bias;
    std::string layername;
    uint8_t* RGBA; // This is data array of layer image
    int getIndex(int x, int y, int channel, int Width);


};


#endif //PROBLEM2_LAYER_H
