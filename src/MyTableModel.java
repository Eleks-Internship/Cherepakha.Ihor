import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel{

    private ArrayList<String> columnNames = new ArrayList<String>();
    private ArrayList<Class> columnTypes = new ArrayList<Class>();
    private ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();

    public int getColumnCount() {

        return columnNames.size();

    }

    public int getRowCount() {
        return data.size();
    }


    public Object getValueAt(int row, int col) {
        synchronized (data) {
            return data.get(row).get(col);
        }
    }

    public String getColumnName(int col) {
        return columnNames.get(col);
    }

    public Class getColumnClass(int col) {
        return columnTypes.get(col);
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object obj, int row, int col) {
        synchronized (data) {
            data.get(row).set(col, obj);
        }
    }
    public void removeRow(int row){
        data.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void setDataSource(ResultSet rs) throws SQLException, ClassNotFoundException {
        ResultSetMetaData rsmd = rs.getMetaData();
        columnNames.clear();
        columnTypes.clear();
        data.clear();

        int columnCount = rsmd.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            columnNames.add(rsmd.getColumnName(i + 1));
            Class type = Class.forName(rsmd.getColumnClassName(i + 1));
            columnTypes.add(type);
        }

        fireTableStructureChanged();

        while (rs.next()) {
            ArrayList rowData = new ArrayList();
            for (int i = 0; i < columnCount; i++) {
                if (columnTypes.get(i) == String.class)
                    rowData.add(rs.getString(i + 1));
                else
                    rowData.add(rs.getObject(i + 1));
            }

            synchronized (data) {
                data.add(rowData);
                this.fireTableRowsInserted(data.size() - 1, data.size() - 1);
            }
        }
    }
}