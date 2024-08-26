package study.datajpa.dto;

import lombok.Data;

@Data
public class MemberDTO {

    private Long id;

    private String username;

    private String teamName;

    public MemberDTO(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        if (teamName != null) {
            this.teamName = teamName;
        }
    }

    public MemberDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
