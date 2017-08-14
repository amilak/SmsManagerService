package com.ap.SMPP;

import com.ap.SuperMessageSender;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by rukman on 7/29/2017.
 */
@WebService(name = "SMSService", targetNamespace = "http://sms.ap.com")
public class ServiceExcecutorSMPP
{
    public static SMPPSession session;
    //    private int maxLength = 150;

    public static void main( String args[] )
    {
        initService();
    }

    @PostConstruct
    public void init()
    {
        initService();
    }

    private static void initService ()
    {
        System.out.println("%%%% SERVICE INIT %%%%%%%%%");
        ConfigReaderSMPP conf = new ConfigReaderSMPP();
        conf.loadConfigurations();
        ServiceExcecutorSMPP service = new ServiceExcecutorSMPP();
        service.createSession();
        mainExecutor();
    }

    public  void createSession()
    {

        Vector<String> ipList = new Vector();
        String smpp_ips = ConfigReaderSMPP.prop.get( "SMPP_IP" );
        ipList.addAll( Arrays.asList( smpp_ips.split( "," ) ) );
        String smpp_port = ConfigReaderSMPP.prop.get( "SMPP_PORT" );
        String smpp_user = ConfigReaderSMPP.prop.get( "SMPP_USER" );
        String smpp_password = ConfigReaderSMPP.prop.get( "SMPP_PASSWORD" );
        System.out.println( smpp_ips + "  " + smpp_port + "  " + smpp_user + "  " + smpp_password );
        //        maxLength = Integer.parseInt( ConfigReaderSMPP.prop.get( "MAX_LENGTH" ) );
        session = new SMPPSession();
        session.setMessageReceiverListener(new Receive(session));
        for( String ip : ipList )
        {
            try
            {
                String systemId = session.connectAndBind( ip, Integer.parseInt( smpp_port ), new BindParameter( BindType.BIND_TRX, smpp_user, smpp_password, "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null ) );
                session.addSessionStateListener( new MySessionStateListener() );
                System.out.println("Session Created ip "+ip+ "  Port "+ smpp_port );
                break;
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }

    }

    public void reconnectAfter( final long timeInMillis )
    {

        new Thread()
        {
            @Override public void run()
            {
                System.out.println( "Schedule reconnect after " + timeInMillis + " milliseconds" );
                try
                {
                    Thread.sleep( timeInMillis );
                }
                catch( InterruptedException e )
                {
                    e.printStackTrace();
                }

                int attempt = 0;
                while( session == null || session.getSessionState().equals( SessionState.CLOSED ) )
                {
                    try
                    {
                        System.out.println( "Reconnecting attempt #" + ( ++attempt ) + "..." );
                        createSession();
                    }
                    catch( Exception e )
                    {
                        e.printStackTrace();
                        // wait for a second
                        try
                        {
                            Thread.sleep( 5000 );
                        }
                        catch( InterruptedException ee )
                        {
                            ee.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    private class MySessionStateListener implements SessionStateListener
    {

        @Override public void onStateChange( SessionState newState, SessionState oldState, Session source )
        {
            if( newState.equals( SessionState.OPEN ) )
            {
                System.out.println( "TCP connection established with SMSC at address " );
            }
            if( newState.equals( SessionState.BOUND_TRX ) )
            {
                System.out.println( "SMPP Transceiver connection established with SMSC at address " );
            }
            if( newState.equals( SessionState.CLOSED ) || newState.equals( SessionState.UNBOUND ) )
            {

                System.out.println( "Connection closed, either by SMSC or there is network problem" );
                if( newState.equals( SessionState.CLOSED ) )
                {
                    System.out.println( "Connection closed" );
                }
                else
                {
                    System.out.println( "Connection unbound" );
                }
                System.out.println( "Reconnecting......." );
                reconnectAfter( 5000 );
            }
        }

    }

    public static void mainExecutor()
    {
        /*Thread readerThread = new Thread( new MassageReader() );
        readerThread.start();*/

        Thread senderThread = new Thread( new SuperMessageSender() );
        senderThread.start();
    }
}
