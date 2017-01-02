package Model;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

//RSA avec padding PKCS
public class RSAModel {
	
	private BigInteger n, d, e, phiN, p, q;

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
		
		p = new BigInteger(1024/2, 100, random);
		q = new BigInteger(1024/2, 100, random);
		
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
		return message.modPow(e, n); 
	}
	
	public BigInteger decrypt(BigInteger cypherMessage) {
		return cypherMessage.modPow(d, n);
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
	
	public void generateSignature(String privateKey) {}
	
	public void verifySignature(String publicKey) {}
	
	/*
	public String bytesToString(byte[] b) {
	    byte[] b2 = new byte[b.length + 1];
	    b2[0] = 1;
	    System.arraycopy(b, 0, b2, 1, b.length);
	    return new BigInteger(b2).toString(36);
	}

	public byte[] stringToBytes(String s) {
	    byte[] b2 = new BigInteger(s, 36).toByteArray();
	    return Arrays.copyOfRange(b2, 1, b2.length);
	}
	*/

	public BigInteger getN() {
		return n;
	}


	public void setN(BigInteger n) {
		this.n = n;
	}


	public BigInteger getD() {
		return d;
	}


	public void setD(BigInteger d) {
		this.d = d;
	}


	public BigInteger getE() {
		return e;
	}


	public void setE(BigInteger e) {
		this.e = e;
	}


	public BigInteger getPhiN() {
		return phiN;
	}


	public void setPhiN(BigInteger phiN) {
		this.phiN = phiN;
	}


	public BigInteger getP() {
		return p;
	}


	public void setP(BigInteger p) {
		this.p = p;
	}


	public BigInteger getQ() {
		return q;
	}


	public void setQ(BigInteger q) {
		this.q = q;
	}
	
}
