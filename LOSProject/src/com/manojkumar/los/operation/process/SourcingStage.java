package com.manojkumar.los.operation.process;

import static com.manojkumar.los.utils.MessageReader.getMessage;
import static com.manojkumar.los.utils.Utility.scanner;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import com.manojkumar.los.customer.Address;
import com.manojkumar.los.customer.Customer;
import com.manojkumar.los.customer.LoanDetails;
import com.manojkumar.los.customer.PersonalInformation;
import com.manojkumar.los.repo.IRepo;
import com.manojkumar.los.repo.LOSRepo;

public interface SourcingStage {

	public static void sourcingStage() {

		/*
		 * Getting application id
		 */
		int applicationId = 999;
		ArrayList<Customer> customerLists = new ArrayList<>();
		try {

			IRepo repo = LOSRepo.getInstance();
			customerLists = repo.getCustomer();
			if (!customerLists.isEmpty()) {
				Customer cus = customerLists.get(customerLists.size() - 1);
				applicationId = cus.getApplicationId();
			}
		} catch (EOFException e) {
//			e.printStackTrace();
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		/*
		 * Initializing objects required and variables
		 */
		Customer customer = new Customer();
		PersonalInformation personalInformation = new PersonalInformation();
		LoanDetails loanDetails = new LoanDetails();
		Address address = new Address();

		String firstName;
		String lastName;
		byte age;
		String loanType;
		double loanAmount;
		int loanDuration;
		byte stage = 1;
		double income;
		/*
		 * Asking customer details
		 */

		scanner.nextLine();
		System.out.println(getMessage("enter.firstname"));
		firstName = scanner.nextLine();
		System.out.println(getMessage("enter.lastname"));
		lastName = scanner.nextLine();
		System.out.println(getMessage("enter.age"));
		age = scanner.nextByte();
		System.out.println(getMessage("enter.loantype"));
		loanType = scanner.nextLine();
		scanner.nextLine();
		System.out.println(getMessage("enter.loanamount"));
		loanAmount = scanner.nextDouble();
		System.out.println(getMessage("enter.loanduration"));
		loanDuration = scanner.nextInt();
		System.out.println(getMessage("enter.yourincome"));
		income = scanner.nextDouble();

		/*
		 * Setting customer object -> PersonalInformation details
		 */

		personalInformation.setAge(age);
		personalInformation.setFirstName(firstName);
		personalInformation.setLastName(lastName);
		personalInformation.setStage(stage);

		/*
		 * Setting customer object -> LoanDetails
		 */

		loanDetails.setLoanType(loanType);
		loanDetails.setLoanAmount(loanAmount);
		loanDetails.setLoanDuration(loanDuration);

		/*
		 * Setting customer class details
		 */

		customer.setApplicationId(applicationId + 1);
		customer.setLoanDetails(loanDetails);
		customer.setPersonalInformation(personalInformation);
		customer.setCustomerAddress(address);
		customer.setIncome(income);

		/*
		 * Printing customer his/her application id
		 */

		/*
		 * Adding data to the repository
		 */

		String result;
		try {
			IRepo repo = LOSRepo.getInstance();
			try {
				customerLists = repo.getCustomer();
				customerLists.add(customer);
			} catch (EOFException e) {
				customerLists.add(customer);
//				e.printStackTrace();
			}
			/*
			 * Printing customer his/her application id
			 */
			result = repo.addCustomer(customerLists) ? (getMessage("sourcing.yourid") + customer.getApplicationId())
					: "Failed";
			System.out.println(result);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
