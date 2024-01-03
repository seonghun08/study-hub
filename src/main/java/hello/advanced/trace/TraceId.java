package hello.advanced.trace;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TraceId {

    private String id;
    private int level;
    
    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    /**
     * 유니크한 ID값 생성
     * @return 8자리 난수
     */
    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 내부로 들어온 TraceId 생성
     * @return 외부 -> 내부 TraceId
     */
    public TraceId createNextId() {
        return new TraceId(this.id, this.level + 1);
    }

    /**
     * 외부로 나온 TraceId 생성
     * @return 내부 -> 외부 TraceId
     */
    public TraceId createPreviousId() {
        return new TraceId(this.id, this.level - 1);
    }

    /**
     * 현재 레벨이 최초 시점인지 확인
     * @return true or false
     */
    public boolean isFirstLevel() {
        return this.level == 0;
    }
}
