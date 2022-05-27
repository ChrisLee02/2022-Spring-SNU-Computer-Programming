#ifndef DELTA_H
#define DELTA_H

#include <iostream>
#include <sstream>
#include <fstream>
#include <math.h>

struct DeltaUnit {
    int delta;
};

std::ostream& operator<<(std::ostream& os, const DeltaUnit& du);

class Delta {
    public:
        Delta();
        ~Delta();

        void Print(std::ostream& os = std::cout) const;
        void Encode(const char* file_name);
        double Evaluate(const char* file_name);

    private:
        DeltaUnit* delta_unit_;
        int base_data_;
        int num_data_;
        int data_bit_width_;
        int delta_bit_width_;
};


#endif //DELTA_H
