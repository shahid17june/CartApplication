package my.cart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import my.cart.R;
import my.cart.model.CartModel;


/**
 * Created by shahid Akhtar on 10-02-2017.
 */

public class CartsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<CartModel> cartModelList;

    public CartsAdapter(Context mContext, List<CartModel> cartModelList) {
        this.mContext = mContext;
        this.cartModelList = cartModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof  MyViewHolder){

            MyViewHolder viewHolder = (MyViewHolder) holder;

            CartModel model =cartModelList.get(position);
            String name = model.getName();

            if(!TextUtils.isEmpty(name)){
                viewHolder.name.setText(name);
            }
        }
    }

    @Override
    public int getItemCount() {
        return cartModelList!=null ? cartModelList.size() :0 ;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
        }
    }
}
