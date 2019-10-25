package cf.nhattruong.ehugo.Models;

public class Message {
    private String msg;
    private boolean check;

    public Message(String msg, boolean check) {
        this.msg = msg;
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
