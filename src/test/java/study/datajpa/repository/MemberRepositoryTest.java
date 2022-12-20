package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void testMember() throws Exception {
        Member member = new Member("winter", 27);
        Member saveMember = memberRepository.save(member);
        memberRepository.flush();

        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow();
        assertEquals(saveMember, findMember);
    }
    
    @Test
    public void basicCRUD() throws Exception {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).orElseThrow();
        Member findMember2 = memberRepository.findById(member2.getId()).orElseThrow();
        assertEquals(member1, findMember1);
        assertEquals(member2, findMember2);

        // 리스트 조회
        List<Member> members = memberRepository.findAll();
        assertEquals(members.size(), 2);
        assertTrue(members.contains(member1));
        assertTrue(members.contains(member2));

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long count = memberRepository.count();
        assertEquals(count, 0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception {
        Member member1 = new Member("member1", 15);
        Member member2 = new Member("member1", 20);
        Member member3 = new Member("member3", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("member1", 13);
        assertTrue(members.size() == 2);
        members.forEach(m -> {
            assertEquals(m.getUsername(), "member1");
            assertTrue(m.getAge() > 13);
        });
    }

    @Test
    public void findUserQuery() throws Exception {
        Member member1 = new Member("member1", 15);
        Member member2 = new Member("member1", 20);
        Member member3 = new Member("member3", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> findMembers = memberRepository.findUser("member3", 12);
        Member findMember = findMembers.get(0);
        assertEquals(findMembers.size(), 1);
        assertEquals(findMember, member3);
    }

    @Test
    public void findUsernameListQuery() throws Exception {
        Member member1 = new Member("member1", 15);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<String> usernameList = memberRepository.findUsernameList();
        usernameList.forEach(u -> {
            System.out.println("username = " + u);
        });
    }

    @Test
    public void findMemberDTOQuery() throws Exception {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 15, teamA);
        Member member2 = new Member("member2", 20, teamB);
        Member member3 = new Member("member3", 12, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<MemberDTO> memberDTO = memberRepository.findMemberDTO();
        memberDTO.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void findByNamesQuery() throws Exception {
        Member member1 = new Member("member1", 15);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<String> list = Arrays.asList("member1", "member3");
        List<Member> members = memberRepository.findByNames(list);
        assertTrue(members.size() == 2);
        members.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void paging() throws Exception {
        for (int i = 1; i <= 10; i++) {
            memberRepository.save(new Member("member" + i, 10));
        }

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "id");
        /**
         * if api 로 보내는 경우 Entity 자체를 반환하면 안된다!!!
         * 꼭 DTO 로 변환시켜 반환해야 한다.
         */
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // 현재 페이지 내 요소 개수
        assertEquals(page.stream().count(), 3);
        // 총 요소 개수
        assertEquals(page.getTotalElements(), 10);
        // 현재 페이지
        assertEquals(page.getNumber(), 0);
        // 총 페이지 개수
        assertEquals(page.getTotalPages(), 4);
        // 첫번째 페이가 맞는가
        assertTrue(page.isFirst());
        // 다음 페이지가 있는가
        assertTrue(page.hasNext());

        page.getContent().forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void paging2() throws Exception {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        Member member3 = new Member("member3", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        em.flush();
        em.clear();

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "id");


        Page<Member> page = memberRepository.findDTOByAge(pageRequest);
        System.out.println(page.getTotalElements());
        System.out.println(page.getContent());

        /**
         * if api 로 보내는 경우 Entity 자체를 반환하면 안된다!!!
         * 꼭 DTO 로 변환시켜 반환해야 한다.
         */
        Page<MemberDTO> map = page.map(p -> new MemberDTO(p.getId(), p.getUsername(), p.getTeam() == null ? null : p.getTeam().getName()));

        System.out.println(map.getTotalElements());
        System.out.println(map.getContent());
    }

    @Test
    public void slice() throws Exception {
        for (int i = 1; i <= 10; i++) {
            memberRepository.save(new Member("member" + i, 10));
        }

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "id");
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        // 현재 페이지 내 요소 개수
        assertEquals(page.stream().count(), 3);
        // 현재 페이지
        assertEquals(page.getNumber(), 0);
        // 첫번째 페이가 맞는가
        assertTrue(page.isFirst());
        // 다음 페이지가 있는가
        assertTrue(page.hasNext());

        page.getContent().forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void bulkUpdate() throws Exception {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 32));
        memberRepository.save(new Member("member5", 50));

        List<Member> members = memberRepository.findAll();
        members.forEach(m -> {
            System.out.println(m);
        });

        // 벌크 연산을 보내고 api 가 끝나면 상관없지만 그 다음 로직이 같은 트랜젝션에서 벌어지면 꼭 영속성 컨텍스트 내에 있는 데이터를 지워야 한다.
        int i = memberRepository.bulkAgePlus(30);
        assertEquals(i, 3);

        // 영속성 컨텍스트 데이터 제거
        // @Modifying(clearAutomatically = true) 로 대체 가능
//        em.clear();

        // 벌크 연산은 영속성 컨텍스트를 무시하고 바로 db로 반영하기 때문에 영속성 컨텍스트는 변경된 값을 알 수 없다.
        List<Member> members2 = memberRepository.findAll();
        members2.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);
        Member member3 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        em.flush();
        em.clear();

//        List<Member> members = memberRepository.findExAll(pageRequest);
//        List<Member> members = memberRepository.findAll();
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        members.forEach(member -> {
            System.out.println(member.getUsername() + " :: " + member.getTeam().getName());
        });
    }

    @Test
    public void queryHint() throws Exception {
        Member member = new Member("member", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

//        Member findMember = memberRepository.findById(member.getId()).get();
//        findMember.changeUsername("admin");
//        em.flush();

        Member findMember = memberRepository.findReadOnlyByUsername("member");
        findMember.changeUsername("admin");

        em.flush();
    }

    @Test
    public void lock() throws Exception {
        Member member = new Member("member", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findLockByUsername("member");
        findMember.changeUsername("admin");
    }

    @Test
    public void callCustom() throws Exception {
        Member member = new Member("member", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        List<Member> members = memberRepository.findMemberCustom();
        System.out.println(members.get(0).getUsername());
    }
}