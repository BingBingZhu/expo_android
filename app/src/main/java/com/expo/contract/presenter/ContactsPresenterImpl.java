package com.expo.contract.presenter;

import com.expo.base.ExpoApp;
import com.expo.contract.ContactsContract;
import com.expo.db.QueryParams;
import com.expo.entity.Contacts;

public class ContactsPresenterImpl extends ContactsContract.Presenter {
    public ContactsPresenterImpl(ContactsContract.View view) {
        super(view);
    }

    @Override
    public void getContactsData() {
        QueryParams params = new QueryParams()
                .add("eq", "u_id", ExpoApp.getApplication().getUser().getUid());
        mView.freshContacts(mDao.query(Contacts.class, params));
    }

    @Override
    public void delContact(Contacts contacts, int position) {
        mDao.delete(contacts);
        mView.removeContact(position);
    }


}
