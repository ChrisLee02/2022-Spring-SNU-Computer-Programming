#ifndef PROBLEM1_USER_H
#define PROBLEM1_USER_H

#include <string>
#include <vector>
#include "product.h"
#define DISCOUNT_RATE 0.1

class User {
public:
    User(std::string name, std::string password);

    User(User *pUser);

    const std::string name;
    bool is_premium;
    int signUpNumber;
    void add_cart(Product* product);
    void add_purchase_history(Product* product);
    bool operator==(User& user) const;
    friend bool operator== ( const User &user1, const User &user2);
    std::vector<Product *> &get_cart();
    std::vector<Product *> &get_purchase_History();
    bool authorize(std::string);
    std::vector<Product*> purchase_History;
    std::vector<Product*> cart;
    int similarity;
    void init_cart();

private:
    std::string password;


};

class NormalUser : public User {
public:
    NormalUser(std::string name, std::string password);
};

class PremiumUser : public User {
public:
    PremiumUser(std::string name, std::string password);
};

#endif //PROBLEM1_USER_H
