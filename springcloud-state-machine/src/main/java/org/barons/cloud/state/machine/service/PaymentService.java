package org.barons.cloud.state.machine.service;


import org.barons.cloud.state.machine.Payment;
import org.barons.cloud.state.machine.PaymentEvent;
import org.barons.cloud.state.machine.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {

    Payment newPayment(Payment payment);

    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);

}