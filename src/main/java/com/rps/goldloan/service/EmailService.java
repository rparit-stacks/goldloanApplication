package com.rps.goldloan.service;

import com.rps.goldloan.entity.Branch;
import com.rps.goldloan.entity.Customer;
import com.rps.goldloan.entity.LoanApplication;
import com.rps.goldloan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendUserCreationEmail(User user) {
        try {
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("rohitparit1934@gmail.com");
                message.setTo(user.getEmail());
                message.setSubject("Welcome to Gold Loan Management System");
                message.setText("Dear " + user.getFullName() + ",\n\n" +
                        "Your account has been successfully created.\n\n" +
                        "Account Details:\n" +
                        "Username: " + user.getUsername() + "\n" +
                        "Role: " + user.getRole() + "\n" +
                        "Branch: " + (user.getBranch() != null ? user.getBranch().getName() : "Not Assigned") + "\n\n" +
                        "Thank you for joining us!\n\n" +
                        "Best Regards,\nGold Loan Management System");
                mailSender.send(message);
            }
        } catch (Exception e) {
            System.err.println("Error sending user creation email: " + e.getMessage());
        }
    }

    public void sendBranchCreationEmail(Branch branch) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("rohitparit1934@gmail.com");
            message.setTo("admin@goldloan.com");
            message.setSubject("New Branch Created");
            message.setText("A new branch has been created.\n\n" +
                    "Branch Details:\n" +
                    "Name: " + branch.getName() + "\n" +
                    "Code: " + branch.getCode() + "\n" +
                    "Address: " + (branch.getAddress() != null ? branch.getAddress() : "N/A") + "\n" +
                    "Contact: " + (branch.getContactNumber() != null ? branch.getContactNumber() : "N/A") + "\n" +
                    "Created At: " + branch.getCreatedAt() + "\n\n" +
                    "Best Regards,\nGold Loan Management System");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending branch creation email: " + e.getMessage());
        }
    }

    public void sendLoanApplicationEmail(LoanApplication loanApplication) {
        try {
            Customer customer = loanApplication.getCustomer();
            if (customer != null && customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("rohitparit1934@gmail.com");
                message.setTo(customer.getEmail());
                message.setSubject("Loan Application Submitted - " + loanApplication.getApplicationNumber());
                message.setText("Dear " + customer.getFirstName() + " " + customer.getLastName() + ",\n\n" +
                        "Your loan application has been successfully submitted.\n\n" +
                        "Application Details:\n" +
                        "Application Number: " + loanApplication.getApplicationNumber() + "\n" +
                        "Requested Amount: " + loanApplication.getRequestedAmount() + "\n" +
                        "Status: " + loanApplication.getStatus() + "\n" +
                        "Branch: " + loanApplication.getBranch().getName() + "\n" +
                        "Submitted At: " + loanApplication.getCreatedAt() + "\n\n" +
                        "We will review your application and update you on the status.\n\n" +
                        "Thank you for choosing our services!\n\n" +
                        "Best Regards,\nGold Loan Management System");
                mailSender.send(message);
            }
        } catch (Exception e) {
            System.err.println("Error sending loan application email: " + e.getMessage());
        }
    }
}

