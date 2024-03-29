package org.barons.cloud.state.machine.service;


import lombok.RequiredArgsConstructor;
import org.barons.cloud.state.machine.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    public static final String PAYMENT_ID_HEADER = "payment_id";
    private final PaymentRepository repository;
    private final StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;
    private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;


    @Override
    public Payment newPayment(Payment payment) {
        payment.setState(PaymentState.NEW);
        return repository.save(payment);
    }

    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.PRE_AUTHORIZE);

        return sm;
    }

    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.AUTH_APPROVED);

        return sm;
    }

    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.AUTH_DECLINED);

        return sm;
    }

    private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event){
        //state machine accept the event as standart spring message
        Message msg = MessageBuilder.withPayload(event)
                .setHeader(PAYMENT_ID_HEADER, paymentId)
                .build();
        //https://www.baeldung.com/reactor-core
        //https://www.cognizantsoftvision.com/blog/getting-started-with-reactive-spring-spring-webflux/
        //synch sendevent deprecated
        //Mono veya flux publisher ve stream oluyor ama akka daki gibi subscribe olmadan materialize olmadan hicbirsey baslamiyor
        //Mono 0 veya tek elemanli stream flux n elemanli
        //The core difference is that Reactive is a push model, whereas the Java 8 Streams are a pull model.
        // In a reactive approach, events are pushed to the subscribers as they come in.
        //mesela jdbcResultset stream mesela, stream'i listeye cevirmek istediginde çekip çekip listeye atıyorsun
        //reactive'de ama stream'den geldikce listeye eklemis oluyorsun, sen subscribersin sadece
        //Mono.defer subscribe olunacak monunun subscribe olunan anda olusmasini istedigimizde kullanabiliriz., Diyelim ki count(*) subscribe olundugunda db'ye gider
        //Yani subscribe olacagimiz datanin aslinda tam subscribe oldugumuzda olusmasini istiyoruz.
        //web request gonderen bir mono istedigimizde de bu kullanilabilir, mono olurken request'i gonderirsek blocking olur
     sm.sendEvent(msg);
    }

    private StateMachine<PaymentState, PaymentEvent> build(Long paymentId){
        Payment payment = repository.getOne(paymentId);

        StateMachine<PaymentState, PaymentEvent> sm = stateMachineFactory.getStateMachine(Long.toString(payment.getId()));

        sm.stopReactively().block();
        //database'deki haline set ediyoruz
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
                    sma.resetStateMachineReactively(new DefaultStateMachineContext<>(payment.getState(), null, null, null)).block();
                });

        sm.startReactively().block();

        return sm;
    }
}