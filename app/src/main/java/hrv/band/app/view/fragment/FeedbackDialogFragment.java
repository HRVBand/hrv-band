package hrv.band.app.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import hrv.band.app.R;

/**
 * Created by Thomas on 27.07.2016.
 */
public class FeedbackDialogFragment extends DialogFragment {
    private static final String FEEDBACK_EMAIL = "hrvband+feedback@gmail.com";
    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.feedback_fragment, null);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.feedback_send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", FEEDBACK_EMAIL, null));
                        email.putExtra(Intent.EXTRA_SUBJECT, getSubject());
                        email.putExtra(Intent.EXTRA_TEXT, getText());
                        startActivity(Intent.createChooser(email, "send feedback"));

                    }
                })
                .setNegativeButton(R.string.feedback_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FeedbackDialogFragment.this.getDialog().cancel();
                    }
                });
        builder.setTitle("Feedback");
        setSpinnerValues(view);
        return builder.create();
    }

    private void setSpinnerValues(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.feedback_category);
        FeedbackCategoryAdapter spinnerArrayAdapter = new FeedbackCategoryAdapter(getActivity().getApplicationContext());

        spinner.setAdapter(spinnerArrayAdapter);
    }

    private String getSubject() {
        if (view == null) {
            return "";
        }
        Spinner spinner = (Spinner) view.findViewById(R.id.feedback_category);
        String subject = FeedbackCategory.values()[spinner.getSelectedItemPosition()].getText(getResources());
        return "Feedback - [" + subject + "]";
    }

    private String getText() {
        EditText text = (EditText) view.findViewById(R.id.feedback_text);
        return text.getText() + "\n" + getDeviceInformation();
    }

    private String getDeviceInformation() {
        String info = "Debug-infos:";
        info += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        info += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        info += "\n Device: " + android.os.Build.DEVICE;
        info += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        return info;
    }

    private class FeedbackCategoryAdapter extends BaseAdapter {

        private final Context context;

        public FeedbackCategoryAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return FeedbackCategory.values().length;
        }

        @Override
        public Object getItem(int position) {
            return FeedbackCategory.values()[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.spinner_category_item, parent, false);
            TextView categoryTxt = (TextView) view.findViewById(R.id.category_name);
            ImageView categoryIcon = (ImageView) view.findViewById(R.id.category_icon);

            categoryTxt.setText(FeedbackCategory.values()[position].getText(context.getResources()));
            categoryIcon.setImageDrawable(FeedbackCategory.values()[position].getIcon(context.getResources()));
            return view;
        }
    }

    private enum FeedbackCategory {
        BUG(R.string.feedback_category_bug, R.drawable.ic_bug),
        IDEA(R.string.feedback_category_idea, R.drawable.ic_idea),
        QUESTION(R.string.feedback_category_question, R.drawable.ic_question)
        ;

        private final int text;
        private final int icon;

        FeedbackCategory(int text, int icon) {
            this.text = text;
            this.icon = icon;
        }

        public String getText(Resources resources) {
            return resources.getString(text);
        }

        public Drawable getIcon(Resources resources) {
            return ResourcesCompat.getDrawable(resources, icon, null);
        }
    }
}
