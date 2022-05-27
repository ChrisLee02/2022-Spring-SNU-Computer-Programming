#ifndef TEST_H
#define TEST_H

#include <iostream>
#include <sstream>
#include <fstream>

namespace TestHelper {
    bool Verify(std::string name, std::string lhs, std::string rhs);
    bool Verify(std::string name, std::ostringstream& oss_lhs, std::string rhs);
    bool Verify(std::string name, std::ostringstream& oss_lhs, std::ostringstream& oss_rhs);
}


#endif //TEST_H
