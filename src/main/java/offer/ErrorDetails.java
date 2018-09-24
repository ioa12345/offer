package offer;

import java.util.List;

public class ErrorDetails {
    private String tag;
    private List<String> message;

    public ErrorDetails() {
    }

    public ErrorDetails(String tag, List<String> message) {
        this.tag = tag;
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
