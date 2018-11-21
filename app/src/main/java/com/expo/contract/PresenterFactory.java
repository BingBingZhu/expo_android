package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.contract.presenter.BindPhonePresenterImpl;
import com.expo.contract.presenter.DistinguishPresenterImpl;
import com.expo.contract.presenter.EncyclopediaSearchPresenterImpl;
import com.expo.contract.presenter.EncyclopediasPresenterImpl;
import com.expo.contract.presenter.FeedbackPresenterImpl;
import com.expo.contract.presenter.FreeWifiPresenterImpl;
import com.expo.contract.presenter.HeartBeatPresenterImpl;
import com.expo.contract.presenter.HomePresenterImpl;
import com.expo.contract.presenter.ListPresenterImpl;
import com.expo.contract.presenter.LoginPresenterImpl;
import com.expo.contract.presenter.MessageKindPresenterImpl;
import com.expo.contract.presenter.MessagesPresenterImpl;
import com.expo.contract.presenter.MinePresenterImpl;
import com.expo.contract.presenter.NationalSmsCodePresenterImpl;
import com.expo.contract.presenter.NavigationPresenterImpl;
import com.expo.contract.presenter.PanoramaPresenterImpl;
import com.expo.contract.presenter.ParkMapPresenterImpl;
import com.expo.contract.presenter.RouteDetailPresenterImpl;
import com.expo.contract.presenter.RoutesPresenterImpl;
import com.expo.contract.presenter.SeekHelpPresenterImpl;
import com.expo.contract.presenter.SettingPresenterImpl;
import com.expo.contract.presenter.SplashPresenterImpl;
import com.expo.contract.presenter.TouristServicePresenterImpl;
import com.expo.contract.presenter.UserInfoPresenterImpl;
import com.expo.contract.presenter.WebPresenterImpl;
import com.expo.contract.presenter.WebTemplatePresenterImpl;

public class PresenterFactory {
    public static IPresenter getPresenter(IView view) {
        if (view instanceof SplashContract.View) {
            return new SplashPresenterImpl( (SplashContract.View) view );
        } else if (view instanceof LoginContract.View) {
            return new LoginPresenterImpl( (LoginContract.View) view );
        } else if (view instanceof HeartBeatContract.View) {
            return new HeartBeatPresenterImpl( (HeartBeatContract.View) view );
        } else if (view instanceof EncyclopediasContract.View) {
            return new EncyclopediasPresenterImpl( (EncyclopediasContract.View) view );
        } else if (view instanceof MineContract.View) {
            return new MinePresenterImpl( (MineContract.View) view );
        } else if (view instanceof NavigationContract.View) {
            return new NavigationPresenterImpl( (NavigationContract.View) view );
        } else if (view instanceof RoutesContract.View) {
            return new RoutesPresenterImpl( (RoutesContract.View) view );
        } else if (view instanceof HomeContract.View) {
            return new HomePresenterImpl( (HomeContract.View) view );
        } else if (view instanceof BindPhoneContract.View) {
            return new BindPhonePresenterImpl( (BindPhoneContract.View) view );
        } else if (view instanceof DistinguishContract.View) {
            return new DistinguishPresenterImpl( (DistinguishContract.View) view );
        } else if (view instanceof EncyclopediaSearchContract.View) {
            return new EncyclopediaSearchPresenterImpl( (EncyclopediaSearchContract.View) view );
        } else if (view instanceof FeedbackContract.View) {
            return new FeedbackPresenterImpl( (FeedbackContract.View) view );
        } else if (view instanceof FreeWiFiContract.View) {
            return new FreeWifiPresenterImpl( (FreeWiFiContract.View) view );
        } else if (view instanceof MessageKindContract.View) {
            return new MessageKindPresenterImpl( (MessageKindContract.View) view );
        } else if (view instanceof MessagesContract.View) {
            return new MessagesPresenterImpl( (MessagesContract.View) view );
        } else if (view instanceof NationalSmsCodeContract.View) {
            return new NationalSmsCodePresenterImpl( (NationalSmsCodeContract.View) view );
        } else if (view instanceof RouteDetailContract.View) {
            return new RouteDetailPresenterImpl( (RouteDetailContract.View) view );
        } else if (view instanceof ParkMapContract.View) {
            return new ParkMapPresenterImpl( (ParkMapContract.View) view );
        } else if (view instanceof SeekHelpContract.View) {
            return new SeekHelpPresenterImpl( (SeekHelpContract.View) view );
        } else if (view instanceof SettingContract.View) {
            return new SettingPresenterImpl( (SettingContract.View) view );
        } else if (view instanceof UserInfoContract.View) {
            return new UserInfoPresenterImpl( (UserInfoContract.View) view );
        } else if (view instanceof ListContract.View) {
            return new ListPresenterImpl( (ListContract.View) view );
        } else if (view instanceof WebTemplateContract.View) {
            return new WebTemplatePresenterImpl( (WebTemplateContract.View) view );
        } else if (view instanceof WebContract.View) {
            return new WebPresenterImpl( (WebContract.View) view );
        } else if (view instanceof TouristServiceContract.View) {
            return new TouristServicePresenterImpl( (TouristServiceContract.View) view );
        } else if (view instanceof PanoramaContract.View) {
            return new PanoramaPresenterImpl( (PanoramaContract.View) view );
        }
        throw new UnsupportedOperationException();
    }
}
