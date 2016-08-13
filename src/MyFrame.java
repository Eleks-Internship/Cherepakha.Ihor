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
        doneButton.addActionListener(new DoneTaskActionlistener());
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
    public class AddTaskActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame addTaskWindow = new JFrame("Add");
            addTaskWindow.setLocationRelativeTo(null);
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
                        String insertSQL = "INSERT INTO TASK " + "VALUES('" + s + "')";
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
        }
    }

    public class ShowTaskActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MyTableModel mtb2 = new MyTableModel();
            ResultSet resultSet = null;
            try {
                resultSet = statement.executeQuery("SELECT * FROM done");
                mtb2.setDataSource(resultSet);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            JFrame showTaskWindow = new JFrame("Todoshka");
            showTaskWindow.setVisible(true);
            showTaskWindow.setLocationRelativeTo(null);
            JTable table1 = new JTable(mtb2);
            JScrollPane jScrollPane = new JScrollPane(table1);
            JButton button = new JButton("Close");
            JButton button2 = new JButton("Clear");
            JPanel panel = new JPanel(new BorderLayout());
            JPanel panel1 = new JPanel();
            showTaskWindow.add(panel);
            panel.add(jScrollPane, BorderLayout.CENTER);
            panel.add(panel1, BorderLayout.SOUTH);
            panel1.add(button2);
            panel1.add(button);
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame jFrame = new JFrame("Done");
                    JPanel jPanel = new JPanel(new BorderLayout());
                    JButton jButton = new JButton("OK");
                    JLabel jLabel = new JLabel("All tasks are deleted");
                    jFrame.setSize(250, 100);
                    jFrame.setResizable(false);
                    jFrame.setLocationRelativeTo(null);
                    jFrame.setVisible(true);
                    jFrame.add(jPanel);
                    JPanel jPanel1 = new JPanel();
                    JPanel jPanel2 = new JPanel();
                    jPanel.add(jPanel1, BorderLayout.NORTH);
                    jPanel.add(jPanel2, BorderLayout.SOUTH);
                    jPanel1.add(jLabel);
                    jPanel2.add(jButton);
                    jButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            showTaskWindow.setVisible(false);
                            jFrame.setVisible(false);
                        }
                    });

                    ResultSet resultSet1 = null;
                    try {
                        resultSet1 = statement.executeQuery("DELETE FROM done");
                    } catch (SQLException e1) {
                        e1.getMessage();
                    }

                }
            });
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showTaskWindow.setVisible(false);
                }
            });
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            showTaskWindow.pack();

        }

    }

    public class DeleteTaskActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = table.getSelectedRow();
            int j = table.getSelectedColumn();
            TableModel model = new MyTableModel();
            if (i >= 0) {
                model = table.getModel();
                Object s = model.getValueAt(i, j);
                String sql = "DELETE FROM task WHERE list ='" + s + "'";
                mtb.removeRow(i);
                try {
                    statement.execute(sql);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            } else {
                JFrame frame = new JFrame("Error");
                frame.setLayout(new BorderLayout());
                JPanel panel = new JPanel();
                JPanel panel1 = new JPanel();
                JButton button = new JButton("OK");
                frame.add(panel, BorderLayout.NORTH);
                frame.add(panel1, BorderLayout.SOUTH);
                JLabel label = new JLabel("Please, select the task, which you want to delete");
                frame.setVisible(true);
                frame.setSize(new Dimension(300, 100));
                frame.setResizable(false);
                frame.setLocationRelativeTo(panel2);
                panel.add(label);
                panel1.add(button);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                    }
                });

            }

        }
    }

    public class DoneTaskActionlistener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = table.getSelectedRow();
            int j = table.getSelectedColumn();
            TableModel model = new MyTableModel();
            if (i >= 0) {
                model = table.getModel();
                Object s = model.getValueAt(i, j);
                String sql = "INSERT INTO done " + "VALUES('" + s + "')";
                String sql1 = "DELETE FROM task WHERE list ='" + s + "'";
                mtb.removeRow(i);
                try {
                    statement.execute(sql);
                    statement.execute(sql1);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            } else {
                JFrame frame = new JFrame("Error");
                frame.setLayout(new BorderLayout());
                JPanel panel = new JPanel();
                JPanel panel1 = new JPanel();
                JButton button = new JButton("OK");
                frame.add(panel, BorderLayout.NORTH);
                frame.add(panel1, BorderLayout.SOUTH);
                JLabel label = new JLabel("Please, select the completed task");
                frame.setVisible(true);
                frame.setSize(new Dimension(300, 100));
                frame.setResizable(false);
                frame.setLocationRelativeTo(panel2);
                panel.add(label);
                panel1.add(button);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                    }
                });

            }
        }
    }
}
