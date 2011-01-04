package net.bioclipse.uppmax.business;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Listener;

public class ColumnSortListener implements Listener {
	private Table table;
	
    public void handleEvent(Event e) {
      // sort column 1
      TableItem[] items = table.getItems();
      Collator collator = Collator.getInstance(Locale.getDefault());
      for (int i = 1; i < items.length; i++) {
        String value1 = items[i].getText(0);
        for (int j = 0; j < i; j++) {
          String value2 = items[j].getText(0);
          if (collator.compare(value1, value2) < 0) {
            String[] values = { items[i].getText(0),
                items[i].getText(1) };
            items[i].dispose();
            TableItem item = new TableItem(table, SWT.NONE, j);
            item.setText(values);
            items = table.getItems();
            break;
          }
        }
      }
    }

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
  }