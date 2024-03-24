package gachicar.gachicarserver.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InviteResponse {
    private String sender;
    private Long groupId;
    private LocalDateTime created_at;

    @Builder
    public InviteResponse(String sender, Long groupId) {
        this.sender = sender;
        this.groupId = groupId;
        this.created_at = LocalDateTime.now();
    }
}
