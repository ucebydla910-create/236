import java.util.*;

public class CollectionsPerformanceTest {

    public static void main(String[] args) {
        System.out.println("=== СРАВНЕНИЕ ПРОИЗВОДИТЕЛЬНОСТИ КОЛЛЕКЦИЙ ===\n");

        int elementCount = 100000;

        // Тестирование List коллекций
        testListCollections(elementCount);

        // Тестирование Set коллекций
        testSetCollections(elementCount);

        // Тестирование Map коллекций
        testMapCollections(elementCount);
    }

    private static void testListCollections(int elementCount) {
        System.out.println("=== ТЕСТИРОВАНИЕ LIST КОЛЛЕКЦИЙ ===");
        System.out.println("Количество элементов: " + elementCount);
        System.out.println("--------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s%n",
                "Коллекция", "Добавление", "Поиск", "Удаление");
        System.out.println("--------------------------------------------------");

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        // Тест добавления
        long arrayListAddTime = testAdd(arrayList, elementCount);
        long linkedListAddTime = testAdd(linkedList, elementCount);

        // Тест поиска
        long arrayListSearchTime = testSearch(arrayList, elementCount / 2);
        long linkedListSearchTime = testSearch(linkedList, elementCount / 2);

        // Тест удаления каждого 10-го элемента
        long arrayListRemoveTime = testRemoveEveryTenth(arrayList);
        // Пересоздаем linkedList для теста удаления
        linkedList = new LinkedList<>();
        testAdd(linkedList, elementCount);
        long linkedListRemoveTime = testRemoveEveryTenth(linkedList);

        System.out.printf("%-15s %-15d %-15d %-15d%n",
                "ArrayList", arrayListAddTime, arrayListSearchTime, arrayListRemoveTime);
        System.out.printf("%-15s %-15d %-15d %-15d%n",
                "LinkedList", linkedListAddTime, linkedListSearchTime, linkedListRemoveTime);
        System.out.println();
    }

    private static void testSetCollections(int elementCount) {
        System.out.println("=== ТЕСТИРОВАНИЕ SET КОЛЛЕКЦИЙ ===");
        System.out.println("Количество элементов: " + elementCount);
        System.out.println("--------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s%n",
                "Коллекция", "Добавление", "Поиск", "Удаление");
        System.out.println("--------------------------------------------------");

        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();

        // Тест добавления
        long hashSetAddTime = testAdd(hashSet, elementCount);
        long treeSetAddTime = testAdd(treeSet, elementCount);
        long linkedHashSetAddTime = testAdd(linkedHashSet, elementCount);

        // Тест поиска
        long hashSetSearchTime = testSearch(hashSet, elementCount / 2);
        long treeSetSearchTime = testSearch(treeSet, elementCount / 2);
        long linkedHashSetSearchTime = testSearch(linkedHashSet, elementCount / 2);

        // Тест удаления
        long hashSetRemoveTime = testRemoveEveryTenth(hashSet);
        treeSet = new TreeSet<>();
        testAdd(treeSet, elementCount);
        long treeSetRemoveTime = testRemoveEveryTenth(treeSet);
        linkedHashSet = new LinkedHashSet<>();
        testAdd(linkedHashSet, elementCount);
        long linkedHashSetRemoveTime = testRemoveEveryTenth(linkedHashSet);

        System.out.printf("%-15s %-15d %-15d %-15d%n",
                "HashSet", hashSetAddTime, hashSetSearchTime, hashSetRemoveTime);
        System.out.printf("%-15s %-15d %-15d %-15d%n",
                "TreeSet", treeSetAddTime, treeSetSearchTime, treeSetRemoveTime);
        System.out.printf("%-15s %-15d %-15d %-15d%n",
                "LinkedHashSet", linkedHashSetAddTime, linkedHashSetSearchTime, linkedHashSetRemoveTime);
        System.out.println();
    }

    private static void testMapCollections(int elementCount) {
        System.out.println("=== ТЕСТИРОВАНИЕ MAP КОЛЛЕКЦИЙ ===");
        System.out.println("Количество элементов: " + elementCount);
        System.out.println("--------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s%n",
                "Коллекция", "Добавление", "Поиск", "Удаление");
        System.out.println("--------------------------------------------------");

        Map<Integer, String> hashMap = new HashMap<>();
        Map<Integer, String> treeMap = new TreeMap<>();
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();

        // Тест добавления
        long hashMapAddTime = testMapAdd(hashMap, elementCount);
        long treeMapAddTime = testMapAdd(treeMap, elementCount);
        long linkedHashMapAddTime = testMapAdd(linkedHashMap, elementCount);

        // Тест поиска
        long hashMapSearchTime = testMapSearch(hashMap, elementCount / 2);
        long treeMapSearchTime = testMapSearch(treeMap, elementCount / 2);
        long linkedHashMapSearchTime = testMapSearch(linkedHashMap, elementCount / 2);

        // Тест удаления
        long hashMapRemoveTime = testMapRemoveEveryTenth(hashMap);
        treeMap = new TreeMap<>();
        testMapAdd(treeMap, elementCount);
        long treeMapRemoveTime = testMapRemoveEveryTenth(treeMap);
        linkedHashMap = new LinkedHashMap<>();
        testMapAdd(linkedHashMap, elementCount);
        long linkedHashMapRemoveTime = testMapRemoveEveryTenth(linkedHashMap);

        System.out.printf("%-15s %-15d %-15d %-15d%n",
                "HashMap", hashMapAddTime, hashMapSearchTime, hashMapRemoveTime);
        System.out.printf("%-15s %-15d %-15d %-15d%n",
                "TreeMap", treeMapAddTime, treeMapSearchTime, treeMapRemoveTime);
        System.out.printf("%-15s %-15d %-15d %-15d%n",
                "LinkedHashMap", linkedHashMapAddTime, linkedHashMapSearchTime, linkedHashMapRemoveTime);
        System.out.println();
    }

    private static long testAdd(Collection<Integer> collection, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            collection.add(i);
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000; // в миллисекундах
    }

    private static long testSearch(Collection<Integer> collection, int element) {
        long startTime = System.nanoTime();
        boolean found = collection.contains(element);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000; // в микросекундах
    }

    private static long testRemoveEveryTenth(Collection<Integer> collection) {
        List<Integer> toRemove = new ArrayList<>();
        int index = 0;
        for (Integer element : collection) {
            if (index % 10 == 0) {
                toRemove.add(element);
            }
            index++;
        }

        long startTime = System.nanoTime();
        collection.removeAll(toRemove);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000; // в миллисекундах
    }

    private static long testMapAdd(Map<Integer, String> map, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            map.put(i, "Value" + i);
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000;
    }

    private static long testMapSearch(Map<Integer, String> map, int key) {
        long startTime = System.nanoTime();
        String value = map.get(key);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000;
    }

    private static long testMapRemoveEveryTenth(Map<Integer, String> map) {
        List<Integer> toRemove = new ArrayList<>();
        for (Integer key : map.keySet()) {
            if (key % 10 == 0) {
                toRemove.add(key);
            }
        }

        long startTime = System.nanoTime();
        for (Integer key : toRemove) {
            map.remove(key);
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000;
    }
