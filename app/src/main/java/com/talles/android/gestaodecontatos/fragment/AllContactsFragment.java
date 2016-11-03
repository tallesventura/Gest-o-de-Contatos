package com.talles.android.gestaodecontatos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talles.android.gestaodecontatos.Adapter.MyContactRecyclerViewAdapter;
import com.talles.android.gestaodecontatos.R;
import com.talles.android.gestaodecontatos.activity.MainActivity;
import com.talles.android.gestaodecontatos.dao.ContactDao;
import com.talles.android.gestaodecontatos.fragment.Support.OnContactListFragmentInteractionListener;
import com.talles.android.gestaodecontatos.fragment.dummy.DummyContactContent;
import com.talles.android.gestaodecontatos.model.Contact;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Iterator;
import java.util.List;


public class AllContactsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnContactListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AllContactsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AllContactsFragment newInstance(int columnCount) {
        AllContactsFragment fragment = new AllContactsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_contacts_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            MyContactRecyclerViewAdapter adapter = new MyContactRecyclerViewAdapter(DummyContactContent.ITEMS, mListener);
            recyclerView.setAdapter(adapter);
            initContactList();

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactListFragmentInteractionListener) {
            mListener = (OnContactListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        DummyContactContent.clearList();
        mListener = null;
    }

    private void initContactList(){
        ContactDao contactDao = MainActivity.contactDao;
        QueryBuilder builder = contactDao.queryBuilder();
        List contacts = builder.orderAsc(ContactDao.Properties.Name).list();

        Iterator it = contacts.iterator();
        while (it.hasNext()){
            Contact c = (Contact) it.next();
            String name = c.getName();
            long id = c.getId();
            float rating = c.getAffinity();
            DummyContactContent.DummyItem item = new DummyContactContent.DummyItem(id,name,rating);
            DummyContactContent.addItem(item);
        }
    }
}
