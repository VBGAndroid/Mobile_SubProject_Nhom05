package com.example.mobile_subproject_nhom05.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_subproject_nhom05.R;
import com.example.mobile_subproject_nhom05.event.UpdateCartEvent;
import com.example.mobile_subproject_nhom05.listener.ICartLoadListener;
import com.example.mobile_subproject_nhom05.listener.IRecylerViewClickListener;
import com.example.mobile_subproject_nhom05.module.Cart;
import com.example.mobile_subproject_nhom05.module.Motor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MotorAdapter extends RecyclerView.Adapter<MotorAdapter.MotorViewHolder> {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    private Context context;
    private List<Motor> motorList;
    private ICartLoadListener cartLoadListener;

    public MotorAdapter(Context context, List<Motor> motorList, ICartLoadListener cartLoadListener) {
        this.context = context;
        this.motorList = motorList;
        this.cartLoadListener = cartLoadListener;
    }

    @NonNull
    @Override
    public MotorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MotorViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MotorViewHolder holder, int position) {
        Glide.with(context)
                .load(motorList.get(position).getImage())
                .into(holder.imageView);
        holder.textPrice.setText(new StringBuffer("$").append(motorList.get(position).getPrice()));
        holder.txtName.setText(new StringBuffer().append(motorList.get(position).getName()));

        holder.setListener((view, adapterPosition) -> {
            Log.d("Add Cart", motorList.get(adapterPosition).getName());
            addToCart(motorList.get(adapterPosition));
        });
    }

    private void addToCart(Motor motor) {

        DatabaseReference userCart = FirebaseDatabase
                .getInstance("https://mobile-subproject-nhom05-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Cart")
                .child(fAuth.getCurrentUser().getUid());

        userCart.child(motor.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Cart cart = snapshot.getValue(Cart.class);
                            cart.setQuantity(cart.getQuantity()+1);
                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("quantity",cart.getQuantity());
                            updateData.put("totalPrice",cart.getQuantity()*Float.parseFloat(cart.getPrice()));

                            userCart.child(motor.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(aVoid ->{
                                        cartLoadListener.onCartLoadFailed("Add to cart success");
                                    })
                                    .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));
                        }
                        else {
                            Cart cart = new Cart();
                            cart.setName(motor.getName());
                            cart.setImage(motor.getImage());
                            cart.setKey(motor.getKey());
                            cart.setQuantity(1);
                            cart.setPrice(motor.getPrice());
                            cart.setTotalPrice(Float.parseFloat(motor.getPrice()));

                            userCart.child(motor.getKey())
                                    .setValue(cart)
                                    .addOnSuccessListener(aVoid ->{
                                        cartLoadListener.onCartLoadFailed("Add to cart sucess");
                                    }).addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));
                        }
                        EventBus.getDefault().postSticky(new UpdateCartEvent());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return motorList.size();
    }

    public class MotorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivListItem   )
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView textPrice;

        IRecylerViewClickListener listener;

        public void setListener(IRecylerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;
        public MotorViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onMotorLoadSuccess(view,getAdapterPosition());
        }
    }
}
