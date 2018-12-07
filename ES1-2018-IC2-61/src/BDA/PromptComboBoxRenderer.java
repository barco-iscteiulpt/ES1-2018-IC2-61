package BDA;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("rawtypes")
class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
	
	private static final long serialVersionUID = 1L;

	private String _title;

	public MyComboBoxRenderer(String title) {
		_title = title;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean hasFocus) {
		if (index == -1 && value == null)
			setText(_title);
		else
			setText(value.toString());
		return this;
	}
}