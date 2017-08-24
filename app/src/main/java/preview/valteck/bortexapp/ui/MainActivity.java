package preview.valteck.bortexapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.model.CartItem;
import preview.valteck.bortexapp.model.Item;
import preview.valteck.bortexapp.ui.browse_fragment.BrowseFragment;
import preview.valteck.bortexapp.ui.browse_fragment.FilteredCategoryFragment;
import preview.valteck.bortexapp.ui.browse_fragment.FragmentName;
import preview.valteck.bortexapp.ui.browse_fragment.ItemFragment;
import preview.valteck.bortexapp.ui.favourites_fragment.FavouritesFragment;
import preview.valteck.bortexapp.ui.home_fragment.HomeFragment;
import preview.valteck.bortexapp.ui.login_and_registration_activities.LoginActivity;
import preview.valteck.bortexapp.ui.shopping_cart_fragment.ShoppingCartFragment;
import preview.valteck.bortexapp.utility.Constants;

public class MainActivity extends AppCompatActivity {

    public Toolbar mToolbar;
    public BottomNavigationBar mBottomNavigationBar;
    public ArrayList<Item> mItemsList = new ArrayList<>();
    public ArrayList<Item> mFavouriteItemsList = new ArrayList<>();
    public ArrayList<CartItem> mCartList = new ArrayList<>();
    private Drawer mDrawer;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser = null;
    private Typeface railwayFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up views
        setUpBottomNavigationBar();
        setUpToolbar();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        new DrawerBuilder().withActivity(this).build();

    }

    /**
     * Set toolbar to visible
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mToolbar.getVisibility() == View.GONE){
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null){
            setUpSignedInDrawer();
        } else {
            setUpAnonymousDrawer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mUser != null){
            setUpSignedInDrawer();
        } else {
            setUpAnonymousDrawer();
        }
    }

    private void setUpBottomNavigationBar(){
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setAutoHideEnabled(true);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_24dp, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_browse_24dp, "Browse"))
                .addItem(new BottomNavigationItem(R.drawable.ic_shopping_cart_24dp, "Cart"))
                .addItem(new BottomNavigationItem(R.drawable.ic_favorite_24dp, "Favourites"))
                .setMode(BottomNavigationBar.MODE_FIXED_NO_TITLE)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor(R.color.colorAccent)
                .setInActiveColor(R.color.colorPrimaryDark)
                .setBarBackgroundColor(R.color.colorPrimary)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Fragment selectedFragment = null;
                switch (position){
                    case 0:
                        selectedFragment = HomeFragment.newInstance();
                        break;
                    case 1:
                        selectedFragment = BrowseFragment.newInstance();
                        break;
                    case 2:
                        selectedFragment = ShoppingCartFragment.newInstance();
                        break;
                    case 3:
                        selectedFragment = FavouritesFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {
                // Do nothing
            }

            @Override
            public void onTabReselected(int position) {
                // Do nothing
            }
        });

        // Manually Display the first fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();
    }

    /**
     *  Necessary initialization for toolbar setup
     */
    private void setUpToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView appTitle = (TextView) findViewById(R.id.appBarTitle);
        railwayFont = Typeface.createFromAsset(getAssets(), "fonts/Raleway-SemiBold.ttf");
        appTitle.setTypeface(railwayFont);
    }

    /**
     * Set up anonymous user drawer
     */
    private void setUpAnonymousDrawer(){
        mDrawer = null;
        // Create drawer object
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withRootView(R.id.drawer_frame_layout)
                .withActionBarDrawerToggleAnimated(true)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withSliderBackgroundColorRes(R.color.colorText)
                .addDrawerItems(
                        new SecondaryDrawerItem().withIdentifier(1).withName("Sign In").withTextColorRes(R.color.colorText).withSelectable(false),
                        new SecondaryDrawerItem().withIdentifier(2).withName("My Points").withTextColorRes(R.color.colorText).withBadge("0").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.colorAccent)).withSelectable(false),
                        new SecondaryDrawerItem().withIdentifier(3).withName("My Orders").withTextColorRes(R.color.colorText).withSelectable(false),
                        new SecondaryDrawerItem().withIdentifier(4).withName("My Details").withTextColorRes(R.color.colorText).withSelectable(false),
                        new SecondaryDrawerItem().withIdentifier(5).withName("About").withTextColorRes(R.color.colorText).withSelectable(false)
                )
                .withSliderBackgroundColorRes(R.color.colorPrimary)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent;
                        if (drawerItem != null){
                            if(drawerItem.getIdentifier() == 0){
                                mDrawer.closeDrawer();
                            }
                            else if(drawerItem.getIdentifier() == 1){
                                intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();
    }

    /**
     * Set up a signed in user drawer
     */
    private void setUpSignedInDrawer(){
        mDrawer = null;

        // Lets material drawer load images
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        // Create header
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(
                        new ProfileDrawerItem().withName(mUser.getDisplayName()).withEmail(mUser.getEmail()).withIcon(mUser.getPhotoUrl())
                )
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withRootView(R.id.drawer_frame_layout)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withSliderBackgroundColorRes(R.color.colorText)
                .withAccountHeader(header)
                .addDrawerItems(
                        new SecondaryDrawerItem().withIdentifier(2).withName("My Points").withTextColorRes(R.color.colorText).withBadge("2000").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.colorAccent)).withSelectable(false),
                        new SecondaryDrawerItem().withIdentifier(3).withName("My Orders").withTextColorRes(R.color.colorText).withSelectable(false),
                        new SecondaryDrawerItem().withIdentifier(4).withName("My Details").withTextColorRes(R.color.colorText).withSelectable(false),
                        new SecondaryDrawerItem().withIdentifier(5).withName("About").withTextColorRes(R.color.colorText).withSelectable(false),
                        new SecondaryDrawerItem().withIdentifier(6).withName("Sign Out").withTextColorRes(R.color.colorText).withSelectable(false)
                )
                .withSliderBackgroundColorRes(R.color.colorPrimary)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null){
                            if(drawerItem.getIdentifier() == 6){
                                mAuth.signOut();
                                mDrawer.closeDrawer();
                                setUpAnonymousDrawer();
                            }
                        }
                        return true;
                    }
                })
                .build();
    }

    /**
     * Starts FilteredCategoryFragment class
     * with Firebase objects
     */
    public void startFilteredCategoryFragment(String category){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CATEGORIES).child(category);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    Item item = child.getValue(Item.class);
                    mItemsList.add(item);
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, FilteredCategoryFragment.newInstance() );
                transaction.addToBackStack(FragmentName.FILTERED_CATEGORY.toString());
                transaction.commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Starts ItemFragment with Object
     * retrieved from ItemList
     */
    public void startItemFragment(Item item){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemFragment.newInstance(item));
        transaction.commit();
    }

    /**
     * Shows a message through a
     * snackbar
     */
    public void showSnackBar(int messageId){
        Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
    }
}
