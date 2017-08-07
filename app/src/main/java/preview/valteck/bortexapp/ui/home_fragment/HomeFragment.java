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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.squareup.picasso.Picasso;

import preview.valteck.bortexapp.R;

/**
 * Created by SterlingRyan on 7/27/2017.
 */

public class HomeFragment extends Fragment {

    private String[] imageUrls;

    public static Fragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.fragment_home, container, false);
        view.setAdapter(new HomeListAdapter());

        return view;
    }

    private class HomeListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return imageUrls[position];
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
                Picasso.with(getContext()).load(R.drawable.acessories_v1).fit().into(imageView);
                convertView = view;
            }
            return convertView;
        }
    }
}
