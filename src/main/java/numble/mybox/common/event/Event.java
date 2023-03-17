package numble.mybox.common.event;

public abstract class Event {

  private Long timestamp;

  public Event() {
    this.timestamp = System.currentTimeMillis();
  }

  public long getTimeStamp() {
    return timestamp;
  }

}
