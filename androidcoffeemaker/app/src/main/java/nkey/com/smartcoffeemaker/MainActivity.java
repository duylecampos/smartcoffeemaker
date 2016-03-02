package nkey.com.smartcoffeemaker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String SERVER_STATUS_URL = "http://kadmus.com.br:5000/status";
    private static final String BLUETOOTH_ADDRESS = "00:12:08:09:24:76";

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mChatService = null;

    private static boolean shouldMakeCoffee = false;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();

        queue = Volley.newRequestQueue(this);

        mChatService = new BluetoothService(this, mHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChatService.start();

        connect();
        readFromServer();
    }

    public void read(View view) {
    }

    private void readFromServer() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("coffeemaker");
                            if (status.equals("making")) {
                                shouldMakeCoffee = true;
                            } else {
                                shouldMakeCoffee = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                readFromServer();
                            }
                        }, 500);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readFromServer();
                    }
                }, 500);

            }
        });
        queue.add(stringRequest);
        queue.start();
    }

    private void startSendMsgs() {
        sendMessage(shouldMakeCoffee ? "1" : "0");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSendMsgs();
            }
        }, 1000);
    }

    private void sendMessage(String message) {
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    public void connect() {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BLUETOOTH_ADDRESS);
        // Attempt to connect to the device
        boolean secure = false;
        mChatService.connect(device, secure);
    }

    private static final String TAG = "MainActivity";

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constants.MESSAGE_STATE_CHANGE && msg.arg1 == BluetoothService.STATE_CONNECTED) {
                Log.e(TAG, "handleMessage: STATE_CONNECTED");
                startSendMsgs();
            }
        }
    };

}
