package com.expo.adapters;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.expo.entity.Venue;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class VenueAdapter implements ListItemData {

    private Venue mVenue;

    public VenueAdapter(Venue dataType) {
        this.mVenue = dataType;
    }

    public VenueAdapter(Parcel in) {
        if (in.readByte() == 0) {
            mVenue = null;
        } else {
            mVenue = in.readParcelable(Venue.class.getClassLoader());
        }
    }

    @Override
    public Long getId() {
        return mVenue.getId();
    }

    @Override
    public String getCaption() {
        return mVenue.getCaption();
    }

    @Override
    public String getPicUrl() {
        return mVenue.getPicUrl();
    }

    @Override
    public Integer getRecommend() {
        return mVenue.getIsRecommended();
    }

    @Override
    public String getRemark() {
        return mVenue.getRemark();
    }

    @Override
    public String getEnCaption() {
        return mVenue.getEnCaption();
    }

    @Override
    public String getEnRemark() {
        return mVenue.getEnRemark();
    }

    public static List<ListItemData> convertToTabList(List<Venue> types) {
        if (types == null)
            return null;
        List<ListItemData> listItemData = new ArrayList<>();
        for (Venue venue : types) {
            listItemData.add(new VenueAdapter(venue));
        }
        return listItemData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mVenue == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeParcelable(mVenue, flags);
        }
    }

    public static final Creator<VenueAdapter> CREATOR = new Creator<VenueAdapter>() {
        @Override
        public VenueAdapter createFromParcel(Parcel in) {
            return new VenueAdapter(in);
        }

        @Override
        public VenueAdapter[] newArray(int size) {
            return new VenueAdapter[size];
        }
    };
}
