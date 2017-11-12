package com.reversecoder.ci.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;
import com.reversecoder.ci.R;
import com.reversecoder.ci.activity.HomeActivity;
import com.reversecoder.ci.adapter.ContractsRecyclerViewAdapter;
import com.reversecoder.ci.interfaces.RecyclerViewOnItemClickListener;
import com.reversecoder.ci.util.LoadingTextIcon;
import com.reversecoder.residemenu.ResideMenu;

import java.util.ArrayList;
import java.util.List;

import br.com.stickyindex.StickyIndex;
import cc.solart.wave.WaveSideBarView;

import static com.reversecoder.ci.util.AllConstants.KEY_CONTACT_DETAIL;
import static com.reversecoder.ci.util.AppUtils.getIndexList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ContactsFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;
    private WaveSideBarView mSideBarView;
    private Activity mActivity;
    private RecyclerView recyclerView;
    StickyIndex indexContainer;
    private RelativeLayout rlLoadingView, rlContacts;
    ContractsRecyclerViewAdapter contactAdapter;
    private ArrayList<Contact> mContacts = new ArrayList<Contact>();
    private static final String TAG = ContactsFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize contacts library
        Contacts.initialize(getActivity());

        // Retrieves a list of Contacts from the phone
//        long ini = new Date().getTime();
//        mContacts = AllConstants.currentContacts;
//        long aft = new Date().getTime();
//        Log.d(TAG, "spent time: ".concat(Long.toString(aft - ini)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_contacts, container, false);
        setUpViews();
        return parentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void setUpViews() {
        HomeActivity parentActivity = (HomeActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        ((HomeActivity) getActivity()).setTitle(getString(R.string.title_fragment_contacts));

        //declare views
        rlLoadingView = (RelativeLayout) parentView.findViewById(R.id.rl_loading);
        rlContacts = (RelativeLayout) parentView.findViewById(R.id.rl_contacts);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recyclerView);
        indexContainer = (StickyIndex) parentView.findViewById(R.id.sticky_index_container);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSideBarView = (WaveSideBarView) parentView.findViewById(R.id.wave_sidebar);

        if (mContacts.size() > 0) {
            rlContacts.setVisibility(View.VISIBLE);
            rlLoadingView.setVisibility(View.GONE);

            setObjectsAfterRetrievingContact(mContacts);
        } else {
            rlContacts.setVisibility(View.GONE);
            rlLoadingView.setVisibility(View.VISIBLE);

            retrieveContacts();
        }
    }

    private void setObjectsAfterRetrievingContact(final ArrayList<Contact> contacts) {
        contactAdapter = new ContractsRecyclerViewAdapter(contacts, mActivity);
        recyclerView.setAdapter(contactAdapter);
        implementsRecyclerViewOnItemClickListener();
        indexContainer.setDataSet(getIndexList(contacts));
        indexContainer.setOnScrollListener(recyclerView);
        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                for (int i = 0; i < contacts.size(); i++) {
                    if (String.valueOf(contacts.get(i).getDisplayName().charAt(0)).equals(letter)) {
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }

    private void implementsRecyclerViewOnItemClickListener() {
        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(mActivity,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Contact contact = ((ContractsRecyclerViewAdapter) recyclerView.getAdapter()).getContact(position);
//                        Toast.makeText(getActivity(), "Title: " + contact.getName(), Toast.LENGTH_SHORT).show();

                        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetFragment();
                        Bundle args = new Bundle();
                        args.putString(KEY_CONTACT_DETAIL, Contact.getResponseString(contact));
                        bottomSheetDialogFragment.setArguments(args);
                        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                    }
                }));
    }

    public class LoadPhoneContacts extends AsyncTask<String, String, ArrayList<Contact>> {

        private Context mContext;

        public LoadPhoneContacts(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<Contact> doInBackground(String... params) {

//            ArrayList<Contact> contacts = ContactsManager.getInstance(mContext).getContacts();

            Query q = Contacts.getQuery();
            q.hasPhoneNumber();
            List<Contact> contacts = q.find();

            return new ArrayList<Contact>(contacts);
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> result) {
            if (result != null) {
//                AllConstants.currentContacts = result;
                mContacts = result;
                LoadingTextIcon.finishLoading(true);
                setObjectsAfterRetrievingContact(mContacts);

                rlContacts.setVisibility(View.VISIBLE);
                rlLoadingView.setVisibility(View.GONE);
            }
        }
    }

    private void retrieveContacts() {
        LoadPhoneContacts loadContacts = new LoadPhoneContacts(getActivity());
        LoadingTextIcon loadingTextIcon = new LoadingTextIcon((TextView) parentView.findViewById(R.id.tv_loading_text), "Loading contacts,\nPlease wait... ");

        loadingTextIcon.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        loadContacts.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
