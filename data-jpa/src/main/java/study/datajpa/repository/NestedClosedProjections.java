package study.datajpa.repository;

public interface NestedClosedProjections {

    String getUsername();
    TeamINfo getTeam();

    interface TeamINfo {
        String getName();
    }
}
