package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestSelectedSeatDTO {
    private Long seatId;
    private Long concertId;
    private Long memberId;
}
