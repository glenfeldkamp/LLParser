/**
 * Author: Glen Feldkamp
 * Program will parse statements in LL1 format
 * Copyright Glen Feldkamp cannot be used or copied without permission.
 */
import java.io.*;
import java.util.*;

public class LL1 
{
	static String line = "";
	static String token = "";
	public static boolean valid = true;
	public static LinkedList<String> statement;
	public static LinkedList<String> curToken;
	public static LinkedList<String> syntax;
	
	// Breaks the statement into strings
	private void getTokens(String input)
	{
		token = "";
		StringTokenizer st = new StringTokenizer(input,"+-*/()$", true);
		while(st.hasMoreTokens())
		{
			token = st.nextToken();
			statement.add(token);
			token = "";
		}
	}
	
	// checks if token is an integer
	private boolean isInt(String token)
	{
		try
		{
			Integer.parseInt(token);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	// Check for integer or "(" else this will set valid to false.
	public void E(String token)
	{
		if(valid)
		{
			if(isInt(token) || token.equals("("))
			{
				syntax.add("E");
				T(statement.peek());
				Eprime(statement.peek());
			}
			else
				valid = false;
		}
	}
	
	// Checks for correct mathematical symbol and will add correct syntax or set valid to false if an error.
	public void Eprime(String token)
	{
		if(valid)
		{
			if(token.equals("+") || token.equals("-"))
			{
				syntax.add("EP");
				syntax.add(statement.peek());
				// pop the + or - and add it to the syntax
				if(!statement.peek().equals("$"))
					curToken.add(statement.pop());
				T(statement.peek());
				Eprime(statement.peek());
			}
			else if(token.equals("$") || token.equals(")"))
			{
				syntax.add("EP");
				syntax.add(".");
			}
			else
				valid = false;
		}
	}
	
	// Checks for the appropriate T conditions, sets false if not correct.
	public void T(String token)
	{
		if(valid)
		{
			if(isInt(token) || token.equals("("))
			{
				syntax.add("T");
				F(statement.peek());
				Tprime(statement.peek());
			}
			else
				valid = false;
		}
	}
	
	// Checks for multiplication and division, accounts for other necessary symbols otherwise sets valid to false.
	public void Tprime(String token)
	{
		if(valid)
		{
			if(token.equals("*") || token.equals("/"))
			{
				syntax.add("TP");
				syntax.add(statement.peek());
				if(!statement.peek().equals("$"))
					curToken.add(statement.pop());
				F(statement.peek());
				Tprime(statement.peek());
			}
			else if(token.equals("$") || token.equals(")") || token.equals("+") || token.equals("-"))
			{
				syntax.add("TP");
				syntax.add(".");
			}
			else
				valid = false;
		}
	}
	
	// Checks for an integer or paranthesis and sets valid to false if neither are found
	public void F(String token)
	{
		if(valid)
		{
			if(isInt(token))
			{
				syntax.add("F");
				syntax.add("n");
				if(!statement.peek().equals("$"))
					curToken.add(statement.pop());
			}
			else if(token.equals("("))
			{
				syntax.add("F");
				syntax.add("(");
				if(!statement.peek().equals("$"))
					curToken.add(statement.pop());
				E(statement.peek());
				if(!statement.peek().equals("$"))
					curToken.add(statement.pop());
				syntax.add(")");
			}
			else
				valid = false;
		}
	}
	
	public static void main(String[] args) 
	{
		LL1 test = new LL1();
		// Check for valid command line argument
		if(args == null || args.length == 0)
		{
			System.out.println("Usage is: java program file.");
			System.exit(0);
		}
		else
		{
			try 
			{
				// read file line by line
				File file = new File(args[0]);
				Scanner scanner = new Scanner(file);
				// while the file has more lines we need to make tokens
				while(scanner.hasNextLine()) 
				{
					statement = new LinkedList<String>();
					curToken = new LinkedList<String>();
					syntax = new LinkedList<String>();
					line = scanner.nextLine();
					valid = true;
					test.getTokens(line);
					
					test.E(statement.peek());
					System.out.println(line);
					if(!curToken.isEmpty())
					{
						while(!curToken.isEmpty())
						{
							System.out.print(curToken.pop());
						}
					}
					if(valid)
					{
						System.out.print(" is valid.");
						System.out.println();
						if(!syntax.isEmpty())
						{
							while(!syntax.isEmpty())
							{
								System.out.print(syntax.pop() + " ");
							}
						}
					}	
					else
					{
						System.out.print(" is unexpected.");
					}
					if(!syntax.isEmpty())
						while(syntax.isEmpty())
							syntax.pop();
					if(!statement.isEmpty())
						while(statement.isEmpty())
							statement.pop();
					if(!curToken.isEmpty())
						while(curToken.isEmpty())
							curToken.pop();
					System.out.println();
					System.out.println();
				}
				// close scanner.
				scanner.close();
			} catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
