package com.workmotion.entity;

import com.workmotion.state_enum.EmployeeEvents;
import com.workmotion.state_enum.EmployeeStates;
import lombok.*;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Employee {
	
	@Id
	@GeneratedValue
	private Long employeeId;
	private String firstName;
	private String lastName;
	private String mail;
	private Integer age;
	@ElementCollection
	private List<EmployeeStates> state=List.of(EmployeeStates.ADDED);
	
	@ElementCollection
	private List<String> history;

	public void setState(EmployeeEvents event, List<EmployeeStates> state) {
		state = state;
		if (event == null) {
			getHistory().add( "Initial State:" + state.toString());
		}else {
			getHistory().add("Event:"+ event+ " State:" + state.toString());
		}
		
	}

}
