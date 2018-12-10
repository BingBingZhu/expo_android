package com.expo.module.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expo.R;
import com.expo.entity.Contacts;

import java.util.List;

import butterknife.BindView;

public class ContactsAddAdapter extends BaseAdapter {

    Context mContext;
    List<ContactsAddActivity.IdType> mData;

    public ContactsAddAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<ContactsAddActivity.IdType> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_add, null);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        holderView.text.setText(mData.get(position).name);
        return convertView;
    }

    class HolderView {
        @BindView(R.id.user_work)
        TextView text;

        public HolderView(View view) {
            text = view.findViewById(R.id.user_work);
        }
    }
}
