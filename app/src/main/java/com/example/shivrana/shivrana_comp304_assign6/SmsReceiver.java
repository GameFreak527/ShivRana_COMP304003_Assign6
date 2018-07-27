package com.example.shivrana.shivrana_comp304_assign6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        String str = "SMS from ";

        if(bundle!=null){
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            for (int i=0 ; i<messages.length; i++){
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                //TODO 1 uncomment If you dont want to show the number of the sender
                str = "";
                if (i==0){
                    //Getting the address of the Sender
                    //str += messages[i].getOriginatingAddress();
                    //str +=": ";
                }
                str += messages[i].getMessageBody().toString()+"\n";
            }
            //Display  the message
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            //send a broadcast intent to update the textfield in the layout
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("message",str);
            context.sendBroadcast(broadcastIntent);


        }
    }
}
