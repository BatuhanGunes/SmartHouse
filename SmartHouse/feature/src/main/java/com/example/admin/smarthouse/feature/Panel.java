package com.example.admin.smarthouse.feature;

import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.icu.lang.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.util.Date;

public class Panel extends Activity {

    //Widgets
    ImageButton Led1, Led2, Led3;
    Button Disconnect, btnRefresh;
    CheckBox chkAlarm;
    EditText txtTime;
    EditText txtDate;
    EditText Heat, Humi, Metan;

    //Bluetooth Değişkenleri
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    int readBufferPosition;
    byte[] readBuffer;
    String sGelenVeri;


    //Zaman değişkenleri
    Date Now = new Date();
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    final Calendar takvim = Calendar.getInstance();
    int hour = takvim.get(Calendar.HOUR_OF_DAY);
    int minute = takvim.get(Calendar.MINUTE);

    //Gelen Datayı okumak için gerekli değişkenler.
    boolean stopThread;
    private InputStream inputStream;

    //Led yakıp söndürmek için gereken değişkenler yani veri göndermek için gerekli değişkenler
    boolean Led1Flag = false;
    boolean Led2Flag = false;
    boolean Led3Flag = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel);

        Intent newint = getIntent();    // btKontrol sınıfından bağlanılan bluetooth cihazının bilgilerini alıyoruz.
        address = newint.getStringExtra(btKontrol.EXTRA_ADDRESS); //receive the address of the bluetooth device




        Led1 = (ImageButton)findViewById(R.id.Led1);
        Led2 = (ImageButton)findViewById(R.id.Led2);
        Led3 = (ImageButton)findViewById(R.id.Led3);
        chkAlarm = (CheckBox)findViewById(R.id.chkAlarm);
        Heat = (EditText)findViewById(R.id.Heat);
        Humi = (EditText)findViewById(R.id.Humi);
        Metan = (EditText)findViewById(R.id.Metan);

        txtTime = (EditText)findViewById(R.id.txtTime);
        txtDate = (EditText) findViewById(R.id.txtDate);
        Disconnect = (Button)findViewById(R.id.Disconnect);
        btnRefresh = (Button)findViewById(R.id.btnRefresh);


        txtTime.setText(hour + " : " + minute);
        txtDate.setText(df.format(Now));


        new ConnectBT().execute(); //Bluetoot cihazına bağlanmak için gerekli methodu çağırıyoruz.


     chkAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                             @Override
                                             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                                 if (btSocket != null) {
                                                     try {
                                                         if (chkAlarm.isChecked()) {
                                                             String AlarmOnData = "9";
                                                             btSocket.getOutputStream().write(AlarmOnData.toString().getBytes());
                                                             msg("Alarm Açıldı.");
                                                         } else {
                                                             String AlarmOffData = "8";
                                                             btSocket.getOutputStream().write(AlarmOffData.toString().getBytes());
                                                             msg("Alarm Kapatıldı.");
                                                         }
                                                     } catch (IOException e) {
                                                         msg("Error");
                                                     }
                                                 }
                                             }
                                         });


        Disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Disconnect();
                msg("Bağlantı Kesildi.");
                Intent i = new Intent(Panel.this, MainActivity.class);
                startActivity(i);
            }
        });

        Led1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Led1Flag == false){
                    turnOnLed("1");
                    msg("Led 1 Yakıldı.");
                    Led1Flag = true;    // Ledin yaktığımız için kontrolü true yapıyoruz
                }
                else{
                    turnOffLed("2");
                    msg("Led 1 Söndürüldü");
                    Led1Flag = false;
                }
            }
        });

        Led2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Led2Flag == false){
                    turnOnLed("3");
                    msg("Led 2 Yakıldı");
                    Led2Flag = true;
                }
                else{
                    turnOffLed("4");
                    msg("Led 2 Söndürüldü");
                    Led2Flag = false;
                }
            }
        });

        Led3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Led3Flag == false){
                    turnOnLed("5");
                    msg("Led 3 Yakıldı.");
                    Led3Flag = true;
                }
                else{
                    turnOffLed("6");
                    msg("Led 3 Söndürüldü");
                    Led3Flag = false;
                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRead();
                msg("Veriler Yenilendi");
            }
        });

        //DataRead();

    }

    //Uygulama çalışrken mesagebox oluşturmak için gerekli method
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }




    private void turnOffLed(String LedOffParemeter)
    {
        if (btSocket!=null)
        {
            try
            {
                //GetOutputStream ile ardiuno bluetooth aracılığla veri gönderiyoruz.Ledi södürmek için
                btSocket.getOutputStream().write(LedOffParemeter.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed(String LedOnParemeter)
    {
        if (btSocket!=null)
        {
            try
            {
                //GetOutputStream ile ardiuno bluetooth aracılığla veri gönderiyoruz.Ledi Yakmak için
                btSocket.getOutputStream().write(LedOnParemeter.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }


    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        //finish(); //return to the first layout

    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Panel.this, "Bağlantı Kuruluyor...",
                    "Lütfen Bekleyiniz!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown            , the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();                                //get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);                //connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);          //create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                    inputStream = btSocket.getInputStream();
                    //DataRead();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)                                                      //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Bağlantı başarısız oldu. Lütfen tekrar deneyiniz.");
                finish();
            }
            else
            {
                msg("Bağlatı başarılı bir şekilde kuruldu.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    // Gelen Verileri okuyoruz.
    private void DataRead()
    {

        final Handler handler = new Handler();
        //final byte delimiter = 10; //This is the ASCII code for a newline character
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        stopThread = false;
        final Thread thread  = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {

                        int byteCount = inputStream.available();
                        if (byteCount > 0) {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            for (int i = 0; i < byteCount; i++) {
                                byte b = rawBytes[i];
                                //if (b == delimiter) {
                                final byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(rawBytes, "US-ASCII");
                                readBufferPosition = 0;

                                handler.post(new Runnable() {
                                    public void run() {

                                        sGelenVeri = data.toString();

                                        final String[] itemBol = sGelenVeri.split("-");

                                            //if (itemBol[3] != null) {

                                            //Distance.setText(itemBol[3].toString());


                                        for(int i = 0; i<=2;i++){
                                            Humi.setText("\n" + itemBol[0] + "\n");
                                            Heat.setText("\n" + itemBol[1] + "\n");
                                            Metan.setText("\n" + itemBol[2] + "\n");

                                            stopThread = true;
                                        }



                                        //}
                                        /*
                                        else {
                                            Humi.setText(itemBol[0]);
                                            Heat.setText(itemBol[1]);
                                            Metan.setText(itemBol[2]);
                                            Distance.setText(itemBol[3]);
                                        }
                                        */
                                    }
                                });
                            }
                        }
                    } catch (IOException ex) {
                        stopThread = true;
                    }
                }
            }
        });
        thread.start();
    }
}
