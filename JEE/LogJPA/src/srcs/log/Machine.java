package srcs.log;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Machine
 *
 */
@Entity

public class Machine implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String nom;
	private static final long serialVersionUID = 1L;

	public Machine() {
		super();
	}   
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public String getNom() {
		return this.nom;
	}

	public void setNom(String name) {
		this.nom = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Machine other = (Machine) obj;
		if (id != other.id)
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}
   
}
