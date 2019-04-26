package myhomewrk;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;



public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		BlockingQueue q1 = new BlockingQueue(100);
		BlockingQueue q2 = new BlockingQueue(100);
		
		ThreadNum1 t1 = new ThreadNum1(q1);
		Thread th1 = new Thread(t1);
		
		ThreadNum2 th2[] = new ThreadNum2[8];

		ThreadNum3 t3 = new ThreadNum3(q2);
		Thread th3 = new Thread(t3);
		
		th1.start();
		for (int i = 0; i < 8; i++) {
			th2[i] = new ThreadNum2(q1, q2);
			th2[i].start();

		}

		th3.start();

		th1.join();

		for (int i = 0; i < 8; i++) {

			th2[i].join();

		}

		th3.join();
		
		System.out.println("end");

	}


}

class ThreadNum1 extends Thread {
	BlockingQueue q1;

	public ThreadNum1(BlockingQueue q) {
		this.q1 = q;
	}

		public void run() {
		
		for (int i = 0; i < 100; i++) {
			try {
				q1.add("f" + i + ".txt");
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}

	}
}


class ThreadNum2 extends Thread {
	
	BlockingQueue q1;
	BlockingQueue q2;
	private String rawDataFromFile;
	private String processedData;
	private String name;

	public ThreadNum2(BlockingQueue q1, BlockingQueue q2) {
		this.q1 = q1;
		this.q2 = q2;
	}

	public void run() {
		
		while (q2.count() <= q2.limit) {
			try {
				
				name = q1.pop().toString();
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
			try {
				
				rawDataFromFile = FileUtils.readFileAsString("data\\" + name);
			} catch (IOException e) {
		
				e.printStackTrace();
			}
			
			processedData =name + SomeMethod.count(rawDataFromFile);
			try {
				
				q2.add(processedData);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}

		}

	}

}

class ThirdThread extends Thread {
	BlockingQueue q2;
	private String processedData;

	public ThirdThread(BlockingQueue q2) {
		this.q2 = q2;
	}

	public void run() {
		for (int i = 0; i < 100; i++) {
			try {
				processedData = (String) q2.pop();
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
			try {
				FileUtils.appendStringToFile("processedData.txt", processedData);
			} catch (IOException e) {
				
				e.printStackTrace();
			}

		}

	}
}



 class ThreadNum3 extends Thread {

	BlockingQueue q2;
	private String processedData;

	public ThreadNum3(BlockingQueue q2) {
		this.q2 = q2;
	}

	public void run() {
				for (int i = 0; i < 100; i++) {
			try {
				
				processedData = (String) q2.pop();
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
			try {
				
				FileUtils.appendStringToFile("OutPut.txt", processedData);
			} catch (IOException e) {
				
				e.printStackTrace();
			}

		}
	}
}
  class SomeMethod {
		
		
		public static String count(String s) {
			int numL = 0;
			int numD = 0;
			int rest = 0;
			char[] ch = s.toCharArray();
			for (char c : ch) {
				if (Character.isLetter(c)) {
					numL++;
				} else if (Character.isDigit(c)) {
					numD++;
				} else
					rest++;
			}
			String p = String.format(numL + "ofleters" + numD + "ofdigits" + rest + "rest");
			return p;

		}

	}

 
  class FileUtils {
 	
 	public static String readFileAsString(String name) throws IOException {
 		return new String(Files.readAllBytes(Paths.get(name)));
 	}

 	
 	public static void appendStringToFile(String name, String line) throws IOException {
 		File file = new File(name);
 		FileWriter fw = new FileWriter(file, true);
 		fw.write(line + "\r\n");
 		fw.close();
 	}

 }


  class BlockingQueue {
 	private List queue;
 	public Integer limit;
 	
 	private int counter = 0;

 	public synchronized int count() {
 		counter++;

 		return counter;

 	}

 	public BlockingQueue(Integer limit) {
 		this.limit = limit;
 		queue = new LinkedList();
 	}

 	public synchronized Boolean isEmpty() {
 		return this.queue.size() == 0;
 	}

 	public synchronized void add(Object o) throws InterruptedException {
 		while (this.queue.size() == this.limit) {
 			wait();
 		}
 		if (this.queue.size() == 0) {
 			notifyAll();
 		}
 		this.queue.add(o);
 	}

 	public synchronized Object pop() throws InterruptedException {
 		while (this.queue.size() == 0) {
 			wait();
 		}
 		if (this.queue.size() == this.limit) {
 			notifyAll();
 		}

 		return this.queue.remove(0);
 	}

 }

 class FirstThread extends Thread {
 	BlockingQueue q1;

 	public FirstThread(BlockingQueue q) {
 		this.q1 = q;
 	}

 	public void run() {
 		for (int i = 0; i < 100; i++) {
 			try {
 				q1.add("f" + i + ".txt");
 			} catch (InterruptedException e) {
 			
 				e.printStackTrace();
 			}
 		}

 	}

 }

