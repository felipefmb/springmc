package cursomc.com.felipebatista.cursomc.tenant;

import java.io.*;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

public final class EncryptionUtils {
	public static final String AES = "AES";
	public static final String AES_CTR = "AES/CTR/NoPadding";
	public static final String DEFAULT_ALGORITHM_ONE_WAY = EncryptionUtils.SHA256;
	public static final AlgorithmParameterSpec DEFAULT_ALGORITHM_PARAM_SPEC = new PBEParameterSpec(
	        new byte[] { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56,
	                (byte) 0x35, (byte) 0xE3, (byte) 0x03 }, 20);
	public static final String DEFAULT_ALGORITHM_SIGNATURE = EncryptionUtils.SHA1_WITH_RSA;
	public static final String DEFAULT_ALGORITHM_TWO_WAY = EncryptionUtils.PBE_MD5_AND_DES;
	public static final String DEFAULT_ENCODING = EncryptionUtils.ENCODING_UTF8;
	public static final int DEFAULT_RSA_KEYSIZE = 1024;
	public static final String ENCODING_ISO88591 = "ISO-8859-1";
	public static final String ENCODING_UTF8 = "UTF8";
	public static final String MD5 = "MD5";
	public static final String PBE_MD5_AND_DES = "PBEWithMD5AndDES";
	public static final String RSA = "RSA";
	public static final String SHA1_WITH_RSA = "SHA1withRSA";
	public static final String SHA256 = "SHA-256";

	private EncryptionUtils() {
		// Classe utilit�ria
	}

	/**
	 * Descriptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a
	 * descriptografia j� � automaticamente aplicado o algoritmo base64 eliminando apenas as quebras
	 * de linhas.
	 */
	public static String decrypt(String data, Cipher cipher) throws EncryptionException {
		return EncryptionUtils.decrypt(data, cipher, Base64.DONT_BREAK_LINES);
	}

	/**
	 * Descriptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a
	 * descriptografia j� � automaticamente aplicado o algoritmo base64 considerando as op��es
	 * passadas por par�metros. As op��es dispon�veis encontram-se na pr�pria classe Base64. Usando
	 * este m�todo fica sob responsabilidade do chamador eliminar as quebras de linhas.
	 * 
	 * @throws EncryptionException
	 */
	public static String decrypt(String data, Cipher cipher, int base64Options)
	        throws EncryptionException {
		return EncryptionUtils.decrypt(data, cipher, EncryptionUtils.DEFAULT_ENCODING,
		        base64Options);
	}

	public static String decrypt(String data, Cipher cipher, String encoding, int base64Options)
	        throws EncryptionException {
		byte[] dec = Base64.decode(data, base64Options);
		try {
			byte[] utf8 = cipher.doFinal(dec);

			return new String(utf8, encoding);
		} catch (IllegalBlockSizeException e) {
			throw new EncryptionException(e.getMessage());
		} catch (BadPaddingException e) {
			throw new EncryptionException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException(e.getMessage());
		}
	}

	/**
	 * Descriptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a
	 * descriptografia j� � automaticamente aplicado o algoritmo base64 eliminando apenas as quebras
	 * de linhas.
	 */
	public static String decrypt(String data, String passPhrase) throws EncryptionException {
		return EncryptionUtils.decrypt(data, passPhrase, Base64.DONT_BREAK_LINES);
	}

	/**
	 * Descriptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a
	 * descriptografia j� � automaticamente aplicado o algoritmo base64 considerando as op��es
	 * passadas por par�metros. As op��es dispon�veis encontram-se na pr�pria classe Base64. Usando
	 * este m�todo fica sob responsabilidade do chamador eliminar as quebras de linhas.
	 */
	public static String decrypt(String data, String passPhrase, int base64Options)
	        throws EncryptionException {
		Cipher cipher = EncryptionUtils.getCipher(Cipher.DECRYPT_MODE,
		        EncryptionUtils.generateSecretKey(passPhrase));

		return EncryptionUtils.decrypt(data, cipher, base64Options);
	}

	/**
	 * Descriptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a
	 * descriptografia j� � automaticamente aplicado o algoritmo base64 eliminando apenas as quebras
	 * de linhas.
	 */
	public static String decryptAES(String data, String passPhrase) throws EncryptionException {
		return EncryptionUtils.decryptAES(data, passPhrase, Base64.DONT_BREAK_LINES);
	}

	/**
	 * Descriptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a
	 * descriptografia j� � automaticamente aplicado o algoritmo base64 considerando as op��es
	 * passadas por par�metros. As op��es dispon�veis encontram-se na pr�pria classe Base64. Usando
	 * este m�todo fica sob responsabilidade do chamador eliminar as quebras de linhas.
	 */
	public static String decryptAES(String data, String passPhrase, int base64Options)
	        throws EncryptionException {
		return EncryptionUtils.decryptAES(data, passPhrase, EncryptionUtils.DEFAULT_ENCODING,
		        base64Options);
	}

	public static String decryptAES(String data, String passPhrase, String encoding,
	        int base64Options) throws EncryptionException {
		MessageDigest result = null;
		try {
			result = MessageDigest.getInstance(EncryptionUtils.MD5);
			byte[] secretKey = result.digest(passPhrase.getBytes());
			SecretKeySpec aesKey = new SecretKeySpec(secretKey, EncryptionUtils.AES);
			IvParameterSpec ivSpec = new IvParameterSpec(secretKey);
			Cipher decryptCipher = EncryptionUtils.getCipher(Cipher.DECRYPT_MODE,
			        EncryptionUtils.AES_CTR, aesKey, ivSpec);
			return EncryptionUtils.decrypt(data, decryptCipher, encoding, base64Options);
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException("O algoritmo a ser utilizado para o hash n�o existe.", e);
		}
	}

	/**
	 * Criptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a criptografia
	 * j� � automaticamente aplicado o algoritmo base64 eliminando apenas as quebras de linhas.
	 */
	public static String encrypt(String data, Cipher cipher) throws EncryptionException {
		return EncryptionUtils.encrypt(data, cipher, Base64.DONT_BREAK_LINES);
	}

	/**
	 * Criptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a criptografia
	 * j� � automaticamente aplicado o algoritmo base64 considerando as op��es passadas por
	 * par�metros. As op��es dispon�veis encontram-se na pr�pria classe Base64. Usando este m�todo
	 * fica sob responsabilidade do chamador eliminar as quebras de linhas.
	 * 
	 * @throws EncryptionException
	 */
	public static String encrypt(String data, Cipher cipher, int base64Options)
	        throws EncryptionException {
		return EncryptionUtils.encrypt(data, cipher, EncryptionUtils.DEFAULT_ENCODING,
		        base64Options);
	}

	public static String encrypt(String data, Cipher cipher, String encoding, int base64Options)
	        throws EncryptionException {
		byte[] utf8;
		try {
			utf8 = data.getBytes(encoding);

			byte[] enc = cipher.doFinal(utf8);

			return Base64.encodeBytes(enc, base64Options);
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			throw new EncryptionException(e.getMessage());
		} catch (BadPaddingException e) {
			throw new EncryptionException(e.getMessage());
		}
	}

	/**
	 * Criptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a criptografia
	 * j� � automaticamente aplicado o algoritmo base64 eliminando apenas as quebras de linhas.
	 */
	public static String encrypt(String data, String passPhrase) throws EncryptionException {
		return EncryptionUtils.encrypt(data, passPhrase, Base64.DONT_BREAK_LINES);
	}

	/**
	 * Criptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a criptografia
	 * j� � automaticamente aplicado o algoritmo base64 considerando as op��es passadas por
	 * par�metros. As op��es dispon�veis encontram-se na pr�pria classe Base64. Usando este m�todo
	 * fica sob responsabilidade do chamador eliminar as quebras de linhas.
	 */
	public static String encrypt(String data, String passPhrase, int base64Options)
	        throws EncryptionException {
		Cipher cipher = EncryptionUtils.getCipher(Cipher.ENCRYPT_MODE,
		        EncryptionUtils.generateSecretKey(passPhrase));

		return EncryptionUtils.encrypt(data, cipher, base64Options);
	}

	/**
	 * Criptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a criptografia
	 * j� � automaticamente aplicado o algoritmo base64 eliminando apenas as quebras de linhas.
	 */
	public static String encryptAES(String data, String passPhrase) throws EncryptionException {
		return EncryptionUtils.encryptAES(data, passPhrase, Base64.DONT_BREAK_LINES);
	}

	/**
	 * Criptografa uma string baseado numa chave passada por par�metro. Ap�s aplicada a criptografia
	 * j� � automaticamente aplicado o algoritmo base64 considerando as op��es passadas por
	 * par�metros. As op��es dispon�veis encontram-se na pr�pria classe Base64. Usando este m�todo
	 * fica sob responsabilidade do chamador eliminar as quebras de linhas.
	 */
	public static String encryptAES(String data, String passPhrase, int base64Options)
	        throws EncryptionException {
		return EncryptionUtils.encryptAES(data, passPhrase, EncryptionUtils.DEFAULT_ENCODING,
		        base64Options);
	}

	public static String encryptAES(String data, String passPhrase, String encoding,
	        int base64Options) throws EncryptionException {
		MessageDigest result = null;
		try {
			result = MessageDigest.getInstance(EncryptionUtils.MD5);
			byte[] secretKey = result.digest(passPhrase.getBytes());
			SecretKeySpec aesKey = new SecretKeySpec(secretKey, EncryptionUtils.AES);
			IvParameterSpec ivSpec = new IvParameterSpec(secretKey);
			Cipher encryptCipher = EncryptionUtils.getCipher(Cipher.ENCRYPT_MODE,
			        EncryptionUtils.AES_CTR, aesKey, ivSpec);
			return EncryptionUtils.encrypt(data, encryptCipher, encoding, base64Options);
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException("O algoritmo a ser utilizado para o hash n�o existe.", e);
		}
	}

	public static String encryptOneWay(String data) throws EncryptionException {
		return EncryptionUtils.encryptOneWay(EncryptionUtils.DEFAULT_ALGORITHM_ONE_WAY, data);
	}

	public static String encryptOneWay(String algorithm, String data) throws EncryptionException {
		MessageDigest result = null;

		try {
			result = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(
			        "O algoritmo a ser utilizado para criptografia n�o existe.", e);
		}

		byte[] bytes = result.digest(data.getBytes());

		return Base64.encodeBytes(bytes, Base64.DONT_BREAK_LINES);
	}

	/*
	 * M�todo duplicado pois necessitou utilizar um encoding diferente e n�o seria correto alterar
	 * o comportamento padr�o do m�todo
	 */
	public static String encryptOneWay(String algorithm, String encoding, String data)
	        throws EncryptionException {
		MessageDigest result = null;

		try {

			result = MessageDigest.getInstance(algorithm);
			byte[] bytes = result.digest(data.getBytes(encoding));
			return Base64.encodeBytes(bytes, Base64.DONT_BREAK_LINES);
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(
			        "O algoritmo a ser utilizado para criptografia n�o existe.", e);
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException(
			        "O encoding a ser utilizado para criptografia n�o existe.", e);
		}

	}

	public static void generateAndSaveKey(int keySize, String privateFileName, String publicFileName)
	        throws EncryptionException {
		EncryptionUtils.generateAndSaveKey(keySize, EncryptionUtils.RSA, privateFileName,
		        publicFileName);
	}

	/**
	 * Gera um par de chaves para o algor�tmo passado por par�metro e salva em disco.
	 */
	public static void generateAndSaveKey(int keySize, String algorithm, String privateFileName,
	        String publicFileName) throws EncryptionException {
		KeyPairGenerator kpg = null;
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try {
			kpg = KeyPairGenerator.getInstance(algorithm);
			kpg.initialize(keySize);
			KeyPair keys = kpg.generateKeyPair();
			// PKCS #8 for Private, X.509 for Public
			// File will contain OID 1.2.840.11359.1.1.1 (RSA)
			// http://java.sun.com/j2se/1.4.2/docs/api/java/security/Key.html

			fos = new FileOutputStream(privateFileName);
			dos = new DataOutputStream(fos);
			dos.write(keys.getPrivate().getEncoded());
			dos.close();
			fos.close();

			fos = new FileOutputStream(publicFileName);
			dos = new DataOutputStream(fos);
			dos.write(keys.getPublic().getEncoded());
			dos.close();
			fos.close();
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e.getMessage());
		} catch (IOException e) {
			throw new EncryptionException(e.getMessage());
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					throw new EncryptionException(e.getMessage());
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new EncryptionException(e.getMessage());
				}
			}
		}
	}

	public static void generateAndSaveKey(String privateFileName, String publicFileName)
	        throws EncryptionException {
		EncryptionUtils.generateAndSaveKey(EncryptionUtils.DEFAULT_RSA_KEYSIZE,
		        EncryptionUtils.RSA, privateFileName, publicFileName);
	}

	public static Key generateSecretKey(String passPhrase) throws EncryptionException {
		return EncryptionUtils.generateSecretKey(EncryptionUtils.DEFAULT_ALGORITHM_TWO_WAY,
		        passPhrase);
	}

	public static Key generateSecretKey(String algorithm, String passPhrase)
	        throws EncryptionException {
		try {
			return SecretKeyFactory.getInstance(algorithm).generateSecret(
			        new PBEKeySpec(passPhrase.toCharArray()));
		} catch (InvalidKeySpecException e) {
			throw new EncryptionException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e.getMessage());
		}
	}

	public static Cipher getCipher(int cipherMode, Key key) throws EncryptionException {
		if (key == null) {
			throw new EncryptionException(
			        "N�o � poss�vel instanciar um algoritmo de criptografia sem especificar a chave.");
		}

		return EncryptionUtils.getCipher(cipherMode, key.getAlgorithm(), key,
		        EncryptionUtils.DEFAULT_ALGORITHM_PARAM_SPEC);
	}

	public static Cipher getCipher(int cipherMode, String cipherAlgorithm, Key key,
	        AlgorithmParameterSpec spec) throws EncryptionException {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(cipherAlgorithm);
			if (spec == null) {
				cipher.init(cipherMode, key);
			} else {
				cipher.init(cipherMode, key, spec);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			throw new EncryptionException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new EncryptionException(e.getMessage(), e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new EncryptionException(e.getMessage(), e);
		}

		return cipher;
	}

	/**
	 * Permite retornar um par de chaves a partir de um arquivo.
	 * 
	 * @param privateFileName
	 *            Nome do arquivo com a chave privada. (permite null)
	 * @param publicFileName
	 *            Nome do arquivo com a chave p�blica. (permite null)
	 * @param algorithm
	 *            Algor�tmo da chave.
	 */
	public static KeyPair loadKeyFromFile(InputStream privateFileName, InputStream publicFileName,
	        String algorithm) throws EncryptionException {
		ByteArrayOutputStream out = null;
		try {
			KeyFactory factory = KeyFactory.getInstance(algorithm);
			PrivateKey privateKey = null;
			PublicKey publicKey = null;

			out = new ByteArrayOutputStream();
			if (privateFileName != null) {
				IOUtils.copy(privateFileName, out);
				PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(out.toByteArray());
				privateKey = factory.generatePrivate(privSpec);
				out.reset();
			}

			if (publicFileName != null) {
				IOUtils.copy(publicFileName, out);
				X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(out.toByteArray());
				publicKey = factory.generatePublic(pubSpec);
				out.reset();
			}

			return new KeyPair(publicKey, privateKey);
		} catch (IOException e) {
			throw new EncryptionException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e.getMessage());
		} catch (InvalidKeySpecException e) {
			throw new EncryptionException(e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new EncryptionException(e.getMessage());
				}
			}
		}
	}

	/**
	 * Permite retornar um par de chaves RSA a partir de um arquivo.
	 * 
	 * @param privateFileName
	 *            Nome do arquivo com a chave privada. (permite null)
	 * @param publicFileName
	 *            Nome do arquivo com a chave p�blica. (permite null)
	 */
	public static KeyPair loadKeyFromFile(String privateFileName, String publicFileName)
	        throws EncryptionException {
		return EncryptionUtils
		        .loadKeyFromFile(privateFileName, publicFileName, EncryptionUtils.RSA);
	}

	/**
	 * Permite retornar um par de chaves a partir de um arquivo.
	 * 
	 * @param privateFileName
	 *            Nome do arquivo com a chave privada. (permite null)
	 * @param publicFileName
	 *            Nome do arquivo com a chave p�blica. (permite null)
	 * @param algorithm
	 *            Algor�tmo da chave.
	 */
	public static KeyPair loadKeyFromFile(String privateFileName, String publicFileName,
	        String algorithm) throws EncryptionException {
		InputStream privateFile = null;
		InputStream publicFile = null;
		try {
			if (privateFileName != null) {
				privateFile = new FileInputStream(privateFileName);
			}
			if (publicFileName != null) {
				publicFile = new FileInputStream(publicFileName);
			}
			return EncryptionUtils.loadKeyFromFile(privateFile, publicFile, algorithm);
		} catch (FileNotFoundException e) {
			throw new EncryptionException(e.getMessage());
		} finally {
			if (privateFile != null) {
				try {
					privateFile.close();
				} catch (IOException e) {
					throw new EncryptionException(e.getMessage());
				}
			}
			if (publicFile != null) {
				try {
					publicFile.close();
				} catch (IOException e) {
					throw new EncryptionException(e.getMessage());
				}
			}
		}
	}

	/**
	 * Permite retornar uma chave a partir de um arquivo.
	 * 
	 * @param fileName
	 *            Nome do arquivo com a chave.
	 * @param filePassword
	 *            Senha do arquivo.
	 * @param alias
	 *            Nome da chave no arquivo.
	 * @param keyPassword
	 *            Senha para obter a chave do arquivo.
	 */
	public static KeyPair loadKeyFromFile(String fileName, String filePassword, String alias,
	        String keyPassword) throws EncryptionException {
		FileInputStream fis = null;

		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			fis = new FileInputStream(fileName);
			keyStore.load(fis, filePassword.toCharArray());

			PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPassword.toCharArray());

			if (privateKey == null) {
				throw new EncryptionException(
				        "N�o foi encontrada uma chave privada no arquivo das chaves.");
			}

			PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();

			return new KeyPair(publicKey, privateKey);
		} catch (Exception e) {
			throw new EncryptionException(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new EncryptionException(e.getMessage());
				}
			}
		}
	}

	/**
	 * Assina uma string baseada em uma chave privada do algoritmo RSA. Na assinatura j� � aplicado
	 * o algoritmo base64 eliminando apenas as quebras de linhas.
	 */
	public static String signerSign(PrivateKey privateKey, String message)
	        throws EncryptionException {
		return EncryptionUtils.signerSign(privateKey, EncryptionUtils.SHA1_WITH_RSA, message,
		        Base64.DONT_BREAK_LINES);
	}

	/**
	 * Assina uma string baseada em uma chave privada do algoritmo RSA. Na assinatura j� � aplicado
	 * o algoritmo base64 considerando as op��es passadas por par�metros. As op��es dispon�veis
	 * encontram-se na pr�pria classe Base64. Usando este m�todo fica sob responsabilidade do
	 * chamador eliminar as quebras de linhas.
	 */
	public static String signerSign(PrivateKey privateKey, String message, int base64Options)
	        throws EncryptionException {
		return EncryptionUtils.signerSign(privateKey, EncryptionUtils.SHA1_WITH_RSA, message,
		        base64Options);
	}

	/**
	 * Assina uma string baseada em uma chave privada do algoritmo que foi passado por par�metro. Na
	 * assinatura j� � aplicado o algoritmo base64 considerando as op��es passadas por par�metros.
	 * As op��es dispon�veis encontram-se na pr�pria classe Base64. Usando este m�todo fica sob
	 * responsabilidade do chamador eliminar as quebras de linhas.
	 */
	public static String signerSign(PrivateKey privateKey, String algorithm, String message,
	        int base64Options) throws EncryptionException {
		byte[] utf8;
		try {
			Signature signer = Signature.getInstance(algorithm);
			signer.initSign(privateKey);
			utf8 = message.getBytes("UTF-8");
			signer.update(utf8);
			byte[] bytes = signer.sign();
			return Base64.encodeBytes(bytes, base64Options);
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException(e.getMessage());
		} catch (SignatureException e) {
			throw new EncryptionException(e.getMessage());
		} catch (InvalidKeyException e) {
			throw new EncryptionException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e.getMessage());
		}
	}

	/**
	 * Valida a assinatura de uma string baseada em uma chave p�blica do algoritmo RSA. Na
	 * assinatura j� deve estar aplicado o algoritmo base64 eliminando apenas as quebras de linhas.
	 */
	public static boolean signerVerify(PublicKey publicKey, String message, String signature)
	        throws EncryptionException {
		return EncryptionUtils.signerVerify(publicKey, message, signature, Base64.DONT_BREAK_LINES);
	}

	/**
	 * Valida a assinatura de uma string baseada em uma chave p�blica do algoritmo RSA. Na
	 * assinatura j� deve estar aplicado o algoritmo base64 considerando as op��es passadas por
	 * par�metros. As op��es dispon�veis encontram-se na pr�pria classe Base64. Usando este m�todo
	 * fica sob responsabilidade do chamador eliminar as quebras de linhas.
	 */
	public static boolean signerVerify(PublicKey publicKey, String message, String signature,
	        int base64Options) throws EncryptionException {
		return EncryptionUtils.signerVerify(publicKey, EncryptionUtils.SHA1_WITH_RSA, message,
		        signature, base64Options);
	}

	/**
	 * Valida a assinatura de uma string baseada em uma chave p�blica do algoritmo que foi passado
	 * por par�metro. Na assinatura j� deve estar aplicado o algoritmo base64 considerando as op��es
	 * passadas por par�metros. As op��es dispon�veis encontram-se na pr�pria classe Base64. Usando
	 * este m�todo fica sob responsabilidade do chamador eliminar as quebras de linhas.
	 */
	public static boolean signerVerify(PublicKey publicKey, String algorithm, String message,
	        String signature, int base64Options) throws EncryptionException {
		byte[] utf8;
		try {
			Signature signer = Signature.getInstance(algorithm);
			signer.initVerify(publicKey);
			utf8 = message.getBytes("UTF-8");
			signer.update(utf8);
			return signer.verify(Base64.decode(signature, base64Options));
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException(e.getMessage());
		} catch (SignatureException e) {
			throw new EncryptionException(e.getMessage());
		} catch (InvalidKeyException e) {
			throw new EncryptionException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e.getMessage());
		}
	}
}