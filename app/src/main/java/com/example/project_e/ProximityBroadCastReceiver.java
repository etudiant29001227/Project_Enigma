package com.example.project_e;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.view.Gravity;
import android.widget.Toast;

public class ProximityBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String message = null;

        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key,false);

        if(entering){
            message = context.getString(R.string.zoneWaringOut);
        }else{
            message = "entré dans la zone";
        }

        Toast toast = Toast.makeText(context,message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
}
