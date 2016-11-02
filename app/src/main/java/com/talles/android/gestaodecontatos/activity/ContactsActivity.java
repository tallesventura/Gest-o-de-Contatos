package com.talles.android.gestaodecontatos.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.talles.android.gestaodecontatos.Adapter.ContactsPagerAdapter;
import com.talles.android.gestaodecontatos.R;
import com.talles.android.gestaodecontatos.fragment.Support.OnContactListFragmentInteractionListener;
import com.talles.android.gestaodecontatos.fragment.dummy.DummyContactContent;


public class ContactsActivity extends AppCompatActivity implements OnContactListFragmentInteractionListener{

    ContactsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ViewPager vp = (ViewPager) findViewById(R.id.contacts_view_pager);
        pagerAdapter = new ContactsPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pagerAdapter);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

            }


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //vp.setCurrentItem(0);
    }

    @Override
    public void onListFragmentInteraction(DummyContactContent.DummyItem item) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_search) {
            Intent i  = new Intent(this,SearchContactActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
