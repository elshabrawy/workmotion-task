package com.workmotion.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.workmotion.exception.custom.NotFoundData;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import com.workmotion.entity.Employee;
import com.workmotion.state_enum.EmployeeEvents;
import com.workmotion.state_enum.EmployeeStates;
import com.workmotion.repository.EmployeeRepository;

import reactor.core.publisher.Mono;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    public static final String EMP_ID_HEADER = "EMPLOYEE_ID";
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private StateMachineFactory<EmployeeStates, EmployeeEvents> stateMachineFactory;

    public Employee save(Employee emp) {

        return employeeRepository.save(emp);
    }

    public Employee getById(Long employeeId) {

        return employeeRepository.findById(employeeId).orElseThrow(()->new NotFoundData("Employee ID not Found"));
    }

    private Employee sendEventAndSaveNewState(Employee employee, StateMachine<EmployeeStates, EmployeeEvents> sm, EmployeeEvents event) {

        Mono<Message<EmployeeEvents>> message = Mono
                .just(MessageBuilder.withPayload(event).setHeader(EMP_ID_HEADER, employee.getEmployeeId()).build());

        sm.sendEvent(message).subscribe();
        employee.getState().clear();
        employee.setState(event, new ArrayList<EmployeeStates>(sm.getState().getIds()));
        return employeeRepository.save(employee);
    }

    private StateMachine<EmployeeStates, EmployeeEvents> buildStateMachine(Employee employee) {

        StateMachine<EmployeeStates, EmployeeEvents> sm = stateMachineFactory.getStateMachine(employee.getEmployeeId().toString());

        sm.stopReactively().subscribe();

        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
                    DefaultStateMachineContext<EmployeeStates, EmployeeEvents> ct = null;
                    List<EmployeeStates> states = new ArrayList<EmployeeStates>(employee.getState());

                    if (states.size() > 1) {
                        List<StateMachineContext<EmployeeStates, EmployeeEvents>> list = new ArrayList<>();
                        states.subList(1, employee.getState().size()).forEach(s -> list.add(
                                new DefaultStateMachineContext<EmployeeStates, EmployeeEvents>(s, null, null, null, null)));
                        ct = new DefaultStateMachineContext<EmployeeStates, EmployeeEvents>(list, states.get(0), null, null,
                                null, null);

                    } else {

                        ct = new DefaultStateMachineContext<EmployeeStates, EmployeeEvents>(states.get(0), null, null, null,
                                null);
                    }
                    sma.resetStateMachineReactively(ct).subscribe();
                }

        );

        sm.startReactively().subscribe();


        return sm;
    }


    @Override
    @Transactional
    public Employee changeState(Long employeeId, EmployeeEvents event) {
        Employee employee = getById(employeeId);
        StateMachine<EmployeeStates, EmployeeEvents> sm = buildStateMachine(employee);
        return sendEventAndSaveNewState(employee, sm, event);
    }


}
