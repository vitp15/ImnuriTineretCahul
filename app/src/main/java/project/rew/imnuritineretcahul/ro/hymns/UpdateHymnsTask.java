package project.rew.imnuritineretcahul.ro.hymns;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentActivity;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.ro.ui.home.Utils;


public class UpdateHymnsTask extends AsyncTask<String, String, String> {
    private Context context;
    private String result;
    private ProgressDialog progressDialog;
    private FragmentActivity fragmentActivity;

    public UpdateHymnsTask(Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,
                context.getString(R.string.updating), null);
    }

    @Override
    protected String doInBackground(String... args) {
        publishProgress(context.getString(R.string.hymns_message_updating));
        Utils.updateHymns(context, fragmentActivity);
        return null;
    }

    @Override
    protected void onProgressUpdate(String... text) {
        progressDialog.setMessage(text[0]);
    }

    protected void onPostExecute(String result) {
        progressDialog.dismiss();
    }
}