package com.example.demo.SmartDashboard.Repository;

import com.example.demo.SmartDashboard.Model.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepo extends CrudRepository<Invoice, Integer> {

}
