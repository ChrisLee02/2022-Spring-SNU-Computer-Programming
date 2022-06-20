#ifndef PROBLEM1_SHOPPING_DB_H
#define PROBLEM1_SHOPPING_DB_H

#include <string>
#include <vector>
#include "user.h"
#include "product.h"

class ShoppingDB {
public:
    ShoppingDB();
    void add_product(std::string name, int price);
    bool edit_product(std::string name, int price);
    Product* find_product(std::string name);
    void add_user(std::string username, std::string password, bool premium);
    int signUpCount;
    std::vector<Product *> & get_products();

    User* find_user(std::string username);
    std::vector<User *> get_users();
    User *logIn(std::string username, std::string password, std::ostream &os);
private:
    std::vector<User*> users;
    std::vector<Product*> products;


};

#endif //PROBLEM1_SHOPPING_DB_H
