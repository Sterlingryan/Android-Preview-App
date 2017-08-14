package preview.valteck.bortexapp.ui.browse_fragment;

import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.ui.MainActivity;
import preview.valteck.bortexapp.utility.Constants;

/**
 * Created by SterlingRyan on 8/1/2017.
 */

public class FilteredCategoryFragment extends Fragment {

    private HashMap<String, String> itemsHashMap;
    private String category;

    public static Fragment newInstance(String category) {
        FilteredCategoryFragment filteredCategoryFragment = new FilteredCategoryFragment();
        Bundle args = new Bundle();
        args.putString(Constants.FIREBASE_CATEGORIES, category);
        filteredCategoryFragment.setArguments(args);
        return filteredCategoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.category = savedInstanceState.getString(Constants.FIREBASE_CATEGORIES);
        retrieveCategoryItems();
        String hry;
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
                ((MainActivity) getActivity()).replaceFragment(FragmentName.ITEM, null);
            }
        });
        return view;
    }

    private void retrieveCategoryItems(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CATEGORIES).child(category);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsHashMap = (HashMap<String, String>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class SubCategoryAdapter extends BaseAdapter {

        private String[] mItemTitles;
        private String[] mImages;

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
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView;
            if(convertView == null){
                itemView = inflater.inflate(R.layout.clothes_subcategory_item, null);
                TextView itemPrice = (TextView) itemView.findViewById(R.id.item_price_text_view);
                TextView itemTitle = (TextView) itemView.findViewById(R.id.item_name_text_view);
                ImageView itemImage = (ImageView) itemView.findViewById(R.id.item_image_view);
                Picasso.with(getContext()).load(R.drawable.acessories_v1).fit().into(itemImage);
                ImageView favouriteImage = (ImageView) itemView.findViewById(R.id.favourite_icon);
            }
            else {
                itemView = convertView;
            }
            return itemView;
        }
    }

}
