package com.ap.SMPP;

import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.MessageType;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;

/**
 * Created by rukman on 7/27/2017.
 */
public class Receive implements MessageReceiverListener
{

    public Receive(){
        super();
    }

    public Receive(Session s){
        this();
    }
    public DataSmResult onAcceptDataSm(DataSm arg0, Session arg1)
            throws ProcessRequestException
    {

        return null;
    }


    public void onAcceptAlertNotification(AlertNotification arg0) {


    }

    public void onAcceptDeliverSm(DeliverSm arg0)
            throws ProcessRequestException {


        if( MessageType.SMSC_DEL_RECEIPT.containedIn(arg0.getEsmClass())){

        }else{
            System.out.println(arg0.getSourceAddr());


             Thread readerThread = new Thread( new MessageReaderSMPP(arg0) );
              readerThread.start();
            //thread = new Thread(new ProcessSMS(arg0));
            //thread.start();
        }

    }

}
