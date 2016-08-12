import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Ihor on 18.07.2016.
 */
public class MyFrame extends JFrame {

    private String title;
    private Dimension d;
    private JButton addTaskButton = new JButton("Додати завдання");
    private JButton deleteTaskButton = new JButton("Видалити завдання");
    private JButton showButton = new JButton("Показати історію завдань");
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JPanel panel3 = new JPanel();
    private JLabel label = new JLabel("Список завданнь");
    private MyTableModel mtb = new MyTableModel();
    private JTable table = new JTable(mtb);
    private JScrollPane scrollPane = new JScrollPane(table);


    //
    private ConnectDB con = new ConnectDB();
    private Statement statement = con.getConnection().createStatement();
    ResultSet rs = statement.executeQuery("SELECT * FROM task");

    //
    public MyFrame(String title, Dimension d) throws SQLException {
        this.title = title;
        this.d = d;
    }

    public void init() throws ClassNotFoundException, SQLException {

        setTitle(title);
        setSize(d);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        addTaskButton.addActionListener(new AddTaskActionListener());
        showButton.addActionListener(new ShowTaskActionListener());
        deleteTaskButton.addActionListener(new DeleteTaskActionListener());
        setVisible(true);
        setResizable(true);
        mtb.setDataSource(rs);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);
        panel1.setLayout(new GridLayout(1, 1));
        panel2.setLayout(new GridLayout(1, 0));
        panel3.setLayout(new GridLayout(1, 3));
        panel1.add(label);
        panel2.add(scrollPane);
        panel3.add(addTaskButton);
        panel3.add(showButton);
        panel3.add(deleteTaskButton);
        //**/*/*/*/***///table.getSelectedRow();

        pack();



    }
    //action listeners
    public class AddTaskActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame addTaskWindow = new JFrame("Додати завдання");
            addTaskWindow.setLocationRelativeTo(panel1);
            addTaskWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addTaskWindow.setSize(350, 120);

            addTaskWindow.setVisible(true);
            JLabel label = new JLabel("Введіть завдання");
            JTextField textField1 = new JTextField();
            JButton button = new JButton("Додати");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String s = textField1.getText();
                        String insertSQL = "INSERT INTO TASK " + "VALUES('"+ s + "')";
                        statement.execute(insertSQL);
                        //
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM task");
                        try {
                            mtb.setDataSource(resultSet);
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        mtb.fireTableDataChanged(); //оновлює дані після редагування
                        //
                        addTaskWindow.setVisible(false);

                    } catch (SQLException e1) {
                        e1.printStackTrace();

                    }
                }
            });
            JButton button2 = new JButton("Відміна");
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addTaskWindow.setVisible(false);
                }
            });
            addTaskWindow.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            JPanel panel4 = new JPanel();
            addTaskWindow.add(panel, BorderLayout.NORTH);
            addTaskWindow.add(panel4, BorderLayout.SOUTH);
            addTaskWindow.setResizable(false);
            panel.setLayout(new GridLayout(2, 2));
            panel.add(label);
            panel.add(textField1);
            panel4.add(button);
            panel4.add(button2);
        //    addTaskWindow.pack();
        }
    }
    public class ShowTaskActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            MyTableModel mtb2 = new MyTableModel();
            ResultSet resultSet = null;
            try {
                resultSet = statement.executeQuery("SELECT * FROM deleted");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                mtb2.setDataSource(resultSet);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            JFrame showTaskWindow = new JFrame("Історія завдань");
            showTaskWindow.setVisible(true);
            showTaskWindow.setLocationRelativeTo(panel1);
            JLabel label = new JLabel("Історія завдань");
            JTable table1 = new JTable(mtb2);
            JScrollPane jScrollPane = new JScrollPane(table1);
            JButton button = new JButton("Закрити");
            JPanel panel = new JPanel(new BorderLayout());
            showTaskWindow.add(panel);
            panel.add(label, BorderLayout.NORTH);
            panel.add(jScrollPane, BorderLayout.CENTER);
            panel.add(button, BorderLayout.SOUTH);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showTaskWindow.setVisible(false);
                }
            });
            showTaskWindow.pack();

        }

    }
    public class DeleteTaskActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = table.getSelectedRow();
            int j = table.getSelectedColumn();
            TableModel model = new MyTableModel();
            if( i >= 0) {
                model = table.getModel();
                Object s = model.getValueAt(i, j);
                String SQL = "INSERT INTO deleted " + "VALUES('"+ s + "')";
                mtb.removeRow(i);
                try {
                    statement.execute(SQL);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            } else {
              JFrame frame = new JFrame("Error");
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Виберіть завдання, яке хочете видалити!");
                frame.setVisible(true);
                frame.setSize(new Dimension(300, 100));
                frame.setResizable(false);
                frame.setLocationRelativeTo(panel2);
                frame.add(panel);
                panel.add(label);

            }

        }
    }
    }
