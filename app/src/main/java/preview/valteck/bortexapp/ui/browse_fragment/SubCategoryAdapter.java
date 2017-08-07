package preview.valteck.bortexapp.ui.browse_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import preview.valteck.bortexapp.R;

/**
 * Created by SterlingRyan on 8/1/2017.
 */

public class SubCategoryAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mItemTitles;
    private String[] mImages;

    public SubCategoryAdapter(Context mContext, String[] mItemTitles, String[] mImages) {
        this.mContext = mContext;
        this.mItemTitles = mItemTitles;
        this.mImages = mImages;
    }

    @Override
    public int getCount() {
        return mItemTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView;
        if(convertView == null){
            itemView = inflater.inflate(R.layout.clothes_subcategory_item, null);
            TextView itemPrice = (TextView) itemView.findViewById(R.id.item_price_text_view);
            TextView itemTitle = (TextView) itemView.findViewById(R.id.item_name_text_view);
            ImageView itemImage = (ImageView) itemView.findViewById(R.id.item_image_view);
            Picasso.with(mContext).load(R.drawable.acessories_v1).fit().into(itemImage);
            ImageView favouriteImage = (ImageView) itemView.findViewById(R.id.favourite_icon);
        }
        else {
            itemView = convertView;
        }
        return itemView;
    }
}
