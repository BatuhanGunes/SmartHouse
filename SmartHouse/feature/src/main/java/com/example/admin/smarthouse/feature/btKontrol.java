package com.example.admin.smarthouse.feature;

import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.util.*;

public class btKontrol extends Activity {

    //widgets
    Button btnPaired;
    ListView devicelist;
    Button HomePage;

    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kontrolbt);

        //Widgets
        btnPaired = (Button)findViewById(R.id.Paireddevices);
        devicelist = (ListView)findViewById(R.id.ListView);
        HomePage = (Button)findViewById(R.id.ResultPage);

        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth cihazı bulunamadı.",
                    Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        }
        else if(!myBluetooth.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList();
            }
        });

        HomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // Cihazın önceden eşleşmiş bluetooth cihazlarını listeliyoruz.
    private void pairedDevicesList()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());                                      //Get the device's name and the address
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Eşleşmiş Bluetooth cihazı bulunamadı.",
                    Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter =
                new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener);                                        //Method called when the device from the list is clicked
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent i = new Intent(btKontrol.this, Panel.class);
            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); // bir sonraki activitye bluetooth cihazının adresini gönderiyoruz.
            startActivity(i);
        }
    };
}

