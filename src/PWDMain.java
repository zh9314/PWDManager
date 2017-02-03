import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import jline.*;

public class PWDMain {
	
	public static PWDBook pb;
	
	public static void initAPP() throws IOException{
		System.out.println("Welcome to use the pwd book! \n          ----Developped by Huan.");
		String curDir = new PWDMain().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		int index = curDir.lastIndexOf('/');
		String pwdBookPath = curDir.substring(0, index+1)+"book.pwd";
		File bookFile = new File(pwdBookPath);  
		if(!bookFile.exists()){
			ConsoleReader reader = new ConsoleReader();
			String input = "";
			System.out.println("The pwd book doesn't exist!");
			while((input = reader.readLine("Do you need to create one?(y/n) ")) != null){
				if(input.toLowerCase().trim().equals("y")){
					String pwd = initPWD();
					pb = new PWDBook(pwdBookPath, pwd);
					pb.createPWDBook();
					break;
				}else if(input.toLowerCase().trim().equals("n")){
					System.out.println("OK!EXIT!");
					System.exit(-1);
				}else{
					System.out.print("Invalid input! Jsut choose to input 'y' or 'n'!\n");
				}
			}
		}else
		{
			while(true){
				ConsoleReader reader = new ConsoleReader();
				String pwd = reader.readLine("Please input the unified password: ", '*');
				pb = new PWDBook(pwdBookPath, pwd);
				if(pb.initBookPWDs(pwd))
					break;
				else{
					
				}
			}
				
		}
	}
	
	public static String initPWD() throws IOException{
		while(true){
			ConsoleReader reader = new ConsoleReader();
	        String pwd = reader.readLine("Please initialize the unified password: ", '*');
	        String rpwd = reader.readLine("Retype the password: ", '*'); 
	        if(pwd.equals(rpwd)){
	        	System.out.println("Password is set correctly!");
	        	return pwd;
	        }else
	        	System.out.println("Password is not correct! Please set again!");
		}
	}
	
	public static void cmdHelp(String cmd){
		System.out.println("This is cmd help!");
		//list\\add\\change\\delete\\save\\copy\\exit
		if(cmd.equals("list"))
			System.out.println("list [tagX] \n0 argument: list all the tags in this password book!\n1 argument: list the detailed information of the record that contains the tagX.");
		else if(cmd.equals("add"))
			System.out.println("add \n0 argument: Add a new record into the password book.");
		else if(cmd.equals("change"))
			System.out.println("change tagX\n1 argument: Change the password of the record that contains the tagX.");
		else if(cmd.equals("delete"))
			System.out.println("delete tagX\n1 argument: Delete the password of the record that contains the tagX.");
		else if(cmd.equals("save"))
			System.out.println("save\n0 argument: Save all the records into the password book.");
		else if(cmd.equals("copy"))
			System.out.println("copy tagX.\n1 argument: Copy the password of the record that contains the tagX into the system clipboard");
		else if(cmd.equals("exit"))
			System.out.println("exit\n0 argument: Exit the password book. Before quit, all the records will be saved into the password book.");
		else
			System.out.println("Invalid commands!");
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
		if(str.equals(""))
			return false;
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(str).matches();    
	  } 
	
	public static String generatePWD() throws IOException{
		ConsoleReader reader = new ConsoleReader();
		String pwdString = "";
		String num = "";
        while((num = reader.readLine("Please enter the number of digits that you want to generate(1-99): ")) != null){
            if(!isInteger(num.trim()))
            	System.out.println("Invalid number, enter again!");
            else{
            	int n = Integer.parseInt(num.trim());
            	pwdString = pb.generatePWD(n);
            	if(pwdString != null){
            		System.out.println("PWD generated->  "+pwdString);
            		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
			        Transferable tText = new StringSelection(pwdString);  
			        clip.setContents(tText, null); 
			        System.out.println("This generated password has been copied to system clipboard!");
            		break;
            	}else{
            		System.out.println("The digits number should be in the range of 0~99! ");
            	}
            }
        }
        return pwdString;
        
	}
	
	public static void addPWD(String [] args) throws IOException{
		if(args.length == 1){
			ConsoleReader reader = new ConsoleReader();
			String accounts = "";
			while(true){
				accounts = reader.readLine("Please enter the account names(separate them with '::'): ");
				if(accounts.trim().equals(""))
					System.out.println("The filed of account cannot be empty!");
				else
					break;
			}
			String tags = "";
			while(true){
				tags = reader.readLine("Please enter some tags for the accounts(separate them with '::'): ");
				if(tags.trim().equals(""))
					System.out.println("The filed of tags cannot be empty!");
				else
					break;
			}
			String des = reader.readLine("Please enter a description for this record: ");
			if(des.equals(""))
				des = "null";
			String pwdString = "";
			String ans = "";
			while((ans = reader.readLine("Generate a password? (y/n) ")) != null){
				if(ans.toLowerCase().trim().equals("y")){
					pwdString = generatePWD();
					break;
				}else if(ans.toLowerCase().trim().equals("n")){
					while((pwdString = reader.readLine("Please type in password: ")) != null){
						if(pwdString.equals("")){
							System.out.println("Password cannot be empty!");
						}else if(pwdString.contains("%$$%")){
							System.out.println("Password cannot contain '%$$%'!");
						}else if(pwdString.contains("::")){
							System.out.println("Password cannot contain '::'!");
						}else if(pwdString.contains(" ")){
							System.out.println("Password cannot contain spaces!");
						}else{
							break;
						}
					}
					break;
				}else
					System.out.print("Please just type in 'y' or 'n'!\n");
			}
			pb.addPWD(accounts, pwdString, tags, des, "null");
		}else
			System.out.println("Too many arguments for 'add'!");
	}
	
	public static void changePWD(String [] args) throws IOException{
		if(args.length != 2){
			System.out.println("The command is wrong!");
			return ;
		}
		boolean changed = false;
		for(int i = 0 ; i<pb.accounts.size() ; i++){
			String tagsString = "";
			for(int j = 0 ; j<pb.accounts.get(i).tags.length ; j++)
				tagsString += pb.accounts.get(i).tags[j]+"    ";
			if(tagsString.toLowerCase().contains(args[1].toLowerCase())){
				System.out.println("\nChange password of this?");
				System.out.println("Tags: "+tagsString);
				System.out.println("Description: "+pb.accounts.get(i).description);
				ConsoleReader reader = new ConsoleReader();
		        String pwdString = "", ans = "";
		        while((ans = reader.readLine("y/n? : ")) != null){
					if(ans.toLowerCase().trim().equals("y")){
						while((ans = reader.readLine("Generate a password? (y/n) ")) != null){
							if(ans.toLowerCase().trim().equals("y")){
								pwdString = generatePWD();
								break;
							}else if(ans.toLowerCase().trim().equals("n")){
								while((pwdString = reader.readLine("Please type in password: ")) != null){
									if(pwdString.equals("")){
										System.out.println("Password cannot be empty!");
									}else if(pwdString.contains("%$$%")){
										System.out.println("Password cannot contain '%$$%'!");
									}else if(pwdString.contains("::")){
										System.out.println("Password cannot contain '::'!");
									}else if(pwdString.contains(" ")){
										System.out.println("Password cannot contain spaces!");
									}else{
										break;
									}
								}
								break;
							}else
								System.out.print("Please just type in 'y' or 'n'!\n");
						}
						pb.accounts.get(i).pwd = pwdString;
						System.out.println("The password has been changed!");
						changed = true;
						break;
					}else if(ans.toLowerCase().equals("n")){
						System.out.println("Try to find another record containing "+args[1]);
						break;
					}else
						System.out.print("Please just type in 'y' or 'n'!\ny/n? : ");
				}
		        
		        if(changed)
		        	break;
			}
		}
		if(!changed)
			System.out.println("There are no more records with tag "+args[1]);
	}
	
	
	public static void copyPWD(String [] args) throws IOException{
		if(args.length != 2){
			System.out.println("The command is wrong!");
			return ;
		}
		boolean copied = false;
		for(int i = 0 ; i<pb.accounts.size() ; i++){
			String tagsString = "";
			for(int j = 0 ; j<pb.accounts.get(i).tags.length ; j++)
				tagsString += pb.accounts.get(i).tags[j]+"    ";
			if(tagsString.toLowerCase().contains(args[1].toLowerCase())){
				System.out.println("\nCopy the password of this?");
				System.out.println("Tags: "+tagsString);
				System.out.println("Description: "+pb.accounts.get(i).description);
				ConsoleReader reader = new ConsoleReader();
				String ans = "";
		        while((ans = reader.readLine("y/n? : ")) != null){
					if(ans.toLowerCase().trim().equals("y")){
						String pwdString = pb.accounts.get(i).pwd;
						Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
				        Transferable tText = new StringSelection(pwdString);  
				        clip.setContents(tText, null); 
				        System.out.println("The password has been copied to system clipboard!");
				        copied = true;
				        break;
					}else if(ans.toLowerCase().trim().equals("n")){
						System.out.println("Try to find another record containing "+args[1]);
						break;
					}else
						System.out.print("Please just type in 'y' or 'n'!\n");
				}
		        if(copied)
		        	break;
			}
		}if(!copied)
			System.out.println("There are no more records with tag "+args[1]);
	}
	
	
	public static void deletePWD(String [] args) throws IOException{
		if(args.length != 2){
			System.out.println("The command is wrong!");
			return ;
		}
		boolean deleted = false;
		for(int i = 0 ; i<pb.accounts.size() ; i++){
			String tagsString = "";
			for(int j = 0 ; j<pb.accounts.get(i).tags.length ; j++)
				tagsString += pb.accounts.get(i).tags[j]+"    ";
			if(tagsString.toLowerCase().contains(args[1].toLowerCase())){
				System.out.println("\nDelete password of this?");
				System.out.println("Tags: "+tagsString);
				System.out.println("Description: "+pb.accounts.get(i).description);
				ConsoleReader reader = new ConsoleReader();
				String ans = "";
		        while((ans = reader.readLine("y/n? : ")) != null){
					if(ans.toLowerCase().trim().equals("y")){
						pb.accounts.remove(i);
						System.out.println("The record has been removed!");
						deleted = true;
						break;
					}else if(ans.toLowerCase().trim().equals("n")){
						System.out.println("Try to find another record!");
						break;
					}else
						System.out.print("Please just type in 'y' or 'n'!\n");
				}
		        if(deleted)
		        	break;
			}
		}if(!deleted)
			System.out.println("There are no more records with tag "+args[1]);
	}
	
	public static void savePWD(String [] args){
		if(args.length != 1){
			System.out.println("The command is wrong!");
			return ;
		}
		pb.saveBook();
		System.out.println("The pwd book has been updated!");
	}
	
	public static void processCMDs() throws IOException{
		System.out.print("Please enter the command of your option(list\\add\\change\\delete\\save\\copy\\exit -h for help):\n");
        while(true){
    		ConsoleReader reader = new ConsoleReader();
    		String input = reader.readLine("PWDBook CMD>>");
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
        	}else if(cmd.equals("copy")){
        		copyPWD(args);
        	}
        	else{
        		System.out.println("Invalid commands!");
        	}
        	
        }
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		initAPP();
		processCMDs();

	}

}
