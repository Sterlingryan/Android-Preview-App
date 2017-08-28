package preview.valteck.bortexapp.ui.browse_fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;
import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.model.CartItem;
import preview.valteck.bortexapp.model.Item;
import preview.valteck.bortexapp.ui.MainActivity;
import preview.valteck.bortexapp.utility.Constants;

/**
 * Created by SterlingRyan on 8/2/2017.
 */

public class ItemFragment extends Fragment {

    private Item mItem;
    private MainActivity mainActivity;
    private Typeface mTypeFaceTitle;
    private Typeface mTypeFacePrice;

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
        this.mainActivity = (MainActivity) getActivity();
        this.mTypeFaceTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");
        this.mTypeFacePrice = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Bold.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Get children views
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        ImageView itemImage = (ImageView) view.findViewById(R.id.item_image_view);
        ImageView backImage = (ImageView) view.findViewById(R.id.back_arrow_image_view);
        ImageView favouriteImage = (ImageView) view.findViewById(R.id.favourite_button);
        final MaterialSpinner colorSpinner = (MaterialSpinner ) view.findViewById(R.id.spinner_color);
        final MaterialSpinner  sizeSpinner = (MaterialSpinner ) view.findViewById(R.id.spinner_size);
        Button addToCartButton = (Button) view.findViewById(R.id.add_to_cart_button);
        TextView titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        TextView priceTextView = (TextView) view.findViewById(R.id.item_price_text_view);

        // Load views with data and design
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
        titleTextView.setTypeface(mTypeFaceTitle);
        priceTextView.setText("€" + mItem.getPrice());
        priceTextView.setTypeface(mTypeFacePrice);
        favouriteImage.setTag(R.drawable.ic_favorite_border_black_24dp);
        if(((MainActivity) getActivity()).mFavouriteItemsList.contains(mItem)){
            favouriteImage.setImageResource(R.drawable.ic_favorite_24dp);
            favouriteImage.setTag(R.drawable.ic_favorite_24dp);
        }
        addToCartButton.setTypeface(mTypeFacePrice);

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
                    mainActivity.showToast(R.string.choose_size);
                }
                else if(colorSpinner.getSelectedItemPosition() == 0){
                    mainActivity.showToast(R.string.choose_color);
                }
                else{
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    v.setEnabled(false);
                    CartItem cartItem = new CartItem(mItem.getItemId(),
                                                mItem.getName(),
                                                mItem.getImageURL(),
                                                colorSpinner.getSelectedItem().toString(),
                                                sizeSpinner.getSelectedItem().toString(),
                                                mItem.getDescription(),
                                                mItem.getPrice(),
                                                mItem.getItemType());
                    mainActivity.showToast(R.string.added_to_cart_success);
                    mainActivity.mCartList.add(cartItem);
                }
            }
        });

        favouriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer) v.getTag() == R.drawable.ic_favorite_border_black_24dp){
                    ((ImageView) v).setImageResource(R.drawable.ic_favorite_24dp);
                    v.setTag(R.drawable.ic_favorite_24dp);
                    ((MainActivity) getActivity()).mFavouriteItemsList.add(mItem);
                } else {
                    ((ImageView) v).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    v.setTag(R.drawable.ic_favorite_border_black_24dp);
                    ((MainActivity) getActivity()).mFavouriteItemsList.remove(mItem);
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
