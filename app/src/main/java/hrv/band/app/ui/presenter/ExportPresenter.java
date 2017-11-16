package hrv.band.app.ui.presenter;

import android.content.Context;

import hrv.band.app.ui.view.fragment.IExportView;

/**
 * Copyright (c) 2017
 * Created by Thomas on 29.04.2017.
 */

public class ExportPresenter implements IExportPresenter {
    private IExportView view;

    public ExportPresenter(IExportView view) {
        this.view = view;
    }

    @Override
    public void exportDatabase() {
        Context context = view.getExportContext();
       /* HRVSQLController sql = new HRVSQLController(context);
        try {
            CharSequence text;
            if (!sql.exportDB()) {
                text = context.getResources().getText(R.string.sentence_export_failed);
            } else {
                text = context.getResources().getText(R.string.sentence_export_worked);
            }
            view.showToast(text);
        } catch (IOException e) {
            Log.e(e.getClass().getName(), "IOException", e);
        }*/
    }
}
