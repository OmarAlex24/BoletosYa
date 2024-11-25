package com.omar.swing;

import com.omar.entity.Aeropuerto;
import com.omar.service.AeropuertoService;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

public class AutoCompleteComboBox extends JComboBox<Aeropuerto> {
    private final AeropuertoService aeropuertoService;
    private List<Aeropuerto> todosLosAeropuertos;

    public AutoCompleteComboBox(AeropuertoService aeropuertoService) {
        this.aeropuertoService = aeropuertoService;
        setEditable(true);
        configurar();
        cargarTodosLosAeropuertos();
    }

    private void configurar() {
        JTextField editor = (JTextField) getEditor().getEditorComponent();

        // Documento personalizado para el editor
        editor.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                super.insertString(offs, str, a);
                filtrarAeropuertos(getText(0, getLength()));
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                filtrarAeropuertos(getText(0, getLength()));
            }
        });

        // Mejorar la navegación con teclado
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        if (isPopupVisible()) {
                            setSelectedIndex(getSelectedIndex());
                            setPopupVisible(false);
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        setPopupVisible(false);
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!isPopupVisible()) {
                            setPopupVisible(true);
                        }
                        break;
                }
            }
        });
    }

    private void cargarTodosLosAeropuertos() {
        SwingWorker<List<Aeropuerto>, Void> worker =
                new SwingWorker<List<Aeropuerto>, Void>() {
                    @Override
                    protected List<Aeropuerto> doInBackground() throws Exception {
                        return aeropuertoService.listarTodos();
                    }

                    @Override
                    protected void done() {
                        try {
                            todosLosAeropuertos = get();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
        worker.execute();
    }

    private void filtrarAeropuertos(String texto) {
        if (todosLosAeropuertos == null) return;

        texto = texto.toLowerCase();
        removeAllItems();

        if (texto.length() < 2) {
            setPopupVisible(false);
            return;
        }

        String finalTexto = texto;
        List<Aeropuerto> coincidencias = todosLosAeropuertos.stream()
                .filter(a -> coincideConBusqueda(a, finalTexto))
                .map(Aeropuerto::new)
                .collect(Collectors.toList());

        if (!coincidencias.isEmpty()) {
            coincidencias.forEach(this::addItem);
            setPopupVisible(true);
            JTextField editor = (JTextField) getEditor().getEditorComponent();
            editor.setCaretPosition(editor.getText().length());
        }
    }

    private boolean coincideConBusqueda(Aeropuerto aeropuerto, String texto) {
        return aeropuerto.getCodigo().toLowerCase().contains(texto) ||
                aeropuerto.getCiudad().toLowerCase().contains(texto) ||
                aeropuerto.getPais().toLowerCase().contains(texto);
    }

}
