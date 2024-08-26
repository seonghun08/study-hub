package hello.proxy.common.service;

import lombok.Data;

@Data
public class Obj {
    private String username;
    private String age;

    public Obj(String username, String age) {
        this.username = username;
        this.age = age;
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\":\"" + username + '\"' +
                ", \"age\":" + age +
                '}';
    }
}
