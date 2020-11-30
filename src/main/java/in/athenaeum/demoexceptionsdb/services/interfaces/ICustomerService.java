package in.athenaeum.demoexceptionsdb.services.interfaces;


import in.athenaeum.demoexceptionsdb.viewmodels.CustomerCreateViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.CustomerViewModel;

import java.util.List;

public interface ICustomerService {
    List<CustomerViewModel> get();
    CustomerViewModel get(int id);
    CustomerViewModel create(CustomerCreateViewModel viewModel);
    void delete(int id);
    void update(int id, CustomerUpdateViewModel viewModel);
}
