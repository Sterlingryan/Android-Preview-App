package preview.valteck.bortexapp.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import preview.valteck.bortexapp.R;
import preview.valteck.bortexapp.ui.browse_fragment.BrowseFragment;
import preview.valteck.bortexapp.ui.browse_fragment.FilteredCategoryFragment;
import preview.valteck.bortexapp.ui.browse_fragment.FragmentName;
import preview.valteck.bortexapp.ui.browse_fragment.ItemFragment;
import preview.valteck.bortexapp.ui.home_fragment.HomeFragment;
import preview.valteck.bortexapp.ui.shopping_cart_fragment.ShoppingCartFragment;

public class MainActivity extends AppCompatActivity {

    public Toolbar mToolbar;
    public BottomNavigationBar mBottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        setUpBottomNavigationBar();
        setUpToolbar();
    }

    private void setUpBottomNavigationBar(){
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setAutoHideEnabled(true);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_24dp, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_browse_24dp, "Browse"))
                .addItem(new BottomNavigationItem(R.drawable.ic_shopping_cart_24dp, "Cart"))
                .setMode(BottomNavigationBar.MODE_FIXED_NO_TITLE)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor(R.color.colorAccent)
                .setInActiveColor(R.color.colorText)
                .setBarBackgroundColor(R.color.colorPrimaryDark)
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
                        selectedFragment = ProfileFragment.newInstance();
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
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorText));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        if(drawer != null){
            drawer.addDrawerListener(toggle);
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorText));
        }
        toggle.syncState();
    }

    /**
     * Replaces the fragment in the activity, this
     * method is accessed from within a fragment.
     */
    public void replaceFragment(FragmentName fragment, int position){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragment){
            case FILTERED_CATEGORY:
                FilteredCategoryFragment filteredCategoryFragment = new FilteredCategoryFragment();
                filteredCategoryFragment.setPosition(position);
                transaction.replace(R.id.frame_layout,filteredCategoryFragment );
                transaction.addToBackStack(FragmentName.FILTERED_CATEGORY.toString());
                break;
            case ITEM:
                ItemFragment itemFragment = new ItemFragment();
                transaction.replace(R.id.frame_layout,itemFragment );
                transaction.addToBackStack(FragmentName.ITEM.toString());
                break;
        }
        transaction.commit();
    }
}
