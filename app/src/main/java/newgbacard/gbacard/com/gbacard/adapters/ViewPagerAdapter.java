package newgbacard.gbacard.com.gbacard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import newgbacard.gbacard.com.gbacard.fragments.AddressBookFragment;
import newgbacard.gbacard.com.gbacard.fragments.AddressBookFragment2;
import newgbacard.gbacard.com.gbacard.fragments.HomeFragment;

/**
 * Created by Williamz on 13-Jul-16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                AddressBookFragment2 addressBookFragment = new AddressBookFragment2();
                return addressBookFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
