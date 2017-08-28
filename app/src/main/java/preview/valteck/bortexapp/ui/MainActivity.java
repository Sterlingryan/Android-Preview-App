package preview.valteck.bortexapp.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
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

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.model.CartItem;
import preview.valteck.bortexapp.model.Item;
import preview.valteck.bortexapp.ui.browse_fragment.BrowseFragment;
import preview.valteck.bortexapp.ui.browse_fragment.FilteredCategoryFragment;
import preview.valteck.bortexapp.ui.browse_fragment.FragmentName;
import preview.valteck.bortexapp.ui.browse_fragment.ItemFragment;
import preview.valteck.bortexapp.ui.drawer_utility.CustomTypefaceSpan;
import preview.valteck.bortexapp.ui.favourites_fragment.FavouritesFragment;
import preview.valteck.bortexapp.ui.home_fragment.HomeFragment;
import preview.valteck.bortexapp.ui.login_and_registration_activities.LoginActivity;
import preview.valteck.bortexapp.ui.shopping_cart_fragment.ShoppingCartFragment;
import preview.valteck.bortexapp.utility.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar mToolbar;
    public BottomNavigationBar mBottomNavigationBar;
    public ArrayList<Item> mItemsList = new ArrayList<>();
    public ArrayList<Item> mFavouriteItemsList = new ArrayList<>();
    public ArrayList<CartItem> mCartList = new ArrayList<>();
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser = null;
    private Typeface railwayFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        // Set up views
        setUpBottomNavigationBar();
        setUpToolbar();
        setUpDrawer();

        mAuth = FirebaseAuth.getInstance();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_sign_in:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_sign_out:
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                mDrawer.closeDrawers();
                setUpAnonymousDrawer();
                return false;
            default:
                mDrawer.closeDrawers();
                showToast(R.string.preview_purpose_only);
                break;
        }
        return true;
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
     * Sets up the drawer
     */
    private void setUpDrawer(){
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Set up signed in drawer and load
     * user information
     */
    private void setUpSignedInDrawer(){
        View headerView = mNavigationView.getHeaderView(0);
        CircleImageView profileImageView = (CircleImageView) headerView.findViewById(R.id.profile_image_view);
        Picasso.with(this).load(mUser.getPhotoUrl()).into(profileImageView);
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.signed_in_drawer);
        changeMenuTypeFace(mNavigationView.getMenu());
    }

    /**
     * Set up anonymous drawer
     */
    private void setUpAnonymousDrawer(){
        View headerView = mNavigationView.getHeaderView(0);
        CircleImageView profileImageView = (CircleImageView) headerView.findViewById(R.id.profile_image_view);
        profileImageView.setImageResource(R.drawable.ic_account_circle_black_48dp);
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.anonymous_drawer);
        changeMenuTypeFace(mNavigationView.getMenu());
    }

    /**
     * Change Typeface of menu
     */
    private void changeMenuTypeFace(Menu menu){
        for (int i = 0;i < menu.size();i++) {
            MenuItem mi = menu.getItem(i);

            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
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
                mItemsList.clear();
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

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    /**
     * Starts ItemFragment with Object
     * retrieved from ItemList
     */
    public void startItemFragment(Item item){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemFragment.newInstance(item));
        transaction.addToBackStack(FragmentName.ITEM.toString());
        transaction.commit();
    }

    /**
     * Shows a message through a
     * toast
     */
    public void showToast(int messageId){
        if(messageId == R.string.preview_purpose_only){
            Toast.makeText(this, getString(messageId), Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
        }
    }
}
