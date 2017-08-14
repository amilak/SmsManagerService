package com.ap.SMPP;

import com.ap.MessageUtills;
import org.jsmpp.bean.DeliverSm;
import java.util.Date;

/**
 * Created by rukman on 7/24/2017.
 */
public class MessageReaderSMPP implements Runnable
{
    private  DeliverSm message;
    public MessageReaderSMPP(DeliverSm arg0)
    {
       message = arg0;
    }



    @Override public void run()
    {
        System.out.println(" Starting Reader thread");
        String sms = new String( this.message.getShortMessage() );
        String sourceAddr = message.getSourceAddr();
        Date date  = new Date(  );
        if ( sms != null && sms.trim().length()>0)
        {
            MessageUtills.processMassage( sms, date, sourceAddr );
            //                        System.out.println(" Update msg " + message);
        }

//        message.getd
    }
}
