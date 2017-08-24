package preview.valteck.bortexapp.ui.shopping_cart_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.model.CartItem;
import preview.valteck.bortexapp.ui.MainActivity;

/**
 * Created by SterlingRyan on 7/27/2017.
 */

public class ShoppingCartFragment extends Fragment {

    private ArrayList<CartItem> mCartItemList;
    private ShoppingCartListAdapter mShoppingCartListAdapter;
    private Button mPayNowButton;

    public static Fragment newInstance() {
        return new ShoppingCartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCartItemList = ((MainActivity) getActivity()).mCartList;
        mShoppingCartListAdapter = new ShoppingCartListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        ListView listView = (ListView) view.findViewById(R.id.shopping_cart_list_view);
        listView.setAdapter(mShoppingCartListAdapter);

        // Set up views functionality
        mPayNowButton = (Button) view.findViewById(R.id.pay_now_button);
        calculateTotalPrice();
        return view;
    }

    /**
     * Calculate the total price of items
     * and show it on the button
     */
    private void calculateTotalPrice(){
        if(!mCartItemList.isEmpty()){
            double totalPrice = 0.0;
            for (CartItem cartItem: mCartItemList) {
                totalPrice += Double.parseDouble(cartItem.getPrice());
            }
            mPayNowButton.setVisibility(View.VISIBLE);
            mPayNowButton.setText(getString(R.string.pay_now_button) + " (€" + totalPrice + ")");
        }
        else {
            mPayNowButton.setVisibility(View.GONE);
        }

    }

    private static class ViewHolder{
        ImageView itemImage;
        ImageView deleteButton;
        TextView itemTitle;
        TextView itemPrice;
        TextView itemDetails;
    }

    private class ShoppingCartListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mCartItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCartItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null){
                // Inflate layout
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.shopping_cart_item, parent, false);

                // Set up the View Holder
                viewHolder = new ViewHolder();
                viewHolder.itemTitle = (TextView) convertView.findViewById(R.id.title_text_view);
                viewHolder.itemPrice = (TextView) convertView.findViewById(R.id.item_price_text_view);
                viewHolder.itemDetails = (TextView) convertView.findViewById(R.id.item_details_text_view);
                viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.item_image_view);
                viewHolder.deleteButton = (ImageView) convertView.findViewById(R.id.delete_button);

                // Store the view holder with the view
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Assign data to the views
            final CartItem cartItem =  mCartItemList.get(position);
            viewHolder.itemTitle.setText(cartItem.getName());
            viewHolder.itemPrice.setText("€" + cartItem.getPrice());
            viewHolder.itemDetails.setText("Qty: 1/ " + cartItem.getColour() + "/ " + cartItem.getSize());
            Picasso.with(getContext()).load(cartItem.getImageURL()).fit().into(viewHolder.itemImage);

            // Assign functionality to views
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCartItemList.remove(cartItem);
                    ((MainActivity) getActivity()).mCartList.remove(cartItem);
                    mShoppingCartListAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            // Change the total price
            calculateTotalPrice();
        }
    }
}
