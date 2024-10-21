package com.microservice.accounts.service.impl;

import com.microservice.accounts.constants.AccountsConstants;
import com.microservice.accounts.dto.CustomerDto;
import com.microservice.accounts.entity.Accounts;
import com.microservice.accounts.entity.Customer;
import com.microservice.accounts.exception.CustomerAlreadyExistsException;
import com.microservice.accounts.mapper.CustomerMapper;
import com.microservice.accounts.repo.AccountsRepository;
import com.microservice.accounts.repo.CustomerRepository;
import com.microservice.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with same mobile" + customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");

        Customer savedCustomer = customerRepository.save(customer);
        System.out.println("CHECKING"+savedCustomer +createNewAccount(savedCustomer));
        accountsRepository.save(createNewAccount(customer));

    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
//        newAccount.setCreatedBy(customer.getCreatedBy());
//        newAccount.setCreatedAt(customer.getCreatedAt());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        newAccount.setUpdatedAt(LocalDateTime.now());
        newAccount.setUpdatedBy("Anonymous");
        return newAccount;
    }
}
