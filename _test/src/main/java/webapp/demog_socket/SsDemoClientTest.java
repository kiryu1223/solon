package webapp.demog_socket;

import org.noear.solon.XApp;
import org.noear.solon.core.SocketMessage;
import org.smartboot.socket.transport.AioQuickClient;

public class SsDemoClientTest {
    public static void test() {
        try {
            Thread.sleep(1000);

            SsDemoProcessor processor = new SsDemoProcessor();

            int port = 20000 + XApp.global().port();
            AioQuickClient<SocketMessage> client = new AioQuickClient<>("localhost", port,new SsDemoProtocol(),processor);
            client.start();

            while (true){
                if(processor.session ==null){
                    Thread.sleep(100);
                }else{
                    break;
                }
            }

            processor.send("/demog/中文/1","Hello 世界!", msg->{
                System.out.println(msg.contentAsString());
            });


            Thread.sleep(100);

            processor.send("/demog/中文/2","Hello 世界2!", msg->{
                System.out.println(msg.contentAsString());
            });


            Thread.sleep(100);

            processor.send("/demog/中文/3","close", msg->{
                System.out.println(msg.contentAsString());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
