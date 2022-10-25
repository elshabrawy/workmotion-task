package com.workmotion.config;

import com.workmotion.state_enum.EmployeeEvents;
import com.workmotion.state_enum.EmployeeStates;
import com.workmotion.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class StateMachineConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StateMachineFactory<EmployeeStates, EmployeeEvents> stateMachineFactory;

    @Autowired
    private EmployeeService employeeService;

    /*
     * Test the configration, starting from ADDED to be ACTIVE
     */
    @Test
    public void testHappyScenario1() {

        StateMachine< EmployeeStates, EmployeeEvents> sm = stateMachineFactory.getStateMachine();
        sm.startReactively().subscribe();
        System.out.println(sm.getState().getIds());

        Mono<Message<EmployeeEvents>> message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.IN_CHECK)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());


        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.SECURITY_FINISH)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.WORK_PERMIT_VERIFY)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.WORK_PERMIT_FINISH)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());


        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.FINAL_APPROVE)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        assertEquals(sm.getState().getId(), EmployeeStates.ACTIVE);


    }

    @Test
    public void testHappyScenario2() {

        StateMachine< EmployeeStates, EmployeeEvents> sm = stateMachineFactory.getStateMachine();
        sm.startReactively().subscribe();
        System.out.println(sm.getState().getIds());

        Mono<Message<EmployeeEvents>> message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.IN_CHECK)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.WORK_PERMIT_VERIFY )
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.WORK_PERMIT_FINISH)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.SECURITY_FINISH)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.FINAL_APPROVE)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        assertEquals(sm.getState().getId(), EmployeeStates.ACTIVE);


    }

    @Test
    public void testHappyScenario3() {

        StateMachine< EmployeeStates, EmployeeEvents> sm = stateMachineFactory.getStateMachine();
        sm.startReactively().subscribe();
        System.out.println(sm.getState().getIds());

        Mono<Message<EmployeeEvents>> message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.IN_CHECK)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.WORK_PERMIT_VERIFY)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.SECURITY_FINISH)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.WORK_PERMIT_FINISH)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.FINAL_APPROVE)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        assertEquals(sm.getState().getId(), EmployeeStates.ACTIVE);


    }

    @Test
    public void testUnhappyScenario1() {

        StateMachine< EmployeeStates, EmployeeEvents> sm = stateMachineFactory.getStateMachine();
        sm.startReactively().subscribe();
        System.out.println(sm.getState().getIds());

        Mono<Message<EmployeeEvents>> message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.IN_CHECK)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.SECURITY_FINISH)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.FINAL_APPROVE)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());


        assertNotEquals(sm.getState().getId(), EmployeeStates.ACTIVE);


    }

    @Test
    public void testUnhappyScenario2() {

        StateMachine< EmployeeStates, EmployeeEvents> sm = stateMachineFactory.getStateMachine();
        sm.startReactively().subscribe();
        System.out.println(sm.getState().getIds());

        Mono<Message<EmployeeEvents>> message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.IN_CHECK)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.SECURITY_FINISH)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());

        message = Mono.just(MessageBuilder
                .withPayload(EmployeeEvents.WORK_PERMIT_FINISH)
                .build());
        sm.sendEvent(message).subscribe();
        System.out.println(sm.getState().getIds());


        assertNotEquals(sm.getState().getId(), EmployeeStates.WORK_PERMIT_CHECK_FINISHED);


    }

}