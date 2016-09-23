package com.example.poowadon.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.poowadon.liveat500px.R;
import com.example.poowadon.liveat500px.daatatype.MutableInteger;
import com.example.poowadon.liveat500px.dao.PhotoItemCollectionDao;
import com.example.poowadon.liveat500px.dao.PhotoItemDao;
import com.example.poowadon.liveat500px.manager.PhotoListManager;
import com.example.poowadon.liveat500px.view.PhotoListItem;

/**
 * Created by poowadon on 07/08/2559.
 */
public class PhotoListAdapter extends BaseAdapter {

    PhotoItemCollectionDao dao;
    MutableInteger lastPositionInteger;

    public PhotoListAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
    }

    @Override
    public int getCount() {
        if (dao == null)
            return 1;
        if (dao.getData() == null)
            return 1;
        return dao.getData().size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return dao.getData().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() - 1 ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1){
            // Progress Bar
            ProgressBar item;
            if(convertView != null)
                item = (ProgressBar) convertView;
            else
                item = new ProgressBar(parent.getContext());
            return item;
        }
        PhotoListItem item;
        if (convertView != null) {
            item = (PhotoListItem) convertView;
        } else {
            item = new PhotoListItem(parent.getContext());
        }

        PhotoItemDao dao = (PhotoItemDao) getItem(position);
        item.setNameText(dao.getCaption());
        item.setDescriptionText(dao.getUserName() + "\n" + dao.getCamera());
        item.setImageUrl(dao.getImageUrl());
        if (position > lastPositionInteger.getValue()) {
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(),
                    R.anim.up_from_bottom);
            item.startAnimation(anim);
            lastPositionInteger.setValue(position);
        }
        return item;
    }

    public void increaseLastPosition(int amount){
        lastPositionInteger.setValue(lastPositionInteger.getValue()+ amount);
    }

    /*@Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (getItemViewType(i) == 0) {
            PhotoListItem item;
            if (view != null) {
                item = (PhotoListItem) view;
            } else {
                item = new PhotoListItem(viewGroup.getContext());
            }
            return item;
        }else {
            TextView item;
            if (view != null) {
                item = (TextView) view;
            } else {
                item = new TextView(viewGroup.getContext());
                item.setText("Postion " + i);
            }
            return item;
        }
    }*/
}
