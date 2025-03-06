import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*; 
import java.awt.event.*; 


public class GUI {
    private static Database DB;
    public GUI() {
        this.DB = new Database(); // Создаем экземпляр Database
        DB.initDatabase(); // Инициализируем базу данных
        StartGUI(); // Показываем GUI для входа
    }

    private void StartGUI() {
        JFrame frame = new JFrame("Вход");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel userLabel = new JLabel("Логин:");
        JTextField userField = new JTextField("postgres",10);

        JLabel passwordLabel = new JLabel("Пароль:");
        JTextField passwordField = new JTextField("mysecretpassword",10);

        JButton button = new JButton("Войти");

        button.addActionListener(e -> {
 
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String user = userField.getText();
                String password = passwordField.getText();
                
                boolean success = DB.connectDatabase(user, password);
                if (success){
                    frame.dispose();
                    WorkGUI();
                } else{
                    JOptionPane.showMessageDialog(frame, "Не удалось войти.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        });

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        frame.add(userLabel, gbc);

        gbc.gridx = 1; 
        frame.add(userField, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 1; 
        frame.add(passwordLabel, gbc);

        gbc.gridx = 1; 
        frame.add(passwordField, gbc);

        gbc.gridy = 2;

        frame.add(button, gbc);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    private void WorkGUI() {
        JFrame frame = new JFrame("База данных: Клиенты");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 500);
    
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
    
        JButton addOrder_button = new JButton("Добавить запись");
        JButton searchOrder_button = new JButton("Найти запись");
        JButton updateOrder_button = new JButton("Обновить запись");
        JButton deleteOrder_button = new JButton("Удалить запись");
        JButton clearTable_button = new JButton("Очистить таблицу");
        JButton dropDB_button = new JButton("Удалить БД");
        JButton loadOrders_button = new JButton("Обновить таблицу");
        JButton createUser_button = new JButton("Создать пользователя");
        JButton exit_button = new JButton("Выйти");
    
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable ordersTable = new JTable(tableModel);
        // ordersTable.tableChanged(TableModelEvent e ->{
        //     JOptionPane.showMessageDialog(frame, "База данных успешно удалена.",
        //     "Успех", JOptionPane.INFORMATION_MESSAGE);        });
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setPreferredSize(new Dimension(550, 300));
    
        try {
            loadOrders(tableModel, DB.getOrders());
        } catch (Exception e) {
            return;
        }
    
        addOrder_button.addActionListener(e -> {
            addOrderGUI();
        });
    
        searchOrder_button.addActionListener(e -> {
            searchOrderGUI(tableModel);
        });
    
        updateOrder_button.addActionListener(e -> {
            updateOrderGUI();
        });
    
        deleteOrder_button.addActionListener(e -> {
            deleteOrderGUI();
        });
    
        clearTable_button.addActionListener(e -> {
            ShowMessage(frame, DB.clearTable());
        });
    
        dropDB_button.addActionListener(e -> {
            boolean dropped = DB.dropDatabase("database_lr5");
            if (dropped) {
                JOptionPane.showMessageDialog(frame, "База данных успешно удалена.",
                        "Успех", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                StartGUI();
            } else {
                JOptionPane.showMessageDialog(frame, "Не удалось удалить базу данных.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        loadOrders_button.addActionListener(e -> {
            loadOrders(tableModel, DB.getOrders());
        });
    
        createUser_button.addActionListener(e -> {
            createUserGUI();
        });
    
        exit_button.addActionListener(e -> {
            boolean closed = DB.exit();
            if (closed) {
                frame.dispose();
                StartGUI();
            } else {
                JOptionPane.showMessageDialog(frame, "Не удалось выйти.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        panel.add(addOrder_button);
        panel.add(searchOrder_button);
        panel.add(updateOrder_button);
        panel.add(deleteOrder_button);
        panel.add(clearTable_button);
        panel.add(dropDB_button);
        panel.add(loadOrders_button);
        panel.add(createUser_button);
        panel.add(exit_button);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static void loadOrders(DefaultTableModel tableModel, Object[][] orders) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
    
        if (orders.length > 0) {
            tableModel.addColumn("client_id");
            tableModel.addColumn("full_name");
            tableModel.addColumn("phone_number");
            tableModel.addColumn("contact_info");
    
            for (Object[] order : orders) {
                tableModel.addRow(order);
            }
        } else {
            
            JOptionPane.showMessageDialog(null, "Клиенты не найдены.", "Информация", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //выбор админа и пользователя для создания с помощью чек боксов
    private static void createUserGUI() {
        JFrame frame = new JFrame("Добавление пользователя");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    
        JLabel userLabel = new JLabel("Логин:");
        JTextField userField = new JTextField(10);
    
        JLabel passwordLabel = new JLabel("Пароль:");
        JTextField passwordField = new JTextField(10);
    
        JCheckBox adminCheckbox = new JCheckBox("Админ");
        JCheckBox userCheckbox = new JCheckBox("Пользователь");
    
        JButton addButton = new JButton("Добавить");
    
        // Ограничиваем возможность выбора только одной роли
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(adminCheckbox);
        roleGroup.add(userCheckbox);
    
        // Добавляем слушателей для кнопок
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userField.getText();
                String password = passwordField.getText();
                boolean isAdmin = adminCheckbox.isSelected();
    
                // Логика добавления пользователя
                boolean added = DB.addUser(user, password, isAdmin);
                if (added) {
                    JOptionPane.showMessageDialog(frame, "Пользователь добавлен");
                    userField.setText("");
                    passwordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Не удалось добавить пользователя.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
    
        // Добавляем элементы на форму
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(userLabel, gbc);
    
        gbc.gridx = 1;
        frame.add(userField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(passwordLabel, gbc);
    
        gbc.gridx = 1;
        frame.add(passwordField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(adminCheckbox, gbc);
    
        gbc.gridx = 1;
        frame.add(userCheckbox, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(addButton, gbc);
    
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void addOrderGUI() {
        JFrame frame = new JFrame("Добавление Клиента");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    
        JLabel client_idLabel = new JLabel("ID клиента:");
        JTextField client_idField = new JTextField(15);
    
        JLabel full_nameLabel = new JLabel("ФИО:");
        JTextField full_nameField = new JTextField(15);
    
        JLabel phone_numberLabel = new JLabel("Номер телефона (8 900 000 00 00):");
        JTextField phone_numberField = new JTextField(15);
    
        JLabel contact_infoLabel = new JLabel("Контактная информация:");
        JTextField contact_infoField = new JTextField(15);
    
        JButton addButton = new JButton("Добавить");
    
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Начало обработки данных...");
                int client_id;
                try {
                    client_id = Integer.parseInt(client_idField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "ID клиента должно быть целым числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (client_id < 1) {
                    JOptionPane.showMessageDialog(frame, "ID клиента должно быть положительным.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                String full_name = full_nameField.getText();
                if (full_name.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "ФИО не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                String phone_number = phone_numberField.getText();
                if (!phone_number.matches("\\d{11}")) {
                    JOptionPane.showMessageDialog(frame, "Номер телефона должен содержать 11 цифр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                String contact_info = contact_infoField.getText();
                if (contact_info.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Контактная информация не может быть пустой.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                boolean added = DB.addOrder(client_id, full_name, phone_number, contact_info);
                if (added) {
                    JOptionPane.showMessageDialog(frame,
                            "Заказ добавлен:\n" +
                                    "ID клиента: " + client_id + "\n" +
                                    "ФИО: " + full_name + "\n" +
                                    "Номер телефона: " + phone_number + "\n" +
                                    "Контактная информация: " + contact_info);
                    client_idField.setText("");
                    full_nameField.setText("");
                    phone_numberField.setText("");
                    contact_infoField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Не удалось добавить запись.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        
        });
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(client_idLabel, gbc);
    
        gbc.gridx = 1;
        frame.add(client_idField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(full_nameLabel, gbc);
    
        gbc.gridx = 1;
        frame.add(full_nameField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(phone_numberLabel, gbc);
    
        gbc.gridx = 1;
        frame.add(phone_numberField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(contact_infoLabel, gbc);
    
        gbc.gridx = 1;
        frame.add(contact_infoField, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        frame.add(addButton, gbc);
    
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        
    }
    


    private static void searchOrderGUI(DefaultTableModel tableModel) {
        JFrame frame = new JFrame("Выбор поля для поиска");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 200);
    
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JButton searchClient_id = new JButton("ID клиента");
        JButton searchFull_name = new JButton("ФИО");
        JButton searchPhone_number = new JButton("Номер телефона");
        JButton searchContact_info = new JButton("Контактная информация");
    
        searchClient_id.addActionListener(e -> {
            frame.dispose();
            searchClient_idGUI(tableModel);
        });
    
        searchFull_name.addActionListener(e -> {
            frame.dispose();
            searchFullNameGUI(tableModel);
        });
    
        searchPhone_number.addActionListener(e -> {
            frame.dispose();
            searchPhoneNumberGUI(tableModel);
        });
    
        searchContact_info.addActionListener(e -> {
            frame.dispose();
            searchContactInfoGUI(tableModel);
        });
    
        panel.add(searchClient_id);
        panel.add(searchFull_name);
        panel.add(searchPhone_number);
        panel.add(searchContact_info);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void ShowMessage(JFrame frame, boolean statement) {
        if (statement) {
            JOptionPane.showMessageDialog(frame, "Операция успешно выполнена.",
                                         "Успех", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Не удалось выполнить операцию.",
                                         "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void searchClient_idGUI(DefaultTableModel tableModel) {
        JFrame frame = new JFrame("Поиск по ID клиента");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextField client_idField = new JTextField(15);
        JButton search_button = new JButton("Искать");
    
        search_button.addActionListener(e -> {
            int client_id;
            try {
                client_id = Integer.parseInt(client_idField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID клиента должно быть целым числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (client_id < 1) {
                JOptionPane.showMessageDialog(frame, "ID клиента должно быть положительным.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Object[][] orders = DB.searchOrderClientId(client_id);
            loadOrders(tableModel, orders);
            frame.dispose();
        });
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 100);
        frame.add(client_idField, gbc);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(search_button, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static void searchFullNameGUI(DefaultTableModel tableModel) {
        JFrame frame = new JFrame("Поиск по ФИО");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextField full_nameField = new JTextField(15);
        JButton search_button = new JButton("Искать");
    
        search_button.addActionListener(e -> {
            String full_name = full_nameField.getText();
            if (full_name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "ФИО не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Object[][] orders = DB.searchOrderFullName(full_name);
            loadOrders(tableModel, orders);
            frame.dispose();
        });
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 100);
        frame.add(full_nameField, gbc);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(search_button, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static void searchPhoneNumberGUI(DefaultTableModel tableModel) {
        JFrame frame = new JFrame("Поиск по номеру телефона");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextField phone_numberField = new JTextField(15);
        JButton search_button = new JButton("Искать");
    
        search_button.addActionListener(e -> {
            String phone_number = phone_numberField.getText();
            if (phone_number.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Номер телефона не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!phone_number.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(frame, "Номер телефона должен содержать 11 цифр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Object[][] orders = DB.searchOrderPhoneNumber(phone_number);
            loadOrders(tableModel, orders);
            frame.dispose();
        });
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 100);
        frame.add(phone_numberField, gbc);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(search_button, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static void searchContactInfoGUI(DefaultTableModel tableModel) {
        JFrame frame = new JFrame("Поиск по контактной информации");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextField contact_infoField = new JTextField(15);
        JButton search_button = new JButton("Искать");
    
        search_button.addActionListener(e -> {
            String contact_info = contact_infoField.getText();
            if (contact_info.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Контактная информация не может быть пустой.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Object[][] orders = DB.searchOrderContactInfo(contact_info);
            loadOrders(tableModel, orders);
            frame.dispose();
        });
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 100);
        frame.add(contact_infoField, gbc);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(search_button, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void deleteOrderGUI() {
        JFrame frame = new JFrame("Выбор поля для удаления");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(190, 200);
    
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JButton searchClient_id = new JButton("ID клиента");
        JButton searchFull_name = new JButton("ФИО");
        JButton searchPhone_number = new JButton("Номер телефона");
        JButton searchContact_info = new JButton("Контактная информация");
    
        searchClient_id.addActionListener(e -> {
            frame.dispose();
            deleteClientIdGUI();
        });
    
        searchFull_name.addActionListener(e -> {
            frame.dispose();
            deleteFullNameGUI();
        });
    
        searchPhone_number.addActionListener(e -> {
            frame.dispose();
            deletePhoneNumberGUI();
        });
    
        searchContact_info.addActionListener(e -> {
            frame.dispose();
            deleteContactInfoGUI();
        });
    
        panel.add(searchClient_id);
        panel.add(searchFull_name);
        panel.add(searchPhone_number);
        panel.add(searchContact_info);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static void deleteClientIdGUI() {
        JFrame frame = new JFrame("Удаление по ID клиента");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextField client_idField = new JTextField(15);
        JButton delete_button = new JButton("Удалить");
    
        delete_button.addActionListener(e -> {
            int client_id;
            try {
                client_id = Integer.parseInt(client_idField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID клиента должно быть целым числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (client_id < 1) {
                JOptionPane.showMessageDialog(frame, "ID клиента должно быть положительным.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean deleted = DB.deleteOrderClientId(client_id);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Записи удалены.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Ошибка при удалении.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 100);
        frame.add(client_idField, gbc);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(delete_button, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static void deleteFullNameGUI() {
        JFrame frame = new JFrame("Удаление по ФИО");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextField full_nameField = new JTextField(15);
        JButton delete_button = new JButton("Удалить");
    
        delete_button.addActionListener(e -> {
            String full_name = full_nameField.getText();
            if (full_name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "ФИО не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean deleted = DB.deleteOrderFullName(full_name);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Записи удалены.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Ошибка при удалении.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 100);
        frame.add(full_nameField, gbc);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(delete_button, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static void deletePhoneNumberGUI() {
        JFrame frame = new JFrame("Удаление по номеру телефона");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextField phone_numberField = new JTextField(15);
        JButton delete_button = new JButton("Удалить");
    
        delete_button.addActionListener(e -> {
            String phone_number = phone_numberField.getText();
            if (phone_number.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Номер телефона не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!phone_number.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(frame, "Номер телефона должен содержать 11 цифр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean deleted = DB.deleteOrderPhoneNumber(phone_number);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Записи удалены.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Ошибка при удалении.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 100);
        frame.add(phone_numberField, gbc);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(delete_button, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private static void deleteContactInfoGUI() {
        JFrame frame = new JFrame("Удаление по контактной информации");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextField contact_infoField = new JTextField(15);
        JButton delete_button = new JButton("Удалить");
    
        delete_button.addActionListener(e -> {
            String contact_info = contact_infoField.getText();
            if (contact_info.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Контактная информация не может быть пустой.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean deleted = DB.deleteOrderContactInfo(contact_info);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Записи удалены.", "Информация", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Ошибка при удалении.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 100);
        frame.add(contact_infoField, gbc);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(delete_button, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
//
private static void updateOrderGUI() {
    JFrame frame = new JFrame("Обновление заказа");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(400, 300);
    frame.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    JLabel client_idLabel = new JLabel("ID клиента:");
    JTextField client_idField = new JTextField(15);

    JLabel full_nameLabel = new JLabel("ФИО:");
    JTextField full_nameField = new JTextField(15);

    JLabel phone_numberLabel = new JLabel("Номер телефона (8 900 000 00 00):");
    JTextField phone_numberField = new JTextField(15);

    JLabel contact_infoLabel = new JLabel("Контактная информация:");
    JTextField contact_infoField = new JTextField(15);

    JButton updateButton = new JButton("Обновить");

    updateButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int client_id;
            try {
                client_id = Integer.parseInt(client_idField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID клиента должно быть целым числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (client_id < 1) {
                JOptionPane.showMessageDialog(frame, "ID клиента должно быть положительным.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String full_name = full_nameField.getText();
            if (full_name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "ФИО не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String phone_number = phone_numberField.getText();
            if (!phone_number.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(frame, "Номер телефона должен содержать 11 цифр.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String contact_info = contact_infoField.getText();
            if (contact_info.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Контактная информация не может быть пустой.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean updated = DB.updateOrder(client_id, full_name, phone_number, contact_info);
            if (updated) {
                JOptionPane.showMessageDialog(frame,
                        "Заказ обновлён:\n" +
                                "ID клиента: " + client_id + "\n" +
                                "ФИО: " + full_name + "\n" +
                                "Номер телефона: " + phone_number + "\n" +
                                "Контактная информация: " + contact_info);
                client_idField.setText("");
                full_nameField.setText("");
                phone_numberField.setText("");
                contact_infoField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Не удалось обновить запись.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    });

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    frame.add(client_idLabel, gbc);

    gbc.gridx = 1;
    frame.add(client_idField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    frame.add(full_nameLabel, gbc);

    gbc.gridx = 1;
    frame.add(full_nameField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    frame.add(phone_numberLabel, gbc);

    gbc.gridx = 1;
    frame.add(phone_numberField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    frame.add(contact_infoLabel, gbc);

    gbc.gridx = 1;
    frame.add(contact_infoField, gbc);

    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.EAST;
    frame.add(updateButton, gbc);

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}
public static void main(String[] args) {
    new GUI(); // Запуск
}
};