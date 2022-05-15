package com.example.mobile_subproject_nhom05.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_subproject_nhom05.R;
import com.example.mobile_subproject_nhom05.activity.ListCartActivity;
import com.example.mobile_subproject_nhom05.event.UpdateCartEvent;
import com.example.mobile_subproject_nhom05.module.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    private ListCartActivity listCartActivity;
    private Context context;
    private List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.items_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context)
                .load(cartList.get(position).getImage())
                .into(holder.imageView);
        holder.textPrice.setText(new StringBuffer("$").append(cartList.get(position).getPrice()));
        holder.textName.setText(new StringBuffer().append(cartList.get(position).getName()));
        holder.textQuantity.setText(new StringBuffer().append(cartList.get(position).getQuantity()));


        holder.btnMinus.setOnClickListener(view -> minusCartItem(holder,cartList.get(position)));

        holder.btnPlus.setOnClickListener(view -> plusCartItem(holder,cartList.get(position)));

        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Delete item")
                    .setMessage("Bạn có muốn xóa sản phẩm này")
                    .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setNegativeButton("Đồng ý", (dialogInterface, i) -> {
                        Intent intent = new Intent(context,ListCartActivity.class);
                        context.startActivity(intent);
                        notifyItemChanged(position);
                        deleteFromFirebase(cartList.get(position));
                        dialogInterface.dismiss();
                    }).create();
            dialog.show();
        });
    }

    private void deleteFromFirebase(Cart cart) {
        FirebaseDatabase.getInstance("https://mobile-subproject-nhom05-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Cart")
                .child(fAuth.getCurrentUser().getUid())
                .child(cart.getKey())
                .removeValue()
                .addOnSuccessListener(unused -> EventBus.getDefault().postSticky(new UpdateCartEvent()));
    }

    private void plusCartItem(CartViewHolder holder, Cart cart) {
        cart.setQuantity(cart.getQuantity()+1);
        cart.setTotalPrice(cart.getQuantity()*Float.parseFloat(cart.getPrice()));

        holder.textQuantity.setText(new StringBuffer().append(cart.getQuantity()));
        updateFirebase(cart);

        Intent i = new Intent(context,ListCartActivity.class);
        context.startActivity(i);
    }

    private void minusCartItem(CartViewHolder holder, Cart cart) {

        if(cart.getQuantity() > 1){
            cart.setQuantity(cart.getQuantity()-1);
            cart.setTotalPrice(cart.getQuantity()*Float.parseFloat(cart.getPrice()));

            holder.textQuantity.setText(new StringBuffer().append(cart.getQuantity()));
            updateFirebase(cart);

            Intent i = new Intent(context,ListCartActivity.class);
            context.startActivity(i);
        }
    }

    private void updateFirebase(Cart cart) {
        FirebaseDatabase.getInstance("https://mobile-subproject-nhom05-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Cart")
                .child(fAuth.getCurrentUser().getUid())
                .child(cart.getKey())
                .setValue(cart)
                .addOnSuccessListener(unused -> EventBus.getDefault().postSticky(new UpdateCartEvent()));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.btnMinus)
        ImageView btnMinus;
        @BindView(R.id.btnPlus)
        ImageView btnPlus;
        @BindView(R.id.btnDelete)
        ImageView btnDelete;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.textName)
        TextView textName;
        @BindView(R.id.txtPrice)
        TextView textPrice;
        @BindView(R.id.txtQuantity)
        TextView textQuantity;

        private Unbinder unbinder;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
