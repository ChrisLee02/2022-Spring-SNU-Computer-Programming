#include <iostream>
#include "product.h"
#include "user.h"
#include <vector>
#include <cmath>
#include <map>
#include <algorithm>
#include <deque>
#include <set>
Product::Product(std::string name, int price): name(name), price(price) {}

bool Product::operator==(Product &product) const {
   /* std::cout << this->name << " " << product.name;*/
    return this->name == product.name;
}

std::string Product::toString(bool premium) {
    if(premium) {
        return "(" + name + ", " + std::to_string((int) std::round((double) price * (1-DISCOUNT_RATE))  ) + ")";
    }
    else return "(" + name + ", " + std::to_string(price) + ")";
}

bool operator==(const Product &product1, const Product &product2) {
    return product1.name == product2.name;
}

Product::Product(Product *pProduct): name(pProduct->name), price(pProduct->price) {

}

// << 오버라이딩 필요


