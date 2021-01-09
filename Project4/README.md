# Producer-Consumer Text File Manipulation
This program reads a text file and returns a list of all unique words with the number of times that word appears in the file, using a producer-consumer model with threads. 

* One producer thread - opens the text fille and read in the words, plaing these words into a buffer.
* Two consumer threads - read words from the buffer.

## Usage

```sh
$ javac Main.java
$ java Main
```

## Input

Users will be asked to enter the following on stdin:

* A text file name to open for input.

## Output 

* Print out a list of all the unique words in the file, as well as the number of times that word appears in the file. For this purpose, a word is a contiguous group of non-white space characters. 
