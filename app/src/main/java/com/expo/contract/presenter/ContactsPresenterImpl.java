package com.expo.contract.presenter;

import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.BadgeContract;
import com.expo.contract.ContactsContract;
import com.expo.db.QueryParams;
import com.expo.entity.Badge;
import com.expo.entity.Contacts;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BadgeResp;
import com.expo.utils.Constants;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

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


}
