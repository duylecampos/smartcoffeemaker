package nkey.com.smartcoffeemaker.service;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import nkey.com.smartcoffeemaker.BluetoothService;
import nkey.com.smartcoffeemaker.Constants;

public class ServerStatusService extends IntentService {

    private static final String TAG = "ServerStatusService";

    private BluetoothService mChatService = null;

    private static boolean shouldMakeCoffee = false;
    private RequestQueue queue;
    Handler mMainThreadHandler = null;

    private static final String SERVER_STATUS_URL = "http://kadmus.com.br:5000/status";

    private static final String BLUETOOTH_ADDRESS = "00:12:08:09:24:76";
    private boolean isConnected;

    public ServerStatusService() {
        super(ServerStatusService.class.getName());
        mMainThreadHandler = new Handler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        readFromServer();

        if (isConnected) {
            startSendMsgs();
        } else {
            mChatService = new BluetoothService(this, mHandler);
            mChatService.start();

            connect();
        }
    }


    private void readFromServer() {
        queue = Volley.newRequestQueue(this);
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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
        queue.start();
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constants.MESSAGE_STATE_CHANGE && msg.arg1 == BluetoothService.STATE_CONNECTED) {
                Log.e(TAG, "handleMessage: STATE_CONNECTED");
                isConnected = true;
            }
        }
    };

    public void connect() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BLUETOOTH_ADDRESS);
        // Attempt to connect to the device
        boolean secure = false;
        mChatService.connect(device, secure);
    }

    private void startSendMsgs() {
        sendMessage(shouldMakeCoffee ? "1" : "0");
    }

    private void sendMessage(String message) {
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }
}
