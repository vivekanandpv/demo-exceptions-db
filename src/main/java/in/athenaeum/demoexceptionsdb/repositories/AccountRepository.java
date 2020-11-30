package in.athenaeum.demoexceptionsdb.repositories;

import in.athenaeum.demoexceptionsdb.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
