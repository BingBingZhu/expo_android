package com.expo.contract.presenter;

import com.expo.contract.TrackContract;
import com.expo.contract.TrackContract.Presenter;
import com.expo.db.QueryParams;
import com.expo.entity.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackPresenterImpl extends Presenter {

    public TrackPresenterImpl(TrackContract.View view) {
        super(view);
    }

    @Override
    public void queryTrack() {
        Map<Long, List<Track>> map = new HashMap<>();
        List<Track> itemTracks = new ArrayList<>();
        List<Track> tracks = mDao.query(Track.class, new QueryParams());
        Long flag = -1L;
        for (int i = 0; i < tracks.size(); i++){
            if (flag == -1L || flag == tracks.get(i).getFlag()){
                itemTracks.add(tracks.get(i));
            } else {
                map.put(flag, itemTracks);
                itemTracks = new ArrayList<>();
                itemTracks.add(tracks.get(i));
            }
            if (i == tracks.size()-1){
                map.put(tracks.get(i).getFlag(), itemTracks);
            }
            flag = tracks.get(i).getFlag();
        }
        if (!map.isEmpty())
            mView.loadTrackRsp(map);
    }
}
