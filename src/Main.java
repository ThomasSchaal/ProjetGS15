import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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
				
				String cle = "1234567890123456789012345678901834567890123456789012345678901234";
				byte[] key = cle.getBytes();
				String pathPlainmessageDES = "DESplainmessage.txt";
				DESModel.encrypt(pathPlainmessageDES, key); 
				
				try{
					String[] aesEncrypt = new String[3];
					aesEncrypt[0] = "e";
					aesEncrypt[1] = "input.txt"; // key 128 bits 
					aesEncrypt[2] = "DEScyphermessage.enc.txt";
					AESModel aesE = new AESModel(aesEncrypt);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				try {
					String[] aesDecrypt = new String[3];
					aesDecrypt[0] = "d";
					aesDecrypt[1] = "input.txt"; // key 128 bits
					aesDecrypt[2] = "DEScyphermessage.enc.txt.enc.txt";
					AESModel aesD = new AESModel(aesDecrypt);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String pathCiphermessageDES = "DEScyphermessage.enc.txt.enc.txt.dec";
				DESModel.decrypt(pathCiphermessageDES, key); //encrypted message will be store in a file
				

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
				// Signature RSA 
				String msg = "Thomas et Alexandre galerent en crypto";
			    System.out.println("Plaintext: " + msg);
			    BigInteger signature;
				try {
					RSAModel rsaa = new RSAModel();
					signature = rsaa.generateSignature(msg);
				    System.out.println("Signature: " + signature);				    
				    Boolean verif = rsaa.verifySignature(msg, signature);
				    System.out.println("Verif: " + verif);
				    //Avec modulo multiple
				    Boolean verifMultiPrime = rsaa.verifySignatureMultiPrime(msg, signature);
				    System.out.println("Multi prime verif : " + verifMultiPrime);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				break;
			default:
				System.out.println("Mauvais choix");
				break;					
			}
		} while (choice < 1 || choice > 3);
		
	}
	
}
