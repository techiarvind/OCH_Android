package och.adityaworks.com.och;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Aditya Pandey on 05-06-2016.
 */
public class DeviceDetailsUpdater extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = DeviceDetailsUpdater.class.getSimpleName();
    private Context mContext;
    private ProgressDialog dialog;
    private String mID;
    public DeviceDetailsUpdater(Context context, String id) {
        mContext = context;
        mID = id;
    }


    @Override
    public void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(mContext);
        dialog.setMessage("Loading");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String specDevice = null;
        try {
            specDevice = CloudConnect.urlToString(CloudConnect.getURL("devices", mID));
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, e.toString());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }
        return specDevice;
    }

    @Override
    public void onPostExecute(String newDeviceInfo) {
        JSONObject deviceInfo;
        Log.v(LOG_TAG, "newDeviceInfo is " + newDeviceInfo);
        try {
            deviceInfo = new JSONObject(newDeviceInfo);
            DeviceDetails.icon.setImageResource(CloudConnect.getIcon(deviceInfo.getString("type")));
            DeviceDetails.name_details.setText(deviceInfo.getString("name"));
            DeviceDetails.switch_button.setChecked(deviceInfo.getBoolean("switchBoolean"));
            DeviceDetails.currentValue.setText(deviceInfo.getString("currentStatus"));
            DeviceDetails.targetValue.setText(deviceInfo.getString("targetValue"));

            dialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
