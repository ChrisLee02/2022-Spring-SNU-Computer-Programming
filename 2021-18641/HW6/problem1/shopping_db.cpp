#include "shopping_db.h"
#include <algorithm>
#include <iostream>
#include <vector>
#include "product.h"
#include "user.h"
#include <deque>


ShoppingDB::ShoppingDB():signUpCount(0) {
}

void ShoppingDB::add_product(std::string name, int price) {
    // TODO: Problem 1.1
    Product *product = new Product(name, price);
    products.push_back(product);
}

bool ShoppingDB::edit_product(std::string name, int price) {
    // TODO: Problem 1.1
    if(price<=0) return false;
    else {
        Product* product = find_product(name);
        product->price = price;

        return true;
    }

}

Product *ShoppingDB::find_product(std::string name) {
    auto found = std::find(products.begin(), products.end(), Product(name, 0));
    if(found == products.end()) {
        return nullptr;
    }

    else {
        return *found;
    }
}

std::vector<Product *> &ShoppingDB::get_products() {
    return products;
}

void ShoppingDB::add_user(std::string username, std::string password, bool premium) {
    // TODO: Problem 1.2
    User* user;
    if(premium) {
        user = new PremiumUser(username, password);
    }
    else {
        user = new NormalUser(username, password);
    }
    users.push_back(user);
    user->signUpNumber = signUpCount;
    signUpCount++;
}



User* ShoppingDB::find_user(std::string username) {
    auto found = std::find(users.begin(), users.end(), User(username, "0"));
    if(found == users.end()) {
        return nullptr;
    }

    else {
        return *found;
    }
}

User *ShoppingDB::logIn(std::string username, std::string password, std::ostream &os) {
    User* user = find_user(username);

    if(user == nullptr || !user->authorize(password)) {
        os << "CLIENT_UI: Invalid username or password." << std::endl;
        return nullptr;
    }

    os << "CLIENT_UI: " + username + " is logged in." << std::endl;

    return user;
}

std::vector<User*> ShoppingDB::get_users() {
    return users;
};
