#include <string>
#include <algorithm>

bool IsPalindrome(std::string s) {
    // TODO: problem 1.1
    std::string s_backup = s;
    reverse(s.begin(), s.end());
    return s == s_backup;
}
