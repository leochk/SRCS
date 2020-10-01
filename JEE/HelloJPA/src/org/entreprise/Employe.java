package org.entreprise;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Employe
 *
 */
@Entity

public class Employe implements Serializable {

	   
	@Id
	private String name;
	private double salary;
	private static final long serialVersionUID = 1L;

	public Employe() {
		super();
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	public double getSalary() {
		return this.salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
   
}
