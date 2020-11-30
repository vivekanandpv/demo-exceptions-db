package in.athenaeum.demoexceptionsdb.services.implementations;

import in.athenaeum.demoexceptionsdb.exceptions.AlreadyDeletedException;
import in.athenaeum.demoexceptionsdb.exceptions.RecordNotFoundException;
import in.athenaeum.demoexceptionsdb.models.Account;
import in.athenaeum.demoexceptionsdb.models.Customer;
import in.athenaeum.demoexceptionsdb.repositories.AccountRepository;
import in.athenaeum.demoexceptionsdb.repositories.CustomerRepository;
import in.athenaeum.demoexceptionsdb.services.interfaces.IAccountService;
import in.athenaeum.demoexceptionsdb.viewmodels.AccountCreateViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.AccountViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.CustomerViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository,
                          CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<AccountViewModel> get() {
        return accountRepository.findAll()
                .stream()
                .filter(a -> !a.isDeleted())
                .map(a -> {
                    AccountViewModel accountViewModel = new AccountViewModel();
                    BeanUtils.copyProperties(a, accountViewModel);
                    CustomerViewModel customerViewModel = new CustomerViewModel();
                    BeanUtils.copyProperties(a.getCustomer(), customerViewModel);

                    accountViewModel.setCustomer(customerViewModel);
                    return accountViewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AccountViewModel get(int id) {
        //  1. Get the optional of account
        Optional<Account> optionalAccount = this.accountRepository.findById(id);
        //  2. If the value doesn't exist, throw the record not found
        Account account = optionalAccount
                .orElseThrow(
                        () -> new RecordNotFoundException("Could not find the account with id: " + id)
                );
        //  3. If the account is deleted, throw already deleted
        if (account.isDeleted()) {
            throw new AlreadyDeletedException("Deleted: account with id: " + id);
        }
        //  4. Prepare accountViewModel and return
        AccountViewModel accountViewModel = new AccountViewModel();
        BeanUtils.copyProperties(account, accountViewModel);

        CustomerViewModel customerViewModel = new CustomerViewModel();
        BeanUtils.copyProperties(account.getCustomer(), customerViewModel);

        accountViewModel.setCustomer(customerViewModel);

        return accountViewModel;
    }

    @Override
    public AccountViewModel create(AccountCreateViewModel viewModel) {
        //  1. Query the customer
        Optional<Customer> customerOptional =
                this.customerRepository.findById(viewModel.getCustomerId());
        //      a. the optional customer has value. else throw record not found
        Customer customer = customerOptional.orElseThrow(
                () -> new RecordNotFoundException("Could not find the customer with id: " + viewModel.getCustomerId())
        );
        //      b. the customer is deleted. If so, throw already deleted
        if (customer.isDeleted()) {
            throw new AlreadyDeletedException("Cannot add to the deleted customer with id: " + viewModel.getCustomerId());
        }
        //  2. Create an Account domain model object (empty)
        Account account = new Account();
        //  3. Copy the properties
        BeanUtils.copyProperties(viewModel, account);
        //  4. set the customer explicitly to this account
        account.setCustomer(customer);
        //  5. add the account to the customer
        customer.getAccounts().add(account);
        //  6. save and flush
        this.accountRepository.saveAndFlush(account);

        //  7. create an AccountViewModel (empty)
        AccountViewModel accountViewModel = new AccountViewModel();
        //  8. copy the properties from account (saved entity)
        BeanUtils.copyProperties(account, accountViewModel);
        //  9. return AccountViewModel
        return accountViewModel;
    }
}
