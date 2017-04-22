package hrv.band.app.ui.presenter;

/**
 * Copyright (c) 2017
 * Created by Thomas on 15.04.2017.
 */

public interface ISettingsPresenter {
    boolean hasFileWritePermission();

    boolean getFileWritePermission();

    boolean isRecordLengthValid(String o);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
