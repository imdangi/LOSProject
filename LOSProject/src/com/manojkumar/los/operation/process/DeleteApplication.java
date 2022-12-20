package com.manojkumar.los.operation.process;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import com.manojkumar.los.customer.Customer;

import com.manojkumar.los.repo.IRepo;
import com.manojkumar.los.repo.LOSRepo;

public interface DeleteApplication {

	public static void deleteApplication(int applicationId) {

		/*
		 * Getting application id
		 */
		ArrayList<Customer> customerLists = new ArrayList<>();
		ArrayList<Customer> modifiedList = new ArrayList<>();
		boolean flag = false;
		String result;
		try {
			IRepo repo = LOSRepo.getInstance();
			try {
				customerLists = repo.getCustomer();
				if (!customerLists.isEmpty()) {
					for (Customer c : customerLists) {
						if (applicationId != c.getApplicationId()) {
							modifiedList.add(c);
						} else {
							flag = true;
						}
					}

				}
			} catch (EOFException e) {
				System.out.println("No task available");
//				e.printStackTrace();
			}
			/*
			 * Printing customer his/her application id
			 */
			if (flag) {
				result = repo.addCustomer(modifiedList) ? "Your application deleted successfully" : "Failed";
			} else {
				result = repo.addCustomer(modifiedList) ? "No application deleted" : "Failed to find id";
			}
			System.out.println(result);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
