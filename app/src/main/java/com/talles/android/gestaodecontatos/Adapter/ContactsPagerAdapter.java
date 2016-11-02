package com.talles.android.gestaodecontatos.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.talles.android.gestaodecontatos.fragment.AllContactsFragment;
import com.talles.android.gestaodecontatos.fragment.FavoritesFragment;

/**
 * Created by talles on 10/9/16.
 */

public class ContactsPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITENS = 2;

    public ContactsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return FavoritesFragment.newInstance(1);
            case 1:
                return AllContactsFragment.newInstance(1);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITENS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Favoritos";
            case 1:
                return "Todos os contatos";
            default:
                return null;
        }
    }
}
