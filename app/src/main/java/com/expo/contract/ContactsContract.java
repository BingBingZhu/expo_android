package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Contacts;

import java.util.List;

public interface ContactsContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getContactsData();

        public abstract void delContact(Contacts contacts, int position);

    }

    interface View extends IView {
        void freshContacts(List<Contacts> list);

        void removeContact(int position);
    }
}
