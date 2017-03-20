package app.radiouser.gstavrinos.radio_user_app;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import geometry_msgs.PoseStamped;
import move_base_msgs.MoveBaseActionGoal;


public class GoalPublisher extends AbstractNodeMain implements NodeMain {

  public boolean new_goal = false;
  private String goal_topic = "move_base_simple/goal";
  private String robot_frame = "base_link";
  public double x,y,z = 0;

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
          goal_msgs.getHeader().setFrameId(robot_frame);
          goal_msgs.getHeader().setStamp(connectedNode.getCurrentTime());
          goal_msgs.getHeader().setStamp(connectedNode.getCurrentTime());
          //goal_msgs.getGoalId().setId("Android_Goal_Publisher_" + connectedNode.getCurrentTime().toString());
          goal_msgs.getPose().getPosition().setX(x);
          goal_msgs.getPose().getPosition().setY(y);
          goal_msgs.getPose().getOrientation().setZ(z);
          publisher.publish(goal_msgs);
          publisher.publish(goal_msgs);
          new_goal = false;
          new_goal = false;
        }

        Thread.sleep(1000);
      }
    };
    connectedNode.executeCancellableLoop(loop);
  }

}
