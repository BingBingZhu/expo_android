package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Contacts;

import java.util.List;

public interface ContactsAddContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void updateContactsData(Contacts contacts);

    }

    interface View extends IView {
        void saveContacts();
    }
}
