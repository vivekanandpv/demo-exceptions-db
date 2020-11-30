package in.athenaeum.demoexceptionsdb.repositories;

import in.athenaeum.demoexceptionsdb.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
