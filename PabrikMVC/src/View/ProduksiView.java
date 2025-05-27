package view;

import controller.ProduksiController;
import model.Produksi;
import exception.ProductionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProduksiView extends JFrame {
    private ProduksiController controller;
    private JTextField namaProdukField, jumlahUnitField, jamKerjaField, jumlahTenagaKerjaField, biayaBahanBakuField;
    private JTable table;
    private DefaultTableModel tableModel;
    private boolean isEditMode = false;
    private int editingId = -1;

    public ProduksiView() {
        try {
            controller = new ProduksiController();
            initializeComponents();
            loadData();
        } catch (ProductionException e) {
            JOptionPane.showMessageDialog(this, "Error inisialisasi: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initializeComponents() {
        setTitle("PABRIK BELA NEGARA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("PABRIK BELA NEGARA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.LIGHT_GRAY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);

        JPanel inputPanel = createInputPanel();
        contentPanel.add(inputPanel, BorderLayout.WEST);

        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        setSize(900, 500);
        setLocationRelativeTo(null);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel namaProdukLabel = new JLabel("Nama Produk");
        panel.add(namaProdukLabel, gbc);
        
        gbc.gridy = 1;
        namaProdukField = new JTextField(15);
        panel.add(namaProdukField, gbc);

        gbc.gridy = 2;
        JLabel jumlahUnitLabel = new JLabel("Jumlah Unit");
        panel.add(jumlahUnitLabel, gbc);
        
        gbc.gridy = 3;
        jumlahUnitField = new JTextField(15);
        panel.add(jumlahUnitField, gbc);

        gbc.gridy = 4;
        JLabel jamKerjaLabel = new JLabel("Jam Kerja");
        panel.add(jamKerjaLabel, gbc);
        
        gbc.gridy = 5;
        jamKerjaField = new JTextField(15);
        panel.add(jamKerjaField, gbc);

        gbc.gridy = 6;
        JLabel jumlahTenagaKerjaLabel = new JLabel("Jumlah Tenaga Kerja");
        panel.add(jumlahTenagaKerjaLabel, gbc);
        
        gbc.gridy = 7;
        jumlahTenagaKerjaField = new JTextField(15);
        panel.add(jumlahTenagaKerjaField, gbc);

        gbc.gridy = 8;
        JLabel biayaBahanBakuLabel = new JLabel("Biaya Bahan Baku");
        panel.add(biayaBahanBakuLabel, gbc);
        
        gbc.gridy = 9;
        biayaBahanBakuField = new JTextField(15);
        panel.add(biayaBahanBakuField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        
        JButton createButton = new JButton("Create");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");

        createButton.addActionListener(e -> {
            if (isEditMode) {
                updateProduksi();
            } else {
                addProduksi();
            }
        });
        editButton.addActionListener(e -> editProduksi());
        deleteButton.addActionListener(e -> deleteProduksi());
        cancelButton.addActionListener(e -> cancelEdit());

        buttonPanel.add(createButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        cancelButton.setVisible(false);

        gbc.gridy = 10;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Nama Produk", "Biaya Tenaga Kerja", "Efisiensi Produksi", "Total Biaya Produksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addProduksi() {
        try {
            String namaProduk = namaProdukField.getText().trim();
            int jumlahUnit = Integer.parseInt(jumlahUnitField.getText().trim());
            double jamKerja = Double.parseDouble(jamKerjaField.getText().trim());
            int jumlahTenagaKerja = Integer.parseInt(jumlahTenagaKerjaField.getText().trim());
            double biayaBahanBaku = Double.parseDouble(biayaBahanBakuField.getText().trim());

            String result = controller.addProduksi(namaProduk, jumlahUnit, jamKerja, jumlahTenagaKerja, biayaBahanBaku);
            
            if (result.contains("berhasil")) {
                JOptionPane.showMessageDialog(this, result, "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input harus berupa angka yang valid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editProduksi() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diedit", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Produksi> produksiList = controller.getAllProduksi();
            if (selectedRow < produksiList.size()) {
                Produksi produksi = produksiList.get(selectedRow);
               
                namaProdukField.setText(produksi.getNamaProduk());
                jumlahUnitField.setText(String.valueOf(produksi.getJumlahUnit()));
                jamKerjaField.setText(String.valueOf(produksi.getJamKerja()));
                jumlahTenagaKerjaField.setText(String.valueOf(produksi.getJumlahTenagaKerja()));
                biayaBahanBakuField.setText(String.valueOf(produksi.getBiayaBahanBaku()));
                
                isEditMode = true;
                editingId = produksi.getId();
                
                setEditMode(true);
            }
        } catch (ProductionException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduksi() {
        try {
            String namaProduk = namaProdukField.getText().trim();
            int jumlahUnit = Integer.parseInt(jumlahUnitField.getText().trim());
            double jamKerja = Double.parseDouble(jamKerjaField.getText().trim());
            int jumlahTenagaKerja = Integer.parseInt(jumlahTenagaKerjaField.getText().trim());
            double biayaBahanBaku = Double.parseDouble(biayaBahanBakuField.getText().trim());

            String result = controller.updateProduksi(editingId, namaProduk, jumlahUnit, jamKerja, jumlahTenagaKerja, biayaBahanBaku);
            
            if (result.contains("berhasil")) {
                JOptionPane.showMessageDialog(this, result, "Sukses", JOptionPane.INFORMATION_MESSAGE);
                cancelEdit();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input harus berupa angka yang valid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelEdit() {
        isEditMode = false;
        editingId = -1;
        clearFields();
        setEditMode(false);
    }

    private void setEditMode(boolean editMode) {
        Container parent = getContentPane();
        JButton createButton = findButton(parent, "Create");
        JButton editButton = findButton(parent, "Edit");
        JButton deleteButton = findButton(parent, "Delete");
        JButton cancelButton = findButton(parent, "Cancel");
        
        if (editMode) {
            if (createButton != null) createButton.setText("Update");
            if (editButton != null) editButton.setEnabled(false);
            if (deleteButton != null) deleteButton.setEnabled(false);
            if (cancelButton != null) cancelButton.setVisible(true);
        } else {
            if (createButton != null) createButton.setText("Create");
            if (editButton != null) editButton.setEnabled(true);
            if (deleteButton != null) deleteButton.setEnabled(true);
            if (cancelButton != null) cancelButton.setVisible(false);
        }
        
        repaint();
    }

    private JButton findButton(Container container, String text) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals(text)) {
                return (JButton) component;
            } else if (component instanceof Container) {
                JButton found = findButton((Container) component, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    private void deleteProduksi() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Produksi> produksiList = controller.getAllProduksi();
            if (selectedRow < produksiList.size()) {
                int id = produksiList.get(selectedRow).getId();
                
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Yakin ingin menghapus data ini?", "Konfirmasi", 
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.deleteProduksi(id);
                    JOptionPane.showMessageDialog(this, result, 
                        result.contains("berhasil") ? "Sukses" : "Error",
                        result.contains("berhasil") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                    
                    if (result.contains("berhasil")) {
                        loadData();
                    }
                }
            }
        } catch (ProductionException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            List<Produksi> produksiList = controller.getAllProduksi();

            for (Produksi p : produksiList) {
                Object[] row = {
                    p.getNamaProduk(),
                    String.format("%.0f", p.getBiayaTenagaKerja()),
                    String.format("%.1f", p.getEfisiensiProduksi()),
                    String.format("%.0f", p.getTotalBiayaProduksi())
                };
                tableModel.addRow(row);
            }

        } catch (ProductionException e) {
            JOptionPane.showMessageDialog(this, "Error memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        namaProdukField.setText("");
        jumlahUnitField.setText("");
        jamKerjaField.setText("");
        jumlahTenagaKerjaField.setText("");
        biayaBahanBakuField.setText("");
    }

    @Override
    public void dispose() {
        controller.close();
        super.dispose();
    }
}