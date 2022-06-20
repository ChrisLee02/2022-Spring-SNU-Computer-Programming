#include "RunLength.h"

#include <vector>
#include <string>
#include <utility>

using namespace std;

RunLength::RunLength() : run_length_unit_(NULL), num_data_(0), data_bit_width_(0), length_bit_width_(0) {}

RunLength::~RunLength() {
    if (run_length_unit_) {
        delete[] run_length_unit_;
    }
}

void RunLength::set_data_bit_width(int data_bit_width) { // length_bit_width를 업데이트 하는 메소드임.
    length_bit_width_ = data_bit_width;
}

void RunLength::Print(std::ostream &os) const {
    os << data_bit_width_ << " ";
    os << length_bit_width_ << " ";
    os << num_data_ << " ";
    for (int i = 0; i < num_data_; i++) {
        os << run_length_unit_[i];
    }
    os << endl;
}

std::ostream &operator<<(std::ostream &os, const RunLengthUnit &rlu) {
    // TODO: problem 2.1
    os << "(" << rlu.data << " " << rlu.length << ")";
    return os;
}


void RunLength::Encode(const char *file_name) {
    // TODO: problem 2.2
    ifstream fin(file_name);
    char tmpChar[10];
    string tmpString, dataWidth, numData;
    vector<int> data;
    vector<pair<int, int>> runLengthUnits;
    fin.getline(tmpChar, 8);
    dataWidth = string(tmpChar);
    fin.getline(tmpChar, 8);
    data_bit_width_ = stoi(dataWidth);
    while (fin) {
        fin.getline(tmpChar, 8);
        tmpString = string(tmpChar);
        if (tmpString.empty()) break;
        data.push_back(stoi(tmpString));
    }
    runLengthUnits.push_back(pair<int, int>(data[0], 1));

    for (int i = 1; i < data.size(); i++) {
        if (runLengthUnits.back().first != data[i] || runLengthUnits.back().second == pow(2, length_bit_width_)) {
            runLengthUnits.push_back(pair<int, int>(data[i], 1));
        } //숫자가 다르거나, length_bit_width_에 도달하면 새로 집어넣어야 함.
        else {
            runLengthUnits.back().second++;
        }
    }
    num_data_ = runLengthUnits.size();
    run_length_unit_ = new RunLengthUnit[runLengthUnits.size()];
    for (int i = 0; i < runLengthUnits.size(); i++) {
        run_length_unit_[i].data = runLengthUnits[i].first;
        run_length_unit_[i].length = runLengthUnits[i].second;
    }
}

double RunLength::Evaluate(const char *file_name) {
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
    int RLDataSize = (num_data_ + 3) * data_bit_width_ + num_data_*length_bit_width_;
    //cout << "RL: " << inputDataSize << " " << RLDataSize<<endl;
    return round((double)inputDataSize/RLDataSize*100)/100;
}
