package com.manojkumar.los.operation.process;

import static com.manojkumar.los.utils.MessageReader.getMessage;
import static com.manojkumar.los.utils.Utility.scanner;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import com.manojkumar.los.customer.Customer;
import com.manojkumar.los.repo.IRepo;
import com.manojkumar.los.repo.LOSRepo;
import com.manojkumar.los.utils.StageConstants;

public interface QDEStage extends StageConstants {

	/*
	 * QDE stage : Quick Data entry stage
	 */

	public static void qdeStage(Customer customer) {

		/*
		 * Getting customers from the database
		 */

		ArrayList<Customer> customerList = new ArrayList<>();
		scanner.nextLine();
		System.out.println(getMessage("qde.enterpancard"));
		String pancard = scanner.nextLine();
		System.out.println(getMessage("qde.enterloan"));
		double liability = scanner.nextDouble();

		scanner.nextLine();
		System.out.println(getMessage("qde.enterphone"));
		String phoneNumber = scanner.nextLine();
		System.out.println(getMessage("qde.enteremail"));
		String email = scanner.nextLine();
		System.out.println(getMessage("address.houseno"));
		String hourseNo = scanner.nextLine();
		System.out.println(getMessage("address.landmark"));
		String landmark = scanner.nextLine();
		System.out.println(getMessage("address.city"));
		String city = scanner.nextLine();
		System.out.println(getMessage("address.state"));
		String state = scanner.nextLine();
		System.out.println(getMessage("address.country"));
		String country = scanner.nextLine();
		System.out.println(getMessage("address.pincode"));
		String pincode = scanner.nextLine();

		/*
		 * Setting values in Customer
		 */
		customer.getCustomerAddress().setCity(city);
		customer.getCustomerAddress().setCountry(country);
		customer.getCustomerAddress().setHouseNo(hourseNo);
		customer.getCustomerAddress().setLandmark(landmark);
		customer.getCustomerAddress().setPinCode(pincode);
		customer.getCustomerAddress().setState(state);
		customer.getPersonalInformation().setStage(QDE);
		customer.getPersonalInformation().setEmailAddress(email);
		customer.getPersonalInformation().setPhoneNumber(phoneNumber);
		customer.getPersonalInformation().setPancard(pancard);
		customer.setLiability(liability);

		/*
		 * 
		 */

		String result;
		ArrayList<Customer> newList = new ArrayList<>();
		try {
			IRepo repo = LOSRepo.getInstance();
			try {
				customerList = repo.getCustomer();

				if (!customerList.isEmpty()) {
					for (Customer c : customerList) {
						if (c.getApplicationId() == customer.getApplicationId()) {
							newList.add(customer);
						} else {
							newList.add(c);
						}
					}
				}
			} catch (EOFException e) {
				newList.add(customer);
				e.printStackTrace();
			}
			result = repo.addCustomer(newList) ? getMessage("qde.complete") : "Failed";
			System.out.println(result);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
