package srcs.log;

import java.io.Serializable;
import javax.persistence.*;


@Embeddable
public class DateLog implements Serializable {
	
	private long annee;
	private long mois;
	private long jour;
	private long heure;
	private long minute;
	private long seconde;
	private long milliseconde;
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return "DateLog [annee=" + annee + ", mois=" + mois + ", jour=" + jour + ", heure=" + heure + ", minute="
				+ minute + ", seconde=" + seconde + ", milliseconde=" + milliseconde + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (annee ^ (annee >>> 32));
		result = prime * result + (int) (heure ^ (heure >>> 32));
		result = prime * result + (int) (jour ^ (jour >>> 32));
		result = prime * result + (int) (milliseconde ^ (milliseconde >>> 32));
		result = prime * result + (int) (minute ^ (minute >>> 32));
		result = prime * result + (int) (mois ^ (mois >>> 32));
		result = prime * result + (int) (seconde ^ (seconde >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateLog other = (DateLog) obj;
		if (annee != other.annee)
			return false;
		if (heure != other.heure)
			return false;
		if (jour != other.jour)
			return false;
		if (milliseconde != other.milliseconde)
			return false;
		if (minute != other.minute)
			return false;
		if (mois != other.mois)
			return false;
		if (seconde != other.seconde)
			return false;
		return true;
	}


	public DateLog() {
		super();
	}   
	public long getAnnee() {
		return this.annee;
	}

	public void setAnnee(long annee) {
		this.annee = annee;
	}   
	public long getMois() {
		return this.mois;
	}

	public void setMois(long mois) {
		this.mois = mois;
	}   
	public long getJour() {
		return this.jour;
	}

	public void setJour(long jour) {
		this.jour = jour;
	}   
	public long getHeure() {
		return this.heure;
	}

	public void setHeure(long heure) {
		this.heure = heure;
	}   
	public long getMinute() {
		return this.minute;
	}

	public void setMinute(long minute) {
		this.minute = minute;
	}   
	public long getSeconde() {
		return this.seconde;
	}

	public void setSeconde(long seconde) {
		this.seconde = seconde;
	}   
	public long getMilliseconde() {
		return this.milliseconde;
	}

	public void setMilliseconde(long milliseconde) {
		this.milliseconde = milliseconde;
	}
   
}
