package study.jpadata.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.jpadata.dto.MemberDto;
import study.jpadata.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    // @Query(name = "Member.findByUsername") 주석처리해도 잘 동작함
    List<Member> findByUsername(@Param("username") String username);

    // 실무에서 많이 쓰는 방법(메서드는 간단하게 쿼리는 직접 입력)
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.jpadata.dto.MemberDto(m.id, m.username, t.username) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 실무에서 많이 쓰는 방법  : IN 절 -> Collection 으로 다건 조회
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션

    Member findMemberByUsername(String username);   // 단건

    Optional<Member> findOptionalByUsername(String username);   // 단건 Optional

    Slice<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
        // select count(m.username) from Member m
    Page<Member> findCountQuery(int age, Pageable pageable);

    @Modifying  // update 처리한다는 어노테이션
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param(("age")) int age);

    // 복잡한 쿼리에서는 JPQL 작성으로 fetch join
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //간단한 쿼리는 공통 메서드 오버라이드하여 @EntityGraph 어노테이션으로 해결
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"}) //JPQL + 엔티티 그래프
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 메서드 이름으로 쿼리에서 특히 편리하다.
    // @EntityGraph("Member.all")
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);

    // 성능 최적화가 필요한 경우에만 설정
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);


}
