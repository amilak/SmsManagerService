package test;

import com.ap.SuperMessageSender;

/**
 * Created by Asus PC on 10/2/2015.
 */
public class SenderTest {


    public static void main(String ar[])
    {
        Thread senderThread = new Thread(new SuperMessageSender());
        senderThread.start();
    }
}
