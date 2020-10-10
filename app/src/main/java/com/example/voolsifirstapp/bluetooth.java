package com.example.voolsifirstapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class bluetooth extends AppCompatActivity {
    Button _btnscan, bluetooth, _btnshow, _btnlist;
    ListView list;
    TextView status, message_box;
    ArrayList<String> stringArrayList= new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    BluetoothAdapter myBluetoothAdapter;

    Intent btEnablingIntent;
    int requestCodeForEnable;
    BluetoothDevice[] btArray;


    private static final String APP_NAME="LogIn";
    private static final UUID MY_UUID= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    static final int STATE_LISTENING=1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    SendReceive sendReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        _btnscan =(Button)findViewById(R.id.btnscan);
        _btnshow= (Button) findViewById(R.id.btnshow);
        _btnlist=(Button) findViewById(R.id.btnlist);
        bluetooth= (Button) findViewById(R.id.bluetooth);
        list =(ListView) findViewById(R.id.list);
        status= (TextView) findViewById(R.id.status);
        message_box= (TextView) findViewById(R.id.message_box);

        myBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable=1;
        bluetoothOnMethod();

        myBluetoothAdapter.startDiscovery();
        ServerClass serverClass= new ServerClass();
        serverClass.start();

      _btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myBluetoothAdapter.startDiscovery();
                ServerClass serverClass= new ServerClass();
                serverClass.start();

            }
        });

        _btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(bluetooth.this, MainActivityClassification.class);
                startActivity(intent);

            }
        });


        IntentFilter intentFilter= new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReceiver,intentFilter);

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,stringArrayList);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientClass clientClass= new ClientClass(btArray[i]);
                clientClass.start();
                status.setText("Connecting");
                ServerClass serverClass= new ServerClass();
                serverClass.start();




            }
        });

        _btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set <BluetoothDevice> bt=myBluetoothAdapter.getBondedDevices();
                String[] strings=new String[bt.size()];
                btArray=new BluetoothDevice[bt.size()];
                int index=0;

                if (bt.size()>0){
                    for (BluetoothDevice device : bt){
                        btArray[index]= device;
                        strings [index]=device.getName();
                        index++;
                    }

                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, stringArrayList);
                    list.setAdapter(arrayAdapter);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==requestCodeForEnable){
            if(resultCode==RESULT_OK){
                Toast.makeText(getApplicationContext(), "Bluetooth is Enabled", Toast.LENGTH_LONG).show();
            }else if (resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Bluetooth enabling is canceled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void bluetoothOnMethod() {
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myBluetoothAdapter==null){
                    Toast.makeText(getApplicationContext(), "The device does not support Bluetooth", Toast.LENGTH_LONG).show();
                }else {
                    if (!myBluetoothAdapter.isEnabled()){
                        startActivityForResult(btEnablingIntent,requestCodeForEnable);
                    }
                }



            }
        });
    }

    BroadcastReceiver myReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action=intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device =intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                stringArrayList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();
            }

        }
    };

    private class SendReceive extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        public SendReceive (BluetoothSocket socket){
            bluetoothSocket= socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;
            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e){
                e.printStackTrace();
            }
            inputStream= tempIn;
            outputStream=tempOut;
        }

        public void run(){

            byte[] buffer= new  byte[1024];
            int bytes;
            while (true){
                try {
                    bytes= inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes,-1,buffer).sendToTarget();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }


        }

        public void write(byte[] bytes){
            try{
                outputStream.write(bytes);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch (message.what){
                case STATE_LISTENING:
                        status.setText("Listening");
                        break;
                case STATE_CONNECTING:
                        status.setText("Connecting");
                        break;
                case STATE_CONNECTED:
                        status.setText("Connected");
                        break;
                case STATE_CONNECTION_FAILED:
                        status.setText("Connection failed");
                        break;
                case STATE_MESSAGE_RECEIVED:
                        byte[] readBuff = (byte[]) message.obj;
                        String tempMsg = new String(readBuff,0,message.arg1);
                        message_box.setText(tempMsg);
                        break;
            }
            return true;
        }

    });

    private class ClientClass extends Thread{

        private BluetoothDevice device;
        private BluetoothSocket socket;

        public  ClientClass (BluetoothDevice device1){
            device=device1;
            try {
                socket=device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            try {
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                SendReceive sendReceive= new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket=myBluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        public  void  run(){
            BluetoothSocket socket=null;

            while (socket==null){
                try {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket=serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if (socket!=null){
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);

                    SendReceive sendReceive= new SendReceive(socket);
                    sendReceive.start();

                    break;

                }
            }

        }


    }


}