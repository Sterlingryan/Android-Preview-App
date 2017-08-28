package preview.valteck.bortexapp.ui.shopping_cart_fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
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
    private LinearLayout mLinearLayout;
    private Button mPayNowButton;
    private Typeface mTextTypeface;
    private Typeface mPriceTypeface;

    public static Fragment newInstance() {
        return new ShoppingCartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCartItemList = ((MainActivity) getActivity()).mCartList;
        this.mShoppingCartListAdapter = new ShoppingCartListAdapter();
        this.mTextTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");
        this.mPriceTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Bold.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        mLinearLayout = (LinearLayout) view.findViewById(R.id.no_content_view);

        ListView listView = (ListView) view.findViewById(R.id.shopping_cart_list_view);
        listView.setAdapter(mShoppingCartListAdapter);

        TextView noContentTextView = (TextView) view.findViewById(R.id.no_content_text);
        noContentTextView.setTypeface(mTextTypeface);

        // Set up views functionality
        mPayNowButton = (Button) view.findViewById(R.id.pay_now_button);
        mPayNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartItemList.clear();
                calculateTotalPrice();
                showToast(R.string.preview_purpose_only);
            }
        });
        calculateTotalPrice();
        return view;
    }

    /**
     * Calculate the total price of items
     * and show it on the button
     */
    private void calculateTotalPrice(){
        if(!mCartItemList.isEmpty()){
            float totalPrice = 0;
            for (CartItem cartItem: mCartItemList) {
                totalPrice += Double.parseDouble(cartItem.getPrice());
            }
            mPayNowButton.setVisibility(View.VISIBLE);
            mPayNowButton.setText(getString(R.string.pay_now_button) + " (€" + round(totalPrice,2)+ ")");
            mLinearLayout.setVisibility(View.GONE);
        }
        else {
            mPayNowButton.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Shows a message through a
     * toast
     */
    public void showToast(int messageId){
        if(messageId == R.string.preview_purpose_only){
            Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Rounding up a float number
     * to two decimal places
     */
    private static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
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

            // Assign design characteristics to the views
            viewHolder.itemTitle.setTypeface(mTextTypeface);
            viewHolder.itemDetails.setTypeface(mTextTypeface);
            viewHolder.itemPrice.setTypeface(mPriceTypeface);

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
