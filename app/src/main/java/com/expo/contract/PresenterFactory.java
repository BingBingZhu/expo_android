package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.contract.presenter.ArPresenterImpl;
import com.expo.contract.presenter.BadgePresenterImpl;
import com.expo.contract.presenter.BindPhonePresenterImpl;
import com.expo.contract.presenter.CircumTrafficPresenterImpl;
import com.expo.contract.presenter.CirnumListPresenterImpl;
import com.expo.contract.presenter.ContactsAddPresenterImpl;
import com.expo.contract.presenter.ContactsPresenterImpl;
import com.expo.contract.presenter.CustomRoutePresenterImpl;
import com.expo.contract.presenter.DistinguishPresenterImpl;
import com.expo.contract.presenter.EncyclopediaSearchPresenterImpl;
import com.expo.contract.presenter.EncyclopediasPresenterImpl;
import com.expo.contract.presenter.ExaminePresenterImpl;
import com.expo.contract.presenter.ExpoActivityPresenterImpl;
import com.expo.contract.presenter.FeedbackPresenterImpl;
import com.expo.contract.presenter.FindDetailPresenterImpl;
import com.expo.contract.presenter.FindListPresenterImpl;
import com.expo.contract.presenter.FindPresenterImpl;
import com.expo.contract.presenter.FindPublishPresenterImpl;
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
import com.expo.contract.presenter.OnlineHomePresenterImpl;
import com.expo.contract.presenter.PanoramaListPresenterImpl;
import com.expo.contract.presenter.ParkMapFragmentPresenterImpl;
import com.expo.contract.presenter.ParkMapPresenterImpl;
import com.expo.contract.presenter.PlayMapPresenterImpl;
import com.expo.contract.presenter.PortalSitePresenterImpl;
import com.expo.contract.presenter.RouteDetailPresenterImpl;
import com.expo.contract.presenter.RoutesPresenterImpl;
import com.expo.contract.presenter.ScenicPresenterImpl;
import com.expo.contract.presenter.SeekHelpPresenterImpl;
import com.expo.contract.presenter.ServiceHistoryPresenterImpl;
import com.expo.contract.presenter.SettingPresenterImpl;
import com.expo.contract.presenter.SplashPresenterImpl;
import com.expo.contract.presenter.TouristServicePresenterImpl;
import com.expo.contract.presenter.TrackPresenterImpl;
import com.expo.contract.presenter.UserInfoPresenterImpl;
import com.expo.contract.presenter.VRDetailPresenterImpl;
import com.expo.contract.presenter.VRImagePresenterImpl;
import com.expo.contract.presenter.VenueListPresenterImpl;
import com.expo.contract.presenter.VenuePresenterImpl;
import com.expo.contract.presenter.WebExpoActivityPresenterImpl;
import com.expo.contract.presenter.WebPresenterImpl;
import com.expo.contract.presenter.WebTemplatePresenterImpl;

public class PresenterFactory {
    public static IPresenter getPresenter(IView view) {
        if (view instanceof SplashContract.View) {
            return new SplashPresenterImpl((SplashContract.View) view);
        } else if (view instanceof LoginContract.View) {
            return new LoginPresenterImpl((LoginContract.View) view);
        } else if (view instanceof HeartBeatContract.View) {
            return new HeartBeatPresenterImpl((HeartBeatContract.View) view);
        } else if (view instanceof EncyclopediasContract.View) {
            return new EncyclopediasPresenterImpl((EncyclopediasContract.View) view);
        } else if (view instanceof MineContract.View) {
            return new MinePresenterImpl((MineContract.View) view);
        } else if (view instanceof NavigationContract.View) {
            return new NavigationPresenterImpl((NavigationContract.View) view);
        } else if (view instanceof RoutesContract.View) {
            return new RoutesPresenterImpl((RoutesContract.View) view);
        } else if (view instanceof HomeContract.View) {
            return new HomePresenterImpl((HomeContract.View) view);
        } else if (view instanceof BindPhoneContract.View) {
            return new BindPhonePresenterImpl((BindPhoneContract.View) view);
        } else if (view instanceof DistinguishContract.View) {
            return new DistinguishPresenterImpl((DistinguishContract.View) view);
        } else if (view instanceof EncyclopediaSearchContract.View) {
            return new EncyclopediaSearchPresenterImpl((EncyclopediaSearchContract.View) view);
        } else if (view instanceof FeedbackContract.View) {
            return new FeedbackPresenterImpl((FeedbackContract.View) view);
        } else if (view instanceof FreeWiFiContract.View) {
            return new FreeWifiPresenterImpl((FreeWiFiContract.View) view);
        } else if (view instanceof MessageKindContract.View) {
            return new MessageKindPresenterImpl((MessageKindContract.View) view);
        } else if (view instanceof MessagesContract.View) {
            return new MessagesPresenterImpl((MessagesContract.View) view);
        } else if (view instanceof NationalSmsCodeContract.View) {
            return new NationalSmsCodePresenterImpl((NationalSmsCodeContract.View) view);
        } else if (view instanceof RouteDetailContract.View) {
            return new RouteDetailPresenterImpl((RouteDetailContract.View) view);
        } else if (view instanceof ParkMapContract.View) {
            return new ParkMapPresenterImpl((ParkMapContract.View) view);
        } else if (view instanceof SeekHelpContract.View) {
            return new SeekHelpPresenterImpl((SeekHelpContract.View) view);
        } else if (view instanceof SettingContract.View) {
            return new SettingPresenterImpl((SettingContract.View) view);
        } else if (view instanceof UserInfoContract.View) {
            return new UserInfoPresenterImpl((UserInfoContract.View) view);
        } else if (view instanceof ListContract.View) {
            return new ListPresenterImpl((ListContract.View) view);
        } else if (view instanceof WebTemplateContract.View) {
            return new WebTemplatePresenterImpl((WebTemplateContract.View) view);
        } else if (view instanceof WebContract.View) {
            return new WebPresenterImpl((WebContract.View) view);
        } else if (view instanceof TouristServiceContract.View) {
            return new TouristServicePresenterImpl((TouristServiceContract.View) view);
        } else if (view instanceof BadgeContract.View) {
            return new BadgePresenterImpl((BadgeContract.View) view);
        } else if (view instanceof TrackContract.View) {
            return new TrackPresenterImpl((TrackContract.View) view);
        } else if (view instanceof FindContract.View) {
            return new FindPresenterImpl((FindContract.View) view);
        } else if (view instanceof ContactsContract.View) {
            return new ContactsPresenterImpl((ContactsContract.View) view);
        } else if (view instanceof ContactsAddContract.View) {
            return new ContactsAddPresenterImpl((ContactsAddContract.View) view);
        } else if (view instanceof FindListContract.View) {
            return new FindListPresenterImpl((FindListContract.View) view);
        } else if (view instanceof ExamineContract.View) {
            return new ExaminePresenterImpl((ExamineContract.View) view);
        } else if (view instanceof FindPublishContract.View) {
            return new FindPublishPresenterImpl((FindPublishContract.View) view);
        } else if (view instanceof FindDetailContract.View) {
            return new FindDetailPresenterImpl((FindDetailContract.View) view);
        } else if (view instanceof ServiceHistoryContract.View) {
            return new ServiceHistoryPresenterImpl((ServiceHistoryContract.View) view);
        } else if (view instanceof CustomRouteContract.View) {
            return new CustomRoutePresenterImpl((CustomRouteContract.View) view);
        } else if (view instanceof VRImageContract.View) {
            return new VRImagePresenterImpl((VRImageContract.View) view);
        } else if (view instanceof OnlineHomeContract.View) {
            return new OnlineHomePresenterImpl((OnlineHomeContract.View) view);
        } else if (view instanceof VRDetailContract.View) {
            return new VRDetailPresenterImpl((VRDetailContract.View) view);
        } else if (view instanceof PanoramaListContract.View) {
            return new PanoramaListPresenterImpl((PanoramaListContract.View) view);
        } else if (view instanceof CirnumListContract.View) {
            return new CirnumListPresenterImpl((CirnumListContract.View) view);
        } else if (view instanceof CircumTrafficContract.View) {
            return new CircumTrafficPresenterImpl((CircumTrafficContract.View) view);
        } else if (view instanceof ExpoActivityContract.View) {
            return new ExpoActivityPresenterImpl((ExpoActivityContract.View) view);
        } else if (view instanceof VenueContract.View) {
            return new VenuePresenterImpl((VenueContract.View) view);
        } else if (view instanceof VenueListContract.View) {
            return new VenueListPresenterImpl((VenueListContract.View) view);
        } else if (view instanceof ParkMapFragmentContract.View) {
            return new ParkMapFragmentPresenterImpl((ParkMapFragmentContract.View) view);
        } else if (view instanceof ScenicContract.View) {
            return new ScenicPresenterImpl((ScenicContract.View) view);
        } else if (view instanceof ArContract.View) {
            return new ArPresenterImpl((ArContract.View) view);
        } else if (view instanceof WebExpoActivityContract.View) {
            return new WebExpoActivityPresenterImpl((WebExpoActivityContract.View) view);
        } else if (view instanceof PortalSiteContract.View) {
            return new PortalSitePresenterImpl((PortalSiteContract.View) view);
        } else if (view instanceof PlayMapContract.View) {
            return new PlayMapPresenterImpl((PlayMapContract.View) view);
        }
        throw new UnsupportedOperationException();
    }
}
