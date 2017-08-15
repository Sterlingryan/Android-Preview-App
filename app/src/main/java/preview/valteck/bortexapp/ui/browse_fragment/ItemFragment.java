package preview.valteck.bortexapp.ui.browse_fragment;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;
import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.model.Item;
import preview.valteck.bortexapp.ui.MainActivity;
import preview.valteck.bortexapp.utility.Constants;

/**
 * Created by SterlingRyan on 8/2/2017.
 */

public class ItemFragment extends Fragment {

    private Item mItem;
    private MainActivity mainActivity;

    /**
     * Create a new ItemFragment object
     * and pass the item object to it.
     * @param item
     * @return
     */
    public static Fragment newInstance(Item item){
        Bundle args = new Bundle();
        args.putSerializable(Constants.FIREBASE_ITEM, item);
        ItemFragment itemFragment = new ItemFragment();
        itemFragment.setArguments(args);
        return itemFragment;
    }

    /**
     * Get the item object and save it to
     * the local variable mItem.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mItem = (Item) getArguments().getSerializable(Constants.FIREBASE_ITEM);
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Get children views
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        ImageView itemImage = (ImageView) view.findViewById(R.id.item_image_view);
        ImageView backImage = (ImageView) view.findViewById(R.id.back_arrow_image_view);
        final MaterialSpinner colorSpinner = (MaterialSpinner ) view.findViewById(R.id.spinner_color);
        final MaterialSpinner  sizeSpinner = (MaterialSpinner ) view.findViewById(R.id.spinner_size);
        Button addToCartButton = (Button) view.findViewById(R.id.add_to_cart_button);
        TextView titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        TextView priceTextView = (TextView) view.findViewById(R.id.item_price_text_view);

        // Load views with data
        Picasso.with(this.getContext()).load(mItem.getImageURL()).fit().into(itemImage);
        ArrayList<String> coloursList = new ArrayList<>(mItem.getColours().values());
        ArrayList<String> sizesList = new ArrayList<>(mItem.getSize().values());
        ArrayAdapter<String> adapterColor = new ArrayAdapter<>(this.getContext(),  R.layout.spinner_focused_item, coloursList);
        ArrayAdapter<String> adapterSize = new ArrayAdapter<>(this.getContext(), R.layout.spinner_focused_item, sizesList);
        adapterColor.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapterSize.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sizeSpinner.setAdapter(adapterSize);
        colorSpinner.setAdapter(adapterColor);
        titleTextView.setText(mItem.getName());
        priceTextView.setText(mItem.getPrice());

        // Set views functionality
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.mToolbar.setVisibility(View.VISIBLE);
                getFragmentManager().popBackStack();
            }
        });
        colorSpinner.setSelection(1);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sizeSpinner.getSelectedItemPosition() == 0 ){
                    mainActivity.showSnackBar(R.string.choose_size);
                }
                else if(colorSpinner.getSelectedItemPosition() == 0){
                    mainActivity.showSnackBar(R.string.choose_color);
                }
                else{
                    v.setBackgroundColor(getResources().getColor(R.color.colorText));
                    v.setEnabled(false);
                    // TODO add Add to Cart Functionality
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainActivity.mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        mainActivity.mToolbar.setVisibility(View.VISIBLE);
        super.onStop();
    }
}
