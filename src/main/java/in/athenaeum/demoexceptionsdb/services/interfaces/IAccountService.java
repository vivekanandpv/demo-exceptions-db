package in.athenaeum.demoexceptionsdb.services.interfaces;

import in.athenaeum.demoexceptionsdb.viewmodels.AccountCreateViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.AccountViewModel;

import java.util.List;

public interface IAccountService {
    List<AccountViewModel> get();
    AccountViewModel get(int id);
    AccountViewModel create(AccountCreateViewModel viewModel);
}
