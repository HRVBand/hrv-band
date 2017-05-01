package hrv.band.app.ui.presenter;

/**
 * Copyright (c) 2017
 * Created by Thomas on 23.04.2017.
 */

public interface IMainPresenter {
    boolean agreedToDisclaimer();

    void handleNavBar(int id);

    boolean askedForSurvey();
}
