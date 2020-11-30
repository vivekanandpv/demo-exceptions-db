package in.athenaeum.demoexceptionsdb.controllers;

import in.athenaeum.demoexceptionsdb.exceptions.AlreadyDeletedException;
import in.athenaeum.demoexceptionsdb.exceptions.RecordNotFoundException;
import in.athenaeum.demoexceptionsdb.services.interfaces.ICustomerService;
import in.athenaeum.demoexceptionsdb.viewmodels.CustomerCreateViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.demoexceptionsdb.viewmodels.CustomerViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerViewModel>> getAll() {
        return new ResponseEntity<>(this.customerService.get(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerViewModel> getById(@PathVariable int id) {
        return new ResponseEntity<>(this.customerService.get(id), HttpStatus.OK);
//        try {
//            return new ResponseEntity<>(this.customerService.get(id), HttpStatus.OK);
//        } catch (RecordNotFoundException rnfe) {
//            //... log the error
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
    }

    @PostMapping
    public ResponseEntity<CustomerViewModel> create(@RequestBody CustomerCreateViewModel viewModel) {
        return new ResponseEntity<>(this.customerService.create(viewModel), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody CustomerUpdateViewModel viewModel) {
        this.customerService.update(id, viewModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        this.customerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
