package preview.valteck.bortexapp.ui.browse_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.ui.MainActivity;

/**
 * Created by SterlingRyan on 8/1/2017.
 */

public class FilteredCategoryFragment extends Fragment {

    private int position;
    private String[] itemTitles = {
            "Jacket 1",
            "Jacket 2",
            "Jacket 3",
            "Jacket 4",
            "Jacket 5",
            "Jacket 6",
            "Jacket 7",
            "Jacket 8",
            "Jacket 9"
    };

    public static Fragment newInstance(){
        return new FilteredCategoryFragment();
    }
    public void setPosition(int position){
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new SubCategoryAdapter(this.getContext(), itemTitles, null));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).replaceFragment(FragmentName.ITEM, position);
            }
        });
        return view;
    }
}
