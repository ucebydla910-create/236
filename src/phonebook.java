{
    private Map<String, Contact> contactsByName;
    private Map<String, Contact> contactsByPhone;
    private Scanner scanner;

    public PhoneBook() {
        contactsByName = new TreeMap<>();
        contactsByPhone = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Телефонная книга ===");
            System.out.println("1. Добавить контакт");
            System.out.println("2. Удалить контакт");
            System.out.println("3. Найти контакт по имени");
            System.out.println("4. Найти контакт по номеру");
            System.out.println("5. Показать все контакты");
            System.out.println("6. Экспорт в файл");
            System.out.println("7. Импорт из файла");
            System.out.println("8. Выйти");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addContact();
                case 2 -> removeContact();
                case 3 -> findContactByName();
                case 4 -> findContactByPhone();
                case 5 -> showAllContacts();
                case 6 -> exportToFile();
                case 7 -> importFromFile();
                case 8 -> {
                    System.out.println("Выход из телефонной книги...");
                    return;
                }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void addContact() {
        System.out.print("Введите имя контакта: ");
        String name = scanner.nextLine();

        Contact contact = contactsByName.getOrDefault(name, new Contact(name));

        System.out.print("Введите номер телефона: ");
        String phone = scanner.nextLine();

        // Проверка формата номера
        if (!isValidPhoneNumber(phone)) {
            System.out.println("Неверный формат номера!");
            return;
        }

        contact.addPhoneNumber(phone);
        contactsByName.put(name, contact);
        contactsByPhone.put(phone, contact);
        System.out.println("Контакт добавлен/обновлен!");
    }

    private void removeContact() {
        System.out.print("Введите имя контакта для удаления: ");
        String name = scanner.nextLine();

        Contact contact = contactsByName.get(name);
        if (contact == null) {
            System.out.println("Контакт не найден!");
            return;
        }

        // Удаляем из обоих мапов
        for (String phone : contact.getPhoneNumbers()) {
            contactsByPhone.remove(phone);
        }
        contactsByName.remove(name);
        System.out.println("Контакт удален!");
    }

    private void findContactByName() {
        System.out.print("Введите имя для поиска: ");
        String name = scanner.nextLine();

        Contact contact = contactsByName.get(name);
        if (contact == null) {
            System.out.println("Контакт не найден!");
            return;
        }

        System.out.println("\nНайден контакт:");
        System.out.println(contact);
    }

    private void findContactByPhone() {
        System.out.print("Введите номер телефона: ");
        String phone = scanner.nextLine();

        Contact contact = contactsByPhone.get(phone);
        if (contact == null) {
            System.out.println("Контакт не найден!");
            return;
        }

        System.out.println("\nНайден контакт:");
        System.out.println(contact);
    }

    private void showAllContacts() {
        if (contactsByName.isEmpty()) {
            System.out.println("Телефонная книга пуста!");
            return;
        }

        System.out.println("\n=== Все контакты (" + contactsByName.size() + ") ===");
        for (Contact contact : contactsByName.values()) {
            System.out.println(contact);
        }
    }

    private void exportToFile() {
        System.out.print("Введите имя файла для экспорта: ");
        String filename = scanner.nextLine();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Contact contact : contactsByName.values()) {
                writer.print(contact.getName() + "|");
                List<String> phones = contact.getPhoneNumbers();
                for (int i = 0; i < phones.size(); i++) {
                    writer.print(phones.get(i));
                    if (i < phones.size() - 1) writer.print(",");
                }
                writer.println();
            }
            System.out.println("Контакты экспортированы в файл: " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка экспорта: " + e.getMessage());
        }
    }

    private void importFromFile() {
        System.out.print("Введите имя файла для импорта: ");
        String filename = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int imported = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String name = parts[0];
                    String[] phones = parts[1].split(",");

                    Contact contact = new Contact(name);
                    for (String phone : phones) {
                        if (!phone.isEmpty()) {
                            contact.addPhoneNumber(phone);
                            contactsByPhone.put(phone, contact);
                        }
                    }
                    contactsByName.put(name, contact);
                    imported++;
                }
            }
            System.out.println("Импортировано " + imported + " контактов из файла: " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка импорта: " + e.getMessage());
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        // Простая проверка - только цифры и длина от 6 до 15
        return phone.matches("\\d{6,15}");
    }

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.run();
    }
}
