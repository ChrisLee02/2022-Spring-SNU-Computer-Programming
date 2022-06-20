#include "Delta.h"
#include <vector>
#include <string>
#include <utility>
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
    os << "(dt " << du.delta << ")";
    return os;
}

void Delta::Encode(const char* file_name){
    // TODO: problem 2.4
    ifstream fin(file_name);
    char tmpChar[10];
    string tmpString, dataWidth, numData;
    vector<int> data;
    vector<pair<int,int>> runLengthUnits;
    fin.getline(tmpChar, 8);
    dataWidth = string(tmpChar);
    fin.getline(tmpChar, 8);
    numData = string(tmpChar);

    data_bit_width_ = stoi(dataWidth);
    num_data_ = stoi(numData);

    while(fin) {
        fin.getline(tmpChar, 8);
        tmpString = string(tmpChar);
        if(tmpString.empty()) break;
        data.push_back(stoi(tmpString));
    }
    delta_unit_ = new DeltaUnit[data.size()-1];
    base_data_ = data[0];
    for(int i = 0; i<data.size()-1; i++) {
        delta_unit_[i].delta = data[i+1] - data[i];
    }
    int maxDelta = delta_unit_[0].delta;
    int minDelta = delta_unit_[0].delta;
    for(int i = 1; i<data.size()-1; i++) {
        if(maxDelta < delta_unit_[i].delta) {
            maxDelta = delta_unit_[i].delta;
        }
        if(minDelta > delta_unit_[i].delta) {
            minDelta = delta_unit_[i].delta;
        }
    }

    delta_bit_width_ = ceil( log2( max( abs( maxDelta ), abs(minDelta) ) +1 ) ) +1;

}

double Delta::Evaluate(const char* file_name) {
    // TODO: problem 2.5
    ifstream fin(file_name);
    char tmpChar[10];
    string tmpString;
    vector<int> data;
    vector<pair<int, int>> runLengthUnits;
    fin.getline(tmpChar, 8);
    fin.getline(tmpChar, 8);
    int originalNumData = stoi(string(tmpChar));
    int inputDataSize = (originalNumData + 2) * data_bit_width_;
    int deltaDataSize = 4*data_bit_width_ + (num_data_ - 1)*delta_bit_width_;
    //cout << "Delta: " << inputDataSize << " " << deltaDataSize<<endl;
    return round((double)inputDataSize/deltaDataSize*100) / 100;
}
