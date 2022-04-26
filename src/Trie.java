/**
 * @author: Suyog Joshi
 * @Project3: Scrabble Game
 * @UNMId: 101846426
 * @Date: 15/04/2021
 */

import java.util.Locale;

public class Trie {

    static final int TOTAL_ALPHABETS = 26;

    static class TrieNode{
        TrieNode[] dictionary = new TrieNode[TOTAL_ALPHABETS];
        boolean isCompleteWord;

        public TrieNode(){
            isCompleteWord = false;
            for(int i = 0; i < TOTAL_ALPHABETS; i++ ){
                dictionary[i] = null;
            }
        }

        public void setChild(TrieNode node, int index){
            dictionary[index] = node;
        }

        public TrieNode getChild(int index){
            return dictionary[index];
        }

        public void setCompleteWord(Boolean isCompleteWord){
            this.isCompleteWord = isCompleteWord;
        }

        public boolean isCompleteWord(){
            return this.isCompleteWord;
        }
    }

    private final TrieNode rootNode;

    public Trie(){
        rootNode = new TrieNode();
    }

    public TrieNode getRootNode(){
        return this.rootNode;
    }

    /**
     * insert function to insert a word
     * @param word
     */
    public void insert(String word){
        TrieNode current = rootNode;

       word = word.toLowerCase(Locale.ROOT);
        for(char c: word.toCharArray()){
            int index = c - 'a';
            if(current.dictionary[index] == null) current.setChild(new TrieNode(),index);
            current = current.getChild(index);
        }
        current.setCompleteWord(true);
    }

    /**
     * search function to search the given word
     * @param word
     * @returns true if word exists, false otherwise
     */
    public boolean search(String word){
        TrieNode curr = rootNode;
        int index;

        for (char c : word.toCharArray()){
            index = c - 'a';
            if(curr.getChild(index) == null) return false;
            curr = curr.getChild(index);
        }
        return curr.isCompleteWord;
    }

    /**
     * reverseWord function reverses the word
     * @param word
     * @returns a reversed word
     */
    public String reverseWord(String word){
        StringBuilder words = new StringBuilder();
        int length = word.length() - 1;
        for (int i = length; i >= 0; i--) {
                words.append(word.charAt(i));
        }
        return words.toString();
    }

    /**
     * getLastNode function gets the last node of the word
     * @param word
     * @returns the last node
     */
    public TrieNode getLastNode(String word){
        TrieNode curr = rootNode;
        int index = word.charAt(0) - 'a';
        if(word.length() == 1) return curr.getChild(word.charAt(0) - 'a');
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            int j = c - 'a';
            if(curr.getChild(j) == null) break;
            else curr = curr.getChild(j);
        }
        return curr;
    }
}
