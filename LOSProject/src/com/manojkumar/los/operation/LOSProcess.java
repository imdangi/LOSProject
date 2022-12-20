package com.manojkumar.los.operation;

import static com.manojkumar.los.utils.MessageReader.getMessage;
import static com.manojkumar.los.utils.Utility.scanner;
import static com.manojkumar.los.utils.MessageReader.getStageName;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.manojkumar.los.customer.Customer;
import com.manojkumar.los.operation.process.ApprovalStage;
import com.manojkumar.los.operation.process.DedupeStage;
import com.manojkumar.los.operation.process.DeleteApplication;
import com.manojkumar.los.operation.process.PrintForm;
import com.manojkumar.los.operation.process.QDEStage;
import com.manojkumar.los.operation.process.Scoring;
import com.manojkumar.los.operation.process.SourcingStage;
import com.manojkumar.los.repo.IRepo;
import com.manojkumar.los.repo.LOSRepo;
import com.manojkumar.los.utils.StageConstants;

public class LOSProcess implements StageConstants, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LOSProcess() {
	}

	public static void withdrawApplication(int applicationId) {
		DeleteApplication.deleteApplication(applicationId);
	}

	/*
	 * 
	 */
	public static void sourcingStage() {
		SourcingStage.sourcingStage();
	}

	/*
	 * Print the application form
	 */
	public static void showApplicationForm(int applicationId) {
		PrintForm.printApplicationForm(applicationId);

	}

	/*
	 * Wrong data entered checking --> fraud user checking
	 */
	public static void deDupeCheck(Customer customer) {
		DedupeStage.deDupeCheck(customer);
	}

	/*
	 * Static method to give the customer object from the application number entered
	 */
	public static Customer getCustomerDetails(int applicationRefNumber) {

		ArrayList<Customer> customers = new ArrayList<>();

		/*
		 * Checking if customer with application number exists or not in database
		 */
		IRepo repo;
		try {
			repo = LOSRepo.getInstance();
			customers = repo.getCustomer();
			if (!customers.isEmpty()) {
				for (Customer customer : customers) {
					if (customer.getApplicationId() == applicationRefNumber) {
						return customer;
					}
				}
			}

		} catch (EOFException e) {
			return null;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;

	}

	/*
	 * Static method to process application to the next stage
	 */

	public static void moveToNextStage(Customer customer) {
		boolean flag = false;

		/*
		 * Checking if customer with application number exists or not
		 */
		ArrayList<Customer> customers = new ArrayList<>();
		IRepo repo;
		try {
			repo = LOSRepo.getInstance();
			customers = repo.getCustomer();
			if (!customers.isEmpty()) {
				flag = true;
				int currentStage = customer.getPersonalInformation().getStage();
				switch (currentStage) {
				case SOURCING:
					QDEStage.qdeStage(customer);
					break;
				case QDE:
					DedupeStage.deDupeCheck(customer);
					break;
				case DEDUPE_CHECK:
					Scoring.getScoring(customer);
					break;
				case SCORING:
					ApprovalStage.approvalStage(customer);
					break;
				case APPROVAL:
					System.out.println("Your application is already approved ........");
					System.out.println("Press 1 to see the form ..Press 2 to exit.");
					int input = scanner.nextInt();
					if (input == 1) {
						PrintForm.printApplicationForm(customer.getApplicationId());
					}
					break;
				case REJECTION:
					System.out.println(
							"OOps your application does not match the requirements. Try again with new information.");

					System.out.println("Your application is already approved ........");
					System.out.println("Press 1 to see the rejection reason ..Press 2 to exit.");
					int input1 = scanner.nextInt();
					if (input1 == 1) {
						PrintForm.printApplicationForm(customer.getApplicationId());
					}
					break;
				default:
					System.out.println("OOps something went wrong");
					break;
				}
			}

			if (!flag) {
				System.out.println(getMessage("move.customerempty"));
			}

		} catch (EOFException e) {
			System.out.println(getMessage("move.customerempty"));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Get the current stage
	 */

	public static void checkStage(int applicationRefNumber) {
		boolean flag = false;
		ArrayList<Customer> customers = new ArrayList<>();
		/*
		 * Checking if customer with application number exists or not in database
		 */

		IRepo repo;
		try {
			repo = LOSRepo.getInstance();
			customers = repo.getCustomer();
			if (!customers.isEmpty()) {
				for (Customer customer : customers) {
					if (customer.getApplicationId() == applicationRefNumber) {
						System.out.println(getStageName(customer.getPersonalInformation().getStage()));
						flag = true;
						break;
					}
				}
			}
			if (!flag) {
				System.out.println(getMessage("appId.invalid"));
			}

		} catch (EOFException e) {
			System.out.println(getMessage("appId.invalid"));
		} catch (IOException | ClassNotFoundException e) {
//			e.printStackTrace();
		}

	}

}
