#ifndef RUNLENGTH_H
#define RUNLENGTH_H

#include <iostream>
#include <sstream>
#include <fstream>
#include <math.h>

struct RunLengthUnit {
    int data;
    int length;
};

std::ostream& operator<<(std::ostream& os, const RunLengthUnit& rlu);

class RunLength {
public:
    RunLength();
    ~RunLength();

    void set_data_bit_width(int data_bit_width);
    void Print(std::ostream& os = std::cout) const;
    void Encode(const char* file_name);
    double Evaluate(const char* file_name);

private:
    RunLengthUnit* run_length_unit_;
    int num_data_;
    int data_bit_width_;
    int length_bit_width_;
};

#endif //RUNLENGTH_H
