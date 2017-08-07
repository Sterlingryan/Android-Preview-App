package preview.valteck.bortexapp.ui.browse_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import fr.ganfra.materialspinner.MaterialSpinner;
import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.ui.MainActivity;

/**
 * Created by SterlingRyan on 8/2/2017.
 */

public class ItemFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_item, container, false);
        ImageView itemImage = (ImageView) view.findViewById(R.id.item_image_view);
        Picasso.with(this.getContext()).load(R.drawable.acessories_v1).fit().into(itemImage);

        String[] colours = new String[]{
                "Black",
                "White",
                "Blue",
                "Yellow"
        };
        String[] sizes = new String[]{
                "Small",
                "Medium",
                "Large",
                "X-Large"
        };

        MaterialSpinner colorSpinner = (MaterialSpinner ) view.findViewById(R.id.spinner_color);
        MaterialSpinner  sizeSpinner = (MaterialSpinner ) view.findViewById(R.id.spinner_size);
        ArrayAdapter<String> adapterColor = new ArrayAdapter<>(this.getContext(),  R.layout.spinner_focused_item, colours);
        ArrayAdapter<String> adapterSize = new ArrayAdapter<>(this.getContext(), R.layout.spinner_focused_item, sizes);
        adapterColor.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapterSize.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sizeSpinner.setAdapter(adapterSize);
        colorSpinner.setAdapter(adapterColor);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        ((MainActivity) getActivity()).mToolbar.setVisibility(View.VISIBLE);
        super.onStop();
    }
}
