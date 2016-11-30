package och.adityaworks.com.och;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Aditya Pandey on 05-06-2016.
 */
public class DeviceDetails extends AppCompatActivity {
    public static final String DETAIL_TYPE = "selected_type";
    public static final String DETAIL_ID = "selected_id";
    public static String selectedType;
    public static String selectedId;
    public static ImageView icon;
    public static TextView name_details;
    public static Switch switch_button;
    public static TextView currentValue;
    public static TextView targetValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        if(savedInstanceState==null) {
            selectedType = getIntent().getStringExtra(DETAIL_TYPE);
            selectedId = getIntent().getStringExtra(DETAIL_ID);
        }

        icon = (ImageView) findViewById(R.id.icon_details);
        name_details = (TextView) findViewById(R.id.name_details);
        switch_button = (Switch) findViewById(R.id.switch_button);
        currentValue = (TextView) findViewById(R.id.current_value);
        targetValue = (TextView) findViewById(R.id.target_value);

        DeviceDetailsUpdater deviceDetailsUpdater = new DeviceDetailsUpdater(this, selectedId);
        deviceDetailsUpdater.execute();
    }
}
