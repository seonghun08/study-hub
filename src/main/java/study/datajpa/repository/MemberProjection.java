package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface MemberProjection {

    Long getId();

    @Value("#{'이름: ' + target.username}")
    String getUsername();

    String getTeamName();
}
