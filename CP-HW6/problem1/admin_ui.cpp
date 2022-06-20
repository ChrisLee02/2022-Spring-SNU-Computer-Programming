#include "admin_ui.h"
#include <string>
#include <vector>
#include "product.h"
#include <iostream>
#include <algorithm>


AdminUI::AdminUI(ShoppingDB &db, std::ostream& os): UI(db, os) { }

void AdminUI::add_product(std::string name, int price) {
    // TODO: Problem 1.1
    if(price>0) {
        os <<  "ADMIN_UI: "+ name + " is added to the database."<<std::endl;
        db.add_product(name, price);
    }
    else {
        os<< "ADMIN_UI: Invalid price." <<std::endl;
    }

}

void AdminUI::edit_product(std::string name, int price) {
    // TODO: Problem 1.1
    Product* found = db.find_product(name);
    if(found != nullptr) {
        if(db.edit_product(name, price)) {
            os <<"ADMIN_UI: " + name + " is modified from the database." << std::endl;
        }
        else {
            os<< "ADMIN_UI: Invalid price." <<std::endl;
        }
    } else {
        os<< "ADMIN_UI: Invalid product name." <<std::endl;
    }
}

void AdminUI::list_products() {
    // TODO: Problem 1.1
    std::vector<Product*> &products = db.get_products();
    std::string list_print ="[";
    for(Product *product: products) {
        list_print += product->toString(false) + ", ";
    }
    if(list_print=="[") {
        list_print = "[]";
    }
    else {
        list_print = list_print.substr(0, list_print.size() - 2) + "]";
    }

    os << "ADMIN_UI: Products: " + list_print << std::endl;
}
