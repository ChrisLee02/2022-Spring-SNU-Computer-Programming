//
// Created by triom on 2022-05-30.
//

#include <iostream>
#include "yourimage.h"
#include "Scene.h"
#include "tools/LayerManipulator.h"
#include "tools/Stamp.h"
#include "utils.h"

void yourImageGenerator(void) {
    //TODO: Problem 2.5
    Scene s = Scene(1960, 1280);
    std::string PAKA_BACK = "./data/p25_0.png";
    std::string RALO = "./data/p25_1.png";
    std::string PAKA_DARAM = "./data/p25_2.jpeg";

    std::string outfile = "./data/p25_output.png";

    s.addLayerfromFile(PAKA_BACK, "PAKA_BACK");
    s.addLayerfromFile(PAKA_DARAM, "PAKA_DARAM");
    s.addLayerfromFile(PAKA_DARAM, "PAKA_DARAM2");
    s.addLayerfromFile(RALO, "RALO");

    s.selectLayer("PAKA_BACK")->resizeLayer(1960, 1280);
    s.selectLayer("PAKA_DARAM")->resizeLayer(600, 300);
    s.selectLayer("PAKA_DARAM2")->resizeLayer(600, 300);

    s.selectLayer("PAKA_DARAM")->setBias(1000, 300);

    s.selectLayer("RALO")->setBias(800, 300);

    LevelChanger(s.selectLayer("RALO"), Util::CHANNELS::B, 50, 100, 120);
    ColorMatcher(s.selectLayer("PAKA_DARAM"), s.selectLayer("PAKA_BACK"));
    BoxBlur(s.selectLayer("PAKA_DARAM"), 5, 10);
    Stamp stamp = Stamp();
    stamp.cropLayer(s.selectLayer("RALO"), 0, 0, 200, 230);
    stamp.StampOnLayer(s.selectLayer("PAKA_BACK"), 500, 100);
    stamp.StampOnLayer(s.selectLayer("PAKA_BACK"), 900, 500);
    stamp.StampOnLayer(s.selectLayer("PAKA_BACK"), 0, 700);
    stamp.StampOnLayer(s.selectLayer("PAKA_BACK"), 350, 200);
    stamp.StampOnLayer(s.selectLayer("PAKA_BACK"), 999, 999);
    s.saveScene(outfile);
}