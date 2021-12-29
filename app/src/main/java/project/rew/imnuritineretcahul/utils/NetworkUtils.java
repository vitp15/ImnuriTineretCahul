package project.rew.imnuritineretcahul.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.material.snackbar.Snackbar;


public class NetworkUtils {
    public static Snackbar noNetworkSnackBar = null;

    public static boolean hasActiveNetworkConnection(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable();
        if (isConnected) {
            if (noNetworkSnackBar != null && noNetworkSnackBar.isShownOrQueued())
                noNetworkSnackBar.dismiss();
        }
        return isConnected;
    }
}