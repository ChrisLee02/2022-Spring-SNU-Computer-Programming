//
// Created by triom on 2022-05-16.
//
#include "utils.h"
#include <iostream>
#include "stb_image.h"
#include "stb_image_write.h"
#include "Scene.h"
#include "tools/Stamp.h"
#include "tools/LayerManipulator.h"
#include "yourimage.h"

void testall(void) {
    Scene s = Scene(1000, 500);
    std::string filename1 = "./data/image0.jpg";
    std::string filename2 = "./data/background0.jpg";
    std::string outfile = "./data/output.png";
    s.addLayerfromFile(filename2, "back0");
    s.addLayerfromFile(filename1, "tree1");

    s.addLayerfromLayer("tree2", "tree1");

    ColorMatcher(s.selectLayer("tree1"), s.selectLayer("back0"));
    LevelChanger(s.selectLayer("tree2"), Util::CHANNELS::B, 50, 130, 180);
    ChannelMask(s.selectLayer("tree2"), s.selectLayer("tree1"), Util::CHANNELS::B);
    s.selectLayer("tree2")->visable = false;
    Stamp stamp = Stamp();
    stamp.cropLayer(s.selectLayer("tree1"), 300, 100, 1300, 900);
    stamp.StampOnLayer(s.selectLayer("tree1"), 1200, 100);
    stamp.StampOnLayer(s.selectLayer("tree1"), 900, 100);
    BoxBlur(s.selectLayer("back0"), 5, 1);
    s.changeLayerOrder("back0", 0);
    s.changeLayerOrder("tree1", -1);
    s.changeCanvasSize(s.selectLayer("tree1")->getW(), s.selectLayer("tree1")->getH());
    s.saveScene(outfile);
};

void test2_2(void) {
    Scene s = Scene(1000, 500);
    std::string filename1 = "./data/birds1.png";
    std::string filename2 = "./data/background0.jpg";
    std::string outfile = "./data/output2_2.png";
    s.addLayerfromFile(filename2, "back0");
    s.addLayerfromFile(filename1, "bird0");
    s.addLayerfromLayer("bird1", "bird0");
    s.selectLayer("bird1")->resizeLayer(200, 200);
    s.selectLayer("bird1")->setBias(200, 100);
    s.selectLayer("bird0")->setBias(400, 100);
    s.selectLayer("back0")->setBias(-1000, -3000);
    s.saveScene(outfile);

};

void test2_3_blur(void) {
    Scene s = Scene(1000, 500);
    std::string filename1 = "./data/birds1.png";
    std::string filename2 = "./data/background0.jpg";
    std::string outfile = "./data/output2_3_blur.png";
    s.addLayerfromFile(filename2, "back0");
    s.addLayerfromFile(filename1, "bird0");
    s.addLayerfromLayer("bird1", "bird0");
    s.selectLayer("bird1")->setBias(0, 100);
    s.selectLayer("bird0")->setBias(400, 100);
    BoxBlur(s.selectLayer("bird0"), 5, 3);

    s.selectLayer("back0")->setBias(-1000, -3000);
    s.saveScene(outfile);
};

void test2_3_level(void) {
    Scene s = Scene(1000, 500);
    std::string filename1 = "./data/image0.jpg";
    std::string outfile = "./data/output2_3_level.png";
    s.addLayerfromFile(filename1, "tree0");
    LevelChanger(s.selectLayer("tree0"), Util::CHANNELS::B, 50, 130, 180);
    s.changeCanvasSize(s.selectLayer("tree0")->getW(), s.selectLayer("tree0")->getH());
    s.saveScene(outfile);
};

void test2_3_scaling(void) {
    Scene s = Scene(2000, 2000);
    std::string filename1 = "./data/image0.jpg";
    std::string outfile2 = "./data/output2_3_scaling.png";
    s.addLayerfromFile(filename1, "tree");
    ChannelScaling(s.selectLayer("tree"), 0, 1.2);
    ChannelScaling(s.selectLayer("tree"), 1, 0.6);
    ChannelScaling(s.selectLayer("tree"), 2, 0.2);
    s.changeCanvasSize(s.selectLayer("tree")->getW(), s.selectLayer("tree")->getH());
    s.saveScene(outfile2);
};

void test2_4_mask(void) {
    Scene s = Scene(2000, 2000);
    std::string filename1 = "./data/image1.jpg";
    std::string filename2 = "./data/background0.jpg";
    std::string outfile2 = "./data/output2_4_mask.png";
    s.addLayerfromFile(filename2, "back0");
    s.addLayerfromFile(filename1, "tree");
    s.addLayerfromLayer("tree_mask", "tree");

    LevelChanger(s.selectLayer("tree_mask"), Util::CHANNELS::B, 105, 130, 157);
    ChannelMask(s.selectLayer("tree_mask"), s.selectLayer("tree"), Util::CHANNELS::B);
    s.selectLayer("tree_mask")->visable = false;
    s.selectLayer("back0")->setBias(0, -3000);
    s.changeCanvasSize(s.selectLayer("tree")->getW(), s.selectLayer("tree")->getH());
    s.saveScene(outfile2);
};

void test2_4_colorMatching(void) {
    Scene s = Scene(1000, 500);
    std::string filename1 = "./data/birds1.png";
    std::string filename2 = "./data/background0.jpg";
    std::string outfile = "./data/output2_4_colorMatching.png";
    s.addLayerfromFile(filename2, "back0");
    s.addLayerfromFile(filename1, "bird0");
    s.addLayerfromLayer("bird1", "bird0");
    s.selectLayer("bird1")->setBias(0, 100);
    s.selectLayer("bird0")->setBias(400, 100);
    ColorMatcher(s.selectLayer("bird0"), s.selectLayer("back0"));
    s.selectLayer("back0")->setBias(-1000, -3000);
    s.saveScene(outfile);
};

int main(void) {
    testall();
    test2_2();
    test2_3_blur();
    test2_3_level();
    test2_3_scaling();
    test2_4_mask();
    test2_4_colorMatching();
    yourImageGenerator();
    return 0;
}
