package com.rps.goldloan.service;

import com.rps.goldloan.dto.CustomerRequest;
import com.rps.goldloan.dto.CustomerResponse;
import com.rps.goldloan.dto.CustomerUpdateDto;
import com.rps.goldloan.entity.Customer;
import com.rps.goldloan.exception.CustomerCreationException;
import com.rps.goldloan.exception.CustomerNotFoundException;
import com.rps.goldloan.exception.CustomerUpdateException;
import com.rps.goldloan.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public CustomerResponse createCustomer(CustomerRequest customerRequest){
        try {
            validateCustomerRequest(customerRequest);

            Customer customer = new Customer();
            customer.setFirstName(customerRequest.getFirstName());
            customer.setLastName(customerRequest.getLastName());
            customer.setDob(customerRequest.getDob());
            customer.setGender(customerRequest.getGender());
            customer.setMobileNumber(customerRequest.getMobileNumber());
            customer.setEmail(customerRequest.getEmail());
            customer.setAddress(customerRequest.getAddress());
            customer.setIdentificationType(customerRequest.getIdentificationType());
            customer.setIdentificationNumber(customerRequest.getIdentificationNumber());
            customer.setCreatedAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());
            customer = customerRepository.save(customer);

            return customerToCustomerResponseDto(customer);
        } catch (IllegalArgumentException e) {
            throw new CustomerCreationException(e.getMessage());
        } catch (Exception e) {
            throw new CustomerCreationException("Error creating customer: " + e.getMessage());
        }
    }

    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with ID: " + customerId));
        return customerToCustomerResponseDto(customer);
    }

    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with ID: " + customerId));
    }

    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerResponse> customerResponses = new ArrayList<>();
        for (Customer customer : customers) {
            customerResponses.add(customerToCustomerResponseDto(customer));
        }
        return customerResponses;
    }

    public CustomerResponse updateCustomer(Long customerId, CustomerUpdateDto customerUpdateDto) {
        try {
            Customer customer = customerRepository.findById(customerId).orElseThrow(()
                    -> new CustomerNotFoundException("Customer not found with ID: " + customerId));

            if (Objects.nonNull(customerUpdateDto.getFirstName()) && !customerUpdateDto.getFirstName().isEmpty()) {
                customer.setFirstName(customerUpdateDto.getFirstName());
            }

            if (Objects.nonNull(customerUpdateDto.getLastName()) && !customerUpdateDto.getLastName().isEmpty()) {
                customer.setLastName(customerUpdateDto.getLastName());
            }

            if (Objects.nonNull(customerUpdateDto.getDob())) {
                customer.setDob(customerUpdateDto.getDob());
            }

            if (Objects.nonNull(customerUpdateDto.getGender()) && !customerUpdateDto.getGender().isEmpty()) {
                customer.setGender(customerUpdateDto.getGender());
            }

            if (Objects.nonNull(customerUpdateDto.getMobileNumber()) && !customerUpdateDto.getMobileNumber().isEmpty()) {
                customer.setMobileNumber(customerUpdateDto.getMobileNumber());
            }

            if (Objects.nonNull(customerUpdateDto.getEmail()) && !customerUpdateDto.getEmail().isEmpty()) {
                customer.setEmail(customerUpdateDto.getEmail());
            }

            if (Objects.nonNull(customerUpdateDto.getAddress()) && !customerUpdateDto.getAddress().isEmpty()) {
                customer.setAddress(customerUpdateDto.getAddress());
            }

            if (Objects.nonNull(customerUpdateDto.getIdentificationType())) {
                customer.setIdentificationType(customerUpdateDto.getIdentificationType());
            }

            if (Objects.nonNull(customerUpdateDto.getIdentificationNumber()) && !customerUpdateDto.getIdentificationNumber().isEmpty()) {
                customer.setIdentificationNumber(customerUpdateDto.getIdentificationNumber());
            }

            customer.setUpdatedAt(LocalDateTime.now());

            customer = customerRepository.save(customer);

            return customerToCustomerResponseDto(customer);
        } catch (CustomerNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomerUpdateException("Error updating customer with ID: " + customerId + ". " + e.getMessage());
        }
    }

    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    private void validateCustomerRequest(CustomerRequest customerRequest) {
        if (Objects.isNull(customerRequest)) {
            throw new IllegalArgumentException("Customer request cannot be null");
        }
        if (Objects.isNull(customerRequest.getFirstName()) || customerRequest.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is required and cannot be empty");
        }
        if (Objects.isNull(customerRequest.getLastName()) || customerRequest.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name is required and cannot be empty");
        }
        if (Objects.isNull(customerRequest.getMobileNumber()) || customerRequest.getMobileNumber().isEmpty()) {
            throw new IllegalArgumentException("Mobile number is required and cannot be empty");
        }
        if (Objects.isNull(customerRequest.getIdentificationType())) {
            throw new IllegalArgumentException("Identification type is required and cannot be null");
        }
        if (Objects.isNull(customerRequest.getIdentificationNumber()) || customerRequest.getIdentificationNumber().isEmpty()) {
            throw new IllegalArgumentException("Identification number is required and cannot be empty");
        }
    }

    private CustomerResponse customerToCustomerResponseDto(Customer customer) {
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customer.getId());
        customerResponse.setFirstName(customer.getFirstName());
        customerResponse.setLastName(customer.getLastName());
        customerResponse.setDob(customer.getDob());
        customerResponse.setGender(customer.getGender());
        customerResponse.setMobileNumber(customer.getMobileNumber());
        customerResponse.setEmail(customer.getEmail());
        customerResponse.setAddress(customer.getAddress());
        customerResponse.setIdentificationType(customer.getIdentificationType());
        customerResponse.setIdentificationNumber(customer.getIdentificationNumber());
        customerResponse.setCreatedAt(customer.getCreatedAt());
        customerResponse.setUpdatedAt(customer.getUpdatedAt());
        return customerResponse;
    }
}
