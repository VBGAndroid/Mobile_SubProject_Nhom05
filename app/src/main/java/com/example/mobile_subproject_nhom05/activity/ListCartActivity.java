package com.example.mobile_subproject_nhom05.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobile_subproject_nhom05.R;
import com.example.mobile_subproject_nhom05.adapter.CartAdapter;
import com.example.mobile_subproject_nhom05.event.UpdateCartEvent;
import com.example.mobile_subproject_nhom05.listener.ICartLoadListener;
import com.example.mobile_subproject_nhom05.module.Cart;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListCartActivity extends AppCompatActivity implements ICartLoadListener {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @BindView(R.id.recyclerViewCart)
    RecyclerView recyclerView;
    @BindView(R.id.mainLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.txtTotalPrice)
    TextView textView;
    @BindView(R.id.btnBack)
    ImageView btnBack;

    ICartLoadListener cartLoadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cart);

        init();
        loadCartFromFirebase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent.class)){
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent.class);
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(UpdateCartEvent event){
        loadCartFromFirebase();
    }

    private void loadCartFromFirebase() {
        List<Cart> cartList = new ArrayList<>();
        FirebaseDatabase.getInstance("https://mobile-subproject-nhom05-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Cart")
                .child(fAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot drinkSnapshot : snapshot.getChildren()){
                                Cart cart = drinkSnapshot.getValue(Cart.class);
                                cart.setKey(drinkSnapshot.getKey());
                                cartList.add(cart);
                            }
                            cartLoadListener.onCartLoadSuccess(cartList);
                        }
                        else{
                            cartLoadListener.onCartLoadFailed("Cart empty");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    private void init(){
        ButterKnife.bind(this);

        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListCartActivity.this,ListMainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onCartLoadSuccess(List<Cart> carts) {
        double sum = 0;
        for (Cart cart : carts){
            sum += cart.getTotalPrice();
        }
        textView.setText(new StringBuffer("$").append((long) sum));
        CartAdapter adapter =  new CartAdapter(this,carts);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(constraintLayout,message,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}