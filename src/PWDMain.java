import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class PWDMain {
	
	public static PWDBook pb;
	
	public static void initAPP(){
		System.out.println("Welcome to use the pwd book! \n          ----Developped by Huan.");
		String curDir = new PWDMain().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = curDir.lastIndexOf('/');
		String pwdBookPath = curDir.substring(0, index+1)+"book.pwd";
		File bookFile = new File(pwdBookPath);
		Scanner sc = new Scanner(System.in);  
        sc.useDelimiter("\n");  
		if(!bookFile.exists()){
			System.out.println("The pwd book doesn't exist! Do you need to create one?(y/n)");
			while(sc.hasNext()){
				String input = sc.next();
				if(input.equals("y")){
					String pwd = initPWD();
					pb = new PWDBook(pwdBookPath, pwd);
					pb.createPWDBook();
					break;
				}else if(input.equals("n")){
					System.out.println("OK!EXIT!");
					System.exit(-1);
				}else{
					System.out.print("Invalid input! Jsut choose to input 'y' or 'n'!\ny/n? : ");
				}
			}
		}else
		{
			while(true){
				System.out.print("Please input the unified password:");
				Console console = System.console();  
		        String pwd = new String(console.readPassword());
				pb = new PWDBook(pwdBookPath, pwd);
				if(pb.initBookPWDs(pwd))
					break;
				else{
					
				}
			}
				
		}
	}
	
	public static String initPWD(){
		while(true){
			System.out.print("Please initialize the unified password:");
			Console console = System.console();  
	        String pwd = new String(console.readPassword()); 
	        System.out.print("Retype the password:");
	        String rpwd = new String(console.readPassword()); 
	        if(pwd.equals(rpwd)){
	        	System.out.println("Password is set correctly!");
	        	return pwd;
	        }else
	        	System.out.println("Password is not correct! Please set again!");
		}
	}
	
	public static void cmdHelp(String cmd){
		System.out.println("This is cmd help!");
	}
	
	public static void searchPWD(String args[]){
		String result = "";
		////list all the tags that contains in the file
		if(args.length == 1){
			result = pb.listAllTags();
		}else if(args.length == 2){
			result = pb.listPWD(args[1]);
		}else
			result = "The command of 'list' is wrong!";

		System.out.println(result);
	}
	
	public static boolean isInteger(String str) {    
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(str).matches();    
	  } 
	
	public static String generatePWD(){
		System.out.print("Please enter the number of digits that you want to generate(1-99): ");
		Scanner sc = new Scanner(System.in);  
        sc.useDelimiter("\n"); 
        String pwdString = "";
        while(sc.hasNext()){
        	String num = sc.next();
            if(!isInteger(num))
            	System.out.print("Invalid number, enter again: ");
            else{
            	int n = Integer.parseInt(num);
            	pwdString = pb.generatePWD(n);
            	if(pwdString != null){
            		System.out.println("PWD generated->  "+pwdString);
            		break;
            	}else{
            		System.out.print("The digits number should be in the range of 0~99: ");
            	}
            }
        }
        return pwdString;
        
	}
	
	public static void addPWD(String [] args){
		if(args.length == 1){
			System.out.print("Please enter the account names(separate them with '::'): ");
			Scanner sc = new Scanner(System.in);  
	        sc.useDelimiter("\n"); 
	        String accounts = sc.next();
	        System.out.print("Please enter some tags for the accounts(separate them with '::'): ");
			String tags = sc.next();
			System.out.print("Please enter a description for this record: ");
			String des = sc.next();
			if(des.equals(""))
				des = "null";
			String pwdString = "";
			System.out.print("Generate a password? (y/n) ");
			while(sc.hasNext()){
				String ans = sc.next();
				if(ans.toLowerCase().equals("y")){
					pwdString = generatePWD();
					break;
				}else if(ans.toLowerCase().equals("n")){
					System.out.print("Please type in password: ");
					pwdString = sc.next();
					while(true){
						if(!pwdString.contains("%$$%") && !pwdString.contains("::"))
							break;
						else{
							System.out.print("Password cannot contains '%$$%' or '::'!\nPlease type in password: ");
							pwdString = sc.next();
						}
					}
					break;
				}else
					System.out.print("Please just type in 'y' or 'n'!\n y/n? : ");
			}
			pb.addPWD(accounts, pwdString, tags, des, "null");
		}else
			System.out.println("Too many arguments for 'add'!");
	}
	
	public static void changePWD(String [] args){
		if(args.length != 2){
			System.out.println("The command is wrong!");
			return ;
		}
		for(int i = 0 ; i<pb.accounts.size() ; i++){
			String tagsString = "";
			for(int j = 0 ; j<pb.accounts.get(i).tags.length ; j++)
				tagsString += pb.accounts.get(i).tags[j]+"    ";
			if(tagsString.toLowerCase().contains(args[1].toLowerCase())){
				System.out.println("Change password of this?");
				System.out.println("Tags: "+tagsString);
				System.out.println("Description: "+pb.accounts.get(i).description);
				System.out.print("y/n? : ");
				Scanner sc = new Scanner(System.in);  
		        sc.useDelimiter("\n"); 
		        String pwdString = "";
		        while(sc.hasNext()){
					String ans = sc.next();
					if(ans.toLowerCase().equals("y")){
						System.out.print("Generate a password? (y/n) ");
						while(sc.hasNext()){
							ans = sc.next();
							if(ans.toLowerCase().equals("y")){
								pwdString = generatePWD();
								break;
							}else if(ans.toLowerCase().equals("n")){
								System.out.print("Please type in password: ");
								pwdString = sc.next();
								while(true){
									if(!pwdString.contains("%$$%") && !pwdString.contains("::"))
										break;
									else{
										System.out.print("Password cannot contains '%$$%' or '::'!\nPlease type in password: ");
										pwdString = sc.next();
									}
								}
								break;
							}else
								System.out.print("Please just type in 'y' or 'n'!\ny/n? : ");
						}
						pb.accounts.get(i).pwd = pwdString;
						break;
					}else if(ans.toLowerCase().equals("n")){
						System.out.println("Try to find another record containing "+args[1]);
						break;
					}else
						System.out.print("Please just type in 'y' or 'n'!\ny/n? : ");
				}
			}
		}
	}
	
	
	public static void deletePWD(String [] args){
		if(args.length != 2){
			System.out.println("The command is wrong!");
			return ;
		}
		for(int i = 0 ; i<pb.accounts.size() ; i++){
			String tagsString = "";
			for(int j = 0 ; j<pb.accounts.get(i).tags.length ; j++)
				tagsString += pb.accounts.get(i).tags[j]+"    ";
			if(tagsString.toLowerCase().contains(args[1].toLowerCase())){
				System.out.println("Delete password of this?");
				System.out.println("Tags: "+tagsString);
				System.out.println("Description: "+pb.accounts.get(i).description);
				System.out.print("y/n? : ");
				Scanner sc = new Scanner(System.in);  
		        sc.useDelimiter("\n");
		        while(sc.hasNext()){
					String ans = sc.next();
					if(ans.toLowerCase().equals("y")){
						pb.accounts.remove(i);
						break;
					}else if(ans.toLowerCase().equals("n")){
						System.out.println("Try to find another record!");
						break;
					}else
						System.out.print("Please just type in 'y' or 'n'!\ny/n? : ");
				}
			}
		}
	}
	
	public static void savePWD(String [] args){
		if(args.length != 1){
			System.out.println("The command is wrong!");
			return ;
		}
		pb.saveBook();
	}
	
	public static void processCMDs(){
		System.out.print("Please enter the command of your option(list\\add\\change\\delete\\save\\exit -h for help):\n");
		Scanner sc = new Scanner(System.in);  
        sc.useDelimiter("\n|\r"); 
        while(true){
    		System.out.print("PWDBook CMD>>");
        	String input = sc.nextLine();
        	if(input.trim().equals("")){
        		continue;
        	}
        	String args[] = input.trim().split("\\s{1,}");
        	String cmd = args[0].toLowerCase().trim();
        	if(args.length>=2 && args[1].toLowerCase().equals("-h")){
        		cmdHelp(cmd);
        		continue;
        	}
        	
        	
        	
        	if(cmd.equals("exit") || cmd.equals("quit")){
        		pb.saveBook();
        		break;
        	}else if(cmd.equals("list")){
        		searchPWD(args);
        	}else if(cmd.equals("add")){
        		addPWD(args);
        	}else if(cmd.equals("change")){
        		changePWD(args);
        	}else if(cmd.equals("delete")){
        		deletePWD(args);
        	}else if(cmd.equals("save")){
        		savePWD(args);
        	}
        	else{
        		System.out.println("Invalid commands!");
        	}
        	
        }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initAPP();
		processCMDs();

	}

}
