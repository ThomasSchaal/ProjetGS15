import java.util.Scanner;

/**
 * Class principale qui va permettre à l'utilisateur 
 * de faire un choix parmi les différents algorithme de chiffrement
 * @author daussy
 *
 */
public class Blahblah {

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
