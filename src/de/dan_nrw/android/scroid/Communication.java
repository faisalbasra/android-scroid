package de.dan_nrw.android.scroid;


/**
 * @author Daniel Czerwonk
 *
 */
public class Communication {

	// Fields
	private final Type type;
	private final String value;
	
	// Constructors
	/**
	 * Method for creating a new instance of Communication
	 * @param type
	 * @param value
	 */
	public Communication(Type type, String value) {
	    super();
	    
	    this.type = type;
	    this.value = value;
    }
    
    // Methods 
    /**
     * @return Type (Mobile, E-Mail)
     */
    public Type getType() {
	    return type;
    }

	/**
     * @return Address / Number
     */
    public String getValue() {
	    return value;
    }

	// Inner-Classes
	public enum Type {
		Mobile,
		Email
	}
}
