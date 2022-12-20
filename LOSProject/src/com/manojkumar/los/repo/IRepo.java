package com.manojkumar.los.repo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import com.manojkumar.los.customer.Customer;

public interface IRepo extends Serializable {

	boolean addCustomer(ArrayList<Customer> customer) throws IOException;

	ArrayList<Customer> getCustomer() throws ClassNotFoundException, IOException;

}
