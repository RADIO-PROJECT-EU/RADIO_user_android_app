package app.radiouser.gstavrinos.radio_user_app;

import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.AbstractNodeMain;
import org.ros.node.topic.Publisher;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import std_msgs.Int32;
import org.ros.node.NodeMain;

import java.util.concurrent.TimeUnit;

/**
 * Created by gstavrinos on 11/8/17.
 */

public class IntPublisher extends AbstractNodeMain implements NodeMain {


    private String topic = "android_app/other";
    private int pub_i = 0;

    public IntPublisher(int i){
        super();
        pub_i = i;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("IntPublisher");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        final Publisher<Int32> publisher = connectedNode.newPublisher(GraphName.of(topic), Int32._TYPE);
        Int32 int_msg = publisher.newMessage();
        int_msg.setData(pub_i);
        try {
            // sleep before publishing, to make sure that the publisher is ready...
            TimeUnit.SECONDS.sleep(2);
            publisher.publish(int_msg);
        }
        catch(Exception e){
            // do nothing
        }
    }

}
