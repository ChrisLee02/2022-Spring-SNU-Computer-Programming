#include "RunLength.h"
#include "Delta.h"
#include "TestHelper.h"

using namespace std;
int main() {
    /* Main code for problem 2
    Implement TODOs in RunLength.cpp and Delta.cpp
    Use print_cout flag to print & test the implementation
    If print_cout is true, main code print results to console.
    Otherwise, verify implementation with outputs in test/ folder.
    */

    bool print_cout = false;
    ostringstream oss_lhs;
    ostream& os_lhs = print_cout ? cout : oss_lhs;

    // Test code 2.1
    RunLengthUnit rlu = {1,8};
    os_lhs << rlu << std::endl;
    if(!print_cout)
        TestHelper::Verify("2-1",oss_lhs, "test/test_2_1.out");
    oss_lhs.str("");
    oss_lhs.clear();

    // Test code 2.3
    DeltaUnit du = {-8};
    os_lhs << du << std::endl;
    if(!print_cout)
        TestHelper::Verify("2-3",oss_lhs, "test/test_2_3.out");
    oss_lhs.str("");
    oss_lhs.clear();

    // Check whether input file exists.
    if(!std::ifstream("test/test.in")) {
        std::cout << "Input file open error" << std::endl;
        exit(1);
    }

    // Run Length Encoding
    //cout<<"Run Length Compression"<<endl;
    RunLength* run_length = new RunLength();
    run_length->set_data_bit_width(3);
    run_length->Encode("test/test.in");
    run_length->Print(os_lhs);

    // Test code 2.2
    if(!print_cout)
        TestHelper::Verify("2-2",oss_lhs, "test/test_2_2.out");
    oss_lhs.str("");
    oss_lhs.clear();

    os_lhs<<run_length->Evaluate("test/test.in")<<endl;

    // Test code 2.5
    if(!print_cout)
        TestHelper::Verify("2-5-RL",oss_lhs, "test/test_2_5_rl.out");
    oss_lhs.str("");
    oss_lhs.clear();
    //cout<<"=========================================================="<<endl;



    // Delta Encoding
    //cout<<"Delta Compression Test"<<endl;
    Delta* delta = new Delta();
    delta->Encode("test/test.in");
    delta->Print(os_lhs);

    // Test code 2.4
    if(!print_cout)
        TestHelper::Verify("2-4",oss_lhs, "test/test_2_4.out");
    oss_lhs.str("");
    oss_lhs.clear();

    os_lhs<<delta->Evaluate("test/test.in")<<endl;
    // Test code 2.5
    if(!print_cout)
        TestHelper::Verify("2-5-Delta",oss_lhs, "test/test_2_5_delta.out");
    oss_lhs.str("");
    oss_lhs.clear();
    //cout<<"=========================================================="<<endl;

    return 0;
}
