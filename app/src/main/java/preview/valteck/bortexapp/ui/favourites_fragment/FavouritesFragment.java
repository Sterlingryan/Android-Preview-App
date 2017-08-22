package preview.valteck.bortexapp.ui.favourites_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.model.CartItem;
import preview.valteck.bortexapp.model.Item;
import preview.valteck.bortexapp.ui.MainActivity;
import preview.valteck.bortexapp.ui.shopping_cart_fragment.ShoppingCartFragment;

/**
 * Created by SterlingRyan on 7/27/2017.
 */

public class FavouritesFragment extends Fragment{

    private ArrayList<Item> mFavouritesItemList;

    public static Fragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavouritesItemList = ((MainActivity) getActivity()).mFavouriteItemsList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListView favouritesListView = (ListView) inflater.inflate(R.layout.fragment_favourites, container, false);

        favouritesListView.setAdapter(new FavouritesListAdapter());
        return favouritesListView;

    }

    private static class ViewHolder{
        ImageView itemImage;
        ImageView deleteButton;
        TextView itemTitle;
        TextView itemPrice;
        TextView itemDetails;
    }

    private class FavouritesListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFavouritesItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFavouritesItemList.get(position);
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
            final Item favouriteItem =  mFavouritesItemList.get(position);
            viewHolder.itemTitle.setText(favouriteItem.getName());
            viewHolder.itemPrice.setText("€" + favouriteItem.getPrice());
            viewHolder.itemDetails.setText("");
            Picasso.with(getContext()).load(favouriteItem.getImageURL()).fit().into(viewHolder.itemImage);

            // Assign functionality to views
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFavouritesItemList.remove(favouriteItem);
                    ((MainActivity) getActivity()).mFavouriteItemsList.remove(favouriteItem);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
}
