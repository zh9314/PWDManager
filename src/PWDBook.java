import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class PWDBook {
	
	private String cryptKey = null;
	private String bookPath = null;
	public ArrayList<PWDBookEle> accounts = new ArrayList<PWDBookEle>();
	
	public PWDBook(String BookPath, String CryptKey){
		bookPath = BookPath;
		cryptKey = CryptKey;
	}
	
	public void createPWDBook(){
		try {
			FileWriter bookFile = new FileWriter(bookPath, false);
			if(cryptKey == null){
				System.out.println("ERROR: Please set crypt key first!");
				return ;
			}
			AESCrypto.setKey(cryptKey);
			String firstLine = "This is the password book!";
            AESCrypto.encrypt(firstLine);
            String cryptLine = AESCrypto.getEncryptedString();
            bookFile.write(cryptLine+"\n");
            bookFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean initBookPWDs(String userInput){
		File bookFile = new File(bookPath);
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(bookFile));
			String line = null;
			boolean firstline = true;
			while((line = in.readLine()) != null){
				AESCrypto.setKey(userInput);
				AESCrypto.decrypt(line.trim());
				String decryptLine = AESCrypto.getDecryptedString();
				if(firstline){
					firstline = false;
					if(decryptLine == null || !decryptLine.equals("This is the password book!")){
						System.out.println("The unified password is wrong!");
						return false;
					}else
						cryptKey = userInput;
				}else{
					//System.out.println(decryptLine);
					String [] fields = decryptLine.split("%\\$\\$%");
					PWDBookEle pwdBookEle = new PWDBookEle();
					pwdBookEle.tags = fields[0].split("::");
					pwdBookEle.description = fields[1];
					String [] accountNames = fields[2].split("::");
					String tmp = "";
					for(int j = 0 ; j<accountNames.length ; j++)
						tmp += accountNames[j]+"    ";
					pwdBookEle.accounts = tmp;
					pwdBookEle.pwd = fields[3];
					pwdBookEle.keyFilePath = fields[4];
					accounts.add(pwdBookEle);
					
				}
				
			} 
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	
	public String listAllTags(){
		String result = "";
		for(int i = 0 ; i<accounts.size() ; i++){
			for(int j = 0 ; j<accounts.get(i).tags.length ; j++){
				result += accounts.get(i).tags[j]+"    ";
			}result += "\n";
		}
		return result;
	}
	
	public String listPWD(String targetTag){
		String result = "";
		for(int i = 0 ; i<accounts.size() ; i++){
			String tags = ""; boolean findTag = false;
			for(int j = 0 ; j<accounts.get(i).tags.length ; j++){
				tags += accounts.get(i).tags[j]+"    ";
				if(accounts.get(i).tags[j].toLowerCase().contains(targetTag.toLowerCase()))
					findTag = true;
			}
			if(findTag){
				result += "Tags: "+tags+"\n";
				result += "Description: "+accounts.get(i).description+"\n";
				result += "Account: "+ accounts.get(i).accounts+"\n";
				result += "Password: "+accounts.get(i).pwd+"\n";
				result += "PrivateKeyFile: "+accounts.get(i).keyFilePath+"\n********************************\n\n";
			}
		}
		return result;
	}
	
	
	public String generatePWD(int n){
		StringBuffer buf = new StringBuffer();  
		if(n>0 && n<100){
			while(true){
				for(int i = 0 ; i<n ; i++){
					Random random = new Random();
					int num = random.nextInt(95)+33;
					buf.append((char)num);
				}
				String pwd = buf.toString();
				if(!pwd.contains("%$$%") && !pwd.contains("::"))
					return buf.toString();
			}
		}else
			return null;
	}
	
	public void addPWD(String accountNames, String pwd, String tags, String des, String keyPath){
		PWDBookEle pbe = new PWDBookEle();
		pbe.accounts = accountNames;
		pbe.tags = tags.split("::");
		pbe.description = des;
		pbe.pwd = pwd;
		pbe.keyFilePath = keyPath;
		accounts.add(pbe);
	}
	
	public void saveBook(){
		try {
			FileWriter bookFile = new FileWriter(bookPath, false);
			if(cryptKey == null){
				System.out.println("ERROR: Please set crypt key first!");
				return ;
			}
			AESCrypto.setKey(cryptKey);
			String firstLine = "This is the password book!";
            AESCrypto.encrypt(firstLine);
            String cryptLine = AESCrypto.getEncryptedString();
            bookFile.write(cryptLine+"\n");
            for(int i = 0 ; i<accounts.size() ; i++)
            {
            	String rec = ""; String tagString = accounts.get(i).tags[0];
            	for(int j = 1 ; j<accounts.get(i).tags.length ; j++)
            		tagString += "::"+accounts.get(i).tags[j];
            	rec += tagString+"%$$%";
            	rec += accounts.get(i).description+"%$$%";
            	rec += accounts.get(i).accounts.replaceAll("    ", "::")+"%$$%";
            	rec += accounts.get(i).pwd+"%$$%";
            	rec += accounts.get(i).keyFilePath;
            	
            	AESCrypto.setKey(cryptKey);
                AESCrypto.encrypt(rec);
                String cryptRec = AESCrypto.getEncryptedString();
            	bookFile.write(cryptRec+"\n");
            }
            
            
            bookFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
