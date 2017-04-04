package hrv.band.app.view.fragment;

import java.util.Observable;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 13.03.2017
 */


public class FragmentObserver extends Observable {
    @Override
    public void notifyObservers() {
        setChanged(); // Set the changed flag to true, otherwise observers won't be notified.
        super.notifyObservers();
    }
}
