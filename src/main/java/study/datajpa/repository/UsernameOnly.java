package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

//    @Value("#{'이름: ' + target.username + ', 나이: ' + target.age}")
    String getUsername();
}
