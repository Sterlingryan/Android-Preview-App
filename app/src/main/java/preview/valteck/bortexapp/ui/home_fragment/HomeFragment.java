package preview.valteck.bortexapp.ui.home_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import preview.valteck.bortexapp.R;

/**
 * Created by SterlingRyan on 7/27/2017.
 */

public class HomeFragment extends Fragment {

    private Integer[] saleImagesUrls = new Integer[]{
            R.drawable.sale_summer_collection,
            R.drawable.new_winter_collection,
            R.drawable.clearance_items_sale
    };

    public static Fragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.fragment_home, container, false);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast(R.string.preview_purpose_only);
            }
        });
        view.setAdapter(new HomeListAdapter());
        return view;
    }

    /**
     * Shows a message through a
     * toast
     */
    private void showToast(int messageId){
        if(messageId == R.string.preview_purpose_only){
            Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_SHORT).show();
        }
    }

    private class HomeListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return saleImagesUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return saleImagesUrls[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                CardView view = (CardView) inflater.inflate(R.layout.advertisment_item, null, false);
                ImageView imageView = (ImageView) view.findViewById(R.id.advertisment_image_view);
                Picasso.with(getContext()).load(saleImagesUrls[position]).fit().into(imageView);
                convertView = view;
            }
            return convertView;
        }
    }
}
