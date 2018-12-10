package com.expo.contract.presenter;

import com.expo.contract.ContactsAddContract;
import com.expo.entity.Contacts;

public class ContactsAddPresenterImpl extends ContactsAddContract.Presenter {
    public ContactsAddPresenterImpl(ContactsAddContract.View view) {
        super(view);
    }

    @Override
    public void updateContactsData(Contacts contacts) {
        mDao.saveOrUpdate(contacts);
        mView.saveContacts();
    }

}
