package com.example.mobile_subproject_nhom05.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_subproject_nhom05.decoration.SpaceItemDecoration;
import com.example.mobile_subproject_nhom05.R;
import com.example.mobile_subproject_nhom05.adapter.MotorAdapter;
import com.example.mobile_subproject_nhom05.event.UpdateCartEvent;
import com.example.mobile_subproject_nhom05.listener.ICartLoadListener;
import com.example.mobile_subproject_nhom05.listener.IMotorLoadListener;
import com.example.mobile_subproject_nhom05.module.Cart;
import com.example.mobile_subproject_nhom05.module.Motor;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMainActivity extends AppCompatActivity implements IMotorLoadListener, ICartLoadListener {

    @BindView(R.id.recyclerViewMotor)
    RecyclerView recyclerView;
    @BindView(R.id.mainLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;

    List<Motor> motorList;
    List<Cart> cartList;
    IMotorLoadListener motorLoadListener;
    ICartLoadListener cartLoadListener;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        init();
        loadMotorFromFirebase();
    }

    private void init(){
        ButterKnife.bind(this);
        motorLoadListener = this;
        cartLoadListener = this;

        motorList = new ArrayList<Motor>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration());

//        Log.d("Size",getAllMotorFromLocal().size()+"");
//        if(getAllMotorFromLocal().size() <= 0) {
//            readDataFromFirebase();
//            for(Motor motor : motorList){
//                Log.d("SOS",motor.getName());
//            }
//            addMotorToLocalUsingRoom(motorList);
//        }
//        motorList = getAllMotorFromLocal();
//        for(Motor motor : motorList){
//            Log.d("SOS",motor.getName());
//        }
//        MotorAdapter adapter = new MotorAdapter(this,motorList,cartLoadListener);
//        recyclerView.setAdapter(adapter);

//        addDataToFireStore();

        btnCart.setOnClickListener(view -> {
            Intent i = new Intent(ListMainActivity.this,ListCartActivity.class);
            startActivity(i);
        });
    }

//    public void addMotorToLocalUsingRoom(List<Motor> motors) {
//        for (Motor motor: motors) {
//            MotorDB.getInstance(this).motorDao().insertMotor(motor);
//        }
//    }

//    public List<Motor> getAllMotorFromLocal () {
//        motorList = MotorDB.getInstance(this).motorDao().getAll();
//        return motorList;
//    }

//    public void addDataToFireStore() {
//        for (Motor motor: motorList) {
//
//            String pathOject = String.valueOf(motor.getKey());
//
//            FirebaseDatabase.getInstance("https://mobile-subproject-nhom05-default-rtdb.asia-southeast1.firebasedatabase.app")
//                    .getReference("Motor")
//                    .child(pathOject)
//                    .setValue(motor)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(ListMainActivity.this, "Add success", Toast.LENGTH_SHORT).show();
//                            motorLoadListener.onMotorLoadSuccess(motorList);
//                        }
//                    });
//        }
//    }

    private void loadMotorFromFirebase() {
        FirebaseDatabase.getInstance("https://mobile-subproject-nhom05-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Motor")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot drinkSnapshot : snapshot.getChildren()){
                                Motor motor = drinkSnapshot.getValue(Motor.class);
                                motor.setKey(drinkSnapshot.getKey());
                                motorList.add(motor);
                            }
//                            for(Motor motor : motorList){
//                                Log.d("SOS",motor.getName());
//                            }
                            motorLoadListener.onMotorLoadSuccess(motorList);
                        }
                        else{
                            motorLoadListener.onMotorLoadFailed("Can't find Motor");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        motorLoadListener.onMotorLoadFailed(error.getMessage());
                    }
                });
    }

//    private void readDataFromFirebase(){
//        FirebaseDatabase.getInstance("https://mobile-subproject-nhom05-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference("Motor")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//                            for (DataSnapshot drinkSnapshot : snapshot.getChildren()){
//                                Motor motor = drinkSnapshot.getValue(Motor.class);
//                                motor.setKey(drinkSnapshot.getKey());
//                                motorList.add(motor);
//                            }
//                            motorLoadListener.onMotorLoadSuccess(motorList);
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.d("ERROR",error.getMessage());
//                    }
//                });
//    }

    @Override
    public void onMotorLoadSuccess(List<Motor> motors) {
        MotorAdapter adapter = new MotorAdapter(this,motors,cartLoadListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMotorLoadFailed(String message) {
        Snackbar.make(constraintLayout,message,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCartLoadSuccess(List<Cart> carts) {
        int cartSum = 0;
        for(Cart cart : carts){
            cartSum += cart.getQuantity();
        }

        badge.setNumber(cartSum );
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(constraintLayout,message,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        cartList = new ArrayList<>();
        FirebaseDatabase
                .getInstance("https://mobile-subproject-nhom05-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot cartSnapshot : snapshot.getChildren()){
                            Cart cart = cartSnapshot.getValue(Cart.class);
                            cart.setKey(cartSnapshot.getKey());
                            cartList.add(cart);
                        }
                        cartLoadListener.onCartLoadSuccess(cartList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }
}