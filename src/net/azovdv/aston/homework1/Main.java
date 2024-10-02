package net.azovdv.aston.homework1;

public class Main {
    public static void main(String[] args) {

        MyHashMap<String, Integer> ages = new MyHashMap<>();

        ages.put("Женя", 36);
        ages.put("Витя", 15);
        ages.put("Толя", 65);

        System.out.println(ages.entrySet());
        System.out.println(ages.keySet());
        System.out.println(ages.get("Женя"));
        System.out.println(ages.containsKey("Веня"));
        System.out.println(ages.containsKey("Толя"));
        System.out.println(ages.containsValue(15));
        System.out.println(ages.containsValue(16));

        System.out.println(ages.remove("Женя"));
        System.out.println(ages.entrySet());
    }
}