#ifndef PROBLEM1_PRODUCT_H
#define PROBLEM1_PRODUCT_H

#include <string>

struct Product {
    Product(std::string name, int price);

    Product(Product *pProduct);

    const std::string name;
    int price;
public:
    bool operator==(Product& product) const;
    friend bool operator== ( const Product &product1, const Product &product2);
    std::string toString(bool premium);
};

#endif //PROBLEM1_PRODUCT_H
