package org.kivio.event;

public class UpdateEvent {
    private String message;

    public UpdateEvent() {

    }

    public UpdateEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
