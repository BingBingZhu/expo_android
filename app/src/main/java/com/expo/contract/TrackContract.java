package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Track;

import java.util.List;
import java.util.Map;

public interface TrackContract {
    abstract class Presenter extends IPresenter<View> {
        public Presenter(View view) {
            super(view);
        }

        public abstract void queryTrack();

        public abstract void clearTrack();
    }

    interface View extends IView {
        void loadTrackRsp(Map<Long, List<Track>> map);

        void clearTrackRes();
    }
}
