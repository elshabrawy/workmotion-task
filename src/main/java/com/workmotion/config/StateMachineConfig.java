package com.workmotion.config;

import com.workmotion.exception.custom.NotValidState;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.workmotion.state_enum.EmployeeEvents;
import com.workmotion.state_enum.EmployeeStates;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;


@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<EmployeeStates, EmployeeEvents>{
	
	@Override
    public void configure(StateMachineStateConfigurer<EmployeeStates, EmployeeEvents> states) throws Exception {		

		 states
         .withStates()
         .initial(EmployeeStates.ADDED)
         .fork(EmployeeStates.FORK)
         .state(EmployeeStates.IN_CHECK)
         .join(EmployeeStates.JOIN)
         .state(EmployeeStates.APPROVED)
         .end(EmployeeStates.ACTIVE)
         .end(EmployeeStates.REJECTED)
         
         .and()
         .withStates()
             .parent(EmployeeStates.IN_CHECK)
             .initial(EmployeeStates.SECURITY_CHECK_STARTED)
             .end(EmployeeStates.SECURITY_CHECK_FINISHED)
         .and()
         .withStates()
             .parent(EmployeeStates.IN_CHECK)
             .initial(EmployeeStates.WORK_PERMIT_CHECK_STARTED)
				 .state(EmployeeStates.WORK_PERMIT_CHECK_VERIFIED)
             .end(EmployeeStates.WORK_PERMIT_CHECK_FINISHED);
		 
		   }
	@Override
    public void configure(StateMachineTransitionConfigurer<EmployeeStates, EmployeeEvents> transitions) throws Exception {
		
		transitions.withExternal()
        .source(EmployeeStates.ADDED).target(EmployeeStates.IN_CHECK).event(EmployeeEvents.IN_CHECK)
        .and().withExternal()
            .source(EmployeeStates.SECURITY_CHECK_STARTED).target(EmployeeStates.SECURITY_CHECK_FINISHED).event(EmployeeEvents.SECURITY_FINISH)
				.and().withExternal().source(EmployeeStates.WORK_PERMIT_CHECK_STARTED).target(EmployeeStates.WORK_PERMIT_CHECK_VERIFIED).event(EmployeeEvents.WORK_PERMIT_VERIFY)
        .and().withExternal()
            .source(EmployeeStates.WORK_PERMIT_CHECK_VERIFIED).target(EmployeeStates.WORK_PERMIT_CHECK_FINISHED).event(EmployeeEvents.WORK_PERMIT_FINISH)
        .and()
        .withFork()
            .source(EmployeeStates.FORK)
            .target(EmployeeStates.IN_CHECK)
        .and()
        .withJoin()
            .source(EmployeeStates.IN_CHECK)
            .target(EmployeeStates.JOIN)
         .and().withExternal()
         .source(EmployeeStates.JOIN).target(EmployeeStates.APPROVED)
         .and().withExternal()
         	.source(EmployeeStates.APPROVED).target(EmployeeStates.ACTIVE).event(EmployeeEvents.FINAL_APPROVE)
         
         .and().withExternal()
         .source(EmployeeStates.ADDED).target(EmployeeStates.REJECTED).event(EmployeeEvents.REJECT_ADDED)
         
         .and().withExternal()
         .source(EmployeeStates.SECURITY_CHECK_STARTED).target(EmployeeStates.REJECTED).event(EmployeeEvents.REJECT_SECURITY_CHECK)
         
         .and().withExternal()
         .source(EmployeeStates.WORK_PERMIT_CHECK_STARTED).target(EmployeeStates.REJECTED).event(EmployeeEvents.REJECT_WORK_PERMIT)
         .and().withExternal()
         .source(EmployeeStates.APPROVED).target(EmployeeStates.REJECTED).event(EmployeeEvents.FINAL_REJECT);
				
    }


	@Override
	public void configure(
			StateMachineConfigurationConfigurer
					<EmployeeStates, EmployeeEvents> config) throws Exception {

		config.withConfiguration()
				.listener(listener())
				.autoStartup(true);
	}

	private StateMachineListener<EmployeeStates, EmployeeEvents> listener() {
		return new StateMachineListenerAdapter<EmployeeStates, EmployeeEvents>(){

			@Override
			public void eventNotAccepted(Message<EmployeeEvents> event) {
				throw new NotValidState("event not accepted: "+ event);
			}
		};
	}


}
