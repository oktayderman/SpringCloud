package org.barons.cloud.state.machine;


import org.barons.cloud.state.machine.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {
    //https://medium.com/@hareesh.veduraj/spring-boot-using-spring-state-machine-1c5a6d35b9ad
    //https://github.com/hareeshav/springstatemachine-demo
    //https://codeburst.io/spring-state-machine-what-is-it-and-do-you-need-it-e894c78f5d84
    //https://stackoverflow.com/questions/69506161/spring-statemachinefactory-getstatemachine-returns-currents-state-as-initial-sta
    //https://medium.com/garantibbva-teknoloji/delegation-flow-implementation-with-spring-statemachine-633a4aca4b87
    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("15.75")).build();
    }

    @Transactional
    @Test
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        Payment preAuthPayment = paymentRepository.getOne(savedPayment.getId());
        System.out.println(sm.getState().getId());
        System.out.println(preAuthPayment);
    }
}