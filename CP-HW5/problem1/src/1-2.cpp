
int HammingDistance(int x, int y) {
    // TODO: problem 1.2
    int cnt = 0;
    if(x%2 != y%2) cnt++;
    x /= 2;
    y /= 2;
    while(x!=0 || y!=0) {
        if(x%2 != y%2) cnt++;
        x /= 2;
        y /= 2;
    }
    return cnt;
}

