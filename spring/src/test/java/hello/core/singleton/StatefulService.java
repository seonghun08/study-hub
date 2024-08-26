package hello.core.singleton;

public class StatefulService {

    // 상태를 유지하는 필드
    private int price;

    int order(String name, int price){
        System.out.println("name = " + name + " price = " + price);
        return price;
    }

//    public int getPrice() {
//        return price;
//    }

}
