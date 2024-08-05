
class Trie {
    private TrieNode root;
import java.util.Scanner;

    class BSTNode {
        long balance;
        String name;
        BSTNode left, right;
        BSTNode(String name,long balance) {
            this.balance = balance;
            this.name = name;
        }
    }

    class BST {
        public BSTNode root;
        public BSTNode minOfBST(BSTNode root) {
            BSTNode node = root;
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }
        public BSTNode maxOfBST(BSTNode root) {
            BSTNode node = root;
            while (node.right != null) {
                node = node.right;
            }
            return node;
        }
        public BSTNode getLessName(BSTNode key, BSTNode node) {
            if (node == null) {
                return key;
            }
            if (key.balance > node.balance) {
                return getLessName(key, node.right);
            } else {
                if (key.name.compareTo(node.name) < 0) {
                    return getLessName(key, node.left);
                } else if (key.name.compareTo(node.name) > 0) {
                    BSTNode temp = new BSTNode(node.name , node.balance);
                    return getLessName(temp, node.left);
                } else {
                    return getLessName(key, node.left);
                }
            }
        }
        public void delete(String name , long balance){
            root = deleteNode(root , balance , name);
        }
        private BSTNode deleteNode(BSTNode node, long balance, String name) {
            if (node == null)
                return null;
            if (balance < node.balance) {
                node.left = deleteNode(node.left, balance, name);
                return node;
            }else if (balance == node.balance){
                if (name.compareTo(node.name) < 0){
                    node.left = deleteNode(node.left , balance , name);
                    return node ;
                }else if (name.compareTo(node.name) > 0){
                    node.right = deleteNode(node.right , balance , name);
                    return node ;
                }
            }else if (balance > node.balance) {
                node.right = deleteNode(node.right, balance, name);
                return node;
            }
            if (node.left == null && node.right == null){
                return null ;
            }
            else if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left ;
            } else {
                BSTNode minParent = node;
                BSTNode min = node.right;
                while (min.left != null) {
                    minParent = min;
                    min = min.left;
                }
                if (minParent != node)
                    minParent.left = min.right;
                else
                    minParent.right = min.right;
                node.balance = min.balance;
                node.name = min.name;
                return node;
            }
        }
        public void insert(String name , long balance) {
            if (root == null) {
                root = new BSTNode(name,balance);
                return ;
            }
            root = insertNode(root, name, balance);
        }
        private BSTNode insertNode( BSTNode node, String name,long balance) {
            if (node == null) {
                BSTNode newNode = new BSTNode(name,balance);
                return  newNode ;
            }
            if (balance < node.balance) {
                node.left = insertNode(node.left, name,balance);
            }
            else if (balance == node.balance ){
                if (name.compareTo(node.name) < 0){
                    node.left = insertNode (node.left,name,balance);
                }else if (name.compareTo(node.name) > 0){
                    node.right = insertNode(node.right,name,balance);
                }
            }else if (balance > node.balance) {
                node.right = insertNode(node.right,name,balance);
            }
            return node;
        }
    }

    class TrieNode {
        long balance;
        int creditor;
        int debtor;
        TrieNode[] children;
        Trie TrieChildren;
        boolean isEndOfWord;

        public TrieNode() {
            isEndOfWord = false;
            children = new TrieNode[30];
            balance = 0;
        }
    }

    Trie() {
        root = new TrieNode();
    }

    public void insert(String name) {
        TrieNode node = root;
        for (int i = 0; i < name.length(); i++) {
            int index = name.charAt(i) - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEndOfWord = true;
        node.TrieChildren = new Trie();
    }

    public boolean search(String name) {
        TrieNode node = root;
        for (int i = 0; i < name.length(); i++) {
            int index = name.charAt(i) - 'a';
            if (node.children[index] == null) {
                return false;
            }
            node = node.children[index];
        }
        return node.isEndOfWord;
    }

    public TrieNode getNode(String name) {
        TrieNode node = root;
        for (int i = 0; i < name.length(); i++) {
            int index = name.charAt(i) - 'a';
            if (node.children[index] == null) {
                return null;
            }
            node = node.children[index];
        }
        return node;
    }
}

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        BST bst = new BST();
        Scanner scanner = new Scanner(System.in);
        int lineNumber = scanner.nextInt();
        String s = scanner.nextLine();
        for (int i = 0; i < lineNumber; i++) {
            String line = scanner.nextLine();
            String[] splited = line.split(" ");
            if (splited[0].equals("1")) {
                String first = splited[1];
                String second = splited[2];
                if (!trie.search(first)) {
                    trie.insert(first);
                }
                if (!trie.search(second)) {
                    trie.insert(second);
                }
                long firstPart = Long.parseLong(splited[3].split("\\.")[0]) * 100;
                long balance = firstPart + Long.parseLong(splited[3].split("\\.")[1]);
                TrieNode firstNode = trie.getNode(first);
                TrieNode secondNode = trie.getNode(second);
                Trie firstTrie = firstNode.TrieChildren;
                Trie secondTrie = secondNode.TrieChildren;
                if (!firstTrie.search(second)) {
                    firstTrie.insert(second);
                    secondTrie.insert(first);
                }
//                    4
//                    1 a b 78.50
//                    4 c
//                    5 d
                TrieNode secondInFirst = firstTrie.getNode(second);
                TrieNode firstInSecond = secondTrie.getNode(first);
                if (secondInFirst.balance < 0) {
                    //
                } else if (secondInFirst.balance == 0) {
                    firstNode.creditor++;
                    secondNode.debtor++;
                } else {
                    if (secondInFirst.balance - balance > 0) {
                        //
                    } else if (secondInFirst.balance - balance == 0) {
                        firstNode.debtor--;
                        secondNode.creditor--;
                    } else {
                        firstNode.debtor--;
                        firstNode.creditor++;
                        secondNode.creditor--;
                        secondNode.debtor++;
                    }
                }
                bst.delete(first,firstNode.balance);
                bst.insert(first,firstNode.balance - balance);
                bst.delete(second , secondNode.balance);
                bst.insert(second,secondNode.balance + balance);
                secondInFirst.balance -= balance;
                secondNode.balance += balance;
                firstNode.balance -= balance;
                firstInSecond.balance += balance;
            } else if (splited[0].equals("4")) {
                System.out.println(trie.getNode(splited[1]).debtor);
            } else if (splited[0].equals("5")) {
                System.out.println(trie.getNode(splited[1]).creditor);
            } else if (splited[0].equals("6")) {
                TrieNode first = trie.getNode(splited[1]);
                if (first == null) {
                    System.out.println("0.00");
                } else {
                    TrieNode second = first.TrieChildren.getNode(splited[2]);
                    if (second == null) {
                        System.out.println("0.00");
                    } else {
                        long firstPart = second.balance / 100;
                        long secondPart = second.balance % 100;
                        if (firstPart == 0 && secondPart < 0) {
                            System.out.print("-");
                        }
                        if (secondPart < 0){
                            secondPart *= -1 ;
                        }
                        String formattedBalance = String.format("%d.%02d", firstPart, secondPart);
                        System.out.println(formattedBalance);
                    }
                }
            } else if (splited[0].equals("2")) {
                if (bst.maxOfBST(bst.root) == null || bst.maxOfBST(bst.root).balance == 0) {
                    System.out.println(-1);
                } else {
                    System.out.println(bst.getLessName(bst.maxOfBST(bst.root), bst.root).name);
                }
            } else if (splited[0].equals("3")) {
                if (bst.minOfBST(bst.root) == null || bst.minOfBST(bst.root).balance == 0) {
                    System.out.println(-1);
                } else {
                    System.out.println(bst.minOfBST(bst.root).name);
                }
            }
        }
    }
}
