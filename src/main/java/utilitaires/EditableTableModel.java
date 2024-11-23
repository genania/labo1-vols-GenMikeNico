package utilitaires;

import javax.swing.table.DefaultTableModel;

// return false on ne peut pas modifier, return true, on peut modifier
public class EditableTableModel extends DefaultTableModel {
    private boolean estModifiable = false;

    public EditableTableModel(Object[] columnNames, int rowCount){
        super (columnNames, rowCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return estModifiable;
    }

  public void setEstModifiable(boolean estModifiable) {
    this.estModifiable = estModifiable;
  }
}


