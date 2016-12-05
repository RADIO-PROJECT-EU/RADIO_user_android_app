package app.radiouser.gstavrinos.radio_user_app;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import std_msgs.Int32;

public class GoalPublisher extends AbstractNodeMain implements NodeMain {

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("GoalPublisher");
  }

  //TODO send an int for now. We will publish the goal here!
  @Override
  public void onStart(ConnectedNode connectedNode) {
    final Publisher<Int32> publisher = connectedNode.newPublisher(GraphName.of("test_int"), std_msgs.Int32._TYPE);

    final CancellableLoop loop = new CancellableLoop() {
      @Override
      protected void loop() throws InterruptedException {

        std_msgs.Int32 stop = publisher.newMessage();
        stop.setData(1);
        publisher.publish(stop);

        Thread.sleep(500);
      }
    };
    connectedNode.executeCancellableLoop(loop);
  }

}
