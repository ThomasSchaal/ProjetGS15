package Model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

//RSA avec padding PKCS
public class RSAModel {
	
	private BigInteger n, d, e, phiN, p, q;
	private String H, G, r, X, Z, Y;


	// Create new keys
	public RSAModel() {
		generateKeys();
	}
	
	// Decrypt with a given key
	public RSAModel(BigInteger n, BigInteger e){
		this.n = n;
		this.e = e;
	}
	
	//key = true if we have public key, false if we have private
	public RSAModel(String path, boolean key){
		try {
			List<String> lines = Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
			if (key){
				this.e = new BigInteger(lines.get(0));
				this.n = new BigInteger(lines.get(1));
			} else {
				this.d = new BigInteger(lines.get(0));
				this.p = new BigInteger(lines.get(1));
				this.q = new BigInteger(lines.get(2));
				this.n = p.multiply(q);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// Generate the needed keys
	public void generateKeys(){
		SecureRandom random = new SecureRandom();
		
		p = new BigInteger(2048, 100, random);
		q = new BigInteger(2048, 100, random);
		
		//n = p*q
		n = p.multiply(q);
		//phi(n) = (p-1)(q-1)
		phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		
		//Selecting at random the encryption key e such that  1<e<ø(N)  and gcd(e,ø(N))=1
		e = new BigInteger("3");
	    while (phiN.gcd(e).intValue() > 1) {
	      e = e.add(new BigInteger("2"));
	    }
	    
	    //Solve following equation to find decryption key d such that e*d=1 mod ø(N) and 0<d<N
	    d = e.modInverse(phiN);	
	    
	    //Save results in a file
	    writePublicKeysInFile();
	    writePrivateKeysInFile();
	}
	
	
	
	public BigInteger encrypt(BigInteger message) {
		try {
			message = padding(message);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return message.modPow(e, n); 
	}
	
	public BigInteger decrypt(BigInteger cypherMessage) {
		try {
			cypherMessage = depadding(cypherMessage.modPow(d, n));
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cypherMessage;
	}
	
	public BigInteger decryptMultiPrime(BigInteger cypherMessage){
		    
		BigInteger dp = d.mod(p.subtract(BigInteger.ONE));
		BigInteger dq = d.mod(q.subtract(BigInteger.ONE));
		
		BigInteger invq = q.modInverse(p);
		
		BigInteger mp = cypherMessage.modPow(dp, p);
		BigInteger mq = cypherMessage.modPow(dq, q);
		
		//k = (mp − mq)q
		BigInteger k = (mp.subtract(mq)).multiply(q);

		//return m = m'q^(-1)+mq
		BigInteger m = k.multiply(invq).add(mq);
		
		try {
			m = depadding(m);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}
	
	
	
	//Publish public encryption key: PU={e, n}
	public void writePublicKeysInFile() {
		List<String> lines = Arrays.asList(e.toString(), n.toString());
		Path file = Paths.get("publicKey.txt");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//Keep secret private decryption key: PR={d, p, q}
	public void writePrivateKeysInFile(){
		List<String> lines = Arrays.asList(d.toString(), p.toString(), q.toString());
		Path file = Paths.get("privateKeys.txt");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public BigInteger generateSignature(String plainText) throws NoSuchAlgorithmException {
		//using private key
		/*
		 * hash(plaintext)^d mod n = m'
		 */		
		BigInteger hashPlainText = hashPlaintext(plainText);
		System.out.println("hashPlainText before : " + hashPlainText);
		BigInteger signature = hashPlainText.modPow(d,n);			
		return signature; //return hash(plaintext)^d [n]		
	}
	
	
	public Boolean verifySignature(String plainText, BigInteger signature) throws NoSuchAlgorithmException {
		/*
		 * H(m) == m'^e mod n 
		 */
		//We need hash(plaintext) to compare
		BigInteger hashPlainText = hashPlaintext(plainText);
		
		//We need to compute signature^e[n]		
		BigInteger verify = signature.modPow(e,n);
		
		//If hash(plaintext) = signature^e[n], it means the signature is verified
		return verify.compareTo(hashPlainText) == 0;
	}
	
	
	public Boolean verifySignatureMultiPrime(String plainText, BigInteger signature) throws NoSuchAlgorithmException {
		/*
		 * H(m) == m'^e mod n 
		 */
		//We need hash(plaintext) to compare
		BigInteger hashPlainText = hashPlaintext(plainText);
		
		//We need to compute signature^e[n]	 but in multiprime way	
		BigInteger ep = e.mod(p.subtract(BigInteger.ONE));
		BigInteger eq = e.mod(q.subtract(BigInteger.ONE));
		
		BigInteger invq = q.modInverse(p);
		
		BigInteger signaturep = signature.modPow(ep, p);
		BigInteger signatureq = signature.modPow(eq, q);
		
		//k = (signaturep − signatureq)q
		BigInteger k = (signaturep.subtract(signatureq)).multiply(q);

		//return m = m'q^(-1)+mq
		BigInteger signatureHash = k.multiply(invq).add(signatureq);		
		
		
		//If hash(plaintext) = signature^e[n], it means the signature is verified
		return signatureHash.compareTo(hashPlainText) == 0;
	}
	
	
	public BigInteger hashPlaintext(String plainText) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(plainText.getBytes(StandardCharsets.UTF_8));
		byte[] digest = md.digest();
		
		StringBuffer hashPlainText = new StringBuffer();
		for (byte b : digest) { //print byte[] in hexa
			hashPlainText.append(String.format("%02x", b & 0xff));
		}
		
		BigInteger hash = new BigInteger(hashPlainText.toString().getBytes());
		
		return hash;
	}
	
	
	public BigInteger padding(BigInteger message) throws NoSuchAlgorithmException
	{
		String m = new String(message.toByteArray());
		
		r= "0000000000000000"; // We fix r.length = 16
		G = new String(hashPlaintext(m).toByteArray()); //sha-256(m)

		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < m.length(); i++)
			sb.append((char)(m.charAt(i) ^ G.charAt(i % G.length()))); // m XOR G
		
		
		X = sb.toString(); // X = m XOR G
		H = new String(hashPlaintext(X).toByteArray()); // H = sha-256(X)
		
		sb = new StringBuilder();
		for(int i = 0; i < r.length(); i++)
			sb.append((char)(r.charAt(i) ^ H.charAt(i % H.length()))); //r XOR H
		
		Y = sb.toString(); // Y = r XOR H and we already had X = m XOR G
		
		BigInteger msg = new BigInteger(Y.getBytes());

		return msg;

	}

	public BigInteger depadding(BigInteger cyperMessage) throws FileNotFoundException, UnsupportedEncodingException
	{
		// Depadding
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < Y.length(); i++)
			sb.append((char)(Y.charAt(i) ^ H.charAt(i % H.length()))); 
		
		r = sb.toString(); // we retrieve r = Y XOR H
		
		sb = new StringBuilder();
		for(int i = 0; i < X.length(); i++)	
			sb.append((char)(X.charAt(i) ^ G.charAt(i % G.length())));
		
		String m = sb.toString(); // w retrieve message = X XOR G 
		
		BigInteger msg = new BigInteger(m.getBytes());

		return msg;

	}

}
