package com.ap;

import com.ap.SMPP.MessageSenderSMPP;
import common.MessageQueue;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Yasantha
 * Date: 8/9/15
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageSender implements Runnable
{
    private boolean finished = false;
    private ArrayList<MessageQueue> msgList;

    public MessageSender( ArrayList<MessageQueue> data )
    {
        msgList = data;
    }




    public static String sendSMSTest( String number, String message )
    {
        System.out.println( message );
        String msgStatus = "";


        return msgStatus;
    }


    private void updateMessageStatus( MessageQueue outMessage )
    {
        outMessage.update();
    }

    public boolean isFinished()
    {
        return finished;
    }


    public void run()
    {
        System.out.println( " Starting sender sub thread" );

        try
        {

            if( msgList.size() > 0 )
            {
                MessageSenderSMPP SMPPSender = new MessageSenderSMPP();
//                String session = SMPPSender.createSession();
//                if( session != null )
//                {
                    for( MessageQueue outQueue : msgList )
                    {
                        boolean sucess = SMPPSender.sendMessage( outQueue.getPhoneNumber(), outQueue.getMessage() );
                        if( sucess )
                        {
                            updateMessageStatus( outQueue );
                            System.out.println( " :::::::::::::::: Message send successfully " );
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                //                LOGGER.info("Interrupted exception", e);
                            }

                        }
                        else
                        {
                            System.out.println( " :::::::::::::::: Message Failed " );
//                            if(!SMPPSender.getSession().getSessionState().equals( SessionState.BOUND_TRX ))
//                            {
//                                SMPPSender.endSession();
////                                String session1 = SMPPSender.createSession();
//                                if(session1 == null)
//                                {
//                                    System.out.println( "-> session creation error. Session reconnect failed" );
//                                    break;
//                                }
//                            }
                        }
                    }

//                    if( SMPPSender.getSession()!=null )
//                    {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            //                LOGGER.info("Interrupted exception", e);
                        }
//                        System.out.println( "-> session Ending ::::::::::::::" );
//                        SMPPSender.endSession();
//                    }
//                }
//                else
//                {
//                    System.out.println( "-> session creation error" );
//                }


            }
            else
            {
                System.out.println( "-> Message loading error" );

            }
            finished = true;
        }
        catch( Exception e )
        {
            finished = true;
            e.printStackTrace();
        }

        finished = true;


    }

    public ArrayList<MessageQueue> getMsgList()
    {
        return msgList;
    }

    public void setMsgList( ArrayList<MessageQueue> msgList )
    {
        this.msgList = msgList;
    }
}
