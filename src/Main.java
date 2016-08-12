import java.awt.*;
import java.sql.SQLException;

/**
 * Created by Ihor on 18.07.2016.
 */
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
    MyFrame frame = new MyFrame("Todoshka", new Dimension(250, 250));
        frame.init();
    }
}
