package app.radiouser.gstavrinos.radio_user_app;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.AbstractNodeMain;
import org.ros.node.topic.Publisher;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import geometry_msgs.PoseStamped;
import org.ros.node.NodeMain;


public class GoalPublisher extends AbstractNodeMain implements NodeMain {

  public boolean new_goal = false;
  private String goal_topic = "android_app/goal";
  private String robot_frame = "base_link";
  public double x, y, z, w = 0;

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("GoalPublisher");
  }

  @Override
  public void onStart(final ConnectedNode connectedNode) {
    final Publisher<PoseStamped> publisher = connectedNode.newPublisher(GraphName.of(goal_topic), PoseStamped._TYPE);
    new_goal = false;

    final CancellableLoop loop = new CancellableLoop() {
      @Override
      protected void loop() throws InterruptedException {

        if(new_goal) {
          geometry_msgs.PoseStamped goal_msgs = publisher.newMessage();
          goal_msgs.getHeader().setFrameId(robot_frame);
          goal_msgs.getHeader().setStamp(connectedNode.getCurrentTime());
          //goal_msgs.getGoalId().setId("Android_Goal_Publisher_" + connectedNode.getCurrentTime().toString());
          goal_msgs.getPose().getPosition().setX(x);
          goal_msgs.getPose().getPosition().setY(y);
          goal_msgs.getPose().getOrientation().setZ(z);
          goal_msgs.getPose().getOrientation().setW(w);
          publisher.publish(goal_msgs);
          new_goal = false;
        }

        Thread.sleep(1000);
      }
    };
    connectedNode.executeCancellableLoop(loop);
  }

}
