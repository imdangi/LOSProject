package com.manojkumar.los.operation.process;

import static com.manojkumar.los.utils.MessageReader.getMessage;
import static com.manojkumar.los.utils.MessageReader.getStageName;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import com.manojkumar.los.customer.Customer;
import com.manojkumar.los.repo.IRepo;
import com.manojkumar.los.repo.LOSRepo;
import com.manojkumar.los.utils.StageConstants;

public interface PrintForm extends StageConstants {

	public static void printAddress(Customer customer) {
		System.out.println("Address: -----------------------");
		System.out.println("House No : " + customer.getCustomerAddress().getHouseNo());
		System.out.println("Area : " + customer.getCustomerAddress().getArea());
		System.out.println("Landmark : " + customer.getCustomerAddress().getLandmark());
		System.out.println("City : " + customer.getCustomerAddress().getCity());
		System.out.println("State : " + customer.getCustomerAddress().getState());
		System.out.println("Country : " + customer.getCustomerAddress().getCountry());
		System.out.println("Pincode : " + customer.getCustomerAddress().getPinCode());
	}

	public static void basicInfo(Customer customer) {

		String loanStatus = getStageName(customer.getPersonalInformation().getStage());
		/*
		 * Printing
		 */
		System.out.println("Applicatnt details : -----------------------");
		System.out.println("Application Id : " + customer.getApplicationId());
		System.out.println("Name : " + customer.getPersonalInformation().getFirstName() + " "
				+ customer.getPersonalInformation().getLastName());
		System.out.println("Age : " + customer.getPersonalInformation().getAge());
		System.out.println("Phone number : " + customer.getPersonalInformation().getPhoneNumber());
		System.out.println("Email Address : " + customer.getPersonalInformation().getEmailAddress());
		System.out.println("Annual Income : " + customer.getIncome());
		System.out.println("Loan Status : " + loanStatus);
	}

	public static void printEMI(Customer customer) {

		System.out.println("Loan Details: -----------------------");
		System.out.println("Loan Type : " + customer.getLoanDetails().getLoanType());
		System.out.println("Loan Amount : " + customer.getLoanDetails().getLoanAmount());
		System.out.println("Duration : " + customer.getLoanDetails().getLoanDuration());
		System.out.println("ROI : " + customer.getLoanDetails().getRoi());
		System.out.println("============================================================");
		System.out.println("Monthly Emi : " + customer.getLoanDetails().getEmi());
		System.out.println("============================================================");
	}

	public static void printReject(Customer customer) {
		System.out.println("Loan Details: -----------------------");
		System.out.println("Loan Type : " + customer.getLoanDetails().getLoanType());
		System.out.println("Loan Amount : " + customer.getLoanDetails().getLoanAmount());
		System.out.println("Duration : " + customer.getLoanDetails().getLoanDuration());
		System.out.println("============================================================");
		System.out.println(
				"Sorry your application got rejected because\n " + customer.getLoanDetails().getRejectionReason());
		System.out.println("============================================================");
	}

	public static void customPrint(Customer customer) {

		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
		byte stage = customer.getPersonalInformation().getStage();

		switch (stage) {
		case SOURCING:
			basicInfo(customer);
			break;

		case QDE:
			basicInfo(customer);
			printAddress(customer);
			break;
		case DEDUPE_CHECK:
			basicInfo(customer);
			printAddress(customer);
			break;

		case SCORING:
			basicInfo(customer);
			printAddress(customer);
			break;

		case APPROVAL:
			System.out.println("Congratulations .. Your loan is approved");
			basicInfo(customer);
			printAddress(customer);
			printEMI(customer);
			break;
		case REJECTION:
			basicInfo(customer);
			printAddress(customer);
			printReject(customer);
			break;
		default:
			System.out.println("OOPs something went wrong......");
		}

		/*
		 * Address :
		 */
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

	}

	public static void printApplicationForm(int applicationId) {
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
					if (customer.getApplicationId() == applicationId) {
						System.out.println("=================================================================");
						customPrint(customer);
						flag = true;
						System.out.println("==================================================================");
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
			System.out.println("Class Not Found || I/O exception");
		}
	}
}
