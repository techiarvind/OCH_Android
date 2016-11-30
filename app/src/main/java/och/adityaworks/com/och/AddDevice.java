package och.adityaworks.com.och;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddDevice extends AppCompatActivity {

    private static final String LOG_TAG = AddDevice.class.getSimpleName();
    public static GridView gridView;
    public static DeviceAdapter deviceAdapter;
    public static Context context;
    final static String DEVICE_NAME = "name";
    final static String DEVICE_TYPE = "type";
    final String DEVICE_ID = "_id";

    @Override
    public void onStart() {
        super.onStart();
        DeviceListUpdater updaterTask = new DeviceListUpdater(this);
        updaterTask.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        context = getApplicationContext();

        gridView = (GridView) findViewById(R.id.device_list);
        ArrayList<Device> devices = getDeviceList();
        deviceAdapter = new DeviceAdapter(
                this, R.layout.grid_single,
                devices
        );

        gridView.setAdapter(deviceAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    openActivity(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openActivity(int position) throws JSONException {
        JSONArray deviceArray = new JSONArray(getDeviceStr());
        JSONObject deviceObject = deviceArray.getJSONObject(position);
        String type = deviceObject.getString(DEVICE_TYPE);
        Log.v(LOG_TAG,"Device type is " + type);
        String id = deviceObject.getString(DEVICE_ID);

        if (type.equals("add")) {
            startActivity(new Intent(this, NewDevice.class));
        }
        else {
            Intent intent = new Intent(this, DeviceDetails.class);
            intent.putExtra(DeviceDetails.DETAIL_TYPE, type);
            intent.putExtra(DeviceDetails.DETAIL_ID, id);
            startActivity(intent);
        }
    }

    public static ArrayList<Device> getDeviceList() {
        ArrayList<Device> devices;

        try {
            devices = getDeviceListFromJson(getDeviceStr());
            return devices;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Device> getDeviceListFromJson(String deviceStr)
            throws JSONException {

        JSONArray deviceArray = new JSONArray(deviceStr);
        ArrayList<Device> resultList = new ArrayList<>();

        for (int i = 0; i < deviceArray.length(); i++) {
            JSONObject deviceObject = deviceArray.getJSONObject(i);
            String name = deviceObject.getString(DEVICE_NAME);
            String type = deviceObject.getString(DEVICE_TYPE);

            resultList.add(new Device(name, type));
        }

        return resultList;
    }

    public static String getDeviceStr() {
        String deviceList = "[{\"_id\":\"5753c81c76c503dc03000002\",\"name\":\"Add New\",\"type\":\"add\",\"__v\":0}]";
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString("deviceStr", deviceList);
    }
}
