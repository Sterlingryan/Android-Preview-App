package preview.valteck.bortexapp.ui.browse_fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import preview.valteck.bortexapp.R;

/**
 * Created by SterlingRyan on 7/29/2017.
 */

public class CategoryViewAdapter extends BaseAdapter {

    private Fragment mFragment;
    private String[] mTitles;
    private Integer[] mImages;

    public CategoryViewAdapter(Fragment mFragment, String[] mTitles, Integer[] mImages) {
        this.mFragment = mFragment;
        this.mTitles = mTitles;
        this.mImages = mImages;
    }

    @Override
    public int getCount() {
        return mTitles.length;
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
        LayoutInflater inflater = (LayoutInflater) mFragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View categoryView;
        if(convertView == null){
            categoryView = inflater.inflate(R.layout.clothes_category_item, null);
            TextView textView = (TextView) categoryView.findViewById(R.id.textView);
            textView.setText(mTitles[position]);
            ImageView imageView = (ImageView) categoryView.findViewById(R.id.imageView);
            Picasso.with(mFragment.getContext()).load(R.drawable.acessories_v1).fit().into(imageView);
        }
        else {
            categoryView = convertView;
        }

        return categoryView;
    }

    private int dpToPx(int dp){
        DisplayMetrics displayMetrics = mFragment.getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
