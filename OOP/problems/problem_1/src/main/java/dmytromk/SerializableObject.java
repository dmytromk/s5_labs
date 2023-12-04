package dmytromk;

import java.io.Serializable;

public class SerializableObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;

    public SerializableObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SerializableObject{" +
                "message='" + message + '\'' +
                '}';
    }
}
