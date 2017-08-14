package preview.valteck.bortexapp.ui.browse_fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.ui.MainActivity;
import preview.valteck.bortexapp.utility.Constants;

/**
 * Created by SterlingRyan on 7/27/2017.
 */

public class BrowseFragment extends Fragment{

    private String[] mTitles;
    private int[] mImages = new int[]{
            R.drawable.acessories,
            R.drawable.t_shirt,
            R.drawable.hoodies,
            R.drawable.coat,
            R.drawable.shoes,
            R.drawable.suit,
            R.drawable.jeans,
            R.drawable.socks
    } ;

    public static Fragment newInstance() {
        BrowseFragment fragment = new BrowseFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GridView gridView = (GridView) inflater.inflate(R.layout.fragment_browse, container, false);
        gridView.setAdapter(new CategoryViewAdapter());
        mTitles = getResources().getStringArray(R.array.categories);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = null;
                switch (position){
                    case 0 :
                        category = Constants.CATEGORY_ACCESSORIES;
                        break;
                    case 1 :
                        category = Constants.CATEGORY_T_SHIRTS;
                        break;
                    case 2:
                        category = Constants.CATEGORY_HOODIES;
                        break;
                    case 3 :
                        category = Constants.CATEGORY_JACKETS_AND_COATS;
                        break;
                    case 4 :
                        category = Constants.CATEGORY_SHOES;
                        break;
                    case 5 :
                        category = Constants.CATEGORY_SUITS;
                        break;
                    case 6 :
                        category = Constants.CATEGORY_JEANS;
                        break;
                    case 7 :
                        category = Constants.CATEGORY_UNDERWEAR_AND_SOCKS;
                        break;
                }
                ((MainActivity) getActivity()).replaceFragment(FragmentName.FILTERED_CATEGORY, category);
            }
        });
        ViewCompat.setNestedScrollingEnabled(gridView,true);
        return gridView;
    }

    static class ViewHolderItem{
        TextView textView;
        ImageView imageView;
    }

    private class CategoryViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderItem viewHolder;

            if(convertView == null){
                // Inflate the layout
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.clothes_category_item, null);

                // Set up the View Holder
                viewHolder = new ViewHolderItem();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

                // store the view holder with the view
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolderItem) convertView.getTag();
            }

            // Assign values according to position
            viewHolder.textView.setText(mTitles[position]);
            Picasso.with(getContext()).load(mImages[position]).fit().into(viewHolder.imageView);

            return convertView;
        }

        private int dpToPx(int dp){
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }
    }

}
