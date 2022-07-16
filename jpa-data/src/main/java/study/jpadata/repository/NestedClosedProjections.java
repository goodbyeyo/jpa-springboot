package study.jpadata.repository;

public interface NestedClosedProjections {

    String getUsername();   // 최적화
    TeamInfo getTeam();     // 엔티티 전체

    interface TeamInfo {    // left join
        String getUsername();
    }
}
