#include "Delta.h"

using namespace std;

Delta::Delta() : delta_unit_(NULL), num_data_(0), data_bit_width_(0), delta_bit_width_(0) {}

Delta::~Delta() {
    if(delta_unit_){
        delete[] delta_unit_;
    }
}

void Delta::Print(std::ostream& os) const{
    os << data_bit_width_ << " ";
    os << delta_bit_width_ << " ";
    os << num_data_ << " ";
    os << base_data_ << " ";
    for(int i=0; i<num_data_-1; i++){
        os << delta_unit_[i];
    }
    os<<endl;
}

std::ostream& operator<<(std::ostream& os, const DeltaUnit& du){
    // TODO: problem 2.3

    return os;
}

void Delta::Encode(const char* file_name){
    // TODO: problem 2.4

}

double Delta::Evaluate(const char* file_name) {
    // TODO: problem 2.5

    return 0;
}
