#include "user.h"
#include <vector>
#include "product.h"
#include <algorithm>
User::User(std::string name, std::string password): name(name), password(password) {
    purchase_History = std::vector<Product*>();
    cart = std::vector<Product*>();
}

bool User::authorize(std::string password) {
    return this->password == password;
}

PremiumUser::PremiumUser(std::string name, std::string password): User(name, password) {
    is_premium = true;
}

NormalUser::NormalUser(std::string name, std::string password): User(name, password) {
    is_premium = false;
}
void User::add_purchase_history(Product* product){
    // TODO: Problem 1.2
    purchase_History.push_back(product);
}

bool User::operator==(User &user) const {
    return this->name == user.name;
}

bool operator==(const User &user1, const User &user2) {
    return user1.name == user2.name;
}

User::User(User *pUser): name(pUser->name), password(pUser->password), is_premium(pUser->is_premium) {
}

void User::add_cart(Product *product) {
    cart.push_back(product);
}

std::vector<Product *> &User::get_cart() {
    return cart;
}

void User::init_cart() {
    cart = std::vector<Product *>();
}

std::vector<Product *> &User::get_purchase_History() {
    return purchase_History;
}

