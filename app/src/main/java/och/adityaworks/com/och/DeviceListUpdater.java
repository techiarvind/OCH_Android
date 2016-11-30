package och.adityaworks.com.och;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Aditya Pandey on 05-06-2016.
 */
public class DeviceListUpdater extends AsyncTask<Void, Void, String> {

    private final static String LOG_TAG = DeviceListUpdater.class.getSimpleName();
    private Context mContext;

    public DeviceListUpdater(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String deviceStr = null;
        try {
            deviceStr = CloudConnect.urlToString(CloudConnect.getURL("devices"));
            Log.v(LOG_TAG, "Device String is :" + deviceStr);
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, e.toString());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }
        return deviceStr;
    }

    @Override
    public void onPostExecute(String newDeviceList) {
        Log.v(LOG_TAG, "New Device List is " + newDeviceList);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AddDevice.context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("deviceStr", newDeviceList);
        editor.apply();

        ArrayList<Device> devices = AddDevice.getDeviceList();
        AddDevice.deviceAdapter = new DeviceAdapter(
                mContext, R.layout.grid_single,
                devices
        );
        AddDevice.gridView.setAdapter(AddDevice.deviceAdapter);
        }

    }
