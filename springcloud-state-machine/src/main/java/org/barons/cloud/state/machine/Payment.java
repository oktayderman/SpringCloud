package org.barons.cloud.state.machine;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)

    private PaymentState state;
    private BigDecimal amount;
}
