import java.util.Scanner;

import Model.DESModel;

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
				
				break;
			case 2:
				// Chiffrement RSA avec module multiple
				break;
			case 3:
				// Signature RSA avec module multiple
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
