package com.example.poowadon.liveat500px.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.poowadon.liveat500px.R;
import com.example.poowadon.liveat500px.activity.MoreInfoActivity;
import com.example.poowadon.liveat500px.adapter.PhotoListAdapter;
import com.example.poowadon.liveat500px.daatatype.MutableInteger;
import com.example.poowadon.liveat500px.dao.PhotoItemCollectionDao;
import com.example.poowadon.liveat500px.dao.PhotoItemDao;
import com.example.poowadon.liveat500px.manager.PhotoListManager;
import com.example.poowadon.liveat500px.manager.http.HttpManager;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class MainFragment extends Fragment {

    //Variables

    public interface FragmentListener{
        void onPhotoItemClicked(PhotoItemDao dao);
    }

    ListView listView;
    Button btnNewPhotos;

    PhotoListAdapter photoListAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    PhotoListManager photoListManager;
    MutableInteger lastPositionInteger;

    /*********
     * Function
     ********/

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState); //Restore Instatnce state

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    /*สำหรับประกาศตัวแปรของ Fragment**/
    private void init(Bundle savedInstanceState) {
        photoListManager = new PhotoListManager();
        lastPositionInteger = new MutableInteger(-1);

        //TODO: make file directory
       /* *//*ไว้สร้างไฟล์ที่จะอยู๋ไปตลอดจนกว่าเราจะลบเอง*//*
       // File dir = getActivity().getDir("Hello", Context.MODE_PRIVATE); // or getFileDir();

        *//*สร้างไฟล์ catch**//*
        File dir = getActivity().getCacheDir();
        Log.d("Storage", String.valueOf(dir));
        File file = new File(dir, "testfile.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("hello".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /**ให้ andorid จัดการเก็บข้อมูลให้เอง*/
        /**วิธีการเขียนข้อมูล*/
       /*
        SharedPreferences prefs = getContext().getSharedPreferences("dummy",
        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //Add/Edit/Delete
        editor.putString("Hello", "World");
        editor.apply();*/

        /**วิธีการอ่านข้อมูล*/
        SharedPreferences prefs = getContext().getSharedPreferences("dummy",
                Context.MODE_PRIVATE);
        String value = prefs.getString("Hello", null);


    }

    /*สำหรับประกาศตัวแปรของ View**/
    private void initInstances(View rootView, Bundle savedInstanceState) {
        btnNewPhotos = (Button) rootView.findViewById(R.id.btnNewPhotos);
        btnNewPhotos.setOnClickListener(buttonClickListener);

        // Init 'View' instance(s) with rootView.findViewById here
        listView = (ListView) rootView.findViewById(R.id.listView);
        photoListAdapter = new PhotoListAdapter(lastPositionInteger);
        photoListAdapter.setDao(photoListManager.getDao());
        listView.setAdapter(photoListAdapter);
        listView.setOnItemClickListener(listViewItemClickListener);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(pullToRefreshListener);
        listView.setOnScrollListener(listViewScrollListener);

        if (savedInstanceState == null)
            refreshData();
    }

    private void reloadData() {
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD));
    }

    private void refreshData() {
        if (photoListManager.getCount() == 0)
            reloadData();
        else
            reloadDataNewer();
    }


    private void reloadDataNewer() {
        int maxId = photoListManager.getMaximamId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoList();
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD_NEWER));
    }

    boolean isLoadingMore = false;

    private void loadMoreData() {
        if (isLoadingMore)
            return;
        isLoadingMore = true;
        int minId = photoListManager.getMinimamId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListBeforeId(minId);
        call.enqueue(
                new PhotoListLoadCallback(PhotoListLoadCallback.MODE_LOAD_MORE)
        );
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        outState.putBundle("photoListManager"
                , photoListManager.onSaveInstanceState());

        outState.putBundle("lastPositionInteger",
                lastPositionInteger.onSaveInstanceState());
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore instance state here
        photoListManager.onRestoreInstanceState(
                savedInstanceState.getBundle("photoListManager"));
        lastPositionInteger.onRestoreInstanceState(
                savedInstanceState.getBundle("lastPositionInteger")
        );

    }


    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showButtonNewPhotos() {
        btnNewPhotos.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_in
        );
        btnNewPhotos.startAnimation(anim);
    }

    private void hideButtonNewPhotos() {
        btnNewPhotos.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_out
        );
        btnNewPhotos.startAnimation(anim);
    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext()
                , text
                , Toast.LENGTH_SHORT).show();
    }

    /*********
     * Listener ZOne
     *********/

    final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnNewPhotos) {
                listView.smoothScrollToPosition(0);
                hideButtonNewPhotos();
            }
        }
    };

    final SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };

    final AbsListView.OnScrollListener listViewScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (absListView == listView) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0); /*เพื่อ handle ให้ refresh เฉพาะตอนที่ อยู๋บนสุดเท่านั้น**/
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    if (photoListManager.getCount() > 0) {
                        //Load More
                        loadMoreData();
                    }
                }
            }
        }
    };

    final AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if(position < photoListManager.getCount()) {
                PhotoItemDao dao = photoListManager.getDao().getData().get(position);
                FragmentListener listener = (FragmentListener) getActivity();
                listener.onPhotoItemClicked(dao);
            }

            /*Intent intent = new Intent(getContext(),
                    MoreInfoActivity.class);
            startActivity(intent);*/

        }
    };
    /*********
     * Inner Class
     ********/

    class PhotoListLoadCallback implements Callback<PhotoItemCollectionDao> {

        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;
        public static final int MODE_LOAD_MORE = 3;

        int mode;

        public PhotoListLoadCallback(int mode) {
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {
            swipeRefreshLayout.setRefreshing(false);
            if (response.isSuccessful()) {
                PhotoItemCollectionDao dao = response.body();

                int firstVisiblePosition = listView.getFirstVisiblePosition();
                View c = listView.getChildAt(0);
                int top = c == null ? 0 : c.getTop();

                if (mode == MODE_RELOAD_NEWER) {
                    photoListManager.insertDaoAtTopPosition(dao);
                } else if (mode == MODE_LOAD_MORE) {
                    photoListManager.appendtDaoAtBottomPosition(dao);
                    isLoadingMore = false;
                } else {
                    photoListManager.setDao(dao);
                }
                clearLoadingMoreFlagIfCapable(mode);
                photoListAdapter.setDao(photoListManager.getDao());
                photoListAdapter.notifyDataSetChanged(); ///tell adapter that we have new information to get data

                if (mode == MODE_RELOAD_NEWER) {
                    //MainTain Scroll Position
                    int additionNalSize = (dao != null && dao.getData() != null) ? dao.getData().size() : 0;
                    photoListAdapter.increaseLastPosition(additionNalSize);
                    listView.setSelectionFromTop(firstVisiblePosition + additionNalSize,
                            top);

                    if (additionNalSize > 0)
                        showButtonNewPhotos();
                } else {

                }

                showToast("Load Completed");
            } else {
                //Handle
                clearLoadingMoreFlagIfCapable(mode);
                try {
                    showToast(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {
            //Handle
            clearLoadingMoreFlagIfCapable(mode);

            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(Contextor.getInstance().getContext(), t.toString(), Toast.LENGTH_SHORT).show();
        }

        private void clearLoadingMoreFlagIfCapable(int mode) {
            if (mode == MODE_LOAD_MORE)
                isLoadingMore = false;
        }

    }
}
