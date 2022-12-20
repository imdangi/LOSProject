package com.manojkumar.los.utils;

import java.util.ResourceBundle;

public class MessageReader implements StageConstants {
	public static final String MESSAGE_BUNDLE_FILE_NAME = "messages";

	private MessageReader() {
	}

	public static String getMessage(String key) {
		return ResourceBundle.getBundle(MESSAGE_BUNDLE_FILE_NAME).getString(key);
	}

	public static String getStageName(int stage) {
		switch (stage) {
		case SOURCING:
			return getMessage("stage.sourcing");
		case QDE:
			return getMessage("stage.qde");
		case DEDUPE_CHECK:
			return getMessage("stage.dedupe");
		case SCORING:
			return getMessage("stage.scoring");
		case APPROVAL:
			return getMessage("stage.approval");
		case REJECTION:
			return getMessage("stage.rejection");
		default:
			return getMessage("stage.invalid");
		}

	}
}
