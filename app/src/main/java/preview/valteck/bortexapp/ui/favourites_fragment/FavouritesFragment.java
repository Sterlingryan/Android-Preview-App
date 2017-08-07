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

import com.squareup.picasso.Picasso;

import preview.valteck.bortexapp.R;

/**
 * Created by SterlingRyan on 7/27/2017.
 */

public class FavouritesFragment extends Fragment{

    public static Fragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListView favouritesListView = (ListView) inflater.inflate(R.layout.fragment_favourites, container, false);
        favouritesListView.setAdapter(new FavouritesListAdapter());
        return favouritesListView;

    }

    private class FavouritesListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 20;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View shoppingCartItemView;
            if(convertView == null){
                shoppingCartItemView = inflater.inflate(R.layout.shopping_cart_item, parent, false);
                ImageView imageView = (ImageView) shoppingCartItemView.findViewById(R.id.item_image_view);
                Picasso.with(getContext()).load(R.drawable.acessories_v1).fit().into(imageView);
            }
            else {
                shoppingCartItemView = convertView;
            }
            return shoppingCartItemView;
        }
    }
}
