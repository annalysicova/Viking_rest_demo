package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.Viking;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VikingTableModel extends AbstractTableModel {

    private final String[] columns = {"Id", "Name", "Age", "Height (cm)", "Hair color", "Beard style", "Equipment"};
    private final List<Viking> data = new ArrayList<>();

    public void addViking(Viking viking) {
        int row = data.size();
        data.add(viking);
        fireTableRowsInserted(row, row);
    }

    public void addOrUpdateViking(Viking viking) {
        for (int i = 0; i < data.size(); i++) {
            Viking current = data.get(i);
            if (current.id() != null && current.id().equals(viking.id())) {
                data.set(i, viking);
                fireTableRowsUpdated(i, i);
                return;
            }
        }
        addViking(viking);
    }

    public void removeVikingById(int id) {
        for (int i = 0; i < data.size(); i++) {
            Viking current = data.get(i);
            if (current.id() != null && current.id() == id) {
                data.remove(i);
                fireTableRowsDeleted(i, i);
                return;
            }
        }
    }

    public void setVikings(List<Viking> vikings) {
        data.clear();
        data.addAll(vikings);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Viking viking = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> viking.id();
            case 1 -> viking.name();
            case 2 -> viking.age();
            case 3 -> viking.heightCm();
            case 4 -> viking.hairColor();
            case 5 -> viking.beardStyle();
            case 6 -> formatEquipment(viking.equipment());
            default -> "";
        };
    }

    private String formatEquipment(List<EquipmentItem> equipment) {
        if (equipment == null || equipment.isEmpty()) {
            return "";
        }
        return equipment.stream()
                .map(item -> item.name() + " [" + item.quality() + "]")
                .collect(Collectors.joining(", "));
    }
}
