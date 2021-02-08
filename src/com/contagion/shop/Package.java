package com.contagion.shop;

import java.util.List;
import java.util.UUID;

public class Package {
    private final List<Product> productList;
    private final UUID from;
    private final UUID to;

    public Package(List<Product> productList, UUID from, UUID to) {
        this.productList = productList;
        this.from = from;
        this.to = to;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public int getPackageSize(){
        return productList.size();
    }
}
