public interface Command {
    Player.Status execute(Player player);

    Player.Status respondWith(Response response);
}
