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
    private JButton addTaskButton = new JButton("Add");
    private JButton deleteTaskButton = new JButton("Delete");
    private JButton showButton = new JButton("History");
    private JButton doneButton = new JButton("Done");
    private JPanel panel2 = new JPanel();
    private JPanel panel3 = new JPanel();
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
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);
        panel2.setLayout(new GridLayout(1, 0));
        panel3.setLayout(new GridLayout(1, 4));
        panel2.add(scrollPane);
        panel3.add(addTaskButton);
        panel3.add(showButton);
        panel3.add(doneButton);
        panel3.add(deleteTaskButton);
        pack();


    }
    //action listeners
    public class AddTaskActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame addTaskWindow = new JFrame("Add");
            addTaskWindow.setLocationRelativeTo(panel2);
            addTaskWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addTaskWindow.setSize(350, 120);

            addTaskWindow.setVisible(true);
            JLabel label = new JLabel("Enter task");
            JTextField textField1 = new JTextField();
            JButton button = new JButton("Add");
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
            JButton button2 = new JButton("Cancel");
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
            table.clearSelection();
        }
    }
    public class ShowTaskActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            MyTableModel mtb2 = new MyTableModel();
            ResultSet resultSet = null;
            try {
                resultSet = statement.executeQuery("SELECT * FROM done");
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
            JFrame showTaskWindow = new JFrame("Todoshka");
            showTaskWindow.setVisible(true);
            showTaskWindow.setLocationRelativeTo(panel2);
            JTable table1 = new JTable(mtb2);
            JScrollPane jScrollPane = new JScrollPane(table1);
            JButton button = new JButton("Close");
            JPanel panel = new JPanel(new BorderLayout());
            showTaskWindow.add(panel);
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
                String SQL = "DELETE FROM task WHERE list ='" + s + "'";
                mtb.removeRow(i);
                try {
                    statement.execute(SQL);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            } else {
              JFrame frame = new JFrame("Error");
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Select the task, which you want to delete");
                frame.setVisible(true);
                frame.setSize(new Dimension(300, 100));
                frame.setResizable(false);
                frame.setLocationRelativeTo(panel2);
                frame.add(panel);
                panel.add(label);

            }

        }
    }
    public class DoneTaskActionlistener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = table.getSelectedRow();
            int j = table.getSelectedColumn();
            TableModel model = new MyTableModel();
                model = table.getModel();
                Object s = model.getValueAt(i, j);
                String SQL = "INSERT INTO done " + "VALUES('"+ s + "')";
                mtb.removeRow(i);
                try {
                    statement.execute(SQL);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
        }
    }
    }
