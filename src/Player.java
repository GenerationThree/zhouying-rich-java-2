import com.sun.tools.javac.util.Pair;

public class Player {
    private Status status;
    private Command lastExecuted;

    public Status getStatus() {
        return status;
    }

    public Pair<Status, MessageType> execute(Command command) {
        Pair<Status, MessageType> result = command.execute(this);
        lastExecuted = command;
        status = result.fst;
        return result;
    }

    public boolean setStatus(Status status) {
        this.status = status;
        return true;
    }

    public Pair<Status, MessageType> respond(Response response) {
        Pair<Status, MessageType> result = lastExecuted.respondWith(response);
        status = result.fst;
        return result;
    }

    public enum Status {WAIT_FOR_RESPONSE, END_TURN, WAIT_FOR_COMMAND}
}
