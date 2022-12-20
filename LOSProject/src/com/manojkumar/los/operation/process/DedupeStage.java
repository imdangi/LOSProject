package com.manojkumar.los.operation.process;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import com.manojkumar.los.customer.Customer;
import com.manojkumar.los.repo.IRepo;
import com.manojkumar.los.repo.LOSRepo;
import com.manojkumar.los.utils.StageConstants;

public interface DedupeStage extends StageConstants {

	public static void deDupeCheck(Customer customer) {

		/*
		 * Checking fraud information based on some parameters
		 */
		boolean flag = false;
		if (customer.getPersonalInformation().getPancard().length() != 10) {
			flag = true;
		}
		if (customer.getPersonalInformation().getPhoneNumber().length() != 10) {
			flag = true;
		}
		if (customer.getCustomerAddress().getPinCode().length() != 6) {
			flag = true;
		}

		/*
		 * Adding reason to information
		 */
		if (flag) {
			customer.getLoanDetails().setRejectionReason("You entered wrong details");
			customer.getPersonalInformation().setStage(REJECTION);
		} else {
			customer.getPersonalInformation().setStage(DEDUPE_CHECK);
		}

		String result;
		ArrayList<Customer> customerLists = new ArrayList<>();
		ArrayList<Customer> modifiedList = new ArrayList<>();
		int applicationId = customer.getApplicationId();

		try {
			IRepo repo = LOSRepo.getInstance();
			try {
				customerLists = repo.getCustomer();
				if (!customerLists.isEmpty()) {
					for (Customer c : customerLists) {
						if (applicationId != c.getApplicationId()) {
							modifiedList.add(c);
						} else {
							modifiedList.add(customer);
						}
					}

				}
			} catch (EOFException e) {
				System.out.println("No task available");
//					e.printStackTrace();
			}
			/*
			 * Printing customer his/her application id
			 */
			result = repo.addCustomer(modifiedList) ? "Data checked successfully" : "Failed";

			System.out.println(result);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
