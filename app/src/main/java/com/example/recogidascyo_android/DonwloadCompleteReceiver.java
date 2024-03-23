package com.example.recogidascyo_android;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by gustavo.peiretti
 */
public class DonwloadCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
            Toast.makeText(context,"Descarga completada", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));



        }
    }


}
