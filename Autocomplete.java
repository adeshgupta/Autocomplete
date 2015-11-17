import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.List;



/*I have used the Trie datastructure to implement this autocomplete feature problem
In terms of scalability, this is better, since the look up time for each word is O(k)
where k is the length of each word
*/
public class Autocomplete {

	private node root;
	
	/*
	 * The constructor, as mentioned in the problem statement, take a List as an input
	 * I have created the trie, for every word*/
	
	public Autocomplete(List<String> word_list){
		//System.out.println("constructor called here");
		for(String str:word_list){
			addWord(str);
		}
		//System.out.println("added values without error");
	}
	
	
	/*
	 * This is a main function used to test my class
	 * I am also attaching a set of input file, from which i parsed data to form it into a list
	 * */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
		List<String> array= new ArrayList<String>();
		BufferedReader file=new BufferedReader(new FileReader(args[1]));
		String line;
		while((line=file.readLine())!=null){
		String[] values=line.split(",");
		for(int i=0;i<values.length;i++)
			array.add(values[i]);
		//array=Arrays.asList(values);
		}
		Autocomplete auto=new Autocomplete(array);
		
		List<String> matches = auto.find("venture");
		if(matches==null || matches.size() == 0)
		{
			System.out.println("None");
		}
		else
		{
			for(String str:matches)
			{
				System.out.println(str);
			}
		}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	/*this method adds words into the trie datastructure.
	 
	*/
	public void addWord(String word)
	{
		if(root == null)
		{
			root = new node(' ');
		}
		node start = root;
		char[] characters = word.toCharArray();
		//System.out.println("length is:"+characters.length);
		//for(char c : characters)
		for(int i=0;i<characters.length;i++)
		{
			char c=characters[i];
			if( start.getChildren().size() == 0)
			{
				node newNode = new node(c);
				if(i==characters.length-1)
					newNode.setLeaf(true);
				start.getChildren().add(newNode);
				start = newNode;
			}
			else
			{
				ListIterator<node> iterator = start.getChildren().listIterator();
				node node=null;
				while(iterator.hasNext())
				{
					node = (node) iterator.next();
					//if(node.getCharacter() >= c)
					if(node.getCharacter() == c)
						break;
				}
				if(node.getCharacter() == c)
				{
					if(i==characters.length-1)
						node.setLeaf(true);
					start = node;
				}
				else
				{
					node newNode = new node(c);
					if(i==characters.length-1)
						newNode.setLeaf(true);
					iterator.add(newNode);	
					start = newNode;	
				}
			}
		}	
	}
	
	/*
	 * This method, finds all possible strings with a certain pattern
	 * It is a recursive procedure, which helps in finding all the words
	 * */
	
	public List<String> find(String pattern)
	{
		if(pattern == null || pattern.isEmpty())
			return null;
		
		char[] chars = pattern.toCharArray();
		node newNode = root;
		boolean flag = false;
		List<node> iterate=newNode.getChildren();
		int i;
		for(i=0;i<chars.length;i++)
		{
			char c=chars[i];
			if(newNode.getChildren().size() > 0)
			{
				for(node node : iterate)
				{
					if(node.getCharacter() == c)
					{
						//System.out.println("modified start!");
						newNode = node;
						//System.out.println(start.getCharacter()+"is");
						flag=true;
						break;
					}
					else
						flag=false;
				}
				iterate=newNode.getChildren();
			}
			else
			{
				flag = false;
				break;
			}
		}
		if(flag && i==pattern.length())
		{
			List<String> matches = findAllWords(newNode,pattern);
			return matches;
		}
		
		return null;
	}
	
	private List<String> findAllWords(node newNode,final String prefix)
	{
		List<String> list = new LinkedList<String>();
		if(newNode.getLeaf()==true && newNode.getChildren().size()!=0){
			list.add(prefix);
		}
		if(newNode.getChildren().size() == 0)
		{
			list.add(prefix);
			//System.out.println("prefix vlaue:"+prefix);
			return list;
		}
		else
		{
			
			for(node n: newNode.getChildren())
			{
				//System.out.println(prefix+n.getCharacter()+":");
				list.addAll(findAllWords(n, prefix+n.getCharacter()));
			}
			return list;
		}
		
	}
}

/*
 * The following class, defines a datastructure which is used for every node in the tree
 * It has associated with it - a character, the list of children, and if that character is the last
 * character of some word in the trie*/

class node {
	
	private char character;
	private List<node> children;
	private boolean isLeaf;
	public node(char character) {
		this.character = character;
		this.children = new LinkedList();
		this.isLeaf=false;
	}
	
	public char getCharacter() {
		return character;
	}
	public void setCharacter(char character) {
		this.character = character;
	}
	public List<node> getChildren() {
		return children;
	}
	public void setChildren(List children) {
		this.children = children;
	}
	public boolean getLeaf(){
		return isLeaf;
	}
	public void setLeaf(boolean value){
		this.isLeaf=value;
	}
}