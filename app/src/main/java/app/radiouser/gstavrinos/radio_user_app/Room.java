package app.radiouser.gstavrinos.radio_user_app;

/**
 * Created by gstavrinos on 3/6/17.
 */

public class Room {

  private String name;
  private float x;
  private float y;
  private float z;
  private String image;

  public Room(String name, float x, float y, float z, String image){
    this.name = name;
    this.x = x;
    this.y = y;
    this.z = z;
    this.image = image;
  }

  public String getName(){
    return name;
  }

  public float getX(){
    return x;
  }

  public float getY(){
    return y;
  }

  public float getZ(){
    return z;
  }

  public String getImage(){
    return image;
  }



  public void setName(String n){
    name = n;
  }

  public void setX(float x){
    this.x = x;
  }

  public void setY(float y){
    this.y = y;
  }

  public void setZ(float z){
    this.z = z;
  }

  public void getImage(String i){
    image = i;
  }

  // For debugging
  public String getRoom(){
    return "Room name: "+name+"\nX: "+x+"\nY: "+y+"\nZ: "+z+"\nImage: "+image;
  }

}
