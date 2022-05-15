package com.example.mobile_subproject_nhom05.listener;

import com.example.mobile_subproject_nhom05.module.Cart;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<Cart> carts);
    void onCartLoadFailed(String message);
}
