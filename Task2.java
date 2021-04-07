import java.util.*;

public class Task2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<String> names = new ArrayList<>();
        int maxLength = 0; // найдем максимальную длину слова
        for (int i = 0; i < n; i++) {
            String name = in.next();
            int length = name.length();
            if (length > maxLength) {
                maxLength = length;
            }
            names.add(name);
        }

        char empty = 'a' - 1; // этот символом будем дополнять слова, чтобы все слова стали одинаковой длины
        for (int i = 0; i < n; i++) {
            String name = names.get(i);
            char[] chars = new char[maxLength - name.length()];
            Arrays.fill(chars, empty);
            String addition = new String(chars);
            names.set(i, name + addition);
        }

        // создадим ор. граф, где вершины - все буквы алфавита, в виде списка связности
        // ребро а графе A->B будет означать, что A в алфавите должно идти раньше, чем B
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> visited = new HashMap<>();
        Map<Character, Integer> topOrder = new HashMap<>();
        Set<Character> alphabet = new HashSet<>();
        for (char c = 'a'; c <= 'z'; c++) {
            graph.put(c, new HashSet<>());
            visited.put(c, 0);
            topOrder.put(c, 0);
            alphabet.add(c);
        }
        // символ, обозначающий, что слово уже закончилось, должен в алфавите идти раньше всех букв
        // поэтому проведем из него ребра во все буквы
        graph.put(empty, alphabet);
        visited.put(empty, 0);
        topOrder.put(empty, 0);

        List<Boolean> letterChanged = new ArrayList<>(Collections.nCopies(n - 1, false));

        for (int j = 0; j < maxLength; j++) {
            // пройдемся по всем именам и посмотрим, как меняется j-й символ
            for (int i = 0; i < n - 1; i++) {
                char prev = names.get(i).charAt(j);
                char next = names.get(i + 1).charAt(j);
                if (prev != next && !letterChanged.get(i)) { // j-й символ поменялся, а левые части имён одинаковы
                    letterChanged.set(i, true); // в будущем левые части этих имен уже не будут одинаковы
                    graph.get(prev).add(next); // проводим ребро
                }
            }
        }

        // делаем топологическую сортировку графа, это и будет алфавит
        // там же ищем циклы, если найдем, то нужного алфавита не существует
        boolean foundCycle = false;
        for (char c = 'z'; c >= empty; c--) {
            if (visited.get(c) == 0) {
                foundCycle = dfs(graph, visited, topOrder, c);
                if (foundCycle) {
                    break;
                }
            }
        }

        if (foundCycle) {
            System.out.println("Impossible");
        } else {
            topOrder.remove(empty);
            List<Map.Entry<Character, Integer>> orderList = new ArrayList<>(topOrder.entrySet());
            orderList.sort(Map.Entry.comparingByValue());
            for (Map.Entry<Character, Integer> entry : orderList) {
                System.out.print(entry.getKey());
            }
        }
    }

    static int k = 100;

    static boolean dfs(Map<Character, Set<Character>> graph, Map<Character, Integer> visited, Map<Character, Integer> topOrder, char start) {
        visited.put(start, 1);
        boolean foundCycle = false;
        for (char next : graph.get(start)) {
            if (visited.get(next) == 0) {
                foundCycle = dfs(graph, visited, topOrder, next);
            } else if (visited.get(next) == 1) { // нашли цикл
                foundCycle = true;
            }
            if (foundCycle) {
                break;
            }
        }
        if (!foundCycle) {
            visited.put(start, 2);
            topOrder.put(start, k);
            k--;
        }
        return foundCycle;
    }
}
