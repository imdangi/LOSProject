package com.manojkumar.los.operation.process;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import com.manojkumar.los.customer.Customer;
import com.manojkumar.los.repo.IRepo;
import com.manojkumar.los.repo.LOSRepo;
import com.manojkumar.los.utils.StageConstants;

public interface ApprovalStage extends StageConstants {

	public static void approvalStage(Customer customer) {

		double roiMonthly;
		double approvedLoan;
		if (customer.getIncome() * 10 >= customer.getLoanDetails().getLoanAmount()) {
			approvedLoan = customer.getLoanDetails().getLoanAmount();
		} else {
			approvedLoan = customer.getIncome() * 10;
		}

		if (customer.getLoanDetails().getLoanType().equalsIgnoreCase("HL")) {
			roiMonthly = 0.1;
		} else if (customer.getLoanDetails().getLoanType().equalsIgnoreCase("PL")) {
			roiMonthly = 0.2;
		} else {
			roiMonthly = 0.3;
		}

		double emiWithoutInterest = approvedLoan / customer.getLoanDetails().getLoanDuration();
		double monthlyEMI = emiWithoutInterest + (emiWithoutInterest * roiMonthly);

		customer.getLoanDetails().setEmi(monthlyEMI);
		customer.getLoanDetails().setRoi(roiMonthly);
		customer.getPersonalInformation().setStage(APPROVAL);
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
