package com.manojkumar.los.view;

import static com.manojkumar.los.utils.Utility.scanner;
import static com.manojkumar.los.operation.LOSProcess.sourcingStage;
import static com.manojkumar.los.operation.LOSProcess.checkStage;
import static com.manojkumar.los.operation.LOSProcess.showApplicationForm;
import static com.manojkumar.los.operation.LOSProcess.moveToNextStage;
import static com.manojkumar.los.operation.LOSProcess.getCustomerDetails;
import static com.manojkumar.los.operation.LOSProcess.withdrawApplication;

import static com.manojkumar.los.utils.MessageReader.getMessage;

public class View {

	private static void newUserRegistration() {
		sourcingStage();
	}

	private static void oldUser() {
		/*
		 * olduser.input= Press 1 -> To Show application status\Press 2 -> To show
		 * application form
		 */

		System.out.println(getMessage("olduser.enterid"));
		int id = scanner.nextInt();
		System.out.println(getMessage("olduser.input"));
		int input = scanner.nextInt();
		switch (input) {
		case 1:
			checkStage(id);
			break;
		case 2:
			showApplicationForm(id);
			break;
		case 3:
			moveToNextStage(getCustomerDetails(id));
			break;
		case 4:
			withdrawApplication(id);
			break;
		default:
			System.out.println(getMessage("invalid.input"));
			break;

		}
	}

	public static void main(String[] args) {
		System.out.println(getMessage("system.welcome"));
		System.out.println("================================================");
		System.out.println();
		int input;
		do {
			/*
			 * view.userinput=Press 0 -> New Registration \n Press 1 -> Already Registered
			 * \n Press 2 -> To Exit
			 */
			System.out.println(getMessage("view.userinput"));
			input = scanner.nextInt();
			switch (input) {
			case 0:
				newUserRegistration();
				break;
			case 1:
				oldUser();
				break;
			case 2:
				System.out.println(getMessage("system.bye"));
				break;
			default:
				System.out.println(getMessage("invalid.input"));
				break;
			}
		} while (input != 2);

	}

}
