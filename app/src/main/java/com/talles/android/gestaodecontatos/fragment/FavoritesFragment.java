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

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnContactListFragmentInteractionListener mListener;
    private List<Contact> contacts;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoritesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavoritesFragment newInstance(int columnCount) {
        FavoritesFragment fragment = new FavoritesFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            contacts = initContactList();
            MyContactRecyclerViewAdapter adapter = new MyContactRecyclerViewAdapter(contacts, mListener);
            recyclerView.setAdapter(adapter);
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
        mListener = null;
    }

    private List<Contact> initContactList(){

        ContactDao contactDao = MainActivity.contactDao;
        QueryBuilder qb = contactDao.queryBuilder();
        return qb.where(ContactDao.Properties.Affinity.gt(Float.valueOf(3))).orderDesc(ContactDao.Properties.Affinity).list();
    }

}
