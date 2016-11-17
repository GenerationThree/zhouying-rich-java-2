public class Player {
    private Status status;
    private Command lastExecuted;

    public Status getStatus() {
        return status;
    }

    public Player.Status execute(Command command) {
        status = command.execute(this);
        lastExecuted = command;
        return status;
    }

    public boolean setStatus(Status status) {
        this.status = status;
        return true;
    }

    public Status respond(Response response) {
        status = lastExecuted.respondWith(response);
        return status;
    }

    public enum Status {WAIT_FOR_RESPONSE, END_TURN, WAIT_FOR_COMMAND}
}
