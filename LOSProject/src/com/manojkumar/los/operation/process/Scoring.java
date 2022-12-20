package com.manojkumar.los.operation.process;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import com.manojkumar.los.customer.Customer;
import com.manojkumar.los.repo.IRepo;
import com.manojkumar.los.repo.LOSRepo;
import com.manojkumar.los.utils.StageConstants;

public interface Scoring extends StageConstants {
	public static void getScoring(Customer customer) {

		/*
		 * Checking fraud information based on some parameters
		 */
		int score = 30;
		if (customer.getPersonalInformation().getAge() > 25 && customer.getPersonalInformation().getAge() < 50) {
			score = score + 10;
		}
		if (customer.getLiability() == 0) {
			score = score + 20;
		} else {
			score -= 20;
		}

		if (customer.getIncome() > 500000) {
			score = score + 10;
		} else {
			score -= 10;
		}

		if (customer.getIncome() * 10 > customer.getLoanDetails().getLoanAmount()) {
			score = score + 20;
		} else {
			score -= 10;
		}
		/*
		 * Adding reason to information
		 */
		if (score <= 50) {
			customer.getLoanDetails().setRejectionReason("You doesnot match our requirements to get loan");
			customer.getPersonalInformation().setStage(REJECTION);
		} else {
			customer.getPersonalInformation().setStage(SCORING);
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
