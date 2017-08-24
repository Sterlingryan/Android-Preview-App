package preview.valteck.bortexapp.ui.browse_fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.model.Item;
import preview.valteck.bortexapp.ui.MainActivity;

/**
 * Created by SterlingRyan on 8/1/2017.
 */

public class FilteredCategoryFragment extends Fragment {

    private ArrayList<Item> mItemsList;
    private ArrayList<Item> mFavouriteItemsList;
    private Typeface mRalewayFontLight;
    private Typeface mRalewayFontSemiBold;

    public static Fragment newInstance() {
        return new FilteredCategoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mItemsList = ((MainActivity) getActivity()).mItemsList;
        this.mFavouriteItemsList = ((MainActivity) getActivity()).mFavouriteItemsList;
        this.mRalewayFontLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Light.ttf");
        this.mRalewayFontSemiBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-SemiBold.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new SubCategoryAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).startItemFragment(mItemsList.get(position));
            }
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mItemsList.clear();
    }

    static class ViewHolder{
        TextView itemPrice;
        TextView itemTitle;
        ImageView itemImage;
        ImageView favouriteImage;
    }

    class SubCategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItemsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.clothes_subcategory_item, null);

                // Retrieve the children views
                viewHolder = new ViewHolder();
                viewHolder.itemPrice = (TextView) convertView.findViewById(R.id.item_price_text_view);
                viewHolder.itemTitle = (TextView) convertView.findViewById(R.id.item_name_text_view);
                viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.item_image_view);
                viewHolder.favouriteImage = (ImageView) convertView.findViewById(R.id.favourite_icon);

                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Populate views with data
            viewHolder.itemPrice.setText("€" + mItemsList.get(position).getPrice());
            viewHolder.itemPrice.setTypeface(mRalewayFontSemiBold);
            viewHolder.itemTitle.setText(mItemsList.get(position).getName());
            viewHolder.itemTitle.setTypeface(mRalewayFontLight);
            Picasso.with(getContext()).load(mItemsList.get(position).getImageURL()).fit().into(viewHolder.itemImage);

            if(((MainActivity) getActivity()).mFavouriteItemsList.contains(mItemsList.get(position))){
                viewHolder.favouriteImage.setImageResource(R.drawable.ic_favorite_24dp);
                viewHolder.favouriteImage.setTag(R.drawable.ic_favorite_24dp);
            } else {
                viewHolder.favouriteImage.setTag(R.drawable.ic_favorite_border_black_24dp);
            }

            // set views functionality
            viewHolder.favouriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((Integer) v.getTag() == R.drawable.ic_favorite_border_black_24dp){
                        ((ImageView) v).setImageResource(R.drawable.ic_favorite_24dp);
                        v.setTag(R.drawable.ic_favorite_24dp);
                        ((MainActivity) getActivity()).mFavouriteItemsList.add(mItemsList.get(position));
                    } else {
                        ((ImageView) v).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        v.setTag(R.drawable.ic_favorite_border_black_24dp);
                        ((MainActivity) getActivity()).mFavouriteItemsList.remove(mItemsList.get(position));
                    }
                }
            });

            return convertView;
        }
    }

}
