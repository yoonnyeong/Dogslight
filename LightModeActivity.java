package com.example.user.dogslight;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

// 야간모드를 설정하는 액티비티 : 아두이노의 불빛을 제어한다.
public class LightModeActivity extends AppCompatActivity {

    private static final String TAG = "bluetooth1";
    private TextView red, green, yellow, msg;
    private Button rOnButton, rOffButton, gOnButton, gOffButton,yOnButton, yOffButton,aOnButton, aOffButton;
    private LinearLayout lLayout;
    private BluetoothAdapter buttonAdapter = null;
    private BluetoothSocket buttonSocket = null;
    private OutputStream outStream = null;

    // SPP에 사용되는 UUID.
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // 연결할 아두이노 블루투스 주소.
    private static String address = "98:D3:35:71:04:27";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_mode);

        // xml의 요소들을 읽어온다.
        lLayout= (LinearLayout) findViewById(R.id.lLayout);
        rOnButton = (Button) findViewById(R.id.R_On_Button);
        rOffButton = (Button) findViewById(R.id.R_Off_Button);
        gOnButton = (Button) findViewById(R.id.G_On_Button);
        gOffButton = (Button) findViewById(R.id.G_Off_Button);
        yOnButton = (Button) findViewById(R.id.Y_On_Button);
        yOffButton = (Button) findViewById(R.id.Y_Off_Button);
        aOnButton = (Button) findViewById(R.id.All_On_Button);
        aOffButton = (Button) findViewById(R.id.All_Off_Button);
        red=(TextView)findViewById(R.id.RED);
        green=(TextView)findViewById(R.id.GREEN);
        yellow=(TextView)findViewById(R.id.YELLOW);
        msg= (TextView)findViewById(R.id.commentText);
        buttonAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        // 빨간 LED를 켜는 버튼을 누르면 아두이노에 이를 의미하는 1을 보낸다.
        rOnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                red.setBackgroundColor(Color.RED);
                msg.setText("빨간불이 켜졌습니다.");
                sendData("1");
            }
        });
        // 빨간 LED를 끄는 버튼을 누르면 아두이노에 이를 의미하는 0을 보낸다.
        rOffButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                red.setBackgroundColor(Color.WHITE);
                msg.setText("빨간불이 꺼졌습니다.");
                sendData("0");


            }
        });
        // 초록 LED를 켜는 버튼을 누르면 아두이노에 이를 의미하는 2을 보낸다.
        gOnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                green.setBackgroundColor(Color.GREEN);
                msg.setText("초록불이 켜졌습니다.");
                sendData("2");

            }
        });
        // 초록 LED를 끄는 버튼을 누르면 아두이노에 이를 의미하는 3을 보낸다.
        gOffButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                green.setBackgroundColor(Color.WHITE);
                msg.setText("초록불이 꺼졌습니다.");
                sendData("3");

            }
        });
        // 노란 LED를 켜는 버튼을 누르면 아두이노에 이를 의미하는 4을 보낸다.
        yOnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                yellow.setBackgroundColor(Color.YELLOW);
                msg.setText("노란불이 켜졌습니다.");
                sendData("4");

            }
        });
        // 노란 LED를 끄는 버튼을 누르면 아두이노에 이를 의미하는 5을 보낸다.
        yOffButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                yellow.setBackgroundColor(Color.WHITE);
                msg.setText("노란불이 꺼졌습니다.");
                sendData("5");

            }
        });
        // 모든 LED를 켜는 버튼을 누르면 아두이노에 이를 의미하는 6을 보낸다.
        aOnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                red.setBackgroundColor(Color.RED);
                green.setBackgroundColor(Color.GREEN);
                yellow.setBackgroundColor(Color.YELLOW);
                msg.setText("모든불이 켜졌습니다.");
                sendData("6");

            }
        });
        // 모든 LED를 끄는 버튼을 누르면 아두이노에 이를 의미하는 7을 보낸다.
        aOffButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                red.setBackgroundColor(Color.WHITE);
                green.setBackgroundColor(Color.WHITE);
                yellow.setBackgroundColor(Color.WHITE);
                msg.setText("모든불이 꺼졌습니다.");
                sendData("7");

            }
        });

    }

    // 블루투스 통신을 위한 소켓 생성.
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        // 아두이노 기기 버전 체크
        if(Build.VERSION.SDK_INT >= 10){
            try {
                // 블루투스 기기와 통신할 수 있는 소켓을 생성한다.
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        // 생성한 소켓을 리턴한다.
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        // 블루투스가 연결된 장치를 가져온다.
        BluetoothDevice device = buttonAdapter.getRemoteDevice(address);
        try {
            // 생성한 소켓을 buttonSocket에 저장한다.
            buttonSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
        }

        // 블루투스 연결 전에, 탐색 작업을 중지한다.
        buttonAdapter.cancelDiscovery();

        // 장치에 연결한다.
        Log.d(TAG, "...Connecting...");
        try {
            // 블루투스 소켓, 즉 아두이노에 연결한다.
            buttonSocket.connect();
            Log.d(TAG, "...Connection ok...");
        } catch (IOException e) {
            try {
                buttonSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        Log.d(TAG, "...Create Socket...");
        // 소켓을 통해 송수신을 핸들링하는 outputstream을 얻어온다.
        try {
            outStream = buttonSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        // 송신된 것이 없다면 아래 문장을 실행한다.
       if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        // 액티비티가 화면에 없다면, 소켓을 닫는다.
        try {
            buttonSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    // 블루투스가 활성화되어 있는지 상태를 확인한다.
    private void checkBTState() {
        // 블루투스가 연결되있지 않는다면 에러를 내보낸다.
        if(buttonAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        }
        else {
            if (buttonAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                // 블루투스를 활성화하기 위한 퍼미션을 요구하는 대화창이 나타난다.
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    // Toast로 에러메시지를 보여준다.
    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    // 연결된 기기, 아두이노에 데이터를 보내준다.
    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            // 기기에 데이터를 보내준다.
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

}
