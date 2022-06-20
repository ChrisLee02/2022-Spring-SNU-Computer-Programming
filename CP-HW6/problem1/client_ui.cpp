#include <vector>
#include "client_ui.h"
#include "product.h"
#include "user.h"
#include <iostream>
#include <cmath>
#include <map>
#include <algorithm>
#include <deque>
#include <set>

ClientUI::ClientUI(ShoppingDB &db, std::ostream &os) : UI(db, os), current_user(nullptr) {}

void ClientUI::signup(std::string username, std::string password, bool premium) {
    // TODO: Problem 1.2
    os << "CLIENT_UI: " + username + " is signed up." << std::endl;
    db.add_user(username, password, premium);

}

void ClientUI::login(std::string username, std::string password) {
    // TODO: Problem 1.2
    if (current_user != nullptr) {
        os << "CLIENT_UI: Please logout first." << std::endl;
        return;
    }

    current_user = db.logIn(username, password, os);
}

void ClientUI::logout() {
    // TODO: Problem 1.2
    if (current_user == nullptr) {
        os << "CLIENT_UI: There is no logged-in user." << std::endl;
        return;
    }
    os << "CLIENT_UI: " + current_user->name + " is logged out." << std::endl;
    current_user = nullptr;
}

bool ClientUI::checkLogin() {
    if (current_user == nullptr) {
        os << "CLIENT_UI: Please login first." << std::endl;
    }
    return current_user != nullptr;
}

void ClientUI::buy(std::string product_name) {
    // TODO: Problem 1.2
    if (!checkLogin()) return;
    Product *product = db.find_product(product_name);
    if (product == nullptr) {
        os << "CLIENT_UI: Invalid product name." << std::endl;
        return;
    }
    /// 신버전코드
    buy_product(product);

    /// 구버전코드
   /* if (current_user->is_premium) {
        //premium case
       // std::cout << current_user->name << " " << current_user->is_premium;
       buy_product(new Product(product->name, std::round((double) product->price * (1-DISCOUNT_RATE))));
    } else {
        //normal case
        buy_product(product);
    }*/

}

void ClientUI::buy_product(Product *product) {
    /// 구버전 코드
    /*current_user->add_purchase_history(product);
    os << "CLIENT_UI: Purchase completed. Price: " << product->price << "." << std::endl;
    */

    /// 수정
    current_user->add_purchase_history(product);
     if (current_user->is_premium) {
        //premium case
        os << "CLIENT_UI: Purchase completed. Price: " << std::round((double) product->price * (1-DISCOUNT_RATE)) << "." << std::endl;
    } else {
        //normal case
         os << "CLIENT_UI: Purchase completed. Price: " << product->price << "." << std::endl;
    }

}

void ClientUI::add_to_cart(std::string product_name) {
    if (!checkLogin()) return;
    // TODO: Problem 1.2
    Product *product = db.find_product(product_name);
    if (product == nullptr) {
        os << "CLIENT_UI: Invalid product name." << std::endl;
        return;
    }

    /// 신버전코드
    add_product_to_cart(product);

    /// 구버전코드

    /*if (current_user->is_premium) {
        //typeid(current_user).name() ==  typeid(new PremiumUser("","")).name()
        //premium case
        add_product_to_cart(new Product(product->name, std::round((double) product->price * (1-DISCOUNT_RATE))));
    } else {
        //normal case
        add_product_to_cart(product);
    }*/
}

void ClientUI::add_product_to_cart(Product *product) {
    current_user->add_cart(product);
    os << "CLIENT_UI: " << product->name << " is added to the cart." << std::endl;
}

void ClientUI::list_cart_products() {
    if (!checkLogin()) return;
    std::string list_print = "[";
    for (Product *product: current_user->get_cart()) {
        list_print += product->toString(current_user->is_premium) + ", ";
    }
    if (list_print == "[") {
        list_print = "[]";
    } else {
        list_print = list_print.substr(0, list_print.size() - 2) + "]";
    }


    os << "CLIENT_UI: Cart: " + list_print << std::endl;
    // TODO: Problem 1.2.

}

void ClientUI::buy_all_in_cart() {
    if (!checkLogin()) return;
    int sum = 0;
    for (Product *product: current_user->get_cart()) {
        if(current_user->is_premium) {
            sum += std::round((double) product->price * (1-DISCOUNT_RATE));
        }
        else {
            sum += product->price;
        }
        current_user->add_purchase_history(product);
    }
    current_user->init_cart();
    os << "CLIENT_UI: Cart purchase completed. Total price: " << sum << "." << std::endl;
    // TODO: Problem 1.2

}

class pairComparator {
public:
    int operator()(std::pair<Product *, int> *lhs, std::pair<Product *, int> *rhs) {
        return lhs->second > rhs->second;
    }
};

class userComparator {
public:
    int operator()(User *lhs, User *rhs) {
        if (lhs->similarity == rhs->similarity) {
            return lhs->signUpNumber < rhs->signUpNumber;
        }
        else return lhs->similarity > rhs->similarity;
    }
};

void ClientUI::recommend_products() {
    if (!checkLogin()) return;
    // TODO: Problem 1.3
    std::string list_print = "[";


    if (current_user->is_premium) {
        std::vector<User *> users = db.get_users();
        users.erase(remove(users.begin(), users.end(), current_user), users.end());
       // std::cout << users.back()->name << db.get_users().back()->name << std::endl;
        std::set<Product *> pivotProductSet(current_user->get_purchase_History().begin(),
                                            current_user->get_purchase_History().end());

        for (User *user: users) {
            user->similarity = 0;
            for (Product *pivotProduct: pivotProductSet) {
                for (Product *product: user->get_purchase_History()) {
                    if (*pivotProduct == *product) {
                        user->similarity = user->similarity + 1;
                    }
                }
            }
        }
        //같은 유사도면 로그인 순으로 처리?
        //-> DB의 퍼블릭 변수로 로그인 총 순서를 관리하고, 유저의 퍼블릭 변수로 로그인 순서 ㄴ엏어서 비교해주면 될 듯.
        //vector로 추천 목록 관리 & 만약 이미 있는 물건이면 다음 순서의 유저로 넘어간다.
        std::sort(users.begin(), users.end(), userComparator());
        std::vector<Product *> recommend;
        recommend.back();
        for (User *user: users) {
            if (recommend.size() == 3) break;
            bool exist = false;
            if(user->get_purchase_History().empty()) continue; //빈 벡터 예외처리
            Product *pivotProduct = user->get_purchase_History().back();
            for (Product *product: recommend) {
                if(*product == *pivotProduct) {
                    exist = true;
                    break;
                }
            }
            if(!exist) {
                recommend.push_back(pivotProduct);
               /* if(user->is_premium) {
                    recommend.push_back(pivotProduct);
                }
                else {
                    recommend.push_back(new Product(pivotProduct->name, std::round((double) pivotProduct->price * (1-DISCOUNT_RATE))) );
                }*/
            }
        }

        for (Product *product: recommend) {
            list_print += product->toString(current_user->is_premium) + ", ";
        }

    }
    else {
        //normal case
        // 유저의 구매 목록을 이용한다. 비교 기준은 가장 많이 산 것, 횟수 같으면 최신의 구매 대상으로
        std::deque<std::pair<Product *, int> *> count_product;
//        for (Product *product: current_user->purchase_History)
        for(int i = current_user->purchase_History.size() - 1; i>=0; i--) {
            Product *product = current_user->purchase_History[i];

            bool found = false;
            for (std::pair<Product *, int> *pair: count_product) {

                if (pair->first->name == product->name) {
                    found = true;
                    pair->second = pair->second + 1;
                    break;
                }
            }
            if (!found) {
                count_product.push_back(new std::pair<Product *, int>(product, 1));
            }
        }

       /* for(int i = current_user->purchase_History.size() - 1; i>=0; i--) {
            Product *product = current_user->purchase_History[i];
            std::cout << product->name  << std::endl;
        }
        std::cout << "================" << std::endl;
        for (std::pair<Product *, int> *pair: count_product) {
            std::cout << pair->first->name + " " << pair->second << std::endl;
        }
        std::cout << "================" << std::endl;*/
        std::stable_sort(count_product.begin(), count_product.end(), pairComparator());
        int cnt = 0;
        for (std::pair<Product *, int> *pair: count_product) {
            if (cnt == 3) break;
           // std::cout << pair->first->name + " " << pair->second << std::endl;
            list_print += pair->first->toString(current_user->is_premium) + ", ";
            cnt++;
        }


    }
    if (list_print == "[") {
        list_print = "[]";
    } else {
        list_print = list_print.substr(0, list_print.size() - 2) + "]";
    }
    os << "CLIENT_UI: Recommended products: " + list_print << std::endl;
}





