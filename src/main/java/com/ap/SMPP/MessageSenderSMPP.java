package com.ap.SMPP;

import com.ap.MessageUtills;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameters;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 * Created by rukman on 7/28/2017.
 */
public class MessageSenderSMPP
{
//    private SMPPSession session;
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();
    private int maxLength = 150;

 /*   public String createSession()
    {
        String systemId = null;

        Vector<String> ipList = new Vector<>();
        String smpp_ips = ConfigReaderSMPP.prop.get( "SMPP_IP" );
        ipList.addAll( Arrays.asList( smpp_ips.split( "," ) ) );
        String smpp_port = ConfigReaderSMPP.prop.get( "SMPP_PORT" );
        String smpp_user = ConfigReaderSMPP.prop.get( "SMPP_USER" );
        String smpp_password = ConfigReaderSMPP.prop.get( "SMPP_PASSWORD" );
        System.out.println(smpp_ips+"  "+ smpp_port+"  "+smpp_user+"  "+smpp_password+"  "+maxLength);
        maxLength = Integer.parseInt( ConfigReaderSMPP.prop.get( "MAX_LENGTH" ) );
        session = new SMPPSession();
        for( String ip : ipList )
        {
            try
            {
                systemId = session.connectAndBind( ip, Integer.parseInt( smpp_port ), new BindParameter( BindType.BIND_TRX, smpp_user, smpp_password, "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null ) );
                break;
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
        return systemId;
    }

    public void endSession()
    {
        session.unbindAndClose();
        System.out.println( "-> session ended *************************************" );
        session = null;
    }*/

    public boolean sendMessage( String number, String msg )
    {
        maxLength = Integer.parseInt( ConfigReaderSMPP.prop.get( "MAX_LENGTH" ) );

        boolean success = true;
        String internationalNumber = MessageUtills.getInternationalNumber( number );
        int length = msg.length();
        if( length < maxLength )
        {
            String messageId = "";
            try
            {

                final RegisteredDelivery registeredDelivery = new RegisteredDelivery();
                registeredDelivery.setSMSCDeliveryReceipt( SMSCDeliveryReceipt.DEFAULT );

                messageId = ServiceExcecutorSMPP.session.submitShortMessage( "CMT", TypeOfNumber.ALPHANUMERIC, NumberingPlanIndicator.UNKNOWN, "AsianPaints", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, internationalNumber, new ESMClass(), (byte) 0, (byte) 1, TIME_FORMATTER.format( new Date() ), null, registeredDelivery, (byte) 0, new GeneralDataCoding( Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false ), (byte) 0, msg.getBytes() );
                System.out.println( "Message submitted, message_id is {}" + messageId +"  "+internationalNumber);
            }
            catch( Exception e )
            {
                e.printStackTrace();
                success = false;
            }
        }
        else // multiple message for length more than 160
        {
            double segCount = Math.ceil( length / (double) maxLength );
            Random random = new Random();

            final int totalSegments = (int) segCount;
            OptionalParameter sarMsgRefNum = OptionalParameters.newSarMsgRefNum( (short) random.nextInt() );
            OptionalParameter sarTotalSegments = OptionalParameters.newSarTotalSegments( totalSegments );

            for( int i = 0; i < totalSegments; i++ )
            {
                final int seqNum = i + 1;
                String message = msg.substring( ( maxLength * ( i) ), ( length> (maxLength * (i+1) ))?(maxLength * (i+1) ):length );
                OptionalParameter sarSegmentSeqnum = OptionalParameters.newSarSegmentSeqnum( seqNum );
                String messageId = submitMessage(  message, sarMsgRefNum, sarSegmentSeqnum, sarTotalSegments, internationalNumber );
                if(messageId == null)
                {
                    success = false;
                    break;
                }
                System.out.println( "Message submitted, message_id is {}" + messageId );
                //              LOGGER.info("Message submitted, message_id is {}", messageId);
            }
        }
        return success;
    }

    public static String submitMessage(  String message, OptionalParameter sarMsgRefNum, OptionalParameter sarSegmentSeqnum, OptionalParameter sarTotalSegments, String internationalNumber )
    {
        String messageId = null;
        try
        {
            messageId = ServiceExcecutorSMPP.session.submitShortMessage( "CMT", TypeOfNumber.ALPHANUMERIC, NumberingPlanIndicator.UNKNOWN, "AsianPaints", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, internationalNumber, new ESMClass(), (byte) 0, (byte) 1, TIME_FORMATTER.format( new Date() ), null, new RegisteredDelivery( SMSCDeliveryReceipt.DEFAULT ), (byte) 0, new GeneralDataCoding( Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false ), (byte) 0, message.getBytes(), sarMsgRefNum, sarSegmentSeqnum, sarTotalSegments );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        return messageId;
    }

    public static void main( String at[] )
    {
        String message = "I have a java application which uses jsmpp library to send SMSs to SMSC. Application connects successfully and sends SMSs. Connection issue occurs after a week or so up time, during this up time it sends thousands of SMSs. But suddenly after few days application starts facing connection issues, some time 'Negative bind response 0x00045' and some time waiting bind response. When I check from wireshark, Application constantly sends enquire line packets and receives responses for them with status 'OK'. This means that application is connected but still it is attempting for new connection. Below is code for connection management.";
        int length = message.length();
        double aa = Math.ceil( length / (double) 150 );
        System.out.println( length );
       int totalSegments = (int)aa;
        for( int i = 0; i < totalSegments; i++ )
        {
            String msg  = message.substring( ( 150 * ( i) ), ( length> (150 * (i+1) ))?(150 * (i+1) ):length );
            System.out.println(msg);
        }

//        System.out.println( message.substring( ( 4 * ( 1 - 1 ) ), 4 * 1 ) );
//        System.out.println( message.substring( ( 4 * ( 2 - 1 ) ), 4 * 2 ) );
//        System.out.println( 310 / 150 );
//        System.out.println( (int) aa );
//        System.out.println( 310 % 150 );
    }

  /*  public SMPPSession getSession()
    {
        return session;
    }

    public void setSession( SMPPSession session )
    {
        this.session = session;
    }*/
}
