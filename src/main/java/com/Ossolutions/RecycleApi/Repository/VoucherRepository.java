package com.Ossolutions.RecycleApi.Repository;

import com.Ossolutions.RecycleApi.Model.User;
import com.Ossolutions.RecycleApi.Model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findByUserOrderByGenerationDateTimeDesc(User user);
    Optional<Voucher> findBySerialNumber(String serialNumber);
}
