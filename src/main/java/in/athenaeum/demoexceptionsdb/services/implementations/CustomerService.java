package in.athenaeum.demoexceptionsdb.services.implementations;

import in.athenaeum.demoexceptionsdb.exceptions.AlreadyDeletedException;
import in.athenaeum.demoexceptionsdb.exceptions.ApplicationGeneralException;
import in.athenaeum.demoexceptionsdb.exceptions.RecordNotFoundException;
import in.athenaeum.demoexceptionsdb.models.Account;
import in.athenaeum.demoexceptionsdb.models.Customer;
import in.athenaeum.demoexceptionsdb.repositories.CustomerRepository;
import in.athenaeum.demoexceptionsdb.services.interfaces.ICustomerService;
import in.athenaeum.demoexceptionsdb.viewmodels.AccountViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.CustomerCreateViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.CustomerViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerViewModel> get() {
        return this.customerRepository
                .findAll()
                .stream()
                .filter(c -> !c.isDeleted())
                .map(c -> {
                    CustomerViewModel customerViewModel = new CustomerViewModel();
                    List<AccountViewModel> accounts = new ArrayList<>();

                    BeanUtils.copyProperties(c, customerViewModel);

                    for (Account account : c.getAccounts()) {
                        //  Add only the live (not-deleted) accounts
                        if (!account.isDeleted()) {
                            AccountViewModel accountViewModel = new AccountViewModel();
                            BeanUtils.copyProperties(account, accountViewModel);
                            accounts.add(accountViewModel);
                        }
                    }

                    customerViewModel.setAccounts(accounts);
                    return customerViewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerViewModel get(int id) {
        Optional<Customer> customerOptional = this.customerRepository.findById(id);
        if (!customerOptional.isPresent() || customerOptional.get().isDeleted()) {
            throw new RecordNotFoundException("Could not find customer with id: " + id);
        }

        CustomerViewModel viewModel = new CustomerViewModel();
        BeanUtils.copyProperties(customerOptional.get(), viewModel);

        List<AccountViewModel> accounts = new ArrayList<>();

        for (Account account : customerOptional.get().getAccounts()) {
            //  Add only the live (not-deleted) accounts
            if (!account.isDeleted()) {
                AccountViewModel accountViewModel = new AccountViewModel();
                BeanUtils.copyProperties(account, accountViewModel);
                accounts.add(accountViewModel);
            }
        }

        viewModel.setAccounts(accounts);

        return viewModel;
    }

    @Override
    public CustomerViewModel create(CustomerCreateViewModel viewModel) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(viewModel, customer);
        this.customerRepository.saveAndFlush(customer);

        CustomerViewModel customerViewModel = new CustomerViewModel();
        BeanUtils.copyProperties(customer, customerViewModel);

        return customerViewModel;
    }

    @Override
    public void delete(int id) {
        //  1. Get the optional of customer
        Optional<Customer> optionalCustomer = this.customerRepository.findById(id);

        //  2. Does the customer exist? If no, throw record not found
        Customer customer = optionalCustomer.orElseThrow(
                () -> new RecordNotFoundException("Could not find the customer")
        );
        //  3. Is the customer already deleted? If yes, throw already deleted
        if (customer.isDeleted()) {
            throw new AlreadyDeletedException();
        }
        //  4. Set the isDeleted to true and save and flush
        customer.setDeleted(true);

        this.customerRepository.saveAndFlush(customer);
    }

    @Override
    public void update(int id, CustomerUpdateViewModel viewModel) {
        //  1. check if the id and viewModel id are same, if not throw domain validation
        if (id != viewModel.getId()) {
            throw new ApplicationGeneralException("Id mismatch");
        }
        //  2. If the customer doesn't exist, throw record not found
        Customer customer = this.customerRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find the customer with id: " + id));
        //  3. If the customer is deleted, throw already deleted
        if (customer.isDeleted()) {
            throw new AlreadyDeletedException();
        }
        //  4. update using setters
        BeanUtils.copyProperties(viewModel, customer);
        //  5. save and flush
        this.customerRepository.saveAndFlush(customer);
    }
}
