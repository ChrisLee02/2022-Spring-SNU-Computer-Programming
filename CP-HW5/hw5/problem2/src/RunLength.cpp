#include "RunLength.h"

using namespace std;
RunLength::RunLength() : run_length_unit_(NULL), num_data_(0), data_bit_width_(0), length_bit_width_(0) {}
RunLength::~RunLength() {
    if(run_length_unit_){
        delete[] run_length_unit_;
    }
}
void RunLength::set_data_bit_width(int data_bit_width) {
    length_bit_width_ = data_bit_width;
}

void RunLength::Print(std::ostream& os) const{
    os << data_bit_width_ << " ";
    os << length_bit_width_ << " ";
    os << num_data_ << " ";
    for(int i=0; i<num_data_; i++){
        os<<run_length_unit_[i];
    }
    os<<endl;
}

std::ostream& operator<<(std::ostream& os, const RunLengthUnit& rlu){
    // TODO: problem 2.1

    return os;
}

void RunLength::Encode(const char* file_name){
    // TODO: problem 2.2

}

double RunLength::Evaluate(const char* file_name) {
    // TODO: problem 2.5

    return 0;
}
