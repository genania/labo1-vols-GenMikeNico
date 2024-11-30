package utilitaires;

import javax.swing.table.DefaultTableModel;

import modele.DateVol;

// return false on ne peut pas modifier, return true, on peut modifier
public class EditableTableModel extends DefaultTableModel {
  private boolean estModifiable = false;

  public EditableTableModel(Object[] columnNames, int rowCount) {
    super(columnNames, rowCount);
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return estModifiable;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
      case 0:
      case 3:
        return Integer.class;

      case 2:
        return DateVol.class;

      default:
        return super.getColumnClass(columnIndex);
    }
  }

  public void setEstModifiable(boolean estModifiable) {
    this.estModifiable = estModifiable;
  }
}
