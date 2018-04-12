package com.javatech.core.api.test;

import java.util.Random;

public class ProducerConsumerExample {
	public static void main(String[] args) {
		Drop drop = new Drop();
		new Thread(new Producer(drop)).start();
		new Thread(new Consumer(drop)).start();

	}

}

class Drop {
	private String message;
	private boolean isMessageAvailable = false;

	public synchronized String get() throws InterruptedException {
		while (isMessageAvailable == false) {
			wait();
		}
		isMessageAvailable = false;
		notifyAll();
		return message;
	}

	public synchronized void put(String message) throws InterruptedException {
		while (isMessageAvailable == true) {
			wait();
		}

		this.message = message;
		isMessageAvailable = true;
		notifyAll();
	}
}

class Producer implements Runnable {
	private Drop drop;

	Producer(Drop drop) {
		this.drop = drop;
	}

	public void run() {
		String[] messages = { "Programming language", "Database Mangement System","Web Designing" };
		Random random = new Random();
		for (int i = 0; i < messages.length; i++) {
			System.out.println("produces:" + messages[i]);

			try {
				drop.put(messages[i]);

				Thread.sleep(random.nextInt(5000));

			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		try {
			drop.put("DONE");
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
}

class Consumer implements Runnable {
	private Drop drop;

	Consumer(Drop drop) {
		this.drop = drop;
	}

	public void run() {

		Random random = new Random();
		try {
			for (String message = drop.get(); !message.equals("DONE"); message = drop.get()) {
				System.out.println("consumes:" + message);
				Thread.sleep(random.nextInt(5000));
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
}
