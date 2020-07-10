package in.rajpusht.pc.utils.event_bus;

public class MessageEvent {
    private int eventType;
    private String id;

    private MessageEvent(int eventType, String mileStoneId) {
        this.eventType = eventType;
        this.id = mileStoneId;
    }

    private MessageEvent(int eventType) {
        this.eventType = eventType;
        id = "";

    }

    public static MessageEvent getMessageEvent(int eventType) {
        return new MessageEvent(eventType);
    }

    public static MessageEvent getMessageEvent(int eventType, String mileStoneId) {
        return new MessageEvent(eventType, mileStoneId);
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "eventType=" + eventType +
                ", mileStoneId='" + id + '\'' +
                '}';
    }

    public interface MessageEventType {
        int SYNC_SUCCESS = 0;
    }
}
