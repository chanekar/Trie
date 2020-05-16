package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	private Trie() { }
	
	public static TrieNode buildTrie(String[] allWords) {
		
		TrieNode root = new TrieNode (null, null, null);
		short s = 0; short e = 0; 
		
		if (allWords.length == 0) return root;
		
		e = (short) (allWords[0].length() - 1);
		Indexes index = new Indexes (0, s, e);
		root.firstChild = new TrieNode (index, null, null);
		
		TrieNode ptr = root.firstChild;
		TrieNode prev = root.firstChild;
		
		int simIndex = -1; 
		int wordIndex = -1;
		
		for(int i = 1; i < allWords.length; i++) {
			
			String str = allWords[i];
		
			while(ptr != null) {
				
				s = ptr.substr.startIndex;
				e = ptr.substr.endIndex;
				wordIndex = ptr.substr.wordIndex;
			
				if(ptr.substr.startIndex > str.length()) {
					prev = ptr;
					ptr = ptr.sibling;
					continue;
				}
				
				simIndex = sim(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1), str.substring(ptr.substr.startIndex));
				
				if(simIndex != -1) simIndex += ptr.substr.startIndex;
				
				if(simIndex == -1) {
					
					prev = ptr;
					ptr = ptr.sibling;
					
				} else {
					
					if (simIndex == ptr.substr.endIndex) {
						prev = ptr;
						ptr = ptr.firstChild;
					} else if (simIndex < ptr.substr.endIndex){ 
						prev = ptr;
						break;
					}
					
				}
				
			}
			
			if (ptr == null) {
				
				index = new Indexes(i, s, (short) (str.length()-1));
				prev.sibling = new TrieNode (index, null, null);
				
			} else {

				TrieNode under = prev.firstChild;
				
				Indexes ptrI = new Indexes (prev.substr.wordIndex, (short) (simIndex+1), prev.substr.endIndex);
				prev.substr.endIndex = (short) simIndex; 
				
				prev.firstChild = new TrieNode (ptrI, null, null);
				prev.firstChild.firstChild = under;
				index = new Indexes ((short) i, (short) (simIndex+1), (short) (str.length()-1));
				prev.firstChild.sibling = new TrieNode (index, null, null);
				
			}
			
			ptr = root.firstChild; prev = root.firstChild;
			simIndex = -1; s = -1; e = -1; wordIndex = -1;
			
		}
		
		return root;
		
	}
	
	private static int sim (String s1, String s2) {

		String s = "";
		int lower = 0;
		if (s1.length() < s2.length()) lower = s1.length();
		else lower = s2.length();

		for (int i = 0; i < lower; i++) {
			if (s1.charAt(i) == s2.charAt(i)) s += s1.charAt(i);
			else break;
		}

		return s.length()-1;

	}
	
	// -------SECOND METHOD------- //
	
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		
		ArrayList<TrieNode> result = new ArrayList<TrieNode>();
		TrieNode target = searchTarget(root, allWords, prefix);
		if (target == null) return null;
		return returnedList(target, target.firstChild, result);
		
	}
	
	private static TrieNode searchTarget (TrieNode root, String[] allWords, String s) {

		TrieNode ptr = root.firstChild;
		TrieNode target = null;
		while (ptr != null) {
			String c = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1);
			if (sim(c, s) != -1) {
				target = ptr;
				ptr = target;
				s = s.substring(sim(s, c) + 1);
				ptr = ptr.firstChild;
			} else
				ptr = ptr.sibling;
		}

		return target;
	}
	
	private static ArrayList<TrieNode> returnedList(TrieNode target, TrieNode root, ArrayList<TrieNode> list) {

		if (target.firstChild == null) {
			list.add(target);
			return list;
		}
		if (root.firstChild == null) list.add(root);
		if (root.sibling != null) returnedList(target, root.sibling, list);
		if (root.firstChild != null) returnedList(target, root.firstChild, list);
		return list;

	}
	
	
	// --------------- //
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
