package project.rew.imnuritineretcahul.ro.ui.audio;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentActivity;

import project.rew.imnuritineretcahul.R;


public class UpdateAudioTask extends AsyncTask<String, String, String> {
    private Context context;
    private String result;
    private ProgressDialog progressDialog;
    private FragmentActivity fragmentActivity;
    private boolean single;

    public UpdateAudioTask(Context context, FragmentActivity fragmentActivity,boolean single) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.single=single;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,
                context.getString(R.string.updating), null);
    }

    @Override
    protected String doInBackground(String... args) {
        publishProgress("Se actualizeaza audiourile");
        Utils.updateAudio(context, fragmentActivity,single);
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