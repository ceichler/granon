package parser;

class HelloWorld {
    public static void main(String[] args) {
        String str = "EdgeChord ((*,\"type\",\"Person\"),\"livesIn\",(*,null,null),\"inGroup\",(*,null,null),\"livesIn\")";
        str = str.replace("EdgeChord","");
        String new_str = str.replace("(","").replace(")","").replace("\"","");
        System.out.println(new_str);
        String[] list_str = new_str.split(",");
        for (String i : list_str){
            System.out.println(i);
        }
    }
}