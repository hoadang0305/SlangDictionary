import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Dictionary {
    private TreeMap<String, Set<String>> dictionary;

    public Dictionary() {
        dictionary = new TreeMap<>();
    }

    public TreeMap<String, Set<String>> getDictionary() {
        return dictionary;
    }

    public void readFileWord(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\`");
                if (data.length == 2) {
                    String[] def = data[1].split("\\|");
                    for (String s : def) {
                        addSlangWordByDefine(data[0], s.trim());
                    }
                }
            }
            System.out.print("read completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFileWord(String path) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.append("Slag`Meaning" + "\n");
            if (dictionary.size() != 0) {
                for (Map.Entry<String, Set<String>> entry : dictionary.entrySet()) {
                    writer.append(entry.getKey() + "`");
                    List<String> stringlist = new ArrayList<>(entry.getValue());
                    for (int i = 0; i < stringlist.size(); i++) {
                        writer.append(stringlist.get(i));
                        if (i < stringlist.size() - 1) {
                            writer.append("| ");
                        }
                    }
                    writer.append("\n");
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSlangWordByDefine(String key, String def) {
        Set<String> definition = new HashSet<String>();
        if (!dictionary.containsKey(key)) {
            definition.add(def);
        } else {
            definition = dictionary.get(key);
            definition.add(def);
        }
        dictionary.put(key, definition);
    }

    public void addSlangWord(String key, Set<String> def) {
        dictionary.put(key, def);
    }

    void print() {
        for (Map.Entry<String, Set<String>> entry : dictionary.entrySet()) {
            String word = entry.getKey();
            Set<String> definitions = entry.getValue();

            System.out.println("Word: " + word);
            System.out.println("Definitions:");
            for (String definition : definitions) {
                System.out.println("- " + definition);
            }
            System.out.println();
        }
    }

    public Dictionary searchBySlang(String key) {
        Dictionary result = new Dictionary();
        String lowKey = key.toLowerCase();
        String upKey = key.toUpperCase();
        for (Map.Entry<String, Set<String>> entry : dictionary.entrySet()) {
            if (entry.getKey().contains(lowKey) || entry.getKey().contains(upKey)) {
                result.addSlangWord(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public Dictionary serchByDefine(String define) {
        Dictionary result = new Dictionary();
        // xử lí các trg hợp input
        String lowWord = define.toLowerCase();
        String upWord = define.toUpperCase();
        String capitalizedWord = lowWord.substring(0, 1).toUpperCase() + lowWord.substring(1);
        for (Map.Entry<String, Set<String>> entry : dictionary.entrySet()) {
            Set<String> defs = entry.getValue();
            for (String def : defs) {
                if (def.contains(lowWord) || def.contains(upWord) || def.contains(capitalizedWord)
                        || def.contains(define)) {
                    result.addSlangWord(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    public void clearDictionary() {
        this.dictionary.clear();
    }

    public static void main(String[] args) {
        Dictionary slang = new Dictionary();
        slang.readFileWord("slang.txt");
        Dictionary check = slang.serchByDefine("it");
        check.print();
    }
}
