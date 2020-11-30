package in.athenaeum.demoexceptionsdb.controllers;

import in.athenaeum.demoexceptionsdb.exceptions.AlreadyDeletedException;
import in.athenaeum.demoexceptionsdb.exceptions.RecordNotFoundException;
import in.athenaeum.demoexceptionsdb.services.interfaces.IAccountService;
import in.athenaeum.demoexceptionsdb.viewmodels.AccountCreateViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.AccountViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountViewModel>> getAll() {
        return new ResponseEntity<>(this.accountService.get(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountViewModel> getById(@PathVariable int id) {
        return new ResponseEntity<>(this.accountService.get(id), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<AccountViewModel> create(@RequestBody AccountCreateViewModel accountCreateViewModel) {
        return new ResponseEntity<>(this.accountService.create(accountCreateViewModel), HttpStatus.CREATED);
    }
}
