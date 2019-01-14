package com.expo.module.prompt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.utils.PrefsHelper;
import com.expo.utils.Constants;

public class PromptAdapter extends BaseAdapter {

    Context context;
    LayoutInflater mInflater;

    public PromptAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Constants.RawResource.resourceIds.length ;
    }

    @Override
    public Integer getItem(int position) {
        return Constants.RawResource.resourceIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_prompt_item, null);
            holder.radioBtn = (TextView) convertView.findViewById(R.id.prompt_item_rb);
            holder.radioBtn.setClickable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == PrefsHelper.getInt(Constants.Prefs.KEY_RAW_SELECTOR_POSITION, 0)){
            holder.radioBtn.setSelected(true);
        }else{
            holder.radioBtn.setSelected(false);
        }
        holder.radioBtn.setText(Constants.RawResource.rawName[position]);
        return convertView;
    }

    class ViewHolder {
        TextView radioBtn;
    }

}
