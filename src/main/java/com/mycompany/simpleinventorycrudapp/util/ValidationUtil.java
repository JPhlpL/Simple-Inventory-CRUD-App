package com.mycompany.simpleinventorycrudapp.util;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class ValidationUtil {

    public static void setupNumericField(JTextField field) {
        if (field != null && field.getDocument() instanceof AbstractDocument) {
            ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                        throws BadLocationException {
                    StringBuilder filteredText = new StringBuilder();
                    for (char c : text.toCharArray()) {
                        if (Character.isDigit(c)) {
                            filteredText.append(c);
                        }
                    }
                    super.replace(fb, offset, length, filteredText.toString(), attrs);
                }
            });

            field.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    String input = field.getText();
                    if (!input.matches("\\d*")) {
                        field.setText("");
                    }
                }
            });
        }
    }
}
