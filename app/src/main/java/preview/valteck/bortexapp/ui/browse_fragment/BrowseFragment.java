package preview.valteck.bortexapp.ui.browse_fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.ui.MainActivity;

/**
 * Created by SterlingRyan on 7/27/2017.
 */

public class BrowseFragment extends Fragment{

    public static Fragment newInstance() {
        BrowseFragment fragment = new BrowseFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GridView gridView = (GridView) inflater.inflate(R.layout.fragment_browse, container, false);
        String[] categories = getResources().getStringArray(R.array.categories);
        gridView.setAdapter(new CategoryViewAdapter(this, categories, null));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).replaceFragment(FragmentName.FILTERED_CATEGORY, position);
            }
        });
        ViewCompat.setNestedScrollingEnabled(gridView,true);
        return gridView;
    }

}
