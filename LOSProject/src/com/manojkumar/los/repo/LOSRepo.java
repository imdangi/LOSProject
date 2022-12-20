package com.manojkumar.los.repo;

import static com.manojkumar.los.utils.StageConstants.PATH;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.manojkumar.los.customer.Customer;

public class LOSRepo implements IRepo {

	private static final long serialVersionUID = 1L;
	File file;

	static LOSRepo repo;

	private LOSRepo() throws IOException {
		file = new File(PATH);
		file.createNewFile();
	}

	public static LOSRepo getInstance() throws IOException {
		if (repo == null) {
			repo = new LOSRepo();
		}
		return repo;
	}

	@Override
	public boolean addCustomer(ArrayList<Customer> customers) throws IOException {
		boolean flag = false;
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
				objectOutputStream.writeObject(customers);
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public ArrayList<Customer> getCustomer() throws ClassNotFoundException, IOException {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
				return (ArrayList<Customer>) objectInputStream.readObject();
			}
		}
	}

}
