package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.ActivityTrailers;
import com.example.demo.SmartDashboard.Model.Invoice;
import com.example.demo.SmartDashboard.Repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepo invoiceRepo;


    public void save(Invoice invoice){ invoiceRepo.save(invoice);}
}
