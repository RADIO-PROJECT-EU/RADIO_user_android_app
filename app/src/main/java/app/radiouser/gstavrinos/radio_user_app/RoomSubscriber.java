package app.radiouser.gstavrinos.radio_user_app;

import org.ros.message.MessageListener;
import org.ros.node.topic.Subscriber;
import org.ros.node.AbstractNodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import std_msgs.String;


public class RoomSubscriber extends AbstractNodeMain implements NodeMain {

  public java.lang.String rooms_topic = "radio_home_rooms";
  public java.lang.String rooms = "";

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("RoomSubscriber");
  }

  @Override
  public void onStart(final ConnectedNode connectedNode) {
    final Subscriber<String> subscriber = connectedNode.newSubscriber(rooms_topic, "std_msgs/String");
    subscriber.addMessageListener(new MessageListener<String>() {
      @Override
      public void onNewMessage(String s) {
        rooms = s.getData();
      }
    });

  }

}
