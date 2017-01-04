import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import Model.AESModel;
import Model.DESModel;
import Model.RSAModel;

/**
 * Class principale qui va permettre à l'utilisateur 
 * de faire un choix parmi les différents algorithme de chiffrement
 * @author daussy
 *
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("->1<- Chiffrement symétrique VCES");
		System.out.println("->2<- Chiffrement RSA avec module multiple");
		System.out.println("->3<- Signature RSA avec module multiple");
		System.out.println("->4<- Déchiffrement RSA");
		System.out.println("->5<- Vérifier une signature RSA");
		
		//Input de l'user
		int choice = 0;
		do {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Selectionner votre fonction de chiffrement : ");
			String choix = scanner.nextLine();
			
			try {
				choice = Integer.parseInt(choix);
			} catch (Exception e) {
				// TODO: handle exception
			}
			//Renvoie vers le choix
			switch (choice){
			case 1:
				// Chiffrement symétrique VCES
				
				//Test DES
				String message = "Message que je veux crypter on va faire en sorte qu'il dépasse les \n beaucoup de caractères pour voir ce que ça fait";
				System.out.println("Clear message : " + message);
				byte[] data = message.getBytes();
				String cle = "1234567890123456789012345678901834567890123456789012345678901234";
				byte[] key = cle.getBytes();
				
				byte[] encrypted = DESModel.encrypt(data, key);
				String encryptedString = new String(encrypted);
				System.out.println("Encrypted message : " + encryptedString);
				
				
				byte[] decrypted = DESModel.decrypt(encrypted, key);		
				String test = new String(decrypted);
				System.out.println("Decrypted message : " + test);
				//Fin test DES
				
				
				//Test AES
				try {
					String[] aesEncrypt = new String[3];
					aesEncrypt[0] = "e";
					aesEncrypt[1] = "input.txt";
					aesEncrypt[2] = "inputFile.txt";
					AESModel aesE = new AESModel(aesEncrypt);
					
					String[] aesDecrypt = new String[3];
					aesDecrypt[0] = "d";
					aesDecrypt[1] = "input.txt";
					aesDecrypt[2] = "inputFile.txt.enc";
					AESModel aesD = new AESModel(aesDecrypt);							
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Fin test AES
				
				break;
			case 2:
				// Chiffrement RSA avec module multiple				
				//Test RSA
				RSAModel rsa = new RSAModel();

			    String text1 = "Thomas et Alexandre galerent en crypto";
			    System.out.println("Plaintext: " + text1);
			    BigInteger plaintext = new BigInteger(text1.getBytes());

			    BigInteger ciphertext = rsa.encrypt(plaintext);
			    System.out.println("Ciphertext: " + ciphertext);
			    plaintext = rsa.decrypt(ciphertext);

			    String text2 = new String(plaintext.toByteArray());
			    System.out.println("Plaintext (normal): " + text2);
			    
			    BigInteger txt3 = rsa.decryptMultiPrime(ciphertext);
			    String text3 = new String(txt3.toByteArray());
			    System.out.println("Plaintext (multiprime):" + text3);
				//Fin test RSA				
				break;
			case 3:
				// Signature RSA avec module multiple
				String msg = "Thomas et Alexandre galerent en crypto";
			    System.out.println("Plaintext: " + msg);
			    BigInteger signature;
				try {
					RSAModel rsaa = new RSAModel();
					signature = rsaa.generateSignature(msg);
				    System.out.println("Signature: " + signature);				    
				    Boolean verif = rsaa.verifySignature(msg, signature);
				    System.out.println("Verif: " + verif);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				break;
			case 4:
				// Déchiffrement RSA
				break;
			case 5:
				// Vérifier une signature RSA
				break;
			default:
				System.out.println("Mauvais choix");
				break;					
			}
		} while (choice < 1 || choice > 5);
		
	}
	
}
