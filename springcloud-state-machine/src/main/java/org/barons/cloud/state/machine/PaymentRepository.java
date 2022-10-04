package org.barons.cloud.state.machine;

import org.springframework.data.jpa.repository.JpaRepository;
//https://medium.com/@hareesh.veduraj/spring-boot-using-spring-state-machine-1c5a6d35b9ad
public interface PaymentRepository  extends JpaRepository<Payment,Long> {
}
