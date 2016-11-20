package rich_new;

import org.junit.Test;

import java.util.Iterator;

public class Player3Test {

    interface Command {

    }

    class Player implements Iterable<rich.command.Command> {

        @Override
        public Iterator<rich.command.Command> iterator() {
            return null;
        }
    }

    @Test
    public void should() {

    }
}
