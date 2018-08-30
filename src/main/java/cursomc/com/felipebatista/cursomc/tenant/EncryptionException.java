package cursomc.com.felipebatista.cursomc.tenant;

public class EncryptionException extends Exception {
	private static final long serialVersionUID = 7668011950345758410L;

	/**
	 * Cria a exce��o com a mensagem padr�o da classe <code>Exeption</code>.
	 */
	public EncryptionException() {
		super();
	}

	/**
	 * Cria a exce��o com a mensagem passada como par�metro.
	 * 
	 * @param msg
	 *            Mensagem espec�fica da exce��o.
	 */
	public EncryptionException(String msg) {
		super(msg);
	}

	/**
	 * Cria a exce��o com a mensagem e causa passadas por par�metro.
	 * 
	 * @param msg
	 *            Mensagem espec�fica da exce��o.
	 * @param cause
	 *            Indica a exce��o que gerou essa exce�ao.
	 */
	public EncryptionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
